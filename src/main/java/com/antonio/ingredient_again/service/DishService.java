package com.antonio.ingredient_again.service;

import com.antonio.ingredient_again.dto.DishResponse;
import com.antonio.ingredient_again.dto.IngredientResponse;
import com.antonio.ingredient_again.entity.dish.Dish;
import com.antonio.ingredient_again.repository.DishRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishService {
    private final DishRepository dishRepository;

    public DishService(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    public List<DishResponse> getAllDishes() {
        List<Dish> dishes = dishRepository.findAllWithIngredients();

        return dishes.stream().map(dish -> {
            List<IngredientResponse> ingredientResponses = dish.getIngredients().stream()
                    .map(i -> new IngredientResponse(i.getId(), i.getName(), i.getPrice(), i.getCategory()))
                    .toList();
            return new DishResponse(dish.getId(), dish.getName(), dish.getPrice(), ingredientResponses);
        }).toList();
    }
}
