package com.rikim.donation.service.repository;

import com.rikim.donation.entity.Account;
import com.rikim.donation.repository.AccountRepository;

public class TestAccountRepository implements AccountRepository {
    private final long AMOUNT;

    public TestAccountRepository(long amountToInitAccount) {
        this.AMOUNT = amountToInitAccount;
    }

    @Override
    public Account find(long userId) {
        return new Account(userId, AMOUNT);
    }
}
