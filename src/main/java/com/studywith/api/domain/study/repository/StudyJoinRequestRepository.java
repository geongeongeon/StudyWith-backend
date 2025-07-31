package com.studywith.api.domain.study.repository;

import com.studywith.api.domain.study.entity.StudyJoinRequest;
import com.studywith.api.domain.study.entity.id.StudyJoinRequestId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyJoinRequestRepository extends JpaRepository<StudyJoinRequest, StudyJoinRequestId> {

    List<StudyJoinRequest> findByStudyIdOrderByRequestDateDesc(Long id);

    void deleteAllByMemberId(Long id);

}
