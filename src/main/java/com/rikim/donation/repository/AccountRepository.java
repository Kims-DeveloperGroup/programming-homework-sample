package com.rikim.donation.repository;

import com.rikim.donation.entity.Account;

public interface AccountRepository {
    Account find(long userId);

    void insert(Account account);

    void updateBalance(long userId, long amount);
}
