package com.luxury.jung.domain.resume.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * 이력서 다중 필터 조회 시 사용자가 입력하는 조건을 담는 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumeSearchCondition {
    private Integer minCareer;           // 최소 연차 제한
    private Integer maxCareer;           // 최대 연차 제한
    private List<String> techStacks;     // 보유 기술 스택 다중 선택 (AND 혹은 OR 조건 활용 가능)
    private String os;                   // OS 경험 (단일 조건 예시)
    private String language;             // 구사 외국어
}
