package com.studywith.api.domain.member.exception;

import lombok.Getter;

@Getter
public class MemberNotFoundException extends RuntimeException {

    public MemberNotFoundException(String message) {
        super(message);
    }

}
