package com.luxury.jung.domain.resume.repository;

import com.luxury.jung.domain.resume.dto.ResumeSearchCondition;
import com.luxury.jung.domain.resume.entity.Resume;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

// Q-Class 정적 임포트 (컴파일 후 접근 가능)
import static com.luxury.jung.domain.resume.entity.QResume.resume;

@RequiredArgsConstructor
public class ResumeRepositoryImpl implements ResumeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Resume> searchWithConditions(ResumeSearchCondition condition, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        // 1. 최소/최대 연차 조건 (WHERE years_of_experience >= ? AND years_of_experience <= ?)
        if (condition.getMinCareer() != null) {
            builder.and(resume.yearsOfExperience.goe(condition.getMinCareer()));
        }
        if (condition.getMaxCareer() != null) {
            builder.and(resume.yearsOfExperience.loe(condition.getMaxCareer()));
        }
        
        // 2. 다중 기술 스택 조건 (선택한 기술들 중 하나라도 매칭되면 포함 - IN 커리 활용)
        if (condition.getTechStacks() != null && !condition.getTechStacks().isEmpty()) {
            builder.and(resume.techStacks.any().techName.in(condition.getTechStacks()));
        }

        // 3. 언어 및 OS 필터 적용
        if (condition.getLanguage() != null && !condition.getLanguage().isEmpty()) {
            builder.and(resume.languages.any().languageName.eq(condition.getLanguage()));
        }
        
        if (condition.getOs() != null && !condition.getOs().isEmpty()) {
            builder.and(resume.osList.any().osName.eq(condition.getOs()));
        }

        // 메인 데이터 추출 쿼리 + 오프셋 페이징 결합
        List<Resume> content = queryFactory
                .selectFrom(resume)
                .where(builder)
                .orderBy(resume.createdAt.desc()) // 최신순 기본 정렬
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 카운트(Count) 최적화: count(*) 전용 질의 분리 전송
        JPAQuery<Long> countQuery = queryFactory
                .select(resume.count())
                .from(resume)
                .where(builder);

        // 총 개수를 측정해 PageImpl 이라는 규격화된 페이징 객체로 응답
        return new PageImpl<>(content, pageable, countQuery.fetchOne() == null ? 0L : countQuery.fetchOne());
    }
}
