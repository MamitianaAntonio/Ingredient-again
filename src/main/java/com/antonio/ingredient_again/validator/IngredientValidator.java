package com.antonio.ingredient_again.validator;

import com.antonio.ingredient_again.entity.ingredient.Ingredient;
import com.antonio.ingredient_again.exception.BadRequestException;
import org.springframework.stereotype.Component;

@Component
public class IngredientValidator {
    public Ingredient validateIngredient (Ingredient ingredient, Integer id) {
        if (ingredient == null) {
            throw new BadRequestException("Ingredient.id=" + id + " is not found");
        }
        return ingredient;
    }
}
