package com.antonio.ingredient_again.dto;

import com.antonio.ingredient_again.entity.ingredient.Ingredient;

import java.util.List;

public record DishResponse(
        Integer id,
        String name,
        Double price,
        List<IngredientResponse> ingredientList
) {
}
