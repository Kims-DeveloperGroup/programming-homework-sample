package com.rikim.donation.controller;

import com.rikim.donation.controller.requestbody.DonationGenerationRequestBody;
import com.rikim.donation.entity.Donation;
import com.rikim.donation.service.MoneyDonationGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class DonationController {
    private final MoneyDonationGenerator moneyDonationGenerator;

    public DonationController(MoneyDonationGenerator moneyDonationGenerator) {
        this.moneyDonationGenerator = moneyDonationGenerator;
    }

    @PostMapping(path = "/donations", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public String generateDonation(@RequestHeader("X-USER-ID") long userId,
                                   @RequestHeader("X-ROOM-ID") String roomId,
                                   @RequestBody DonationGenerationRequestBody requestBody) {
        if (requestBody.getAmount() <= 0) {
            log.warn("Amount of donation should be greater than zero. userId: {}, amount: {}", userId, requestBody.getAmount());
        }
        Donation donation = moneyDonationGenerator.generateDonation(userId, roomId, requestBody.getAmount(), requestBody.getDividendCount());
        return donation.getId();
    }
}