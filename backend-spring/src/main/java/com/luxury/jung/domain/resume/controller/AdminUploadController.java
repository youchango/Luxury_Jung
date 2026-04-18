package com.luxury.jung.domain.resume.controller;

import com.luxury.jung.domain.resume.service.AdminUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/resumes")
@RequiredArgsConstructor
public class AdminUploadController {

    private final AdminUploadService adminUploadService;

    /**
     * 하나만의 특정한 이력서를 올려 AI 파싱을 시키는 Endpoint
     */
    @PostMapping("/upload/single")
    public ResponseEntity<String> uploadSingleResume(
            @AuthenticationPrincipal UserDetails customUserDetails,
            @RequestParam("file") MultipartFile file
    ) {
        String adminEmail = customUserDetails.getUsername();
        Long jobId = adminUploadService.processSingleFile(adminEmail, file);
        return ResponseEntity.ok("이력서 단일 업로드 및 AI 파싱 요청이 완료되었습니다. Job ID: " + jobId);
    }

    /**
     * 화면 상에서 폴더를 드래그 앤 드롭 하거나 파일 여러개를 선택하여 일괄 파싱하는 Endpoint
     */
    @PostMapping("/upload/multi")
    public ResponseEntity<String> uploadMultiResumes(
            @AuthenticationPrincipal UserDetails customUserDetails,
            @RequestParam("files") List<MultipartFile> files
    ) {
        String adminEmail = customUserDetails.getUsername();
        Long jobId = adminUploadService.processMultipleFiles(adminEmail, files);
        return ResponseEntity.ok(files.size() + "개의 이력서 다중 업로드 및 연계 파싱이 완료되었습니다. Job ID: " + jobId);
    }
}
