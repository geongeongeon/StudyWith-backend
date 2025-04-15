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
    private String region;

    @NotNull
    private String profileImage;

    @NotNull
    private String bio;

}
