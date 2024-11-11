package com.chefmate.model;

import java.util.ArrayList;

public class Recipe extends RecipeRequest{
    private String instructions;

    public Recipe(int diners, int time, String groceries, String instructions) {
        super(null,diners, time, groceries);

        this.instructions = instructions;
    }

    public Recipe(String id, int diners, int time, String groceries, String instructions) {
        super(id,diners, time, groceries);

        this.instructions = instructions;
    }

    public String getInstructions() {
        return instructions;
    }
}
