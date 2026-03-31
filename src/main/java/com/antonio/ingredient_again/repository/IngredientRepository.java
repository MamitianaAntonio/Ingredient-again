package com.antonio.ingredient_again.repository;

import com.antonio.ingredient_again.entity.ingredient.CategoryEnum;
import com.antonio.ingredient_again.entity.ingredient.Ingredient;
import com.antonio.ingredient_again.entity.ingredient.UnitEnum;
import com.antonio.ingredient_again.entity.stock.MovementTypeEnum;
import com.antonio.ingredient_again.entity.stock.StockMovement;
import com.antonio.ingredient_again.entity.stock.StockValue;
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

    // find ingredient by id
    public Ingredient findIngredientById (Integer id) {
        String sql = "SELECT id, name, price, category FROM Ingredient WHERE id = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                Ingredient i = new Ingredient();
                i.setId(rs.getInt("id"));
                i.setName(rs.getString("name"));
                i.setPrice(rs.getDouble("price"));
                i.setCategory(CategoryEnum.valueOf(rs.getString("category").toUpperCase()));
                return i;
            }

            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // find ingredient by id filtered with time and unit
    public Ingredient findIngredientByIdWithMovements (Integer id) {
        String sqlIngredient = "select id from ingredient where id = ?";
        String sqlMovements = "select id, quantity, unit, type, creation_datetime from stockmovement where id_ingredient = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stIngredient = conn.prepareStatement(sqlIngredient);
             PreparedStatement stMovement = conn.prepareStatement(sqlMovements)) {

            // verify if the ingredient exist
            stIngredient.setInt(1, id);
            ResultSet rsIngredient = stIngredient.executeQuery();
            if (!rsIngredient.next()) return null;
            // collect movements
            stMovement.setInt(1, id);
            ResultSet rsMovements = stMovement.executeQuery();

            List<StockMovement> movements = new ArrayList<>();
            while (rsMovements.next()) {
                StockValue value = new StockValue();
                value.setQuantity(rsMovements.getDouble("quantity"));
                value.setUnit(UnitEnum.valueOf(rsMovements.getString("unit").toUpperCase()));

                StockMovement movement = new StockMovement();
                movement.setId(rsMovements.getInt("id"));
                movement.setValue(value);
                movement.setType(MovementTypeEnum.valueOf(rsMovements.getString("type").toUpperCase()));
                movement.setCreationDataTime(rsMovements.getTimestamp("creation_datetime").toInstant());
                movements.add(movement);
            }

            Ingredient ingredient = new Ingredient();
            ingredient.setId(id);
            ingredient.setStockMovementList(movements);
            return ingredient;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // verify if ingredient doesn't exist
    public boolean existsById(Integer id) {
        String sql = "SELECT id FROM ingredient WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {
            st.setInt(1, id);
            return st.executeQuery().next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
