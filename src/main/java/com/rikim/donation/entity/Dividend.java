package com.rikim.donation.entity;

import lombok.Data;
import org.springframework.data.annotation.PersistenceConstructor;

@Data
public class Dividend {
    long amount;
    long doneeUserId;

    public Dividend(long amount) {
        this.amount = amount;
    }

    @PersistenceConstructor
    public Dividend(long amount, long doneeUserId) {
        this.amount = amount;
        this.doneeUserId = doneeUserId;
    }
}