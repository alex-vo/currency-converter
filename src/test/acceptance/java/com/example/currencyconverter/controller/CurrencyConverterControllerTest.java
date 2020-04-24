package com.example.currencyconverter.controller;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.postgresql.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ContextConfiguration(initializers = {CurrencyConverterControllerTest.Initializer.class})
public class CurrencyConverterControllerTest {

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:11.1")
            .withDatabaseName("currency_rate_db")
            .withUsername("postgres")
            .withPassword("postgres");

    @Autowired
    MockMvc mvc;

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

    @Test
    public void shouldIdentifyLatvianNumber() throws Exception {
        String responseBody = mvc.perform(MockMvcRequestBuilders
                .post("/api/v1/convertCurrency")
                .param("currencyFrom", "USD")
                .param("currencyTo", "EUR")
                .param("amount", "10"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        MatcherAssert.assertThat(BigDecimal.valueOf(Double.parseDouble(responseBody)), Matchers.closeTo(BigDecimal.valueOf(5), BigDecimal.valueOf(0.0001)));
    }

}
