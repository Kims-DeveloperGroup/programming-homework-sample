package com.rikim.donation.service.repository;

import com.rikim.donation.entity.Donation;
import com.rikim.donation.repository.DonationRepository;

public class TestDonationRepository implements DonationRepository {

    @Override
    public Donation insertDonation(Donation donation) {
        return donation;
    }
}