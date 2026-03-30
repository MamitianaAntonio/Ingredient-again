package com.antonio.ingredient_again.dto;

import com.antonio.ingredient_again.entity.ingredient.UnitEnum;

public record StockValueResponse(
        Double quantity,
        UnitEnum unit
) {}
