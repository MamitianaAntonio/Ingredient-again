package com.antonio.ingredient_again.controller;

import com.antonio.ingredient_again.dto.IngredientResponse;
import com.antonio.ingredient_again.dto.StockValueResponse;
import com.antonio.ingredient_again.entity.ingredient.UnitEnum;
import com.antonio.ingredient_again.exception.BadRequestException;
import com.antonio.ingredient_again.exception.IngredientNotFoundException;
import com.antonio.ingredient_again.repository.IngredientRepository;
import com.antonio.ingredient_again.service.IngredientService;
import com.antonio.ingredient_again.validator.StockQueryValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {
    private final IngredientService service;
    private final StockQueryValidator stockQueryValidator;

    public IngredientController(IngredientService service, StockQueryValidator stockQueryValidator) {
        this.service = service;
        this.stockQueryValidator = stockQueryValidator;
    }

    @GetMapping
    public ResponseEntity<List<IngredientResponse>> getIngredients () {
        return ResponseEntity.status(HttpStatus.OK)
                .body(service.getAllIngredients());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getIngredientById(@PathVariable Integer id) {
        try {
            IngredientResponse ingredientResponse = service.getIngredientById(id);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ingredientResponse);
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/{id}/stock")
    public ResponseEntity<?> getStock(
            @PathVariable Integer id,
            @RequestParam(required = false) Instant at,
            @RequestParam(required = false) String unit
    ) {
        try {
            stockQueryValidator.validateStockQuery(at, unit);
            UnitEnum targetUnit = UnitEnum.valueOf(unit.toUpperCase());
            StockValueResponse response = service.getStockAt(id, at, targetUnit);
            return ResponseEntity.ok(response);
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IngredientNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
