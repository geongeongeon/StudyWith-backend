package com.studywith.api.domain.member.exception;

import lombok.Getter;

@Getter
public class MemberNoChangesException extends RuntimeException {

    public MemberNoChangesException(String message) {
        super(message);
    }

}
