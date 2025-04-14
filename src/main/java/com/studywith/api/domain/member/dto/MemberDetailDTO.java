package com.studywith.api.domain.member.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDetailDTO {

    private String nickname;

    private String email;

    private String gender;

    private String birth;

    private String region;

    private String profileImage;

    private String bio;

    private String createDate;

    private String modifyDate;

}
