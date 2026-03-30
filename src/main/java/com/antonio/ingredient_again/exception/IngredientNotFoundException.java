package com.antonio.ingredient_again.exception;

public class IngredientNotFoundException extends RuntimeException {
    public IngredientNotFoundException(Integer id) {
        super("Ingredient.id=" + id + " is not found");
    }
}