package com.chefmate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.chefmate.ai.OpenAiJsonService;
import com.chefmate.ai.OpenAiService;
import com.chefmate.ai.RecipePrompts;
import com.chefmate.model.CookTime;
import com.chefmate.model.Recipe;
import com.chefmate.model.RecipeRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * RecipeActivity is responsible for displaying detailed information about a specific recipe.
 * This includes the recipe title, number of diners, cooking time, ingredients, and instructions.
 * The activity allows users to share the recipe via messaging apps and fetch full recipe details
 * from the OpenAI API.
 */
public class RecipeActivity extends OpenAiService {

    private TextView dinersTextView;      // Displays the number of diners
    private TextView timeTextView;        // Displays the cooking time
    private TextView groceriesTextView;   // Displays the list of ingredients
    private TextView instructionsTextView; // Displays the cooking instructions

    private String title;                 // Title of the recipe
    private RecipeRequest recipeRequest;  // RecipeRequest object with input details
    private Recipe recipe;                // Recipe object containing detailed recipe information
    private String instructions;          // Cooking instructions

    /**
     * Called when the activity is created. Sets up the layout, initializes views,
     * fetches the recipe details, and configures the share functionality.
     *
     * @param savedInstanceState if the activity is being re-initialized after being shut down,
     *                           this Bundle contains the most recent data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve recipe details from the Intent
        this.title = getIntent().getStringExtra("title");
        this.recipeRequest = (RecipeRequest) getIntent().getSerializableExtra("recipeRequest");

        // Set the content layout and title
        super.setContentLayout(R.layout.recipe_activity, title);

        // Hide the content of the page until data is loaded
        super.showPageLayout(false);

        // Initialize views
        dinersTextView = findViewById(R.id.dinersNum);
        timeTextView = findViewById(R.id.makeTime);
        groceriesTextView = findViewById(R.id.ingredients);
        instructionsTextView = findViewById(R.id.instructions);

        // Fetch the full recipe details
        fetchFullRecipe(title, this.recipeRequest.getDiners(), this.recipeRequest.getTime(), this.recipeRequest.getGroceries());

        // Set up the share button to share the recipe
        ImageButton shareButton = findViewById(R.id.shareButton);
        shareButton.setOnClickListener(v -> shareRecipe());
    }

    /**
     * Clears the recipe details from the UI. Called when the activity resumes.
     */
    @Override
    protected void onResume() {
        super.onResume();
        clearRecipeUI();
    }

    /**
     * Clears all recipe-related data from the UI.
     */
    private void clearRecipeUI() {
        dinersTextView.setText("");
        timeTextView.setText("");
        groceriesTextView.setText("");
        instructionsTextView.setText("");
    }

    /**
     * Populates the UI with the recipe details fetched from the OpenAI API.
     */
    private void setupRecipeUI() {
        dinersTextView.setText(String.valueOf(this.recipe.getDiners()));
        timeTextView.setText(this.recipe.getActualCookingTime() + " דקות");
        groceriesTextView.setText(this.recipe.getGroceries());
        instructionsTextView.setText(this.instructions);

        // Show the content of the page
        super.showPageLayout(true);
    }

    /**
     * Allows the user to share the recipe details via messaging apps.
     * Constructs a message with the recipe information and opens a share intent.
     */
    @SuppressLint("QueryPermissionsNeeded")
    private void shareRecipe() {
        // Construct the recipe message
        String recipeMessage = this.title + "\n\n" +
                "מספר סועדים:\n" + this.recipeRequest.getDiners() + "\n" +
                "זמן הכנה:\n" + this.recipeRequest.getTime() + "\n" +
                "מצרכים:\n" + this.recipeRequest.getGroceries() + "\n" +
                "הוראות הכנה:\n" + this.instructions;

        // Create a share intent
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, recipeMessage);
        shareIntent.setType("text/plain");

        // Open the share intent
        if (shareIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(shareIntent, "Share Recipe via"));
        } else {
            Toast.makeText(this, "No app available to share the recipe", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Fetches the full recipe details, including ingredients and instructions, from the OpenAI API.
     *
     * @param recipeTitle The title of the recipe.
     * @param diners      The number of diners for the recipe.
     * @param time        The cooking time for the recipe.
     * @param groceries   The list of ingredients for the recipe.
     */
    private void fetchFullRecipe(String recipeTitle, int diners, CookTime time, String groceries) {
        String prompt = RecipePrompts.createGetRecipePrompt(recipeTitle, diners, time, groceries);
        callOpenAI(prompt);
    }

    /**
     * Handles the response from the OpenAI API. Parses the response to extract recipe details
     * such as ingredients and instructions, and updates the UI.
     *
     * @param response The JSON response from OpenAI.
     */
    @Override
    protected void handleOpenAiResponse(String response) {
        try {
            String content = OpenAiJsonService.cleanJsonResponse(response, false);

            // Parse the JSON response to extract recipe details
            JSONObject recipeJson = new JSONObject(content);
            JSONArray groceries = recipeJson.getJSONArray("groceries");
            StringBuilder ingredients = new StringBuilder();
            for (int i = 0; i < groceries.length(); i++) {
                ingredients.append("• ").append(groceries.getString(i)).append("\n");
            }

            JSONArray instructions = recipeJson.getJSONArray("instructions");
            StringBuilder instructionsBuffer = new StringBuilder();
            for (int i = 0; i < instructions.length(); i++) {
                instructionsBuffer.append("• ").append(instructions.getString(i)).append("\n");
            }

            this.instructions = instructionsBuffer.toString();
            int time = recipeJson.getInt("time");

            // Create a Recipe object with the parsed details
            this.recipe = new Recipe(title, this.recipeRequest.getDiners(), this.recipeRequest.getMealType(), this.recipeRequest.getTime(), ingredients.toString(), this.instructions);
            this.recipe.setActualCookingTime(time);

            // Update the UI on the main thread
            runOnUiThread(this::setupRecipeUI);

        } catch (JSONException e) {
            Log.e("Recipe", "JSON Error: " + e.getMessage());
        }
    }
}
