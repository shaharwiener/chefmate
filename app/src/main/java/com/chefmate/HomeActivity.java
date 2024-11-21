package com.chefmate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

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

/**
 * The HomeActivity class is responsible for handling the main user interface where users can:
 * - Add grocery items.
 * - Select meal type and cooking time.
 * - Generate recipe options based on the inputs provided.
 *
 * This activity extends OpenAiService to communicate with the OpenAI API for recipe validation.
 */
public class HomeActivity extends OpenAiService {

    private EditText groceryInput; // Input field for grocery items
    private FlexboxLayout groceryListContainer; // Container to display the list of grocery items
    private RecipeRequest recipeRequest; // Stores the recipe request details

    /**
     * Called when the activity is created. Sets up the layout, input fields, and user actions such
     * as adding grocery items and finding recipes.
     *
     * @param savedInstanceState if the activity is being re-initialized after being shut down,
     *                           this Bundle contains the most recent data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the layout for the activity and page title
        setContentLayout(R.layout.home_activity, "מצא מתכון");

        // Initialize input fields and buttons
        groceryInput = findViewById(R.id.groceryInput);
        ImageButton addButton = findViewById(R.id.addButton);
        groceryListContainer = findViewById(R.id.groceryListContainer);
        groceryListContainer.setLayoutDirection(View.LAYOUT_DIRECTION_RTL); // Set layout direction to RTL

        // Set the add button's click listener to add grocery items
        addButton.setOnClickListener(v -> {
            addGroceryItem(groceryInput.getText().toString());
            groceryInput.setText(""); // Clear the input field after adding
        });

        // Set the "Find Recipes" button's click listener to navigate to recipe options
        Button findRecipes = findViewById(R.id.findRecipesButton);
        findRecipes.setOnClickListener(v -> navigateToRecipeOptions());

        // Handle meal type and cooking time spinners
        Spinner spinner = findViewById(R.id.mealTypeInput);
        MealTypeSpinnerHandler.handle(spinner, this);

        spinner = findViewById(R.id.cookTimeInput);
        CookTimeSpinnerHandler.handle(spinner, this);

        // Show the main page layout
        super.showPageLayout(true);
    }

    /**
     * Adds a grocery item to the list. The item is displayed in the FlexboxLayout
     * with an option to remove it.
     *
     * @param grocery The grocery item entered by the user.
     */
    private void addGroceryItem(String grocery) {
        if (grocery.isEmpty()) return;

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

    /**
     * Collects user inputs (grocery list, meal type, cooking time, and diners count)
     * and initiates a validation request using OpenAI.
     */
    private void navigateToRecipeOptions() {

        EditText dinersEdit = findViewById(R.id.dinersInput);
        Spinner cookTime = findViewById(R.id.cookTimeInput);

        // Ensure inputs are provided
        if (dinersEdit.getText().equals("") || cookTime.getSelectedItem().toString().equals("") || groceryListContainer.getChildCount() == 0) {
            // Handle missing input error (e.g., show a Toast message)
            return;
        }

        int diners = Integer.parseInt(dinersEdit.getText().toString());
        SpinnerItem cookTimeItem = (SpinnerItem) (cookTime).getSelectedItem();
        SpinnerItem mealTypeItem = (SpinnerItem) ((Spinner) findViewById(R.id.mealTypeInput)).getSelectedItem();

        // Collect grocery list
        StringBuilder groceries = new StringBuilder();
        for (int i = 0; i < groceryListContainer.getChildCount(); i++) {
            GroceryItemView groceryItem = (GroceryItemView) groceryListContainer.getChildAt(i);
            groceries.append("'").append(groceryItem.getGroceryText()).append("'").append(", ");
        }

        // Create RecipeRequest object
        CookTime cooktime = CookTime.valueOf(cookTimeItem.getId());
        MealType mealType = MealType.valueOf(mealTypeItem.getId());
        this.recipeRequest = new RecipeRequest("", diners, mealType, cooktime, groceries.toString());

        // Generate prompt and validate using OpenAI
        String prompt = RecipePrompts.createValidationGroceriesForMealTypePrompt(mealType, groceries.toString());
        callOpenAI(prompt);
    }

    /**
     * Handles the response received from OpenAI. If the response indicates invalid input,
     * displays an error message. Otherwise, starts the RecipeOptionsActivity with the recipe request.
     *
     * @param response The JSON response from OpenAI.
     * @throws JSONException if the JSON response cannot be parsed.
     */
    @Override
    protected void handleOpenAiResponse(String response) throws JSONException {
        String content = OpenAiJsonService.cleanJsonResponse(response, false);

        JSONObject jsonContent = new JSONObject(content);
        boolean isValid = jsonContent.getBoolean("valid");
        if (!isValid) {
            // Show error message
            runOnUiThread(() -> {
                try {
                    TextView errorMessage = findViewById(R.id.errorMessage);
                    errorMessage.setText(jsonContent.getString("reason"));
                    errorMessage.setVisibility(View.VISIBLE);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            });
        } else {
            // Navigate to RecipeOptionsActivity
            Intent intent = new Intent(this, RecipeOptionsActivity.class);
            intent.putExtra("recipeRequest", this.recipeRequest);
            startActivity(intent);
        }
    }
}
