package com.antonio.ingredient_again.controller;

import com.antonio.ingredient_again.dto.DishResponse;
import com.antonio.ingredient_again.dto.UpdateDishIngredientsRequest;
import com.antonio.ingredient_again.exception.DishNotFoundException;
import com.antonio.ingredient_again.service.DishService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/dishes")
public class DishController {
    private final DishService dishService;

    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @GetMapping
    public ResponseEntity<List<DishResponse>> getAllDishes() {
        return ResponseEntity.ok(dishService.getAllDishes());
    }

    @PutMapping("/{id}/ingredients")
    public ResponseEntity<?> updateIngredients(
            @PathVariable Integer id,
            @RequestBody(required = false) UpdateDishIngredientsRequest request
    ) {
        if (request == null || request.ingredientIds() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Request body with ingredientIds is required.");
        }

        try {
            List<DishResponse> response = Collections.singletonList(dishService.updateIngredients(id, request.ingredientIds()));
            return ResponseEntity.ok(response);
        } catch (DishNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{id}/ingredients")
    public ResponseEntity<?> getDishIngredients(
            @PathVariable Integer id,
            @RequestParam(required = false) String ingredientName,
            @RequestParam(required = false) Double ingredientPriceAround
    ) {
        try {
            return ResponseEntity.ok(dishService.getDishIngredients(id, ingredientName, ingredientPriceAround));
        } catch (DishNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
