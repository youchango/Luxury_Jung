import os
import json
from abc import ABC, abstractmethod
from openai import OpenAI
from schemas import ResumeParsedData
from tenacity import retry, wait_exponential, stop_after_attempt

class BaseAIExtractor(ABC):
    """AI 파서의 기본 인터페이스 (Strategy Pattern)"""
    @abstractmethod
    def parse_with_ai(self, raw_text: str) -> ResumeParsedData:
        pass

class OpenAIExtractor(BaseAIExtractor):
    def __init__(self):
        self.client = OpenAI(api_key=os.environ.get("OPENAI_API_KEY"))
        
    @retry(stop=stop_after_attempt(4), wait=wait_exponential(multiplier=1, min=2, max=10), reraise=True)
    def parse_with_ai(self, raw_text: str) -> ResumeParsedData:
        system_prompt = """
        당신은 이력서 데이터를 분석하고 정형화된 JSON 포맷으로 추출하는 전문 HR Assistant AI입니다.
        주어진 텍스트는 지원자의 원본 이력서 내용입니다. 이를 바탕으로 스키마 규칙에 맞추어 추출하세요.
        지원자의 이력서에 관련 내용이 없다면 null 또는 빈 배열([])을 반환해야 합니다. 존재하지 않는 내용을 유추해내서는 안 됩니다.
        특히 '경력(years_of_experience)'은 내용을 기반으로 총 연차를 계산해 오직 '숫자(int)'로만 도출하세요. 신입일 경우 0을 기입합니다.
        """

        resume_schema = ResumeParsedData.model_json_schema()

        tools = [{"type": "function", "function": {"name": "save_resume_data", "description": "이력서 내용을 정형화합니다.", "parameters": resume_schema}}]

        response = self.client.chat.completions.create(
            model="gpt-4o",
            messages=[
                {"role": "system", "content": system_prompt},
                {"role": "user", "content": f"이력서 원본 텍스트:\n\n{raw_text}"}
            ],
            tools=tools,
            tool_choice={"type": "function", "function": {"name": "save_resume_data"}},
            temperature=0.0
        )

        tool_call = response.choices[0].message.tool_calls[0]
        arguments = tool_call.function.arguments
        
        parsed_dict = json.loads(arguments)
        return ResumeParsedData(**parsed_dict)

class GeminiExtractor(BaseAIExtractor):
    def __init__(self):
        self.api_key = os.environ.get("GEMINI_API_KEY")
        # import google.generativeai as genai
        # genai.configure(api_key=self.api_key)
        
    def parse_with_ai(self, raw_text: str) -> ResumeParsedData:
        # TODO: Gemini 1.5 Pro의 Structured Outputs(또는 Function Calling) 연동 로직
        raise NotImplementedError("Gemini 전용 파서는 아직 구체적으로 구현되지 않았습니다. (추후 연동)")

class ClaudeExtractor(BaseAIExtractor):
    def __init__(self):
        self.api_key = os.environ.get("CLAUDE_API_KEY")
        # import anthropic
        # self.client = anthropic.Anthropic(api_key=self.api_key)
        
    def parse_with_ai(self, raw_text: str) -> ResumeParsedData:
        # TODO: Claude 3의 Tool use(Function Calling) 연동 로직
        raise NotImplementedError("Claude 전용 파서는 아직 구체적으로 구현되지 않았습니다. (추후 연동)")

# ---- Factory Method ----
def get_ai_extractor() -> BaseAIExtractor:
    provider = os.environ.get("AI_PROVIDER", "openai").lower()
    
    if provider == "openai":
        return OpenAIExtractor()
    elif provider == "gemini":
        return GeminiExtractor()
    elif provider == "claude":
        return ClaudeExtractor()
    else:
        raise ValueError(f"지원하지 않는 AI 프로바이더입니다: {provider}")

def parse_with_ai(raw_text: str) -> ResumeParsedData:
    """선택된 AI 프로바이더를 통해 텍스트를 파싱합니다."""
    extractor = get_ai_extractor()
    return extractor.parse_with_ai(raw_text)
