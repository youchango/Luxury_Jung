package com.luxury.jung.domain.resume.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileStorageService {
    private final Path fileStorageLocation;

    public FileStorageService() {
        // 프로젝트 루트 폴더 기준 'uploads'를 스토리지로 사용 (향후 S3 변경 가능)
        this.fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("업로드 폴더를 생성할 수 없습니다.", ex);
        }
    }

    /**
     * MultipartFile을 지정된 물리적 디렉터리에 아카이빙합니다.
     */
    public String storeFile(MultipartFile file) {
        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename() != null ? file.getOriginalFilename() : "unknown");
        String fileName = UUID.randomUUID().toString() + "_" + originalFileName;
        try {
            if (fileName.contains("..")) {
                throw new RuntimeException("파일명에 부적절한 경로가 포함되어 있습니다: " + fileName);
            }
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            // 같은 파일명일 경우 덮어쓰기 (UUID 사용으로 거의 발생안함)
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            
            // 향후 다운로드 가능하도록 매핑될 서버 URL의 템플릿
            return "http://localhost:8080/uploads/" + fileName; 
        } catch (IOException ex) {
            throw new RuntimeException("파일 저장에 실패했습니다: " + fileName, ex);
        }
    }
}
