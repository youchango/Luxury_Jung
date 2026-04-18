package com.luxury.jung.domain.resume.dto;

import com.luxury.jung.domain.resume.entity.Resume;
import com.luxury.jung.domain.resume.entity.ResumeTechStack;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 이력서 검색 목록 화면(Table/Grid 등)에 전달할 가벼운 응답 DTO
 */
@Data
public class ResumeListResponseDto {
    private Long id;
    private String name;
    private Integer yearsOfExperience;
    private LocalDate careerStartDate;
    private List<String> techStacks;
    
    // DB Entity를 안전한 응답 DTO 포맷으로 변환하는 변환자 팩토리 메서드
    public static ResumeListResponseDto from(Resume resume) {
        ResumeListResponseDto dto = new ResumeListResponseDto();
        dto.setId(resume.getId());
        dto.setName(resume.getName());
        dto.setYearsOfExperience(resume.getYearsOfExperience());
        dto.setCareerStartDate(resume.getCareerStartDate());
        
        // 지연 로딩된 기술 스택들을 문자열 리스트로 플랫(Flat)화
        if (resume.getTechStacks() != null) {
            dto.setTechStacks(resume.getTechStacks().stream()
                    .map(ResumeTechStack::getTechName)
                    .collect(Collectors.toList()));
        }
        return dto;
    }
}
