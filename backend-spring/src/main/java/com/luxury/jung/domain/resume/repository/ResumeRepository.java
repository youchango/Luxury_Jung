package com.luxury.jung.domain.resume.repository;

import com.luxury.jung.domain.resume.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumeRepository extends JpaRepository<Resume, Long>, ResumeRepositoryCustom {
    // 이제 JpaRepository 기본 제공 메서드는 물론, QueryDSL 전용 searchWithConditions() 지능형 메서드도 함께 사용 가능합니다.
}
