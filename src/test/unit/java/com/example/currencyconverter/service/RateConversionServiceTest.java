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
import java.util.Optional;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RateConversionServiceTest {

    @Mock
    CurrencyRateRepository currencyRateRepository;

    @InjectMocks
    RateConversionService rateConversionService;

    @Test
    public void shouldConvertAmountUsingRate() {
        when(currencyRateRepository.findCurrencyRate("USD", "JPY")).thenReturn(Optional.of(BigDecimal.valueOf(0.5)));

        BigDecimal result = rateConversionService.convert("USD", "JPY", BigDecimal.valueOf(10.2));

        MatcherAssert.assertThat(result, Matchers.closeTo(BigDecimal.valueOf(5.1), BigDecimal.valueOf(0.0001)));
    }

    @Test
    public void shouldThrowNotFoundExceptionIfRateNotFound() {
        when(currencyRateRepository.findCurrencyRate("USD", "JPY")).thenReturn(Optional.empty());

        try {
            rateConversionService.convert("USD", "JPY", BigDecimal.valueOf(10.2));
            fail();
        } catch (NotFoundException e) {
            MatcherAssert.assertThat(e.getMessage(), Matchers.is("rate not found"));
        }

    }

}
