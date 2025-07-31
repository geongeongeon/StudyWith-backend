package com.studywith.api.domain.study.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.studywith.api.domain.study.entity.QStudy;
import com.studywith.api.domain.study.entity.Study;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StudyCustomRepositoryImpl implements StudyCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Study> findByKeyword(Pageable pageable, String sortBy, String keyword) {
        QStudy study = QStudy.study;
        OrderSpecifier<?> orderSpecifier = "hot".equals(sortBy) ? study.members.size().desc() : study.id.desc();

        List<Study> results = jpaQueryFactory
                .selectFrom(study)
                .where(
                        study.title.containsIgnoreCase(keyword)
                                .or(study.description.containsIgnoreCase(keyword))
                                .or(study.region.containsIgnoreCase(keyword))
                )
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory
                .select(study.count())
                .from(study)
                .where(
                        study.title.containsIgnoreCase(keyword)
                                .or(study.description.containsIgnoreCase(keyword))
                                .or(study.region.containsIgnoreCase(keyword))
                )
                .fetchOne();

        return new PageImpl<>(results, pageable, total != null ? total : 0);
    }

    @Override
    public Page<Study> findByKeywordAndRecruit(Pageable pageable, String sortBy, String keyword, boolean isRecruit) {
        QStudy study = QStudy.study;
        OrderSpecifier<?> orderSpecifier = "hot".equals(sortBy) ? study.members.size().desc() : study.id.desc();

        List<Study> results = jpaQueryFactory
                .selectFrom(study)
                .where(
                        study.isRecruit.eq(isRecruit)
                                .and(
                                        study.title.containsIgnoreCase(keyword)
                                                .or(study.description.containsIgnoreCase(keyword))
                                                .or(study.region.containsIgnoreCase(keyword))
                                )
                )
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory
                .select(study.count())
                .from(study)
                .where(
                        study.isRecruit.eq(isRecruit)
                                .and(
                                        study.title.containsIgnoreCase(keyword)
                                                .or(study.description.containsIgnoreCase(keyword))
                                                .or(study.region.containsIgnoreCase(keyword))
                                )
                )
                .fetchOne();

        return new PageImpl<>(results, pageable, total != null ? total : 0);
    }

}
