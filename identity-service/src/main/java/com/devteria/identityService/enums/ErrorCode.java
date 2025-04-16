package com.devteria.identityService.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter

public enum ErrorCode {
    //401 xay ra o tang filter nen globalException khong cham duoc vao
    //nhung cai khong ro INTERNAL_SERVER_ERROR
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized exception", HttpStatus.INTERNAL_SERVER_ERROR),
    //thong tin user  HttpStatus.BAD_REQUEST
    INVALID_KEY(1000, "Invalid message key", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1001, "User existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1004, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    DOB_INVALID(1008, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User is not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN);

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private int code;
    private String message;
    private HttpStatusCode statusCode;

}
