package com.studywith.api.domain.message.repository;

import com.studywith.api.domain.member.entity.Member;
import com.studywith.api.domain.message.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findAllByReceiverOrderByIdDesc(Member member);

    void deleteAllByReceiver(Member member);

}
