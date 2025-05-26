package com.studywith.api.domain.member.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberUpdateDTO {

    @NotNull
    private String nickname;

    @NotNull
    private String email;

    @NotNull
    private String region;

    private String profileImage;

    @NotNull
    private String bio;

}
