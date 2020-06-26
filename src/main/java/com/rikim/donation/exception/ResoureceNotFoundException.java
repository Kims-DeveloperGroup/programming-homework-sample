package com.rikim.donation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResoureceNotFoundException extends Exception {
    public ResoureceNotFoundException(String message) {
        super(message);
    }
}
