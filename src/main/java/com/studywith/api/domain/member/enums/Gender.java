package com.studywith.api.domain.member.enums;

import lombok.AllArgsConstructor;
@AllArgsConstructor
public enum Gender {

    M("남성"), F("여성");

    private final String displayName;

    @Override
    public String toString() {
        return displayName;
    }

}
