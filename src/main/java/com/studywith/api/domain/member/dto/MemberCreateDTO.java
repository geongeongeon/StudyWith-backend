package com.studywith.api.domain.member.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberCreateDTO {

    private String nickname;

    private String gender;

    private String birth;

    private String region;

    private String profileImage;

    private String bio;

}
