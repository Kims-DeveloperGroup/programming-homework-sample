package com.rikim.donation.service;

import com.rikim.donation.entity.Donation;
import com.rikim.donation.repository.DonationRepository;
import com.rikim.donation.service.repository.TestDonationRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;

public class MoneyDonationGeneratorTest {
    private MoneyDonationGenerator moneyDonationGenerator;

    @Mock
    private DonationRepository mockDonationRepository = new TestDonationRepository();

    @Before
    public void initMoneyDonationGenerator() {

        this.moneyDonationGenerator = new MoneyDonationGenerator(mockDonationRepository);
    }

    @Test
    public void whenUserGeneratesDonationForAGivenRoom_thenDonationShouldBeGeneratedWithTheGivenRoomId() {
        // Given
        long userId = 1001;
        String roomId = "x-room-id-1";
        long amountToDonate = 100;
        int dividendCount = 5;

        // When
        Donation generated = moneyDonationGenerator.generateDonation(userId, roomId, amountToDonate, dividendCount);

        // Then
        assertThat(generated.getRoomId()).isEqualTo(roomId);
    }
}