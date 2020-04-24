package com.example.currencyconverter.service;

import com.example.currencyconverter.exception.NotFoundException;
import com.example.currencyconverter.repository.CurrencyRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class RateService {

    private final CurrencyRateRepository currencyRateRepository;

    public void updateRate(String currencyFrom, String currencyTo, BigDecimal rate) {
        int updated = currencyRateRepository.updateRate(currencyFrom, currencyTo, rate);
        if (updated != 1) {
            throw new NotFoundException("rate not found");
        }
    }

}
