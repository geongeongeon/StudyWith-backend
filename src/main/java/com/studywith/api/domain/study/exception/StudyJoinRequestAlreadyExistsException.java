package com.studywith.api.domain.study.exception;

public class StudyJoinRequestAlreadyExistsException extends RuntimeException {

    public StudyJoinRequestAlreadyExistsException(String message) {
        super(message);
    }

}
