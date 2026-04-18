package com.luxury.jung.domain.resume.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * 이력서에서 추출된 사용 기술 스택 엔티티 (검색 최적화 분리형)
 */
@Entity
@Table(name = "resume_tech_stack")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ResumeTechStack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;

    @Column(name = "tech_name", nullable = false, length = 100)
    private String techName;
}
