package com.ashmoday.bets.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_IMPLEMENTED;

public enum BusinessErrorCodes {
    NO_CODE(0, NOT_IMPLEMENTED, "No code"),
    USER_EXISTS(300, FORBIDDEN, "Username already in use"),
    BAD_CREDENTIALS(301, FORBIDDEN, "Login and / or password is incorrect"),

    ;
    @Getter
    private final int code;
    @Getter
    private final String description;
    @Getter
    private final HttpStatus httpStatus;

    BusinessErrorCodes(int code, HttpStatus httpStatus, String description)
    {
        this.code = code;
        this.httpStatus = httpStatus;
        this.description = description;
    }
}
