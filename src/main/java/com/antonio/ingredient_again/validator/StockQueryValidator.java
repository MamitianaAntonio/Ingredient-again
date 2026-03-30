package com.antonio.ingredient_again.validator;

import com.antonio.ingredient_again.exception.BadRequestException;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class StockQueryValidator {
    public void validateStockQuery (Instant at, String unit) {
        if (at == null || unit == null) {
            throw new BadRequestException("Either mandatory query parameter `at` or `unit` is not provided.");
        }
    }
}
