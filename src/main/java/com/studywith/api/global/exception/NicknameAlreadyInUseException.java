package com.studywith.api.global.exception;

import lombok.Getter;

@Getter
public class NicknameAlreadyInUseException extends RuntimeException {

    public NicknameAlreadyInUseException(String message) {
        super(message);
    }

}
