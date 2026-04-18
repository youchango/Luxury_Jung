package com.luxury.jung.domain.resume.repository;

import com.luxury.jung.domain.resume.dto.ResumeSearchCondition;
import com.luxury.jung.domain.resume.entity.Resume;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * QueryDSL을 이용한 복잡한 동적 쿼리 전용 커스텀 인터페이스입니다.
 */
public interface ResumeRepositoryCustom {
    Page<Resume> searchWithConditions(ResumeSearchCondition condition, Pageable pageable);
}
