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

    // update ingredients
    public void updateIngredients(Integer dishId, List<Integer> validIds) {
        String sqlDelete = "DELETE FROM dishingredient WHERE id_dish = ?";
        String sqlInsert = "INSERT INTO dishingredient (id_dish, id_ingredient) VALUES (?, ?)";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stDelete = conn.prepareStatement(sqlDelete);
             PreparedStatement stInsert = conn.prepareStatement(sqlInsert)) {

            stDelete.setInt(1, dishId);
            stDelete.executeUpdate();

            for (Integer ingredientId : validIds) {
                stInsert.setInt(1, dishId);
                stInsert.setInt(2, ingredientId);
                stInsert.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean existsById(Integer id) {
        String sql = "SELECT id FROM dish WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            return st.executeQuery().next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Dish findByIdWithIngredients(Integer id) {
        String sqlDish = "SELECT id, name, selling_price FROM dish WHERE id = ?";
        String sqlIngredients = "SELECT i.id, i.name, i.category, i.price " +
                "FROM ingredient i " +
                "JOIN dishingredient di ON di.id_ingredient = i.id " +
                "WHERE di.id_dish = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stDish = conn.prepareStatement(sqlDish);
             PreparedStatement stIngredients = conn.prepareStatement(sqlIngredients)) {

            stDish.setInt(1, id);
            ResultSet rsDish = stDish.executeQuery();

            if (!rsDish.next()) return null;

            Dish dish = new Dish();
            dish.setId(rsDish.getInt("id"));
            dish.setName(rsDish.getString("name"));
            dish.setPrice(rsDish.getDouble("selling_price"));

            stIngredients.setInt(1, id);
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
            return dish;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
