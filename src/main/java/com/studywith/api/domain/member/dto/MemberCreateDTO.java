package com.studywith.api.domain.member.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MemberCreateDTO {

    @NotNull
    private String nickname;

    @NotNull
    private String gender;

    @NotNull
    private String birth;

    @NotNull
    private String region;

    private String profileImage;

    private String bio;

}
