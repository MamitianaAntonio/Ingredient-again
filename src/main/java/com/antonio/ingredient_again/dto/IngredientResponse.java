package com.antonio.ingredient_again.dto;

import com.antonio.ingredient_again.entity.ingredient.CategoryEnum;

public record IngredientResponse(
        Integer id,
        String name,
        Double price,
        CategoryEnum category
) {}
