package com.example.currencyconverter.repository;

import com.example.currencyconverter.entity.CurrencyRate;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.postgresql.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = {CurrencyRateRepositoryTest.Initializer.class})
public class CurrencyRateRepositoryTest {

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:11.1")
            .withDatabaseName("currency_rate_db")
            .withUsername("postgres")
            .withPassword("postgres");

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                    "spring.datasource.password=" + postgreSQLContainer.getPassword(),
                    "spring.datasource.driverClassName=" + Driver.class.getCanonicalName()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @Autowired
    CurrencyRateRepository currencyRateRepository;

    @Test
    public void shouldFindCheapestConversionRate() {
        currencyRateRepository.saveAll(List.of(
                prepareCurrencyRate("USD", "JPY", BigDecimal.valueOf(1)),
                prepareCurrencyRate("USD", "EUR", BigDecimal.valueOf(0.9)),
                prepareCurrencyRate("EUR", "JPY", BigDecimal.valueOf(0.9)),
                prepareCurrencyRate("EUR", "CHF", BigDecimal.valueOf(0.9)),
                prepareCurrencyRate("CHF", "JPY", BigDecimal.valueOf(0.9))
        ));

        Optional<BigDecimal> result = currencyRateRepository.findCurrencyRate("USD", "JPY");

        MatcherAssert.assertThat(result, Matchers.is(Optional.of(BigDecimal.valueOf(0.9).pow(3))));
    }

    @Test
    public void shouldNotFindConversionRate() {
        Optional<BigDecimal> result = currencyRateRepository.findCurrencyRate("CAD", "AUD");

        MatcherAssert.assertThat(result, Matchers.is(Optional.empty()));
    }

    private CurrencyRate prepareCurrencyRate(String from, String to, BigDecimal rate) {
        CurrencyRate currencyRate = new CurrencyRate();
        currencyRate.setCurrencyFrom(from);
        currencyRate.setCurrencyTo(to);
        currencyRate.setRate(rate);
        return currencyRate;
    }

}
