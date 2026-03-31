package com.antonio.ingredient_again.service;

import com.antonio.ingredient_again.dto.DishResponse;
import com.antonio.ingredient_again.dto.IngredientResponse;
import com.antonio.ingredient_again.entity.dish.Dish;
import com.antonio.ingredient_again.exception.DishNotFoundException;
import com.antonio.ingredient_again.repository.DishRepository;
import com.antonio.ingredient_again.repository.IngredientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishService {
    private final DishRepository dishRepository;
    private final IngredientRepository ingredientRepository;

    public DishService(DishRepository dishRepository, IngredientRepository ingredientRepository) {
        this.dishRepository = dishRepository;
        this.ingredientRepository = ingredientRepository;
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

    // update dish with ingredients
    public DishResponse updateIngredients(Integer dishId, List<Integer> ingredientIds) {
        if (!dishRepository.existsById(dishId)) {
            throw new DishNotFoundException(dishId);
        }

        List<Integer> validIds = ingredientIds.stream()
                .filter(ingredientRepository::existsById)
                .toList();

        dishRepository.updateIngredients(dishId, validIds);

        // return le plat mis à jour
        Dish dish = dishRepository.findByIdWithIngredients(dishId);
        List<IngredientResponse> ingredientResponses = dish.getIngredients().stream()
                .map(i -> new IngredientResponse(i.getId(), i.getName(), i.getPrice(), i.getCategory()))
                .toList();
        return new DishResponse(dish.getId(), dish.getName(), dish.getPrice(), ingredientResponses);
    }

    // find dish ingredients
    public List<IngredientResponse> getDishIngredients(Integer dishId, String ingredientName, Double priceAround) {
        if (!dishRepository.existsById(dishId)) {
            throw new DishNotFoundException(dishId);
        }

        return dishRepository.findIngredientsByDishId(dishId, ingredientName, priceAround)
                .stream()
                .map(i -> new IngredientResponse(i.getId(), i.getName(), i.getPrice(), i.getCategory()))
                .toList();
    }
}
