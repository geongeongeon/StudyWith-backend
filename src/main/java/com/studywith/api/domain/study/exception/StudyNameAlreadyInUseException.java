package com.studywith.api.domain.study.exception;

public class StudyNameAlreadyInUseException extends RuntimeException {

    public StudyNameAlreadyInUseException(String message) {
        super(message);
    }
    
}
