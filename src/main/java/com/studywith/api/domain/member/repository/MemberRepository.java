package com.studywith.api.domain.member.repository;

import com.studywith.api.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("SELECT MAX(m.id) FROM Member m")
    long getLastId();

    boolean existsByNickname(String nickname);

}
