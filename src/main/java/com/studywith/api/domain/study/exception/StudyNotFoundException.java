package com.studywith.api.domain.study.exception;

public class StudyNotFoundException extends RuntimeException {

    public StudyNotFoundException(String message) {
        super(message);
    }

}
