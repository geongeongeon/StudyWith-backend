package com.studywith.api.domain.study.repository;

import com.studywith.api.domain.study.entity.StudyMember;

import java.util.List;

public interface StudyMemberCustomRepository {

    List<StudyMember> findByStudyIdOrdered(Long studyId);

}
