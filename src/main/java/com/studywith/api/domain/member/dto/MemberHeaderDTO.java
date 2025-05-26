package com.studywith.api.domain.member.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MemberHeaderDTO {

    private String nickname;

    private String profileImage;

}
