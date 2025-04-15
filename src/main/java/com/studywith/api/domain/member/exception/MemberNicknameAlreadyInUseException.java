package com.studywith.api.domain.member.exception;

import lombok.Getter;

@Getter
public class MemberNicknameAlreadyInUseException extends RuntimeException {

    public MemberNicknameAlreadyInUseException(String message) {
        super(message);
    }

}
