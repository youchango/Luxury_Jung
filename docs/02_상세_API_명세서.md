# 상세 API 명세서 (Luxury-Jung)

## 1. 공통 사항
- **Base URL:** `/api/v1`
- **응답 형식 (JSON Common Response):**
  ```json
  {
    "status": 200,
    "message": "성공",
    "data": { ... }
  }
  ```

## 2. 인증/회원 API
### 2.1 로그인
- **Endpoint:** `POST /api/v1/auth/login`
- **Request:** `email`, `password`
- **Response:** `accessToken` (JWT), `roles` (USER, ADMIN)

## 3. 관리자 (Admin) 파일 프로세싱 API
### 3.1 이력서 파일 다중 업로드 (첫번째 프로그램 동작 트리거)
- **Endpoint:** `POST /api/v1/admin/resumes/upload`
- **Header:** `Authorization: Bearer {token}` (ADMIN 권한)
- **Content-Type:** `multipart/form-data`
- **Request:** `files` (Array of files)
- **Response:** `jobId` (비동기 작업 상태 추적용 ID)
- **Description:** 업로드된 파일을 스토리지에 저장하고, Parsing 워커에 분석을 지시합니다.

### 3.2 작업 상태 조회
- **Endpoint:** `GET /api/v1/admin/resumes/jobs/{jobId}`
- **Response:** `status` (PENDING, PROCESSING, COMPLETED, FAILED), `successCount`, `failCount`

## 4. 이력서 조회 API
### 4.1 이력서 목록 조회 및 다중 조건 검색 (두번째 프로그램)
- **Endpoint:** `GET /api/v1/resumes`
- **Header:** `Authorization: Bearer {token}`
- **Query Parameters:**
  - `page`: 페이지 번호 (default: 1)
  - `size`: 페이지 크기 (default: 20)
  - `yearsOfExperienceMin`: 최소 연차
  - `yearsOfExperienceMax`: 최대 연차
  - `techStacks`: 사용기술 목록 (콤마로 구분, ex: "Java,Python")
  - `os`: 사용경험 운영체제
  - `languages`: 사용가능 언어
- **Response:**
  ```json
  "data": {
    "content": [
      {
        "id": 1,
        "name": "홍길동",
        "birthdate": "1990-01-01",
        "yearsOfExperience": 5,
        "techStacks": ["Java", "Spring Boot"],
        ...
      }
    ],
    "pageInfo": {
      "totalPages": 10,
      "totalElements": 200,
      "currentPage": 1
    }
  }
  ```

### 4.2 이력서 상세 정보 조회
- **Endpoint:** `GET /api/v1/resumes/{resumeId}`
- **Response:** 이력서 전체 정형화 데이터, 원본 파일 다운로드 링크.
