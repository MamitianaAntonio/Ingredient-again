package com.antonio.ingredient_again.service;

import com.antonio.ingredient_again.dto.IngredientResponse;
import com.antonio.ingredient_again.dto.StockValueResponse;
import com.antonio.ingredient_again.entity.ingredient.Ingredient;
import com.antonio.ingredient_again.entity.ingredient.UnitEnum;
import com.antonio.ingredient_again.entity.stock.StockValue;
import com.antonio.ingredient_again.repository.IngredientRepository;
import com.antonio.ingredient_again.validator.IngredientValidator;
import com.antonio.ingredient_again.validator.StockQueryValidator;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class IngredientService {
    private final IngredientRepository repository;
    private final IngredientValidator validator;
    private final StockQueryValidator stockValidator;
    private final StockCalculator stockCalculator;

    public IngredientService(IngredientRepository repository, IngredientValidator validator, StockCalculator stockCalculator, StockQueryValidator stockValidator) {
        this.repository = repository;
        this.validator = validator;
        this.stockCalculator = stockCalculator;
        this.stockValidator = stockValidator;
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


    // get ingredient by stock service
    public StockValueResponse getStockAt(Integer id, Instant at, UnitEnum targetUnit) {
        Ingredient ingredient = repository.findIngredientByIdWithMovements(id);
        ingredient = validator.validateIngredient(ingredient, id);
        StockValue stock = stockCalculator.computeAt(ingredient.getStockMovementList(), at);
        return new StockValueResponse(stock.getQuantity(), targetUnit);
    }
}
