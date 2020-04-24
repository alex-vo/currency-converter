package com.example.currencyconverter.startup;

import com.example.currencyconverter.entity.CurrencyRate;
import com.example.currencyconverter.repository.CurrencyRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StartupService {

    private final CurrencyRateRepository currencyRateRepository;

    @EventListener
    public void loadRates(ApplicationReadyEvent e) {
        if (currencyRateRepository.count() > 0L) {
            return;
        }
        currencyRateRepository.saveAll(List.of(
                prepareCurrencyRate("USD", "JPY", BigDecimal.valueOf(1)),
                prepareCurrencyRate("USD", "EUR", BigDecimal.valueOf(0.9)),
                prepareCurrencyRate("EUR", "JPY", BigDecimal.valueOf(0.9)),
                prepareCurrencyRate("JPY", "EUR", BigDecimal.valueOf(0.5)),
                prepareCurrencyRate("EUR", "CHF", BigDecimal.valueOf(0.9)),
                prepareCurrencyRate("CHF", "JPY", BigDecimal.valueOf(0.9))
        ));
    }

    private CurrencyRate prepareCurrencyRate(String from, String to, BigDecimal rate) {
        CurrencyRate currencyRate = new CurrencyRate();
        currencyRate.setCurrencyFrom(from);
        currencyRate.setCurrencyTo(to);
        currencyRate.setRate(rate);
        return currencyRate;
    }

}
