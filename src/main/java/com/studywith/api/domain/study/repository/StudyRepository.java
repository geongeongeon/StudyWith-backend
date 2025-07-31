package com.studywith.api.domain.study.repository;

import com.studywith.api.domain.study.entity.Study;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudyRepository extends JpaRepository<Study, Long>, StudyCustomRepository {

    List<Study> findByManagerIdOrderByIdDesc(Long memberId);

    @Query("SELECT s FROM Study s LEFT JOIN s.members m GROUP BY s.id ORDER BY COUNT(m) DESC")
    Page<Study> findAllOrderByMemberCount(Pageable pageable);

    Page<Study> findByIsRecruit(boolean isRecruit, Pageable pageable);

    Optional<Study> findByTitle(String title);

    boolean existsByManagerId(Long id);

}
