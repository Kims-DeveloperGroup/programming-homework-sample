package com.rikim.donation.repository;

import com.rikim.donation.entity.Account;

public interface AccountRepository {
    Account find(long userId);
}
