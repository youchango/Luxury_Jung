# 💎 (이)력서리-정 (Luxury-Jung) 로컬 실행 가이드

이 프로젝트는 3개의 티어(Python, Spring Boot, React)가 독립적으로 서로 통신하며 맞물려 돌아가는 MSA 구조의 시스템입니다. 따라서 각 모듈별로 **3개의 독립된 터미널 창**을 띄워놓고 각각 실행하셔야 합니다. 

이 문서는 프로젝트 최상단에 보관되며, 언제든 서버를 켤 때 참고하시기 바랍니다!

---

## ⚠️ 0. 기동 전 핵심 체크리스트 (필수)
1. **MariaDB 33306 포트 가동 중인가요?**: 로컬 PC에 MariaDB 서버가 켜져 있어야 합니다.
2. **Spring DB 비밀번호를 수정하셨나요?**: `backend-spring/src/main/resources/application.yml` 열어보시면 비밀번호가 `password`로 임의 세팅되어있습니다. 본인 DB 비밀번호로 수정해주세요.
3. **Python AI 키를 넣으셨나요?**: `parser-python` 폴더에 들어가셔서 `.env.example` 이름을 `.env` 로 바꾼 후 본인의 `OPENAI_API_KEY`를 필수로 기입하셔야 파서가 구동됩니다.

---

## 🐍 1. Python AI 파서 실행 (터미널 1)

이 모듈은 AI를 이용해 파일을 파싱하고 8000번 포트로 백엔드의 명령을 수신합니다.

```bash
# 1. 터미널을 열고 Python 디렉토리로 이동
cd E:\GitHub\Luxury_Jung\parser-python

# 2. 파이썬 라이브러리 설치 (최초 1회만)
pip install -r requirements.txt

# 3. FastAPI 서버 기동
uvicorn main:app --host 0.0.0.0 --port 8000 --reload
```
✅ **정상 확인**: 브라우저에서 `http://localhost:8000/docs` 접속 시 Swagger API 문서 창이 뜬다면 성공입니다.

---

## ☕ 2. Spring Boot 백엔드 코어 실행 (터미널 2)

이 모듈은 Python과 React 사이에서 DB를 중개하고, 8080포트로 인증/인가 및 데이터를 서빙합니다.

```bash
# 1. 새 터미널을 열고 Spring Boot 디렉토리로 이동
cd E:\GitHub\Luxury_Jung\backend-spring

# 2. 빌드 및 스프링 세션 가동 (최초 빌드 시 수 분 소요)
.\gradlew.bat bootRun
```
✅ **정상 확인**: 터미널 창에 `Started LuxuryJungApiApplication in OO 초` 가 뜨며 에러 없이 멈춰있다면 성공입니다. (만약 빨간 줄이 뜬다면 거의 100% DB 로그인 실패 즉 비밀번호 미세팅 탓입니다.)

---

## 💻 3. React / Vite 웹 프론트엔드 실행 (터미널 3)

사용자/관리자가 직접 눈으로 보고 이력서를 검색할 수 있는 5173포트의 럭셔리 UI/UX 화면입니다.

```bash
# 1. 새 터미널을 열고 프론트엔드 디렉토리로 이동
cd E:\GitHub\Luxury_Jung\frontend-web

# 2. 패키지 설치 (이전에 설치가 끝났다면 생략 가능)
npm install

# 3. 개발 서버 기동
npm run dev
```
✅ **정상 확인**: 터미널에 Local 링크가 잡힙니다. `http://localhost:5173` 으로 브라우저를 직접 켜서 들어가시면 **[Welcome to Luxury Jung] 로그인 패널**이 화려하게 노출됩니다!

---

💡 **TIP**: 서버를 종료하고 싶으실 땐 켜두었던 터미널 창에서 각자 **`Ctrl + C`** 를 누르시면 안전하게 내려갑니다.
