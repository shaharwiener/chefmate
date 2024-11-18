package com.chefmate.model;

import java.util.ArrayList;

public class Recipe extends RecipeRequest{
    private String instructions;

    private int actualCookingTime;

    public Recipe(int diners, MealType mealtype, CookTime time, String groceries, String instructions) {
        this(null,diners, mealtype, time, groceries, instructions);
    }

    public Recipe(String id, int diners, MealType mealtype, CookTime time, String groceries, String instructions) {
        super(id,diners, mealtype, time, groceries);

        this.instructions = instructions;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setActualCookingTime(int actualCookingTime){
        this.actualCookingTime = actualCookingTime;
    }

    public int getActualCookingTime(){
        return this.actualCookingTime;
    }
}
