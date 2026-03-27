package com.antonio.ingredient_again.entity.dish;

import java.util.ArrayList;

public class Dish {
    private Integer id;
    private String name;
    private Double price;
    private DishTypeEnum dishType;
    private List<Ingredient> ingredients;

    // constructor
    public Dish (int id, String name, Double price, DishTypeEnum dishType) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.dishType = dishType;
        this.ingredients = new ArrayList<>();
    }

    public Dish () {
        this.ingredients = new ArrayList<>();
    }

    // getter and setter
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public DishTypeEnum getDishType() {
        return dishType;
    }

    public void setDishType(DishTypeEnum dishType) {
        this.dishType = dishType;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", dishType=" + dishType +
                ", ingredients=" + ingredients +
                '}';
    }
}
