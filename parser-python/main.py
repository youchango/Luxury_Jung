import os
from fastapi import FastAPI, UploadFile, File, HTTPException, Form
import uvicorn
from dotenv import load_dotenv

# .env 파일 로드
load_dotenv()

from extractor import extract_text_from_file
from ai_processor import parse_with_ai
from schemas import ResumeParsedData
from database import save_parsed_data

app = FastAPI(
    title="Luxury-Jung Parser API",
    description="이력서 파일 파싱 및 AI 정규화 API (첫번째 프로그램 워커)",
    version="1.0.0"
)

@app.get("/")
def read_root():
    provider = os.environ.get("AI_PROVIDER", "openai").upper()
    return {
        "message": "Luxury-Jung Parser API is running!",
        "current_ai_provider": provider
    }

@app.post("/api/v1/parse", response_model=ResumeParsedData)
async def parse_resume(
    file: UploadFile = File(...),
    job_id: int = Form(1, description="SpringBoot 서버에서 전달받은 작업 ID"),
    file_url: str = Form("http://example.com/uploads/sample.pdf", description="스토리지에 저장된 파일 URL")
):
    """
    이력서 파일을 받아 텍스트를 파싱하고 선택된 단위 제공자(AI_PROVIDER)를 통해 JSON으로 정형화하여 DB에 저장 후 반환합니다.
    """
    provider = os.environ.get("AI_PROVIDER", "openai").lower()
    
    if provider == "openai" and not os.environ.get("OPENAI_API_KEY"):
        raise HTTPException(status_code=500, detail="서버에 OPENAI API KEY가 설정되지 않았습니다.")
    elif provider == "gemini" and not os.environ.get("GEMINI_API_KEY"):
        raise HTTPException(status_code=500, detail="서버에 GEMINI API KEY가 설정되지 않았습니다.")
    elif provider == "claude" and not os.environ.get("CLAUDE_API_KEY"):
        raise HTTPException(status_code=500, detail="서버에 CLAUDE API KEY가 설정되지 않았습니다.")

    try:
        # 1. 파일 데이터 읽기
        content = await file.read()
        
        # 2. 확장자에 따른 텍스트 추출
        raw_text = extract_text_from_file(content, file.filename)
        
        # 만약 원문에 텍스트가 너무 없으면(이미지 스캔본 등) 에러 반환
        if not raw_text or len(raw_text.strip()) < 10:
            raise HTTPException(status_code=400, detail="파일에서 읽을 수 있는 텍스트가 너무 적습니다.")

        # 3. AI 파싱 (정규화)
        parsed_data = parse_with_ai(raw_text)
        
        # 4. DB 저장
        try:
            saved_id = save_parsed_data(
                job_id=job_id,
                original_file_name=file.filename,
                file_url=file_url,
                raw_text=raw_text,
                parsed_data=parsed_data
            )
            print(f"[{file.filename}] 이력서가 DB에 성공적으로 저장되었습니다. Resume ID: {saved_id}")
        except Exception as db_e:
            import traceback
            traceback.print_exc()
            print(f"DB 저장 중 오류 발생: {str(db_e)}")
            # 파싱이 완료되었다면, API 응답 자체는 성공하도록 둘 수도 있습니다.
            # 하지만 안전을 위해 에러를 반환합니다.
            raise HTTPException(status_code=500, detail=f"파싱은 되었으나 DB 저장에 실패했습니다. (DB 연결 설정을 확인하세요): {str(db_e)}")
        
        return parsed_data
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

if __name__ == "__main__":
    uvicorn.run("main:app", host="0.0.0.0", port=8000, reload=True)
