package com.rikim.donation.service;

import com.rikim.donation.entity.Donation;
import com.rikim.donation.exception.AccountNotFoundException;
import com.rikim.donation.exception.NotEnoughBalanceException;
import com.rikim.donation.repository.DonationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MoneyDonationGenerator {
    private final AccountService accountService;
    private DonationRepository donationRepository;

    public MoneyDonationGenerator(DonationRepository donationRepository, AccountService accountService) {
        this.donationRepository = donationRepository;
        this.accountService = accountService;
    }

    public Donation generateDonation(long userId, String roomId, long amountToDonate, int dividendCount) throws NotEnoughBalanceException, AccountNotFoundException {
        Donation donation = new Donation(userId, roomId, amountToDonate, dividendCount);
        accountService.withdraw(userId, amountToDonate);
        return donationRepository.insertDonation(donation);
    }
}
