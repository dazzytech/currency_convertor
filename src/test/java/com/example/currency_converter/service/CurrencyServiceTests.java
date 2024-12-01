package com.example.currency_converter.service;

import com.example.currency_converter.exception.CurrencyNotFoundException;
import com.example.currency_converter.model.CurrencyConversionDTO;
import com.example.currency_converter.service.impl.CurrencyServiceImpl;
import com.example.currency_converter.util.FileUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.clearAllCaches;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CurrencyServiceTests {
    @Mock
    CurrencyAPIService currencyAPIService;

    @InjectMocks
    CurrencyService currencyService = new CurrencyServiceImpl();

    @Nested
    @DisplayName("Given the service is called")
    class GivenServiceIsCalled {
        @Nested
        @DisplayName("When the appropriate variables are supplied")
        class WhenAppropriateVariablesSupplied {
            CurrencyConversionDTO currencyConversionGBP = FileUtil.loadResourceAsCurrencyConversion
                    ("mock_api_response_GBP.json");
            CurrencyConversionDTO currencyConversionEUR = FileUtil.loadResourceAsCurrencyConversion
                    ("mock_api_response_EUR.json");
            CurrencyConversionDTO currencyConversionUSD = FileUtil.loadResourceAsCurrencyConversion
                    ("mock_api_response_USD.json");

            WhenAppropriateVariablesSupplied() throws IOException { }

            @BeforeEach
            void init() throws IOException {
                when(currencyAPIService.getRates("GBP")).thenReturn(currencyConversionGBP);
                when(currencyAPIService.getRates("EUR")).thenReturn(currencyConversionEUR);
                when(currencyAPIService.getRates("USD")).thenReturn(currencyConversionUSD);
            }

            @Test
            @DisplayName("Then1GBPShouldEqual1.197899EUR")
            void then1GBPShouldEqualXEUR () throws CurrencyNotFoundException {
                Double expected = 1.20;
                Double actual = currencyService.getConversion("GBP", "EUR", 1.0);
                assertEquals(expected, actual);
            }

            @Test
            @DisplayName("Then1EURShouldEqual0.834795GBP")
            void then1EURShouldEqualXGBP() throws CurrencyNotFoundException {
                Double expected = 0.83;
                Double actual = currencyService.getConversion("EUR", "GBP", 1.0);
                assertEquals(expected, actual);
            }

            @Test
            @DisplayName("Then1USDShouldEqual1.1429800GBP")
            void then1USDShouldEqualXGBP() throws CurrencyNotFoundException {
                Double expected = 0.79;
                Double actual = currencyService.getConversion("USD", "GBP", 1.0);
                assertEquals(expected, actual);
            }

            @Test
            @DisplayName("Then1USDShouldEqualGBPToEUR")
            void then1USDShouldEqualGBPToEUR() throws CurrencyNotFoundException {
                Double expectedGBP = 0.79;
                Double actualGBP = currencyService.getConversion("USD", "GBP", 1.0);
                assertEquals(expectedGBP, actualGBP);
                Double expectedEUR = 0.95;
                Double actualEUR = currencyService.getConversion("GBP", "EUR", actualGBP);
                assertEquals(expectedEUR, actualEUR);
                Double expectedUSD = 1.0;
                Double actualUSD = currencyService.getConversion("EUR", "USD", actualEUR);
                assertEquals(expectedUSD, actualUSD);
            }
        }
        @Nested
        @DisplayName("When invalid parameters are passed to service")
        class WhenInvalidParamtersArePassedToService {
            private final String CURRENCY_NOT_FOUND = "Currency not found: TEST";
            CurrencyConversionDTO currencyConversionUSD = FileUtil.loadResourceAsCurrencyConversion
                    ("mock_api_response_USD.json");

            WhenInvalidParamtersArePassedToService() throws IOException {
            }

            @Test
            @DisplayName("Then will throw exception when invalid currency given")
            void thenWillThrowExceptionWhenInvalidCurrencyGiven() throws CurrencyNotFoundException {
                when(currencyAPIService.getRates("USD")).thenReturn(currencyConversionUSD);
                Exception exception = assertThrows(CurrencyNotFoundException.class, () -> currencyService.getConversion("TEST", "USD", 1.0));
                assertTrue(exception.getMessage().contains(CURRENCY_NOT_FOUND));

                exception = assertThrows(CurrencyNotFoundException.class, () -> currencyService.getConversion("USD", "TEST", 1.0));
                assertTrue(exception.getMessage().contains(CURRENCY_NOT_FOUND));
            }
        }
    }
}
