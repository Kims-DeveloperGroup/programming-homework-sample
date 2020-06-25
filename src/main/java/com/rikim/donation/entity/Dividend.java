package com.rikim.donation.entity;

import lombok.Data;

@Data
public class Dividend {
    long amount;
    long doneeUserId;

    public Dividend(long amount) {
        this.amount = amount;
    }
}