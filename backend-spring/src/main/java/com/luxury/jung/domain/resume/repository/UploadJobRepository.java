package com.luxury.jung.domain.resume.repository;

import com.luxury.jung.domain.resume.entity.UploadJob;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadJobRepository extends JpaRepository<UploadJob, Long> {
}
