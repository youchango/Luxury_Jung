package com.luxury.jung.domain.resume.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * 이력서에서 추출된 자격증 내역 엔티티
 */
@Entity
@Table(name = "resume_certificate")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ResumeCertificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;

    @Column(name = "certificate_name", nullable = false, length = 150)
    private String certificateName;
}
