package com.example.currencyconverter.repository;

import com.example.currencyconverter.entity.CurrencyRate;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CurrencyRateRepositoryTest {

    @Autowired
    CurrencyRateRepository currencyRateRepository;

    @Test
    public void shouldChooseCheapestConversionRate() {
        currencyRateRepository.saveAll(List.of(
                prepareCurrencyRate("USD", "JPY", BigDecimal.valueOf(1)),
                prepareCurrencyRate("USD", "EUR", BigDecimal.valueOf(0.9)),
                prepareCurrencyRate("EUR", "JPY", BigDecimal.valueOf(0.9)),
                prepareCurrencyRate("EUR", "CHF", BigDecimal.valueOf(0.9)),
                prepareCurrencyRate("CHF", "JPY", BigDecimal.valueOf(0.9))
        ));

        Optional<BigDecimal> result = currencyRateRepository.findCurrencyRate("USD", "JPY");

        MatcherAssert.assertThat(result, Matchers.is(BigDecimal.valueOf(0.9).pow(3)));
    }

    private CurrencyRate prepareCurrencyRate(String from, String to, BigDecimal rate) {
        CurrencyRate currencyRate = new CurrencyRate();
        currencyRate.setCurrencyFrom(from);
        currencyRate.setCurrencyTo(to);
        currencyRate.setRate(rate);
        return currencyRate;
    }

}
