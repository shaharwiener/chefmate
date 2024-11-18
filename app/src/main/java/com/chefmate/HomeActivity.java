package com.chefmate;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.chefmate.ai.OpenAiJsonService;
import com.chefmate.ai.OpenAiService;
import com.chefmate.ai.RecipePrompts;
import com.chefmate.grocery.GroceryItemView;
import com.chefmate.model.CookTime;
import com.chefmate.model.MealType;
import com.chefmate.spinner.CookTimeSpinnerHandler;
import com.chefmate.spinner.MealTypeSpinnerHandler;
import com.chefmate.model.RecipeRequest;
import com.chefmate.spinner.SpinnerItem;
import com.google.android.flexbox.FlexboxLayout;

import org.json.JSONException;
import org.json.JSONObject;


public class HomeActivity extends OpenAiService {

    private EditText groceryInput;
    private FlexboxLayout groceryListContainer;
    private RecipeRequest recipeRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentLayout(R.layout.home_activity, "מצא מתכון");

        groceryInput = findViewById(R.id.groceryInput);
        ImageButton addButton = findViewById(R.id.addButton);
        groceryListContainer = findViewById(R.id.groceryListContainer);
        groceryListContainer.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        addButton.setOnClickListener(v -> {
            addGroceryItem(groceryInput.getText().toString());
            groceryInput.setText(""); // Clear the input after adding
        });

        Button findRecipes = findViewById(R.id.findRecipesButton);
        findRecipes.setOnClickListener(v -> navigateToRecipeOptions());

        ImageView infoImage = findViewById(R.id.infoImage);
        infoImage.setOnClickListener(v -> navigateToInfo());

        Spinner spinner = findViewById(R.id.typeInput);
        MealTypeSpinnerHandler.handle(spinner, this);

        spinner = findViewById(R.id.cookTimeInput);
        CookTimeSpinnerHandler.handle(spinner, this);

        super.show(true);
    }

    private void navigateToInfo(){
        Intent intent = new Intent(this, InfoActivity.class);
        startActivity(intent);
    }

    private void addGroceryItem(String grocery) {
        if (grocery.isEmpty()) return;

        // Declare the custom view variable
        final GroceryItemView groceryItemView = null;

        // Create a wrapper to hold the GroceryItemView reference
        final LinearLayout[] groceryItemWrapper = new LinearLayout[1];

        // Initialize the custom view
        groceryItemWrapper[0] = new GroceryItemView(this, grocery, v -> {
            // Remove the grocery item using the wrapper reference
            groceryListContainer.removeView(groceryItemWrapper[0]);
        });

        // Add the custom view to the FlexboxLayout
        groceryListContainer.addView(groceryItemWrapper[0]);
    }

    private void navigateToRecipeOptions(){

        EditText dinersEdit = findViewById(R.id.dinersInput);
        Spinner cookTime = findViewById(R.id.cookTimeInput);

        // Ensure inputs are provided
        if (dinersEdit.getText().equals("") || cookTime.getSelectedItem().toString().equals("") || groceryListContainer.getChildCount() == 0) {
            // Handle missing input error (e.g., show a Toast message)
            return;
        }

        int diners = Integer.valueOf(((EditText) findViewById(R.id.dinersInput)).getText().toString()).intValue();
        SpinnerItem cookTimeItem = (SpinnerItem)((Spinner)findViewById(R.id.cookTimeInput)).getSelectedItem();
        SpinnerItem mealTypeItem = (SpinnerItem)((Spinner)findViewById(R.id.typeInput)).getSelectedItem();

        // Collect grocery list
        StringBuilder groceries = new StringBuilder();
        for (int i = 0; i < groceryListContainer.getChildCount(); i++) {
            GroceryItemView groceryItem = (GroceryItemView) groceryListContainer.getChildAt(i);
            groceries.append(groceryItem.getGroceryText()).append(", ");
        }
        CookTime cooktime = CookTime.valueOf(cookTimeItem.getId());
        MealType mealType = MealType.valueOf(mealTypeItem.getId());

        this.recipeRequest = new RecipeRequest("", diners, mealType, cooktime, groceries.toString());
        String prompt = RecipePrompts.createValidationGroceriesForMealTypePrompt(mealType, groceries.toString());
        callOpenAI(prompt);


    }

    @Override
    protected void handleOpenAiResponse(String response) throws JSONException {
        System.out.println("In handleOpenAiResponse");
        String content = OpenAiJsonService.cleanJsonResponse(response, false);

        JSONObject jsonContent = new JSONObject(content);
        boolean isValid = jsonContent.getBoolean("valid");
        System.out.println("is valid: " + isValid);
        if(!isValid){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        ((TextView) findViewById(R.id.errorMessage)).setText(jsonContent.getString("reason"));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            });        }
        else {
            // Start RecipesOptions activity and pass the recipe list
            Intent intent = new Intent(this, RecipeOptionsActivity.class);
            intent.putExtra("recipeRequest", this.recipeRequest);
            startActivity(intent);
        }

    }
}