package com.luxury.jung.domain.resume.controller;

import com.luxury.jung.domain.resume.dto.ResumeDetailResponseDto;
import com.luxury.jung.domain.resume.dto.ResumeListResponseDto;
import com.luxury.jung.domain.resume.dto.ResumeSearchCondition;
import com.luxury.jung.domain.resume.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/resumes")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;

    /**
     * 사용자 및 관리자가 이력서 다중 조건을 세팅한 뒤에 목록을 호출하는 전용 API
     * 예시 URL: GET /api/v1/resumes?minCareer=3&techStacks=Java,Spring&page=0&size=20
     */
    @GetMapping
    public ResponseEntity<Page<ResumeListResponseDto>> getResumesList(
            ResumeSearchCondition condition,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        Page<ResumeListResponseDto> responsePage = resumeService.searchResumes(condition, pageable);
        
        return ResponseEntity.ok(responsePage);
    }

    /**
     * 단일 이력서에 대한 깊은 상세 뷰(Drawer/Modal 용도) 내용을 응답합니다.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ResumeDetailResponseDto> getResumeDetail(@org.springframework.web.bind.annotation.PathVariable("id") Long id) {
        ResumeDetailResponseDto detail = resumeService.getResumeDetail(id);
        return ResponseEntity.ok(detail);
    }
}
