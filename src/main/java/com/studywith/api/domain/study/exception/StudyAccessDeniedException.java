package com.studywith.api.domain.study.exception;

public class StudyAccessDeniedException extends RuntimeException {

    public StudyAccessDeniedException(String message) {
        super(message);
    }

}
