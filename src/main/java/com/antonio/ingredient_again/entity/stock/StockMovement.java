package com.antonio.ingredient_again.entity.stock;

import java.time.Instant;

public class StockMovement {
    private Integer id;
    private StockValue value;
    private MovementTypeEnum type;
    private Instant creationDatetime;

    public StockMovement(Integer id, StockValue value, MovementTypeEnum type, Instant creationDatetime) {
        this.id = id;
        this.value = value;
        this.type = type;
        this.creationDatetime = creationDatetime;
    }

    public StockMovement() {
        // void constructor
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public StockValue getValue() {
        return value;
    }

    public void setValue(StockValue value) {
        this.value = value;
    }

    public MovementTypeEnum getType() {
        return type;
    }

    public void setType(MovementTypeEnum type) {
        this.type = type;
    }

    public Instant getCreationDatetime() {
        return creationDatetime;
    }

    public void setCreationDataTime(Instant creationDatetime) {
        this.creationDatetime = creationDatetime;
    }

    @Override
    public String toString() {
        return "StockMovement{" +
                "id=" + id +
                ", value=" + value +
                ", type=" + type +
                ", creationDataTime=" + creationDatetime +
                '}';
    }
}
