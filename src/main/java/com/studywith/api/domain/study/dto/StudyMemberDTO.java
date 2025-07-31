package com.studywith.api.domain.study.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class StudyMemberDTO {

    private Long memberId;

    private String nickname;

    private String gender;

    private String birth;

    private String region;

    private String profileImage;

    private String bio;

    private String joinDate;

    private String myRole;

}
