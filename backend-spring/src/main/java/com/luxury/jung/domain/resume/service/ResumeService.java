package com.luxury.jung.domain.resume.service;

import com.luxury.jung.domain.resume.dto.ResumeListResponseDto;
import com.luxury.jung.domain.resume.dto.ResumeSearchCondition;
import com.luxury.jung.domain.resume.entity.Resume;
import com.luxury.jung.domain.resume.repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 성능 향상을 위한 읽기 전용 트랜잭션 사용
public class ResumeService {
    
    private final ResumeRepository resumeRepository;
    
    /**
     * 필터 조건을 바탕으로 페이징된 이력서 목록을 응답 DTO 규격으로 리턴합니다.
     */
    public Page<ResumeListResponseDto> searchResumes(ResumeSearchCondition condition, Pageable pageable) {
        Page<Resume> resultEntityPage = resumeRepository.searchWithConditions(condition, pageable);
        
        // 데이터 노출을 최소화하기 위해 무거운 원본 Entity가 아닌, 정제된 DTO로 변환하여 리턴
        return resultEntityPage.map(ResumeListResponseDto::from);
    }
}
