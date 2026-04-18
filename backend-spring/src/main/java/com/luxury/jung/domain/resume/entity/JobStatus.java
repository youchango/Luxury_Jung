package com.luxury.jung.domain.resume.entity;

/**
 * 파일 다중 업로드 작업에 대한 큐(Queue) 처리 상태를 나타냅니다.
 */
public enum JobStatus {
    PENDING,
    PROCESSING,
    COMPLETED,
    FAILED
}
