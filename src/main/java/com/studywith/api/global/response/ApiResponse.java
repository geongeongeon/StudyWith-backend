package com.studywith.api.global.response;

public record ApiResponse<T>(int code, String message, T data) { }
