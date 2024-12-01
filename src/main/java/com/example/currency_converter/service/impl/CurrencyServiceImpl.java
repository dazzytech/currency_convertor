package com.example.currency_converter.service.impl;

import com.example.currency_converter.exception.CurrencyNotFoundException;
import com.example.currency_converter.model.CurrencyConversionDTO;
import com.example.currency_converter.service.CurrencyAPIService;
import com.example.currency_converter.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    @Autowired
    private CurrencyAPIService currencyAPIService;

    /**
     * Converts amount from one currency to the other currency.
     * @param currencyFrom the currency the amount originally represents
     * @param currencyTo the currency that the amount is converted to
     * @param amount the amount of money to convert between currencies
     * @return {@link Double} the amount of money converted to the new currency
     * @throws {@link CurrencyNotFoundException} when either currency is not found in rates
     */
    @Override
    public Double getConversion(
            String currencyFrom,
            String currencyTo,
            Double amount) throws CurrencyNotFoundException {
        CurrencyConversionDTO rates = currencyAPIService.getRates(currencyFrom);

        if(rates != null) {
            if(rates.getRates().containsKey(currencyTo)) {
                return (double) Math.round((rates.getRates().get(currencyTo) * amount) * 100d) / 100d;
            }
            else {
                throw new CurrencyNotFoundException(currencyTo);
            }
        } else {
            throw new CurrencyNotFoundException(currencyFrom);
        }
    }
}
