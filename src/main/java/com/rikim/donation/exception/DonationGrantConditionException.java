package com.rikim.donation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DonationGrantConditionException extends Exception {
    public DonationGrantConditionException(String message) {
        super(message);
    }

    public DonationGrantConditionException(DonationGrantConditionExceptionType type) {
        super(type.name());
    }
}
