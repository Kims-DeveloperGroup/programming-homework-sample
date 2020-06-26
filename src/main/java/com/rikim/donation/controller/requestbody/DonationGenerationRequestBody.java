package com.rikim.donation.controller.requestbody;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class DonationGenerationRequestBody {
    @NonNull
    private Long amount;
    @NonNull
    private Integer dividendCount;
}