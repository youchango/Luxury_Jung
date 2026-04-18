package com.luxury.jung.domain.resume.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * 이력서에서 추출된 기타 보유 스킬 엔티티
 */
@Entity
@Table(name = "resume_skill")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ResumeSkill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;

    @Column(name = "skill_name", nullable = false, length = 100)
    private String skillName;
}
