package com.chefmate.model;

import java.io.Serializable;
import java.util.ArrayList;

public class RecipeRequest extends Identity implements Serializable {
    private int diners;
    private int time;
    private String groceries;

    public RecipeRequest(String id, int diners, int time, String groceries) {
        super(id);

        this.diners = diners;
        this.time = time;
        this.groceries = groceries;
    }

    public int getDiners() {
        return diners;
    }

    public int getTime() {
        return time;
    }

    public String getGroceries() {
        return groceries;
    }
}
