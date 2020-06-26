package com.rikim.donation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class PermissionNotAllowedAccess extends Exception {
    public PermissionNotAllowedAccess(String message) {
        super(message);
    }
}
