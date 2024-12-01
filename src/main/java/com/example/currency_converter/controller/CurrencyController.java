package com.example.currency_converter.controller;

import com.example.currency_converter.exception.CurrencyNotFoundException;
import com.example.currency_converter.service.CurrencyService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@Validated
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @GetMapping("/convert")
    public ResponseEntity<Double> getConvertedAmount(
            @RequestParam @NotBlank(message = "Please enter currency to convert from") String currencyFrom,
            @RequestParam @NotBlank(message = "Please enter currency to convert to") String currencyTo,
            @RequestParam @NotNull(message = "Please enter an amount") Double amount)
            throws CurrencyNotFoundException {
        return ResponseEntity.ok(currencyService.getConversion(
                currencyFrom, currencyTo, amount));
    }
}
