package com.studywith.api.domain.member.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberSummaryDTO {

    private Long id;

    private String nickname;

    private String email;

    private String profileImage;

    private String role;

    private boolean isActive;

}
