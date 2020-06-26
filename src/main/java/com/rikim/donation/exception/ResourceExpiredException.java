package com.rikim.donation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.GONE)
public class ResourceExpiredException extends Exception {
    public ResourceExpiredException(String message) {
        super(message);
    }
}