package com.rikim.donation.service;

import com.rikim.donation.entity.Dividend;
import com.rikim.donation.entity.Donation;
import com.rikim.donation.exception.DonationGrantConditionException;
import com.rikim.donation.exception.DonationUpdateException;
import com.rikim.donation.repository.DonationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.rikim.donation.exception.DonationGrantConditionExceptionType.*;

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

    public long grantDividend(String donationId, long userId, String roomId) throws DonationGrantConditionException, DonationUpdateException {
        Donation donation = donationRepository.findDonation(donationId, roomId);
        if (donation == null) {
            log.error("Donation({}) does not exist in the room({})", donationId, roomId);
            throw new DonationGrantConditionException(NoDonationInTheRoom);
        } else if (donation.isExpired()) {
          log.warn("Donation {} is expired", donationId);
          throw new DonationGrantConditionException(DonationExpired);
        } else if (donation.getUserId() == userId) {
            log.warn("Users are not allowed to access their own donations.");
            throw new DonationGrantConditionException(UserOwnDonationNotAllowed);
        }

        Dividend grantedDividend = donationRepository.findDividend(donationId, userId);
        if (grantedDividend != null) {
            log.warn("{} already has taken {}", userId, donationId);
            throw new DonationGrantConditionException(DuplicateDonationGrantNotAllowed);
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
