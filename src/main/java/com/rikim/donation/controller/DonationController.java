package com.rikim.donation.controller;

import com.rikim.donation.controller.requestbody.DonationGenerationRequestBody;
import com.rikim.donation.entity.Donation;
import com.rikim.donation.exception.DonationUpdateException;
import com.rikim.donation.exception.InvalidDonationGrantException;
import com.rikim.donation.service.DonationGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class DonationController {
    private final DonationGenerator donationGenerator;

    public DonationController(DonationGenerator donationGenerator) {
        this.donationGenerator = donationGenerator;
    }

    @PostMapping(path = "/donations", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public String generateDonation(@RequestHeader("X-USER-ID") long userId,
                                   @RequestHeader("X-ROOM-ID") String roomId,
                                   @RequestBody DonationGenerationRequestBody requestBody) {
        if (requestBody.getAmount() <= 0) {
            log.warn("Amount of donation should be greater than zero. userId: {}, amount: {}", userId, requestBody.getAmount());
        }
        Donation donation = donationGenerator.generateDonation(userId, roomId, requestBody.getAmount(), requestBody.getDividendCount());
        return donation.getId();
    }

    @PutMapping(path = "/donations/{donationId}")
    @ResponseStatus(HttpStatus.OK)
    public long bidForDonation(@RequestHeader("X-USER-ID") long userId,
                               @RequestHeader("X-ROOM-ID") String roomId, @PathVariable String donationId) throws InvalidDonationGrantException, DonationUpdateException {
        return donationGenerator.grantDividend(donationId, userId, roomId);
    }
}