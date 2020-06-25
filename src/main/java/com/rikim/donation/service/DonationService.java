package com.rikim.donation.service;

import com.rikim.donation.entity.Dividend;
import com.rikim.donation.entity.Donation;
import com.rikim.donation.exception.DonationUpdateException;
import com.rikim.donation.exception.InvalidDonationGrantException;
import com.rikim.donation.repository.DonationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DonationService {
    private final AccountService accountService;
    private DonationRepository donationRepository;

    public DonationService(DonationRepository donationRepository, AccountService accountService) {
        this.donationRepository = donationRepository;
        this.accountService = accountService;
    }

    public Donation generateDonation(long userId, String roomId, long amountToDonate, int dividendCount) {
        Donation donation = new Donation(userId, roomId, amountToDonate, dividendCount);
        accountService.withdraw(userId, amountToDonate);
        return donationRepository.insertDonation(donation);
    }

    public long grantDividend(String donationId, long userId, String roomId) throws InvalidDonationGrantException, DonationUpdateException {
        Donation donation = donationRepository.findDonation(donationId, roomId);
        if (donation == null) {
            log.error("Donation({}) does not exist in the room({})", donationId, roomId);
            throw new InvalidDonationGrantException("NoDonationInTheRoom");
        } else if (donation.isExpired()) {
          log.warn("Donation {} is expired", donationId);
          throw new InvalidDonationGrantException("DonationExpired");
        } else if (donation.getUserId() == userId) {
            log.warn("Users are not allowed to access their own donations.");
            throw new InvalidDonationGrantException("UserOwnDonationNotAllowed");
        }

        Dividend grantedDividend = donationRepository.findDividend(donationId, userId);
        if (grantedDividend != null) {
            log.warn("{} already has taken {}", userId, donationId);
            throw new InvalidDonationGrantException("DuplicateGrantNotAllowed");
        }

        boolean isUpdated = donationRepository.updateDividendDoneeId(donationId, userId);
        if (!isUpdated) {
            return 0;
        }
        Dividend dividendGrantedForUserId = donationRepository.findDividend(donationId, userId);
        if (dividendGrantedForUserId == null) {
            throw new DonationUpdateException();
        }
        accountService.deposit(userId, dividendGrantedForUserId.getAmount());
        return dividendGrantedForUserId.getAmount();
    }
}
