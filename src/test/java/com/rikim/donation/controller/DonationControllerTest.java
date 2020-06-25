package com.rikim.donation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rikim.donation.controller.requestbody.DonationGenerationRequestBody;
import com.rikim.donation.entity.Donation;
import com.rikim.donation.repository.DonationRepository;
import com.rikim.donation.service.AccountService;
import com.rikim.donation.service.MoneyDonationGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class DonationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MoneyDonationGenerator moneyDonationGenerator;

    @MockBean
    private AccountService accountService;

    @MockBean
    private DonationRepository donationRepository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void generateDonation_whenDonationIsSuccessfullyGenerated_thenResponding204WithDonationId() throws Exception {
        // Given
        int userId = 1001;
        String roomId = "test-room-id";
        long amountToDonate = 1000;
        int dividendCount = 10;
        String expectedDonationToken = "TOK";

        Donation generatedDonation = new Donation(userId, roomId, amountToDonate, dividendCount);
        generatedDonation.setId(expectedDonationToken);
        when(moneyDonationGenerator.generateDonation(userId, roomId, amountToDonate, dividendCount))
                .thenReturn(generatedDonation);

        String body = objectMapper.writeValueAsString(new DonationGenerationRequestBody(amountToDonate, dividendCount));

        // When
        MockHttpServletRequestBuilder request =
                post("/donations")
                        .header("X-USER-ID", userId).header("X-ROOM-ID", roomId)
                        .contentType(MediaType.APPLICATION_JSON).content(body);
        ResultActions result = this.mockMvc.perform(request);

        // Then
        result.andDo(print()).andExpect(status().isCreated())
                .andExpect(content().string(containsString(expectedDonationToken)));
    }
}