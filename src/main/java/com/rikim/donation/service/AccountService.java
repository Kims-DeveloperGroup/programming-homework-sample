package com.rikim.donation.service;

import com.rikim.donation.entity.Account;
import com.rikim.donation.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AccountService {
    private AccountRepository accountRepository;
    private long WELCOME_GIFT_BALANCE = 10000L;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void withdraw(long userId, final long amount) {
        Account account = accountRepository.find(userId);

        if(account == null) {
            account = new Account(userId , WELCOME_GIFT_BALANCE);
            accountRepository.insert(account);
        }
        accountRepository.updateBalance(userId, - amount);
    }

    public void deposit(long userId, final long amount) {
        Account account = accountRepository.find(userId);

        if(account == null) {
            account = new Account(userId , WELCOME_GIFT_BALANCE);
            accountRepository.insert(account);
        }
        accountRepository.updateBalance(userId, amount);
    }
}