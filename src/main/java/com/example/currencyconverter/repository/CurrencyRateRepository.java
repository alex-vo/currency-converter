package com.example.currencyconverter.repository;

import com.example.currencyconverter.entity.CurrencyRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

public interface CurrencyRateRepository extends JpaRepository<CurrencyRate, Long> {

    @Query(value = "WITH RECURSIVE r AS ( " +
            "  select currency_from, currency_to, rate, '' as path " +
            "  from currency_rate  " +
            "  where currency_from=:pCurrencyFrom " +
            " " +
            "  union " +
            " " +
            "  SELECT cr.currency_from, cr.currency_to, cr.rate*r.rate, path || '{' || cr.currency_from || '}' as path " +
            "  FROM currency_rate cr " +
            "  JOIN r ON cr.currency_from = r.currency_to " +
            "  where path not like '%{' || cr.currency_from || '}%' and path not like '%{' || cr.currency_to || '}%' " +
            ") " +
            "SELECT rate  " +
            "FROM r " +
            "where currency_to=:pCurrencyTo " +
            "order by rate " +
            "limit 1", nativeQuery = true)
    Optional<BigDecimal> findCurrencyRate(@Param("pCurrencyFrom") String currencyFrom,
                                          @Param("pCurrencyTo") String currencyTo);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update CurrencyRate c set c.rate=:pRate " +
            "where c.currencyFrom=:pCurrencyFrom and c.currencyTo=:pCurrencyTo")
    int updateRate(@Param("pCurrencyFrom") String currencyFrom,
                   @Param("pCurrencyTo") String currencyTo,
                   @Param("pRate") BigDecimal rate);

}
