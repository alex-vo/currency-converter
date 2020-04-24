package com.example.currencyconverter.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@Table(name = "currency_rate")
public class CurrencyRate {

    @Id
    @SequenceGenerator(name = "currency_rate_seq", sequenceName = "currency_rate_seq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(generator = "currency_rate_seq")
    private Long id;
    @Column(name = "currency_from")
    private String currencyFrom;
    @Column(name = "currency_to")
    private String currencyTo;
    private BigDecimal rate;

}
