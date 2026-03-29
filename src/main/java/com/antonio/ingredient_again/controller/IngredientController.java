package com.antonio.ingredient_again.controller;

import com.antonio.ingredient_again.dto.IngredientResponse;
import com.antonio.ingredient_again.exception.BadRequestException;
import com.antonio.ingredient_again.service.IngredientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ingredients")
public class IngredientController {
    private final IngredientService service;

    public IngredientController(IngredientService service) {
        this.service = service;
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
}
