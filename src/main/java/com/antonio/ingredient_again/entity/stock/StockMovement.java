package com.antonio.ingredient_again.entity.stock;

import java.time.Instant;

public class StockMovement {
    private Integer id;
    private StockValue value;
    private MovementTypeEnum type;
    private Instant creationDataTime;
}
