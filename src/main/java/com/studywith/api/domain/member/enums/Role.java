package com.studywith.api.domain.member.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {

    ADMIN("ROLE_ADMIN", "관리자"), MEMBER("ROLE_MEMBER", "회원");

    private final String key;

    private final String value;

}
