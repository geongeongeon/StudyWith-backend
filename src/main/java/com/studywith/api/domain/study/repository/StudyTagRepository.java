package com.studywith.api.domain.study.repository;

import com.studywith.api.domain.study.entity.StudyTag;
import com.studywith.api.domain.study.entity.id.StudyTagId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyTagRepository extends JpaRepository<StudyTag, StudyTagId> {

}
