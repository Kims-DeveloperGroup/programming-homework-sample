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
        generateId();
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
            Dividend dividend = dividends.get(random.nextInt(dividends.size()));
            dividend.setAmount(dividend.getAmount() + remaining);
        }
        return dividends;
    }

    private void generateId() {
        Random random = new Random();
        StringBuilder idBuilder = new StringBuilder();
        while (idBuilder.length() < 3) {
            int asciiCode = random.nextInt(Character.MAX_VALUE);
            char ch = (char) asciiCode;
            idBuilder.append(ch);
        }
        this.id = idBuilder.toString();
    }
}