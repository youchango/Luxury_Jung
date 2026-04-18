package com.luxury.jung.domain.resume.entity;

import com.luxury.jung.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 관리자가 이력서를 다중으로 업로드했을 때, 업로드된 고유 작업 이력을 나타냅니다.
 * 작업 상태를 식별하고 비동기 프로세스(Python 서버) 진행 상태를 보여줄 때 활용됩니다.
 */
@Entity
@Table(name = "upload_job")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UploadJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id", nullable = false)
    private Member admin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private JobStatus status;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // 양방향 매핑 (해당 작업에 등록된 이력서들)
    @Builder.Default
    @OneToMany(mappedBy = "uploadJob", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Resume> resumes = new ArrayList<>();

    // Entity 상태 변경 메서드 (Setter 대용)
    public void updateStatus(JobStatus newStatus) {
        this.status = newStatus;
    }
}
