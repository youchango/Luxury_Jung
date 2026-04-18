import os
from sqlalchemy import create_engine, Column, BigInteger, String, Integer, Text, Date, ForeignKey, DateTime
from sqlalchemy.orm import declarative_base, sessionmaker
from datetime import datetime

# 프로퍼티(환경변수) 파일에서 읽어오며, 기본값은 MariaDB 설정
DATABASE_URL = os.environ.get("DATABASE_URL", "mysql+pymysql://root:password@localhost:33306/luxury_jung?charset=utf8mb4")

engine = create_engine(DATABASE_URL, echo=False)
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)
Base = declarative_base()

class ResumeModel(Base):
    __tablename__ = 'resume'
    id = Column(BigInteger, primary_key=True, index=True, autoincrement=True)
    job_id = Column(BigInteger, nullable=False)
    original_file_name = Column(String(255), nullable=False)
    file_url = Column(Text, nullable=False)
    name = Column(String(100))
    birthdate = Column(Date)
    career_start_date = Column(Date)
    years_of_experience = Column(Integer, default=0)
    parsed_raw_text = Column(Text)
    created_at = Column(DateTime, default=datetime.utcnow)

class ResumeTechStack(Base):
    __tablename__ = 'resume_tech_stack'
    id = Column(BigInteger, primary_key=True, autoincrement=True)
    resume_id = Column(BigInteger, ForeignKey('resume.id', ondelete='CASCADE'), nullable=False)
    tech_name = Column(String(100), nullable=False)

class ResumeCareer(Base):
    __tablename__ = 'resume_career'
    id = Column(BigInteger, primary_key=True, autoincrement=True)
    resume_id = Column(BigInteger, ForeignKey('resume.id', ondelete='CASCADE'), nullable=False)
    summary = Column(Text, nullable=False)

class ResumeOs(Base):
    __tablename__ = 'resume_os'
    id = Column(BigInteger, primary_key=True, autoincrement=True)
    resume_id = Column(BigInteger, ForeignKey('resume.id', ondelete='CASCADE'), nullable=False)
    os_name = Column(String(100), nullable=False)

class ResumeLanguage(Base):
    __tablename__ = 'resume_language'
    id = Column(BigInteger, primary_key=True, autoincrement=True)
    resume_id = Column(BigInteger, ForeignKey('resume.id', ondelete='CASCADE'), nullable=False)
    language_name = Column(String(100), nullable=False)

class ResumeSkill(Base):
    __tablename__ = 'resume_skill'
    id = Column(BigInteger, primary_key=True, autoincrement=True)
    resume_id = Column(BigInteger, ForeignKey('resume.id', ondelete='CASCADE'), nullable=False)
    skill_name = Column(String(100), nullable=False)

class ResumeCertificate(Base):
    __tablename__ = 'resume_certificate'
    id = Column(BigInteger, primary_key=True, autoincrement=True)
    resume_id = Column(BigInteger, ForeignKey('resume.id', ondelete='CASCADE'), nullable=False)
    certificate_name = Column(String(150), nullable=False)

def save_parsed_data(job_id: int, original_file_name: str, file_url: str, raw_text: str, parsed_data) -> int:
    """
    AI로 파싱된 이력서 데이터(ResumeParsedData)를 MariaDB에 관계형으로 자동 매핑하여 저장합니다.
    """
    db = SessionLocal()
    try:
        def safe_date(date_str):
            if not date_str:
                return None
            try:
                # YYYY-MM-DD 형식만 추출해서 저장
                return datetime.strptime(date_str[:10], "%Y-%m-%d").date()
            except ValueError:
                return None
            
        resume_record = ResumeModel(
            job_id=job_id,
            original_file_name=original_file_name,
            file_url=file_url,
            name=parsed_data.name,
            birthdate=safe_date(parsed_data.birthdate),
            career_start_date=safe_date(parsed_data.career_start_date),
            years_of_experience=parsed_data.years_of_experience,
            parsed_raw_text=raw_text
        )
        db.add(resume_record)
        db.flush() # 생성된 resume.id 획득

        resume_id = resume_record.id

        # 1:N 하위 테이블 벌크 Insert 처리
        if parsed_data.tech_stacks:
            db.add_all([ResumeTechStack(resume_id=resume_id, tech_name=ts) for ts in parsed_data.tech_stacks])

        if parsed_data.careers:
            db.add_all([ResumeCareer(resume_id=resume_id, summary=c) for c in parsed_data.careers])

        if parsed_data.os_experience:
            db.add_all([ResumeOs(resume_id=resume_id, os_name=os_n) for os_n in parsed_data.os_experience])

        if parsed_data.languages:
            db.add_all([ResumeLanguage(resume_id=resume_id, language_name=l) for l in parsed_data.languages])

        if parsed_data.skills:
            db.add_all([ResumeSkill(resume_id=resume_id, skill_name=s) for s in parsed_data.skills])

        if parsed_data.certificates:
            db.add_all([ResumeCertificate(resume_id=resume_id, certificate_name=c) for c in parsed_data.certificates])

        db.commit()
        return resume_id
    except Exception as e:
        db.rollback()
        raise e
    finally:
        db.close()
