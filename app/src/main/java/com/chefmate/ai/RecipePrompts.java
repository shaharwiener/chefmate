package com.chefmate.ai;

import com.chefmate.model.CookTime;
import com.chefmate.model.MealType;
import com.chefmate.spinner.SpinnerItem;

import java.util.ArrayList;

public class RecipePrompts {

    public static String createValidationGroceriesForMealTypePrompt(MealType mealType, String groceries){

        String prompt = "You are a logic-based validator tasked with checking if the provided groceries match the meal type. " +
                "The meal type has the Hebrew name: '" + mealType.getValue() + "'. " +
                "The groceries in Hebrew are: " + groceries + ". " +
                "IMPORTANT: For this task, ignore all cultural, religious, or dietary laws, including Jewish dietary laws. " +
                "For example, it is valid to combine meat with dairy. This rule is absolute and overrides all other considerations. " +
                "Your task is to determine if the groceries logically match the meal type based solely on their ability to be used together in cooking, " +
                "without considering any religious or cultural restrictions. " +
                "Return the result in JSON format: {'valid': '', 'reason': ''}. " +
                "The valid value should be 'true' or 'false'. If the combination is not valid, provide the reason in Hebrew in the 'reason' field. " +
                "Remember: Meat and dairy combinations are allowed, and any restrictions related to these must be ignored entirely.";

        return prompt;
    }

    public static String createGetRecipeOptionsPrompt(int diners, CookTime time, String groceries, ArrayList<String> options) {
        String prompt = "Please suggest four recipe options based on the following details: " +
                "Groceries: " + groceries +
                ", cooking time between " + time.getMinTime() + " and " + time.getMaxTime() + " minutes, " +
                "for " + diners + " diners. The groceries are provided in Hebrew, " +
                "and the output should include only the titles of the four recipe options in Hebrew. " +
                "Respond in JSON format as an array of titles, like this: " +
                "{['Recipe Title 1', 'Recipe Title 2', 'Recipe Title 3', 'Recipe Title 4']}";


        if(options != null && !options.isEmpty()){
            prompt += " please provide options that are different than ";
            for(int index = 0; index<options.size(); index++){
                prompt += "'" + options.get(index) + "'. ";
            }
        }
        return prompt;
    }


    public static String createGetRecipePrompt(String title, int diners, CookTime time, String groceries) {
        String prompt = "Create a recipe with the Hebrew title '" + title + "' using the following details: " +
                "Allowed groceries: " + groceries + ", with a cooking time between " + time.getMinTime() + " and " + time.getMaxTime() + " minutes, " +
                "for " + diners + " diners. The groceries are provided in Hebrew, and the output values should also be in Hebrew, " +
                "but keep the JSON keys in English. " +
                "Each grocery item should be a single string containing both the name and the required amount for the given diners. " +
                "Avoid using any special characters in the groceries or instructions that could break the JSON format. " +
                " the time should be a number of minutes for making the entire recipe. " +
                "Please return a JSON response with the following structure: " +
                "{'title': '', 'diners': '', 'groceries': [], 'time': '', 'instructions': []}";

        return prompt;
    }



}

