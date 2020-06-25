package com.rikim.donation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class InvalidDonationGrantException extends Exception {
    public InvalidDonationGrantException(String message) {
        super(message);
    }
}
