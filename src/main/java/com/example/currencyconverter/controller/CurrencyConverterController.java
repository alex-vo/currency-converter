package com.example.currencyconverter.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class CurrencyConverterController {

    @PostMapping
    public BigDecimal convertCurrency(@RequestParam String currencyFrom,
                                      @RequestParam String currencyTo,
                                      @RequestParam BigDecimal amount) {
        return null;
    }

}
