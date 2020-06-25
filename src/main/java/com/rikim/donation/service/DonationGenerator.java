package com.rikim.donation.service;

import com.rikim.donation.entity.Donation;
import com.rikim.donation.repository.DonationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DonationGenerator {
    private final AccountService accountService;
    private DonationRepository donationRepository;

    public DonationGenerator(DonationRepository donationRepository, AccountService accountService) {
        this.donationRepository = donationRepository;
        this.accountService = accountService;
    }

    public Donation generateDonation(long userId, String roomId, long amountToDonate, int dividendCount) {
        Donation donation = new Donation(userId, roomId, amountToDonate, dividendCount);
        accountService.withdraw(userId, amountToDonate);
        return donationRepository.insertDonation(donation);
    }

    public long grantDividend(String donationId, long userId, String roomId) {
        return 0;
    }
}
