package com.rikim.donation.repository;

import com.rikim.donation.entity.Dividend;
import com.rikim.donation.entity.Donation;

public interface DonationRepository {

    Donation insertDonation(Donation donation);
    Donation findDonation(String donationId, String roomId);
    Dividend findDividend(String donationId, long doneeId);
    boolean updateDividendDoneeId(String donationId, long doneeId);
}
