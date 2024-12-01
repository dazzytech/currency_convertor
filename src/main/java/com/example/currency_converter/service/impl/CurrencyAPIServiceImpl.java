package com.example.currency_converter.service.impl;

import com.example.currency_converter.model.CurrencyConversionDTO;
import com.example.currency_converter.service.CurrencyAPIService;
import com.example.currency_converter.util.FileUtil;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class CurrencyAPIServiceImpl implements CurrencyAPIService {
    /**
     * Implements the Mock API call.
     *
     * @return a {@link CurrencyConversionDTO} defined by static/mock_api_response.json
     */
    @Override
    @Cacheable(value = "currency", key = "#base")
    public CurrencyConversionDTO getRates(String base) {
        try {
            CurrencyConversionDTO currencyConversion = buildMockDto(base);
            return currencyConversion;
        } catch (IOException e) {
            return null;
        }
    }

    private CurrencyConversionDTO buildMockDto(String base) throws IOException {
        return FileUtil.loadResourceAsCurrencyConversion(String.format("static/mock_api_response_%s.json", base));
    }
}
