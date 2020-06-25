package com.rikim.donation.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.ArrayList;
import java.util.List;

@Data
public class Donation {
    @Id
    @Indexed(unique = true)
    String id;
    final long userId;
    final String roomId;
    final long amount;
    final List<Dividend> dividends;

    public Donation(long userId, String roomId, long amount, int dividendCount) {
        this.userId = userId;
        this.roomId = roomId;
        this.amount = amount;
        dividends = new ArrayList<>(dividendCount);
    }
}