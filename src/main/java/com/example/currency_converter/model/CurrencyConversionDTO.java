package com.example.currency_converter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyConversionDTO implements Serializable {
    String success;
    Long timestamp;
    @Id String base;
    String date;
    Map<String, Double> rates;
}
