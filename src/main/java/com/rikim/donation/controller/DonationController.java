package com.rikim.donation.controller;

import com.rikim.donation.controller.requestbody.DonationGenerationRequestBody;
import com.rikim.donation.entity.Donation;
import com.rikim.donation.exception.AccountNotFoundException;
import com.rikim.donation.exception.NotEnoughBalanceException;
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
        try {
            Donation donation = moneyDonationGenerator.generateDonation(userId, roomId, requestBody.getAmount(), requestBody.getDividendCount());
            return donation.getId();
        } catch (NotEnoughBalanceException e) {
            log.error("Exception occurred while generating donation", e.getMessage());
        } catch (AccountNotFoundException e) {
            log.error("Exception occurred while generating donation", e.getMessage());
        }
        return "";
    }
}