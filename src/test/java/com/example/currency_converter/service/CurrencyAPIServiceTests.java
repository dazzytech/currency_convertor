package com.example.currency_converter.service;

import com.example.currency_converter.model.CurrencyConversionDTO;
import com.example.currency_converter.util.FileUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest("spring.cache.type=simple")
public class CurrencyAPIServiceTests {
    @Autowired
    CurrencyAPIService currencyAPIService;

    @Autowired
    private CacheManager cacheManager;

    @Nested
    @DisplayName("Given the external Api is called via mock")
    class GivenApiIsCalledWithRestTemplate {
        @Nested
        @DisplayName("When the mock Api provides a successful response")
        class WhenExternalApiReturnsSuccessfulResponse {
            CurrencyConversionDTO currencyConversion;

            @BeforeEach
            void init() throws IOException {
                currencyConversion = FileUtil.loadResourceAsCurrencyConversion
                        ("mock_api_response_GBP.json");
            }

            @Test
            @DisplayName("Then the service will successfully return a new CurrencyConversion instance")
            void thenApiServiceReturnsCurrencyConversionInstance() {
                assertNull(cacheManager.getCache("currency")
                        .get("GBP"));
                CurrencyConversionDTO actual = currencyAPIService.getRates("GBP");
                assertEquals(currencyConversion, actual);
                assertNotNull(cacheManager.getCache("currency")
                        .get("GBP"));
            }

            @Test
            @DisplayName("Then the service will retrieve the item again from cache")
            void thenCacheReturnsCurrencyConvertionInstance() {
                // Ensure we have our object in cache
                assertNotNull(cacheManager.getCache("currency")
                        .get("GBP"));
                CurrencyConversionDTO cache = cacheManager.getCache("currency")
                        .get("GBP", CurrencyConversionDTO.class);
                // Test that we are retrieving the same as is in cash
                CurrencyConversionDTO actual = currencyAPIService.getRates("GBP");
                assertEquals(currencyConversion, actual);
                assertSame(cache, actual);
                // pass different param to make service create new object and
                // check this is difference to the cached value
                actual = currencyAPIService.getRates("EUR");
                assertEquals(currencyConversion, actual);
                assertNotSame(cache, actual);
            }
        }
    }
}