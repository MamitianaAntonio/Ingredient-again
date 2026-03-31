package com.antonio.ingredient_again.dto;

import java.util.List;

public record UpdateDishIngredientsRequest(
        List<Integer> ingredientIds
) {}
