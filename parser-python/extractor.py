import tempfile
import os
import pdfplumber
import docx

def extract_text_from_file(file_content: bytes, filename: str) -> str:
    """
    파일의 확장자를 확인하여 적절한 파서(Parser)를 사용해 텍스트를 추출합니다.
    (현재 PDF, DOCX, TXT 지원. HWP 등은 추후 확장 예정)
    """
    ext = os.path.splitext(filename)[1].lower()
    
    if ext == '.pdf':
        return _extract_text_from_pdf(file_content)
    elif ext in ['.doc', '.docx']:
        return _extract_text_from_docx(file_content)
    elif ext == '.txt':
        try:
            return file_content.decode('utf-8')
        except UnicodeDecodeError:
            # 윈도우 메모장에서 작성된 한국어 파일 대응을 위한 CP949 Fallback
            return file_content.decode('cp949', errors='ignore')
    else:
        # 향후 엑셀(openpyxl) 및 HWP 등 추가 확장에 대비
        try:
            return file_content.decode('utf-8')
        except UnicodeDecodeError:
            return file_content.decode('cp949', errors='ignore')

def _extract_text_from_pdf(file_content: bytes) -> str:
    text = ""
    # 임시 파일 생성
    with tempfile.NamedTemporaryFile(delete=False, suffix=".pdf") as temp_file:
        temp_file.write(file_content)
        temp_file_path = temp_file.name
        
    try:
        with pdfplumber.open(temp_file_path) as pdf:
            for page in pdf.pages:
                page_text = page.extract_text()
                if page_text:
                    text += page_text + "\n"
    finally:
        os.remove(temp_file_path)
    return text

def _extract_text_from_docx(file_content: bytes) -> str:
    # 임시 파일 생성
    with tempfile.NamedTemporaryFile(delete=False, suffix=".docx") as temp_file:
        temp_file.write(file_content)
        temp_file_path = temp_file.name
        
    try:
        doc = docx.Document(temp_file_path)
        text = "\n".join([paragraph.text for paragraph in doc.paragraphs])
    finally:
        os.remove(temp_file_path)
    return text
