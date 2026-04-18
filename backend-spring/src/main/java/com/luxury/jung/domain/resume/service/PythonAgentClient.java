package com.luxury.jung.domain.resume.service;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.MediaType;

@Component
public class PythonAgentClient {
    private final RestClient restClient;

    public PythonAgentClient() {
        // Python FastAPI 워커의 base URL (port 8000)
        this.restClient = RestClient.builder().baseUrl("http://localhost:8000").build();
    }

    /**
     * Python 백엔드로 이력서 원본 파일과 작업 메타데이터를 전송합니다.
     * Spring의 최신 RestClient 객체를 활용하여 form-data 형태로 쏴줍니다.
     */
    public void sendToParser(MultipartFile file, Long jobId, String fileUrl) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("file", file.getResource());
        builder.part("job_id", jobId);
        builder.part("file_url", fileUrl);

        restClient.post()
            .uri("/api/v1/parse")
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .body(builder.build())
            .retrieve()
            .toBodilessEntity();
    }
}
