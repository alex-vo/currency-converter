package com.example.currencyconverter.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@Table(name = "currency_rate")
public class CurrencyRate {

    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "currency_from")
    private String currencyFrom;
    @Column(name = "currency_to")
    private String currencyTo;
    private BigDecimal rate;

}
