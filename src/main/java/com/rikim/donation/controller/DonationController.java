package com.rikim.donation.controller;

import com.rikim.donation.controller.requestbody.DonationGenerationRequestBody;
import com.rikim.donation.entity.Donation;
import com.rikim.donation.exception.*;
import com.rikim.donation.service.DonationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class DonationController {
    private final DonationService donationService;

    public DonationController(DonationService donationService) {
        this.donationService = donationService;
    }

    @PostMapping(path = "/donations", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public String generateDonation(@RequestHeader("X-USER-ID") long userId,
                                   @RequestHeader("X-ROOM-ID") String roomId,
                                   @RequestBody DonationGenerationRequestBody requestBody) {
        if (requestBody.getAmount() <= 0) {
            log.warn("Amount of donation should be greater than zero. userId: {}, amount: {}", userId, requestBody.getAmount());
        }
        Donation donation = donationService.generateDonation(userId, roomId, requestBody.getAmount(), requestBody.getDividendCount());
        return donation.getId();
    }

    @PutMapping(path = "/donations/{donationId}")
    @ResponseStatus(HttpStatus.OK)
    public long bidForDonation(@RequestHeader("X-USER-ID") long userId,
                               @RequestHeader("X-ROOM-ID") String roomId, @PathVariable String donationId) throws DonationGrantConditionException, DonationUpdateException {
        return donationService.grantDividend(donationId, userId, roomId);
    }

    @GetMapping(path = "/donations/{donationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Donation findUserDonation(@RequestHeader("X-USER-ID") long userId,
                                 @PathVariable String donationId) throws ResoureceNotFoundException, ResourceExpiredException, PermissionNotAllowedAccess {
        return donationService.findDonation(userId, donationId);
    }
}