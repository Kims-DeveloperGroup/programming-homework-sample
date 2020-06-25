package com.rikim.donation.service;

import com.rikim.donation.entity.Account;
import com.rikim.donation.entity.Donation;
import com.rikim.donation.exception.AccountNotFoundException;
import com.rikim.donation.exception.NotEnoughBalanceException;
import com.rikim.donation.repository.DonationRepository;
import com.rikim.donation.service.repository.TestDonationRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MoneyDonationGeneratorTest {
    @InjectMocks
    private MoneyDonationGenerator moneyDonationGenerator;

    private DonationRepository mockDonationRepository = new TestDonationRepository();

    @Mock
    private AccountService accountService;

    @Before
    public void initMoneyDonationGenerator() {
        this.moneyDonationGenerator = new MoneyDonationGenerator(mockDonationRepository, accountService);
    }

    @Test
    public void whenUserGeneratesDonationForAGivenRoom_thenDonationShouldBeGeneratedWithTheGivenRoomId() throws NotEnoughBalanceException, AccountNotFoundException {
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

    @Test(expected = NotEnoughBalanceException.class)
    public void whenDonationAmountExceedsTheAmountInTheAccount_thenExceptionShouldBeThrownAndDonationShouldBeGenerated() throws AccountNotFoundException, NotEnoughBalanceException {
        // Given
        long userId = 1001;
        String roomId = "x-room-id-1";
        long amountToDonate = 100;
        int dividendCount = 5;
        Account zeroBalanceAccount = new Account(userId, 0);
        when(accountService.findAccount(userId)).thenReturn(zeroBalanceAccount);

        // When
        moneyDonationGenerator.generateDonation(userId, roomId, amountToDonate, dividendCount);

        // Then
        fail("Exception is expected");
    }
}