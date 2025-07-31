package com.studywith.api.domain.message.service;

import com.studywith.api.domain.member.entity.Member;
import com.studywith.api.domain.member.exception.MemberNotFoundException;
import com.studywith.api.domain.member.repository.MemberRepository;
import com.studywith.api.domain.message.dto.MessageDetailDTO;
import com.studywith.api.domain.message.dto.MessageSendDTO;
import com.studywith.api.domain.message.dto.MessageSummaryDTO;
import com.studywith.api.domain.message.entity.Message;
import com.studywith.api.domain.message.exception.MessageAccessDeniedException;
import com.studywith.api.domain.message.exception.MessageNotFoundException;
import com.studywith.api.domain.message.mapper.MessageMapper;
import com.studywith.api.domain.message.repository.MessageRepository;
import com.studywith.api.domain.study.entity.Study;
import com.studywith.api.domain.study.exception.StudyNotFoundException;
import com.studywith.api.domain.study.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final MemberRepository memberRepository;
    private final StudyRepository studyRepository;
    private final MessageMapper messageMapper;

    @Transactional
    public MessageSendDTO createMessage(String loginId, MessageSendDTO messageSendDTO) {
        Member sender = memberRepository.findByLoginId(loginId).orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다."));
        Member receiver = memberRepository.findByNickname(messageSendDTO.getReceiver()).orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다."));
        Study study = studyRepository.findByTitle(messageSendDTO.getStudy()).orElseThrow(() -> new StudyNotFoundException("존재하지 않는 스터디입니다."));

        Message message = messageMapper.toCreateEntity(messageSendDTO, sender, receiver, study);

        return messageMapper.toSendDTO(messageRepository.save(message));
    }

    public List<MessageSummaryDTO> getMessages(String loginId) {
        Member member = memberRepository.findByLoginId(loginId).orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다."));

        List<Message> messages = messageRepository.findAllByReceiverOrderByIdDesc(member);

        return messages.stream()
                .map(messageMapper::toSummaryDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public MessageDetailDTO getMessage(String loginId, Long messageId) {
        Member member = memberRepository.findByLoginId(loginId).orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다."));
        Message message = messageRepository.findById(messageId).orElseThrow(() -> new MessageNotFoundException("존재하지 않는 메시지입니다."));
        if (!message.getReceiver().getId().equals(member.getId())) throw new MessageAccessDeniedException("권한이 없습니다.");

        message.setRead(true);

        return messageMapper.toDetailDTO(message);
    }

    @Transactional
    public void deleteAllMessages(String loginId) {
        Member member = memberRepository.findByLoginId(loginId).orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다."));

        messageRepository.deleteAllByReceiver(member);
    }

    @Transactional
    public void deleteMessage(String loginId, Long messageId) {
        Member member =memberRepository.findByLoginId(loginId).orElseThrow(() -> new MemberNotFoundException("존재하지 않는 회원입니다."));
        Message message = messageRepository.findById(messageId).orElseThrow(() -> new MessageNotFoundException("존재하지 않는 메시지입니다."));
        if (!message.getReceiver().getId().equals(member.getId())) throw new MessageAccessDeniedException("권한이 없습니다.");

        messageRepository.deleteById(message.getId());
    }

}
