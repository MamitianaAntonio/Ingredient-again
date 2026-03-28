package com.antonio.ingredient_again.service;

import com.antonio.ingredient_again.dto.IngredientResponse;
import com.antonio.ingredient_again.repository.IngredientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientService {
    private final IngredientRepository repository;

    public IngredientService(IngredientRepository repository) {
        this.repository = repository;
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
}
