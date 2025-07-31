package com.studywith.api.domain.message.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessageSummaryDTO {

    private Long id;

    private String title;

    private String sender;

    private String study;

    private String senderProfileImage;

    private String sendDate;

    private Boolean isRead;

}
