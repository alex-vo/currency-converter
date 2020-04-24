package com.example.currencyconverter.repository;

import com.example.currencyconverter.entity.CurrencyRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

public interface CurrencyRateRepository extends JpaRepository<CurrencyRate, Long> {

    @Query(value = "WITH RECURSIVE r AS ( " +
            "  select currency_from, currency_to, rate " +
            "  from currency_rate  " +
            "  where currency_from=:pCurrencyFrom " +
            "   " +
            "  union " +
            "   " +
            "  SELECT cr.currency_from, cr.currency_to, cr.rate*r.rate " +
            "  FROM currency_rate cr " +
            "  JOIN r ON cr.currency_from = r.currency_to " +
            ") " +
            "SELECT rate  " +
            "FROM r " +
            "where currency_to=:pCurrencyTo " +
            "order by rate " +
            "limit 1", nativeQuery = true)
    Optional<BigDecimal> findCurrencyRate(@Param("pCurrencyFrom") String currencyFrom, @Param("pCurrencyTo") String currencyTo);

}
