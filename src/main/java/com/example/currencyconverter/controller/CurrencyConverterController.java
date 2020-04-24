package com.example.currencyconverter.controller;

import com.example.currencyconverter.service.RateConversionService;
import com.example.currencyconverter.service.RateService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
@Validated
public class CurrencyConverterController {

    private final RateConversionService conversionService;
    private final RateService rateService;

    @PostMapping("convertCurrency")
    public BigDecimal convertCurrency(@RequestParam @NotBlank String currencyFrom,
                                      @RequestParam @NotBlank String currencyTo,
                                      @RequestParam @NotNull @Min(0) BigDecimal amount) {
        return conversionService.convert(currencyFrom, currencyTo, amount);
    }

    @PutMapping("updateRate")
    public void updateRate(@RequestParam @NotBlank String currencyFrom,
                           @RequestParam @NotBlank String currencyTo,
                           @RequestParam @NotNull @Min(0) BigDecimal rate) {
        rateService.updateRate(currencyFrom, currencyTo, rate);
    }

}
