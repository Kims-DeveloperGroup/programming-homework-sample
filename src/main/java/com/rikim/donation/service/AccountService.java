package com.rikim.donation.service;

import com.rikim.donation.entity.Account;
import com.rikim.donation.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AccountService {
    private AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account withdraw(long userId, final long amount) {
        Account account = accountRepository.updateBalance(userId, amount);
        if (account.getBalance() > 0) {
            return accountRepository.updateBalance(userId, amount);
        }
        return account;
    }
}