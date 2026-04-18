package com.luxury.jung.domain.resume.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.MediaType;

@Component
public class PythonAgentClient {
    private final RestClient restClient;

    /**
     * Python FastAPI 파서 에이전트의 Base URL을 환경변수로부터 주입받아 클라이언트를 초기화합니다.
     * 로컬 환경: http://localhost:8000
     * Docker 환경: http://parser:8000 (docker-compose의 AGENT_SERVER_URL 환경변수로 오버라이드됨)
     *
     * @param agentUrl application.yml의 app.agent.url 값
     */
    public PythonAgentClient(@Value("${app.agent.url}") String agentUrl) {
        this.restClient = RestClient.builder().baseUrl(agentUrl).build();
    }

    /**
     * Python 백엔드로 이력서 원본 파일과 작업 메타데이터를 전송합니다.
     * Spring의 최신 RestClient 객체를 활용하여 form-data 형태로 쏴줍니다.
     */
    public void sendToParser(MultipartFile file, Long jobId, String fileUrl) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", file.getResource())
               .header("Content-Disposition", "form-data; name=\"file\"; filename=\"" + new String(file.getOriginalFilename().getBytes(java.nio.charset.StandardCharsets.UTF_8), java.nio.charset.StandardCharsets.ISO_8859_1) + "\"");
        builder.part("job_id", String.valueOf(jobId).getBytes(java.nio.charset.StandardCharsets.UTF_8));
        builder.part("file_url", fileUrl.getBytes(java.nio.charset.StandardCharsets.UTF_8));

        restClient.post()
            .uri("/api/v1/parse")
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .body(builder.build())
            .retrieve()
            .toBodilessEntity();
    }
}
