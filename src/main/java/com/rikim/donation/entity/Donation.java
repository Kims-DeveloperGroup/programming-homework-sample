package com.rikim.donation.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

        dividends = distributeDividends(dividendCount);
    }

    private List<Dividend> distributeDividends(int dividendCount) {
        List<Dividend> dividends = new ArrayList<>(dividendCount);
        long remaining = amount;
        Random random = new Random();

        for (int i = 0; i < dividendCount; i++) {
            long dividendAmount = (long) (remaining * random.nextFloat());
            remaining -= dividendAmount;
            dividends.add(new Dividend(dividendAmount));
        }
        if (remaining > 0) {
            Dividend dividend = dividends.get(random.nextInt(dividends.size() - 1));
            dividend.setAmount(dividend.getAmount() + remaining);
        }
        return dividends;
    }
}