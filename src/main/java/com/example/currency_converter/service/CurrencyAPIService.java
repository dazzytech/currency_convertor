package com.example.currency_converter.service;

import com.example.currency_converter.model.CurrencyConversionDTO;

public interface CurrencyAPIService {
    CurrencyConversionDTO getRates(String base);
}
