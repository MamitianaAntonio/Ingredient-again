package com.antonio.ingredient_again.controller;

import com.antonio.ingredient_again.dto.DishResponse;
import com.antonio.ingredient_again.dto.UpdateDishIngredientsRequest;
import com.antonio.ingredient_again.exception.DishNotFoundException;
import com.antonio.ingredient_again.service.DishService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        if (request == null || request.getIngredientIds() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Request body with ingredientIds is required.");
        }

        try {
            List<DishResponse> response = dishService.updateIngredients(id, request.getIngredientIds());
            return ResponseEntity.ok(response);
        } catch (DishNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
