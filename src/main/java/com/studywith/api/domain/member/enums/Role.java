package com.studywith.api.domain.member.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Role {

    ADMIN("관리자"), MEMBER("회원");

    private final String displayName;

    @Override
    public String toString() {
        return displayName;
    }

}
