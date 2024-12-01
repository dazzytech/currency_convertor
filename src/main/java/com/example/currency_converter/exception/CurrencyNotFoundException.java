package com.example.currency_converter.exception;

public class CurrencyNotFoundException extends Exception {
    /**
     * Called when {@link com.example.currency_converter.service.CurrencyService} cannot find a currency
     * @param currency the symbol of the currency not found
     */
    public CurrencyNotFoundException(String currency) {
        super(String.format("Currency not found: %s !", currency));
    }
}
