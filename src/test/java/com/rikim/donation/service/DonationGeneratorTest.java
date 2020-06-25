package com.rikim.donation.service;

import com.rikim.donation.entity.Account;
import com.rikim.donation.entity.Dividend;
import com.rikim.donation.entity.Donation;
import com.rikim.donation.repository.DonationRepository;
import com.rikim.donation.service.repository.TestDonationRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DonationGeneratorTest {
    @InjectMocks
    private DonationGenerator donationGenerator;

    private DonationRepository mockDonationRepository = new TestDonationRepository();

    @Mock
    private AccountService accountService;

    @Before
    public void initMoneyDonationGenerator() {
        this.donationGenerator = new DonationGenerator(mockDonationRepository, accountService);
    }

    @Test
    public void whenUserGeneratesDonationForAGivenRoom_thenDonationShouldBeGeneratedWithTheGivenRoomId() {
        // Given
        long userId = 1001;
        String roomId = "x-room-id-1";
        long amountToDonate = 100;
        int dividendCount = 5;
        Account account = new Account(userId, 1000);
        when(accountService.withdraw(userId, amountToDonate)).thenReturn(account);

        // When
        Donation generated = donationGenerator.generateDonation(userId, roomId, amountToDonate, dividendCount);

        // Then
        assertThat(generated.getRoomId()).isEqualTo(roomId);
    }

    @Test
    public void whenUserGeneratesDonation_thenDividendsShouldBeGeneratedAsManyAsGivenDividendCount() {
        // Given
        long userId = 1001;
        String roomId = "x-room-id-1";
        long amountToDonate = 100;
        int dividendCount = 5;
        Account account = new Account(userId, 1000);
        when(accountService.withdraw(userId, amountToDonate)).thenReturn(account);

        // When
        Donation generated = donationGenerator.generateDonation(userId, roomId, amountToDonate, dividendCount);

        // Then
        assertThat(generated.getDividends()).hasSize(dividendCount);
    }

    @Test
    public void whenUserGeneratesDonation_thenTotalSumOfDividendsAmountShouldBeEqualToAmountOfDonation() {
        // Given
        long userId = 1001;
        String roomId = "x-room-id-1";
        long amountToDonate = 100;
        int dividendCount = 5;
        Account account = new Account(userId, 1000);
        when(accountService.withdraw(userId, amountToDonate)).thenReturn(account);

        // When
        Donation generated = donationGenerator.generateDonation(userId, roomId, amountToDonate, dividendCount);

        // Then
        long sumOfDividendsAmount = generated.getDividends().stream().mapToLong(Dividend::getAmount).sum();
        assertThat(sumOfDividendsAmount).isEqualTo(amountToDonate);
    }

    @Test
    public void whenUserGeneratesDonation_thenAccountBalanceShouldBeReducedAsMuchAsDonationAmount() {
        // Given
        long userId = 1001;
        String roomId = "x-room-id-1";
        long amountToDonate = 100;
        int dividendCount = 5;
        Account account = new Account(userId, 1000);
        when(accountService.withdraw(userId, amountToDonate)).thenReturn(account);
        // When
        Donation generated = donationGenerator.generateDonation(userId, roomId, amountToDonate, dividendCount);

        // Then
        verify(accountService, times(1)).withdraw(userId, amountToDonate);
    }
}