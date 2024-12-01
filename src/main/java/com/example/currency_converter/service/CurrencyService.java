package com.example.currency_converter.service;

import com.example.currency_converter.exception.CurrencyNotFoundException;

public interface CurrencyService {
    Double getConversion(
            String currencyFrom,
            String currencyTo,
            Double amount) throws CurrencyNotFoundException;
}
