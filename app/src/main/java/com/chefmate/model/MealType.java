package com.chefmate.model;

public enum MealType {

    MEAT("בשרית"),
    DAIRY("חלבית"),
    VEGETARIAN("צמחונית"),
    VEGAN("טבעונית");

    private String value;

    MealType(String value){
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }
}
