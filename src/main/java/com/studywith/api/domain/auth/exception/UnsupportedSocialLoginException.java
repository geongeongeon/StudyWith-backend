package com.studywith.api.domain.auth.exception;

public class UnsupportedSocialLoginException extends RuntimeException {

    public UnsupportedSocialLoginException(String message) {
        super(message);
    }

}
