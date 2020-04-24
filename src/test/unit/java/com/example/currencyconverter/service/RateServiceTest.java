package com.example.currencyconverter.service;

import com.example.currencyconverter.exception.NotFoundException;
import com.example.currencyconverter.repository.CurrencyRateRepository;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RateServiceTest {

    @Mock
    CurrencyRateRepository currencyRateRepository;

    @InjectMocks
    RateService rateService;

    @Test
    public void shouldConvertAmountUsingRate() {
        when(currencyRateRepository.updateRate("USD", "JPY", BigDecimal.ONE)).thenReturn(1);

        rateService.updateRate("USD", "JPY", BigDecimal.ONE);

        verify(currencyRateRepository, times(1)).updateRate("USD", "JPY", BigDecimal.ONE);
    }

    @Test
    public void shouldThrowNotFoundExceptionIfRateNotFound() {
        when(currencyRateRepository.updateRate("USD", "JPY", BigDecimal.ONE)).thenReturn(0);

        try {
            rateService.updateRate("USD", "JPY", BigDecimal.ONE);
            fail();
        } catch (NotFoundException e) {
            MatcherAssert.assertThat(e.getMessage(), Matchers.is("rate not found"));
        }

    }

}
