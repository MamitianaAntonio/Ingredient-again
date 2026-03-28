package com.antonio.ingredient_again.repository;

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
public class IngredientRepository {
    private final DataSource dataSource;

    public IngredientRepository (DataSource dataSource) {
        this.dataSource  = dataSource;
    }

    // find all ingredients on db
    public List<Ingredient> findAllIngredient () {
        List<Ingredient> ingredients = new ArrayList<>();
        String sql = "SELECT id, name, price, category from ingredient";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                Ingredient i = new Ingredient();
                i.setId(rs.getInt("id"));
                i.setName(rs.getString("name"));
                i.setPrice(rs.getDouble("price"));
                i.setCategory(CategoryEnum.valueOf(rs.getString("category").toUpperCase()));
                ingredients.add(i);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ingredients;
    }
}
