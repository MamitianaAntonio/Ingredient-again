package com.antonio.ingredient_again.service;

import com.antonio.ingredient_again.dto.IngredientResponse;
import com.antonio.ingredient_again.entity.ingredient.Ingredient;
import com.antonio.ingredient_again.repository.IngredientRepository;
import com.antonio.ingredient_again.validator.IngredientValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientService {
    private final IngredientRepository repository;
    private final IngredientValidator validator;

    public IngredientService(IngredientRepository repository, IngredientValidator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    // use response by repository and map to list
    public List<IngredientResponse> getAllIngredients() {
        return repository.findAllIngredient()
                .stream()
                .map(i -> new IngredientResponse(
                        i.getId(),
                        i.getName(),
                        i.getPrice(),
                        i.getCategory()
                ))
                .toList();
    }

    // find by id
    public IngredientResponse getIngredientById (Integer id) {
        Ingredient ingredient = repository.findIngredientById(id);
        ingredient = validator.validateIngredient(ingredient, id);
        return mapToIngredientResponse(ingredient);
    }

    private IngredientResponse mapToIngredientResponse(Ingredient i) {
        return new IngredientResponse(
                i.getId(),
                i.getName(),
                i.getPrice(),
                i.getCategory()
        );
    }
}
