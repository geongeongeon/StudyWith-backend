package com.studywith.api.domain.member.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender {

    M("남성"), F("여성");

    private final String value;

}
