# 럭셔리-정 (Luxury-Jung) 프로젝트 작업 진척도 (Progress)

## 🐍 1. Python 파서 에이전트 (parser-python) - 진행률: 90%
- [x] 프로젝트 초기화 및 `requirements.txt` 작성
- [x] Pydantic 기반 정형화 스키마(`schemas.py`) 정의 (경력시작년월일, 자격증 포함)
- [x] 파일 확장자(PDF, DOCX 등)별 기초 텍스트 추출기(`extractor.py`) 작성
- [x] 다중 모델(OpenAI, Gemini, Claude) 대응 Factory/Strategy 패턴 적용 (`ai_processor.py`)
- [x] SQLAlchemy / PyMySQL 기반 데이터베이스 결합 로직(`database.py`) 작성
- [x] FastAPI 컨트롤러 연동 및 기능 단위 엔드포인트 세팅 완료 (`main.py`)
- [ ] (향후 과제) 메세지 큐(Celery/RabbitMQ)를 활용한 비동기 백그라운드 처리 도입

## ☕ 2. Spring Boot 백엔드 (backend-spring) - 진행률: 20%
- [x] Spring Boot 3.x 기반 프로젝트 초기화 및 Java 17 설정
- [x] MariaDB 포트 구성 연결 및 `application.yml` 커넥션 설정
- [x] 스키마(db.sql) 1:1 매핑 기반 JPA Entity 전면 클래스 구성 (8개 단위)
- [x] 기본 JpaRepository 계층 인터페이스 생성
- [x] QueryDSL 셋업 및 QClass 생성 완료를 통한 이력서 강력 다중 조건 동적 필터 로직 구현 (`BooleanBuilder`)
- [x] 안전한 Entity 캡슐화를 돕는 응답 DTO 변환 서비스 및 오프셋 기반 페이징 컨트롤러(`ResumeController`) 추가 작성
- [x] 어드민 이력서 다중/단일 파일 아카이빙(File System) 및 Python FastAPI 백엔드 Multipart-Relay 통신 API 구축
- [x] Spring Security 6 & JWT(jjwt) 연동을 통한 관리자/사용자 권한 분리 및 인증/인가 체계 완성

## 💻 3. 웹 프론트엔드 (frontend-web) - 예정
- [ ] Next.js 기반 반응형 프로젝트 초기화
- [ ] TailwindCSS + 선언형 UI 프레임워크 셋업
- [ ] 다수 조회 조건 처리 및 검색 테이블 인터랙티브 UI/UX 디자인 퍼블리싱
- [ ] 관리자 이력서 파일 다중 업로드 시스템 파일 드래그앤드롭 화면 구현
