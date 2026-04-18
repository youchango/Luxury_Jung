package com.luxury.jung.domain.resume.dto;

import com.luxury.jung.domain.resume.entity.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class ResumeDetailResponseDto {
    private Long id;
    private String name;
    private Integer yearsOfExperience;
    private String originalFileName;
    private LocalDate careerStartDate;
    private LocalDateTime createdAt;
    
    private List<String> techStacks;
    private List<String> careers;
    private List<String> osList;
    private List<String> languages;
    private List<String> skills;
    private List<String> certificates;
    private String parsedRawText;

    public static ResumeDetailResponseDto from(Resume resume) {
        return ResumeDetailResponseDto.builder()
                .id(resume.getId())
                .name(resume.getName())
                .yearsOfExperience(resume.getYearsOfExperience())
                .originalFileName(resume.getOriginalFileName())
                .careerStartDate(resume.getCareerStartDate())
                .createdAt(resume.getCreatedAt())
                .techStacks(resume.getTechStacks().stream().map(ResumeTechStack::getTechName).collect(Collectors.toList()))
                .careers(resume.getCareers().stream().map(ResumeCareer::getSummary).collect(Collectors.toList()))
                .osList(resume.getOsList().stream().map(ResumeOs::getOsName).collect(Collectors.toList()))
                .languages(resume.getLanguages().stream().map(ResumeLanguage::getLanguageName).collect(Collectors.toList()))
                .skills(resume.getSkills().stream().map(ResumeSkill::getSkillName).collect(Collectors.toList()))
                .certificates(resume.getCertificates().stream().map(ResumeCertificate::getCertificateName).collect(Collectors.toList()))
                .parsedRawText(resume.getParsedRawText())
                .build();
    }
}
