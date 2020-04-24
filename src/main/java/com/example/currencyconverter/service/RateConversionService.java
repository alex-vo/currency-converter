package com.example.currencyconverter.service;

import com.example.currencyconverter.exception.NotFoundException;
import com.example.currencyconverter.repository.CurrencyRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class RateConversionService {

    private final CurrencyRateRepository currencyRateRepository;

    public BigDecimal convert(String currencyFrom, String currencyTo, BigDecimal amount) {
        BigDecimal rate = currencyRateRepository.findCurrencyRate(currencyFrom, currencyTo)
                .orElseThrow(() -> new NotFoundException("rate not found"));
        return rate.multiply(amount);
    }

}
