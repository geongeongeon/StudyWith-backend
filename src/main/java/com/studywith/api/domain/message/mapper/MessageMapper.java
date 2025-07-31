package com.studywith.api.domain.message.mapper;

import com.studywith.api.domain.member.entity.Member;
import com.studywith.api.domain.message.dto.MessageDetailDTO;
import com.studywith.api.domain.message.dto.MessageSendDTO;
import com.studywith.api.domain.message.dto.MessageSummaryDTO;
import com.studywith.api.domain.message.entity.Message;
import com.studywith.api.domain.study.entity.Study;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class MessageMapper {

    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Message toCreateEntity(MessageSendDTO messageSendDTO, Member sender, Member receiver, Study study) {
        return Message.builder()
                .title(messageSendDTO.getTitle())
                .content(messageSendDTO.getContent())
                .sender(sender)
                .study(study)
                .receiver(receiver)
                .build();
    }

    public MessageSendDTO toSendDTO(Message message) {
        return MessageSendDTO.builder()
                .title(message.getTitle())
                .content(message.getContent())
                .receiver(message.getReceiver().getNickname())
                .study(message.getStudy().getTitle())
                .sendDate(message.getSendDate().format(dateTimeFormatter))
                .build();
    }

    public MessageSummaryDTO toSummaryDTO(Message message) {
        return MessageSummaryDTO.builder()
                .id(message.getId())
                .title(message.getTitle())
                .sender(message.getSender().getNickname())
                .senderProfileImage(message.getSender().getProfileImage())
                .study(message.getStudy().getTitle())
                .sendDate(message.getSendDate().format(dateTimeFormatter))
                .isRead(message.isRead())
                .build();
    }

    public MessageDetailDTO toDetailDTO(Message message) {
        return MessageDetailDTO.builder()
                .content(message.getContent())
                .build();
    }

}
