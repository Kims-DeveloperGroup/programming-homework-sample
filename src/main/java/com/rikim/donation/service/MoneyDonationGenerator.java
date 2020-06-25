package com.rikim.donation.service;

import com.rikim.donation.entity.Donation;
import com.rikim.donation.repository.DonationRepository;
import org.springframework.stereotype.Service;

@Service
public class MoneyDonationGenerator {
    private DonationRepository donationRepository;

    public MoneyDonationGenerator(DonationRepository donationRepository) {
        this.donationRepository = donationRepository;
    }

    public Donation generateDonation(long userId, String roomId, long amountToDonate, int dividendCount) {
        Donation donation = new Donation(userId, roomId, amountToDonate, dividendCount);
        return donationRepository.insertDonation(donation);
    }
}
