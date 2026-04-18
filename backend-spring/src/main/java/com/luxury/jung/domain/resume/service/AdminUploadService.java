package com.luxury.jung.domain.resume.service;

import com.luxury.jung.domain.member.entity.Member;
import com.luxury.jung.domain.member.repository.MemberRepository;
import com.luxury.jung.domain.resume.entity.JobStatus;
import com.luxury.jung.domain.resume.entity.UploadJob;
import com.luxury.jung.domain.resume.repository.UploadJobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminUploadService {

    private final UploadJobRepository uploadJobRepository;
    private final MemberRepository memberRepository;
    private final FileStorageService fileStorageService;
    private final PythonAgentClient pythonAgentClient;

    /**
     * 단일 파일 파싱 요청 처리
     */
    @Transactional
    public Long processSingleFile(String adminEmail, MultipartFile file) {
        return processMultipleFiles(adminEmail, List.of(file));
    }

    /**
     * 다중(폴더 단일화) 파일 파싱 반복 처리
     */
    @Transactional
    public Long processMultipleFiles(String adminEmail, List<MultipartFile> files) {
        // 1. 관리자 정보 조회 (Security Context에 보관된 이메일 정보로 매핑)
        Member admin = memberRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new IllegalArgumentException("해당 이메일의 관리자가 없습니다: " + adminEmail));

        // 2. 새로운 UploadJob DB 큐 생성 (PENDING ~ PROCESSING 중간 상태)
        UploadJob job = UploadJob.builder()
                .admin(admin)
                .status(JobStatus.PROCESSING)
                .build();
        uploadJobRepository.save(job);
        
        // 3. 받은 파일들을 순차적으로 아카이빙 후 파이썬 서버로 파싱 명령 요청
        int successCount = 0;
        for(MultipartFile file : files) {
            try {
                // (a) 물리 파일 저장 후 접속 주소 반환 
                String fileUrl = fileStorageService.storeFile(file);
                
                // (b) 파이썬 API로 이력서 토스
                pythonAgentClient.sendToParser(file, job.getId(), fileUrl);
                successCount++;
            } catch(Exception e) {
                log.error("[Job ID: {}] 이력서 실패: {}", job.getId(), file.getOriginalFilename(), e);
                // 다중 파일 중 일부 실패하더라도 반복문은 계속 돌아갈 수 있도록 예외 무시 및 처리
            }
        }
        
        // 4. 모바일 및 백그라운드 태스크로서의 상태 갱신
        if(successCount == 0 && !files.isEmpty()) {
            job.updateStatus(JobStatus.FAILED);
        } else {
            job.updateStatus(JobStatus.COMPLETED);
        }
        
        return job.getId();
    }
}
