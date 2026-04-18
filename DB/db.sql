-- 데이터베이스 생성 (MariaDB)
CREATE DATABASE IF NOT EXISTS luxury_jung DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE luxury_jung;

-- 회원 테이블 (USER/ADMIN)
CREATE TABLE `member` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `email` VARCHAR(255) NOT NULL UNIQUE COMMENT '로그인 이메일',
    `password` VARCHAR(255) NOT NULL COMMENT '비밀번호',
    `role` VARCHAR(50) NOT NULL COMMENT '권한 (USER, ADMIN)',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '가입일시'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='회원 정보';

-- 관리자 이력서 업로드 작업 테이블
CREATE TABLE `upload_job` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `admin_id` BIGINT NOT NULL COMMENT '업로드한 관리자 ID',
    `status` VARCHAR(50) NOT NULL COMMENT '상태 (PENDING, PROCESSING, COMPLETED, FAILED)',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '작업시작일시',
    CONSTRAINT `fk_upload_job_admin` FOREIGN KEY (`admin_id`) REFERENCES `member` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='이력서 다중업로드 비동기 작업내역';

-- 정형화된 이력서 메인 테이블
CREATE TABLE `resume` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `job_id` BIGINT NOT NULL COMMENT '해당 이력서를 업로드한 작업 ID',
    `original_file_name` VARCHAR(255) NOT NULL COMMENT '업로드된 원본 파일명',
    `file_url` TEXT NOT NULL COMMENT '원본 파일 다운로드 주소(경로)',
    `name` VARCHAR(100) COMMENT '지원자 이름',
    `birthdate` DATE COMMENT '생년월일',
    `career_start_date` DATE COMMENT '경력 시작 년월일',
    `years_of_experience` INT DEFAULT 0 COMMENT '총 경력(연차)',
    `parsed_raw_text` LONGTEXT COMMENT '원본에서 추출한 가공 전 텍스트 풀본',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '분석/생성일시',
    CONSTRAINT `fk_resume_job` FOREIGN KEY (`job_id`) REFERENCES `upload_job` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='이력서 메인 데이터';

-- 이력서 분석 결과: 기술 스택 (1:N 다대다 구조 분리)
CREATE TABLE `resume_tech_stack` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `resume_id` BIGINT NOT NULL,
    `tech_name` VARCHAR(100) NOT NULL COMMENT '기술 스택명 (e.g. Java, Spring)',
    CONSTRAINT `fk_tech_stack_resume` FOREIGN KEY (`resume_id`) REFERENCES `resume` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='이력서 보유 기술 스택';

-- 이력서 분석 결과: 경력 요약 (1:N)
CREATE TABLE `resume_career` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `resume_id` BIGINT NOT NULL,
    `summary` TEXT NOT NULL COMMENT '개별 직장/경력 요약 내용',
    CONSTRAINT `fk_career_resume` FOREIGN KEY (`resume_id`) REFERENCES `resume` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='이력서 경력 사항';

-- 이력서 분석 결과: 경험 OS (1:N)
CREATE TABLE `resume_os` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `resume_id` BIGINT NOT NULL,
    `os_name` VARCHAR(100) NOT NULL COMMENT 'OS명 (e.g. Linux, Windows)',
    CONSTRAINT `fk_os_resume` FOREIGN KEY (`resume_id`) REFERENCES `resume` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='이력서 사용경험 운영체제';

-- 이력서 분석 결과: 구사 언어 (1:N)
CREATE TABLE `resume_language` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `resume_id` BIGINT NOT NULL,
    `language_name` VARCHAR(100) NOT NULL COMMENT '사용가능 언어명',
    CONSTRAINT `fk_language_resume` FOREIGN KEY (`resume_id`) REFERENCES `resume` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='이력서 사용가능 언어';

-- 이력서 분석 결과: 기타 스킬 (1:N)
CREATE TABLE `resume_skill` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `resume_id` BIGINT NOT NULL,
    `skill_name` VARCHAR(100) NOT NULL COMMENT '기타 직업 스킬',
    CONSTRAINT `fk_skill_resume` FOREIGN KEY (`resume_id`) REFERENCES `resume` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='이력서 기타 소유 스킬';

-- 이력서 분석 결과: 자격증 (1:N)
CREATE TABLE `resume_certificate` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `resume_id` BIGINT NOT NULL,
    `certificate_name` VARCHAR(150) NOT NULL COMMENT '자격증명',
    CONSTRAINT `fk_certificate_resume` FOREIGN KEY (`resume_id`) REFERENCES `resume` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='이력서 보유 자격증';
