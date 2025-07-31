package com.studywith.api.domain.message.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessageSendDTO {

    private String title;

    private String content;

    private String receiver;

    private String study;

    private String sendDate;

}
