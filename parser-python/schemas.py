from pydantic import BaseModel, Field
from typing import List, Optional

class ResumeParsedData(BaseModel):
    """
    이력서 원문에서 AI가 추출해 낼 정형화된 JSON 데이터 스키마입니다.
    데이터베이스에 저장되기 전 유효성을 검사합니다.
    """
    name: Optional[str] = Field(default=None, description="지원자 이름")
    birthdate: Optional[str] = Field(default=None, description="생년월일 (YYYY-MM-DD 형식). 정보가 없으면 null")
    career_start_date: Optional[str] = Field(default=None, description="경력 시작 년월일 (YYYY-MM-DD 형식). 신입이거나 정보가 없으면 null")
    years_of_experience: int = Field(default=0, description="총 경력 연차 (숫자만, 신입이나 경력이 전혀 없으면 0)")
    tech_stacks: List[str] = Field(default_factory=list, description="사용 기술 스택 목록 (예: Java, C++, Spring Boot)")
    careers: List[str] = Field(default_factory=list, description="경력 사항 혹은 이력 요약 목록 (회사명 및 주요업무)")
    os_experience: List[str] = Field(default_factory=list, description="사용해 본 운영체제 환경 (예: Linux, Windows 등)")
    languages: List[str] = Field(default_factory=list, description="사용 가능한 외국어 목록 (예: English)")
    skills: List[str] = Field(default_factory=list, description="기타 직무 관련 스킬 목록")
    certificates: List[str] = Field(default_factory=list, description="보유 자격증 목록")

