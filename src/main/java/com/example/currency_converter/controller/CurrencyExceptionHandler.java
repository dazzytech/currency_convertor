package com.example.currency_converter.controller;

import com.example.currency_converter.exception.CurrencyNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class CurrencyExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Called when currency from or to isn't correctly defined in request parameters
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(value = {ConstraintViolationException.class})
    protected ResponseEntity<Object> handleParamViolation(ConstraintViolationException e, WebRequest request) {
        List<String> errors = new ArrayList<>();
        e.getConstraintViolations().forEach(cv -> errors.add(cv.getMessage()));
        Map<String, List<String>> result = new HashMap<>();
        result.put("errors", errors);
        return handleExceptionInternal(e, result, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    /**
     * Called when a currency isn't found within the API
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(value = {CurrencyNotFoundException.class})
    protected ResponseEntity<Object> handleCurrencyException(CurrencyNotFoundException e, WebRequest request) {
        Map<String, String> result = new HashMap<>();
        result.put("errors", e.getMessage());
        return handleExceptionInternal(e, result, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}