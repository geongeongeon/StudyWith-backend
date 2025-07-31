package com.studywith.api.domain.study.repository;

import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.studywith.api.domain.member.entity.QMember;
import com.studywith.api.domain.study.entity.QStudy;
import com.studywith.api.domain.study.entity.QStudyMember;
import com.studywith.api.domain.study.entity.StudyMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StudyMemberCustomRepositoryImpl implements StudyMemberCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<StudyMember> findByStudyIdOrdered(Long studyId) {
        QStudyMember studyMember = QStudyMember.studyMember;
        QStudy study = QStudy.study;
        QMember member = QMember.member;

        return jpaQueryFactory
                .selectFrom(studyMember)
                .join(studyMember.study, study)
                .join(studyMember.member, member)
                .where(studyMember.study.id.eq(studyId))
                .orderBy(new CaseBuilder().when(member.id.eq(study.manager.id)).then(1).when(member.id.eq(study.subManager.id)).then(2).otherwise(3).asc(), studyMember.joinDate.asc())
                .fetch();
    }

}
