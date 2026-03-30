package com.antonio.ingredient_again.service;

import com.antonio.ingredient_again.entity.ingredient.UnitEnum;
import com.antonio.ingredient_again.entity.stock.MovementTypeEnum;
import com.antonio.ingredient_again.entity.stock.StockMovement;
import com.antonio.ingredient_again.entity.stock.StockValue;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class StockCalculator {
    public StockValue computeAt(List<StockMovement> movements, Instant timeTarget) {
        StockValue result = new StockValue();
        result.setUnit(UnitEnum.KG);

        double total = 0;
        for (StockMovement movement : movements) {
            if (!movement.getCreationDatetime().isAfter(timeTarget)) {
                if (movement.getType() == MovementTypeEnum.IN) {
                    total += movement.getValue().getQuantity();
                } else if (movement.getType() == MovementTypeEnum.OUT) {
                    total -= movement.getValue().getQuantity();
                }
            }
        }

        result.setQuantity(total);
        return result;
    }
}
