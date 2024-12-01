package com.example.currency_converter.it;

import com.example.currency_converter.CurrencyConverterApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = CurrencyConverterApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CurrencyConverterIT {
    @Autowired
    CacheManager cacheManager;

    @Nested
    @DisplayName("Given a call is made to our local API")
    class GivenCallMadeToLocalAPI {

        @LocalServerPort
        private int port;
        @Autowired
        private TestRestTemplate restTemplate;

        @BeforeEach
        void init() {
            cacheManager.getCache("currency").clear();
        }
        @Nested
        @DisplayName("When the API provides a successful response")
        class WhenApiProvidesSuccessfulResponse {
            @Test
            @DisplayName("Then 10GBP should equal 12.69 USD")
            void then10GBPEqualsXUSD() {
                Double expected = 12.69;
                assertEquals(expected, restTemplate.getForObject(String.format
                        ("http://localhost:%s/convert?currencyFrom=GBP&currencyTo=USD&amount=10",
                        port), Double.class));
            }

            @Test
            @DisplayName("Then 10USD should equal 7.88 GBP")
            void then10USDEqualsXGBP() {
                Double expected = 7.88;
                assertEquals(expected, restTemplate.getForObject(String.format
                        ("http://localhost:%s/convert?currencyFrom=USD&currencyTo=GBP&amount=10",
                                port), Double.class));
            }

            @Test
            @DisplayName("Then 10EUR should equal 8.32 GBP")
            void then10EUREqualsXGBP() {
                Double expected = 8.32;
                assertEquals(expected, restTemplate.getForObject(String.format
                        ("http://localhost:%s/convert?currencyFrom=EUR&currencyTo=GBP&amount=10",
                                port), Double.class));
            }
        }
        @Nested
        @DisplayName("When the API encounters an exception")
        class WhenApiEncountersException {
            // TODO: rejection minus numbers
            @Autowired
            private MockMvc mockMvc;
            private final String CURRENCY_TO_MISSING = "{\"errors\":[\"Please enter currency to convert to\"]}";
            private final String CURRENCY_FROM_MISSING = "{\"errors\":[\"Please enter currency to convert from\"]}";
            private final String CURRENCY_FROM_TO_MISSING =
                    "{\"errors\":[\"Please enter currency to convert from\",\"Please enter currency to convert to\"]}";

            private final String CURRENCY_NOT_FOUND = "{\"errors\":\"Currency not found: TEST !\"}";

            @Test
            @DisplayName("Then missing request params should return 400 Bad request with validation messages")
            void thenMissingReqParamsReturn400Status() throws Exception {
                // Doesn't cover every possibility for time saving
                mockMvc.perform(get(String.format
                        ("http://localhost:%s/convert?currencyFrom=EUR&currencyTo=&amount=10",
                                port))).andExpect(status().is4xxClientError()).andExpect(content().json(CURRENCY_TO_MISSING));
                mockMvc.perform(get(String.format
                        ("http://localhost:%s/convert?currencyFrom=&currencyTo=GBP&amount=10",
                                port))).andExpect(status().is4xxClientError()).andExpect(content().json(CURRENCY_FROM_MISSING));
                mockMvc.perform(get(String.format
                        ("http://localhost:%s/convert?currencyFrom=&currencyTo=&amount=10",
                                port))).andExpect(status().is4xxClientError()).andExpect(content().json(CURRENCY_FROM_TO_MISSING));
                mockMvc.perform(get(String.format
                        ("http://localhost:%s/convert",
                                port))).andExpect(status().is4xxClientError());
            }

            @Test
            @DisplayName("then return 400 error when currency is not found")
            void thenReturn400WhenCurrencyNotFound() throws Exception {
                mockMvc.perform(get(String.format
                        ("http://localhost:%s/convert?currencyFrom=TEST&currencyTo=GBP&amount=10",
                                port))).andExpect(status().is4xxClientError()).andExpect(content().json(CURRENCY_NOT_FOUND));

                mockMvc.perform(get(String.format
                        ("http://localhost:%s/convert?currencyFrom=GBP&currencyTo=TEST&amount=10",
                                port))).andExpect(status().is4xxClientError()).andExpect(content().json(CURRENCY_NOT_FOUND));
            }
        }
    }
}
