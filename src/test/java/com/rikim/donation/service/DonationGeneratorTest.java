package com.rikim.donation.service;

import com.rikim.donation.entity.Account;
import com.rikim.donation.entity.Dividend;
import com.rikim.donation.entity.Donation;
import com.rikim.donation.exception.DonationUpdateException;
import com.rikim.donation.exception.InvalidDonationGrantException;
import com.rikim.donation.repository.DonationRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DonationGeneratorTest {
    @InjectMocks
    private DonationGenerator donationGenerator;

    @Mock
    private DonationRepository donationRepository;

    @Mock
    private AccountService accountService;

    @Before
    public void initMoneyDonationGenerator() {
        this.donationGenerator = new DonationGenerator(donationRepository, accountService);
        when(donationRepository.insertDonation(any(Donation.class)))
                .then(invocationOnMock -> invocationOnMock.getArgument(0, Donation.class));
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

    @Test
    public void grantDividend_thenDividendAmountShouldBeReturned() throws Exception {
        // Given
        String donationId = "donation-id";
        long userId = 1001;
        String roomId = "x-room-id-1";
        long amountToDonate = 100;
        int expectedDividendAmount = 100;

        Donation donation = new Donation(1L, roomId, amountToDonate, 1);

        mockAllMethodsInGrantDividend(userId, roomId, donationId, donation, expectedDividendAmount, false, false);
        // When
        long actualDividendAmount = donationGenerator.grantDividend(donationId, userId, roomId);

        // Then
        verify(accountService, times(1)).deposit(userId, amountToDonate);
        assertThat(actualDividendAmount).isEqualTo(expectedDividendAmount);
    }

    @Test(expected = InvalidDonationGrantException.class)
    public void grantDividend_whenGratingUserOwnDividend_thenExceptionShouldBeThrown() throws Exception {
        // Given
        String donationId = "donation-id";
        long doneeId = 1001;
        String roomId = "x-room-id-1";
        long amountToDonate = 100;
        int expectedDividendAmount = 100;

        Donation userOwnDonation = new Donation(doneeId, roomId, amountToDonate, 1);

        mockAllMethodsInGrantDividend(doneeId, roomId, donationId, userOwnDonation, expectedDividendAmount, false, false);
        // When
        long actualDividendAmount = donationGenerator.grantDividend(donationId, doneeId, roomId);

        // Then
    }

    @Test(expected = InvalidDonationGrantException.class)
    public void grantDividend_whenUserHasAlreadyTakenTheDonation_thenExceptionShouldBeThrown() throws Exception {
        // Given
        boolean hasTakenDonation = true;
        String donationId = "donation-id";
        long doneeId = 1001;
        String roomId = "x-room-id-1";
        long amountToDonate = 100;
        int expectedDividendAmount = 100;

        Donation donation = new Donation(999, roomId, amountToDonate, 1);

        mockAllMethodsInGrantDividend(doneeId, roomId, donationId, donation, expectedDividendAmount, hasTakenDonation, false);
        // When
        long actualDividendAmount = donationGenerator.grantDividend(donationId, doneeId, roomId);

        // Then
    }

    @Test(expected = DonationUpdateException.class)
    public void grantDividend_whenFailToUpdateDividendWithDoneeId_ThenExceptionShouldBeThrown() throws Exception {
        // Given
        boolean updateFail = true;
        String donationId = "donation-id";
        long doneeId = 1001;
        String roomId = "x-room-id-1";
        long amountToDonate = 100;
        int expectedDividendAmount = 100;

        Donation donation = new Donation(999, roomId, amountToDonate, 1);

        mockAllMethodsInGrantDividend(doneeId, roomId, donationId, donation, expectedDividendAmount, false, updateFail);
        // When
        long actualDividendAmount = donationGenerator.grantDividend(donationId, doneeId, roomId);

        // Then
    }

    private void mockAllMethodsInGrantDividend(long doneeId, String roomId, String donationId, Donation donation, long dividendAmount,
                                               boolean donationTaken, boolean updateFail) {
        when(donationRepository.findDonation(donationId, roomId))
                .thenReturn(donation);

        when(donationRepository.findDividend(donationId, doneeId))
                .thenAnswer(new Answer<>() {
                    int count = 0;
                    @Override
                    public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                        Dividend grantedDividend = new Dividend(dividendAmount);
                        grantedDividend.setDoneeUserId(doneeId);

                        if (count == 0) {
                            count++;
                            return donationTaken ? grantedDividend : null;
                        }
                        return updateFail ? null : grantedDividend;
                    }
                });

        when(donationRepository.updateDividendDoneeId(donationId, doneeId))
                .thenReturn(true);

    }
}