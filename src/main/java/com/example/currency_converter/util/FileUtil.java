package com.example.currency_converter.util;

import com.example.currency_converter.model.CurrencyConversionDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public final class FileUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String loadResourceAsString(String filePath) throws IOException {
        File resource = new ClassPathResource(filePath).getFile();
        byte[] byteArray = Files.readAllBytes(resource.toPath());
        return new String(byteArray);
    }

    public static CurrencyConversionDTO loadResourceAsCurrencyConversion(String filePath) throws IOException, JsonProcessingException {
        String jsonString = loadResourceAsString(filePath);
        return objectMapper.readValue(jsonString, CurrencyConversionDTO.class);
    }
}
