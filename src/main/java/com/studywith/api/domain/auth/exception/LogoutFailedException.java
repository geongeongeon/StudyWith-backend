package com.studywith.api.domain.auth.exception;

public class LogoutFailedException extends RuntimeException {

    public LogoutFailedException(String message) {
        super(message);
    }

}
