package com.studywith.api.domain.message.exception;

public class MessageAccessDeniedException extends RuntimeException {

    public MessageAccessDeniedException(String message) {
        super(message);
    }

}
