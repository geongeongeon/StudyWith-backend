package com.studywith.api.domain.member.exception;

public class MemberNicknameAlreadyInUseException extends RuntimeException {

    public MemberNicknameAlreadyInUseException(String message) {
        super(message);
    }

}
