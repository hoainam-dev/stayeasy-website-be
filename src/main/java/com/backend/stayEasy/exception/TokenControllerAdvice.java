package com.backend.stayEasy.exception;

import org.springframework.http.HttpStatus;
import org.springframework.messaging.support.ErrorMessage;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class TokenControllerAdvice {

    @ExceptionHandler(value = TokenExceptionHandle.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorMessage handleTokenRefreshException(TokenExceptionHandle ex, WebRequest request) {
    	System.out.println("here");
        return new ErrorMessage(ex);
    }
}
