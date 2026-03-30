package com.antonio.ingredient_again.repository;

import com.antonio.ingredient_again.entity.dish.Dish;
import com.antonio.ingredient_again.entity.ingredient.CategoryEnum;
import com.antonio.ingredient_again.entity.ingredient.Ingredient;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class DishRepository {
    private final DataSource dataSource;

    public DishRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Dish> findAllWithIngredients() {
        String sqlDishes = "SELECT id, name, selling_price FROM dish";
        String sqlIngredients = """
            SELECT i.id, i.name, i.category, i.price
            FROM ingredient i
            JOIN dishingredient di ON di.id_ingredient = i.id
            WHERE di.id_dish = ?
        """;

        List<Dish> dishes = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stDishes = conn.prepareStatement(sqlDishes);
             ResultSet rsDishes = stDishes.executeQuery()) {

            while (rsDishes.next()) {
                Dish dish = new Dish();
                dish.setId(rsDishes.getInt("id"));
                dish.setName(rsDishes.getString("name"));
                dish.setPrice(rsDishes.getDouble("selling_price"));

                // charger les ingredients du plat
                try (PreparedStatement stIngredients = conn.prepareStatement(sqlIngredients)) {
                    stIngredients.setInt(1, dish.getId());
                    ResultSet rsIngredients = stIngredients.executeQuery();

                    List<Ingredient> ingredients = new ArrayList<>();
                    while (rsIngredients.next()) {
                        Ingredient ingredient = new Ingredient();
                        ingredient.setId(rsIngredients.getInt("id"));
                        ingredient.setName(rsIngredients.getString("name"));
                        ingredient.setCategory(CategoryEnum.valueOf(rsIngredients.getString("category").toUpperCase()));
                        ingredient.setPrice(rsIngredients.getDouble("price"));
                        ingredients.add(ingredient);
                    }
                    dish.setIngredients(ingredients);
                }
                dishes.add(dish);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return dishes;
    }
}
