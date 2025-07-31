package com.studywith.api.domain.study.repository;

import com.studywith.api.domain.study.entity.Study;
import com.studywith.api.domain.study.entity.StudyMember;
import com.studywith.api.domain.study.entity.id.StudyMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyMemberRepository extends JpaRepository<StudyMember, StudyMemberId>, StudyMemberCustomRepository {

    @Query("SELECT sm.study FROM StudyMember sm WHERE sm.member.id = :memberId ORDER BY sm.study.id DESC")
    List<Study> findStudiesByMemberId(Long memberId);

    List<StudyMember> findByStudyId(Long studyId);

    long countByStudyId(Long studyId);

    boolean existsByStudyIdAndMemberLoginId(Long studyId, String loginId);

    void deleteAllByStudyIdAndMemberIdNot(Long studyId, Long memberId);

    StudyMember findByMemberIdAndStudyId(Long memberId, Long studyId);

}
