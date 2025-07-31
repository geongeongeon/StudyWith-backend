package com.studywith.api.domain.study.exception;

public class StudyMemberAlreadyExistsException extends RuntimeException {

    public StudyMemberAlreadyExistsException(String message) {
        super(message);
    }

}
