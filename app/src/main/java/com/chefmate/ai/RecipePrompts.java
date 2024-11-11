package com.chefmate.ai;

import java.util.ArrayList;

public class RecipePrompts {


    public static String createGetRecipeOptionsPrompt(int diners, int time, String groceries, ArrayList<String> options) {
        String prompt =  "Suggest four options of recipes based on the following details: " +
                "Groceries: " + groceries +
                " and cooking time is up to " + time + " minutes," +
                " and number of diners is ." + diners +
                " The groceries input will be in Hebrew " +
                " and the output should be the titles of the four options in Hebrew. Only the values without keys." +
                " Respond in JSON format with each recipe as a separate element, like this:" +
                "{['Recipe Title 1', 'Recipe Title 2', 'Recipe Title 3', 'Recipe Title 4']}";

        if(options != null && !options.isEmpty()){
            prompt += " please provide options that are different than ";
            for(int index = 0; index<options.size(); index++){
                prompt += "'" + options.get(index) + "'. ";
            }
        }
        return prompt;
    }


    public static String createGetRecipePrompt(String title, int diners, int time, String groceries) {
        String prompt = "Create a recipe with the Hebrew name '" + title + "' based on the following details: " +
                "The only groceries to be used are: " + groceries + " and cooking time is up to " + time + " minutes, " +
                "and number of diners is " + diners + ". The groceries input will be in Hebrew, and the output values should also be in Hebrew, " +
                "but the JSON keys should remain in English. " +
                "Each grocery item should be a single string that includes both the name and the amount needed for the given diners. " +
                "In the groceries and instructions please do not use special symbols that break the json. " +
                "Please provide a JSON response with the following fields: " +
                "{'title': '', 'diners': '', 'groceries': [], 'time': '', 'instructions': []}";
        return prompt;
    }



}

