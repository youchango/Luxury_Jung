package com.luxury.jung.domain.resume.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * 이력서에서 추출된 경력 상세 요약 엔티티
 */
@Entity
@Table(name = "resume_career")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ResumeCareer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String summary;
}
