package com.luxury.jung.domain.resume.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * 이력서에서 추출된 구사 가능 언어 엔티티
 */
@Entity
@Table(name = "resume_language")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ResumeLanguage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;

    @Column(name = "language_name", nullable = false, length = 100)
    private String languageName;
}
