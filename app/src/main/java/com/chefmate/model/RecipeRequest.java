package com.chefmate.model;

import com.chefmate.spinner.SpinnerItem;

import java.io.Serializable;

public class RecipeRequest extends Identity implements Serializable {
    private int diners;
    private CookTime time;
    private MealType mealType;
    private String groceries;

    public RecipeRequest(String id, int diners, MealType mealtype, CookTime time, String groceries) {
        super(id);
        this.diners = diners;
        this.mealType = mealtype;
        this.time = time;
        this.groceries = groceries;
    }

    public int getDiners() {
        return this.diners;
    }

    public MealType getMealType(){ return this.mealType;}

    public CookTime getTime() {
        return this.time;
    }

    public String getGroceries() {
        return this.groceries;
    }
}
