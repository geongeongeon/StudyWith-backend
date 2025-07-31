package com.studywith.api.domain.message.exception;

public class MessageNotFoundException extends RuntimeException {

    public MessageNotFoundException(String message) {
        super(message);
    }

}
