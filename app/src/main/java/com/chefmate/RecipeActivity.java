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

public class RecipeActivity extends OpenAiService {

    TextView titleTextView;
    TextView dinersTextView;
    TextView timeTextView;
    TextView groceriesTextView;
    TextView instructionsTextView;

    private String title;
    private RecipeRequest recipeRequest;
    private Recipe recipe;
    private String instructions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve recipe details from intent
        this.title = getIntent().getStringExtra("title");
        this.recipeRequest = (RecipeRequest)getIntent().getSerializableExtra("recipeRequest");
        super.setContentLayout(R.layout.recipe_activity, title);

        super.showPageLayout(false);

        dinersTextView = findViewById(R.id.dinersNum);
        timeTextView = findViewById(R.id.makeTime);
        groceriesTextView = findViewById(R.id.ingredients);
        instructionsTextView = findViewById(R.id.instructions);

        fetchFullRecipe(title, this.recipeRequest.getDiners(), this.recipeRequest.getTime(), this.recipeRequest.getGroceries());

        ImageButton shareButton = findViewById(R.id.shareButton);
        shareButton.setOnClickListener(v -> shareRecipe());

    }

    @Override
    protected void onResume() {
        super.onResume();
        clearRecipeUI();
    }

    private void clearRecipeUI() {
        dinersTextView.setText("");
        timeTextView.setText("");
        groceriesTextView.setText("");
        instructionsTextView.setText("");
    }

    private void setupRecipeUI(){
        // Populate the TextViews with recipe details

        dinersTextView.setText(String.valueOf(this.recipe.getDiners()));
        timeTextView.setText(String.valueOf(this.recipe.getActualCookingTime()) + " דקות");
        groceriesTextView.setText(this.recipe.getGroceries());
        instructionsTextView.setText(this.instructions);

        super.showPageLayout(true);

    }

    @SuppressLint("QueryPermissionsNeeded")
    private void shareRecipe() {
        // Construct the message
        String recipeMessage = this.title + "\n\n" + "מספר סועדים:\n" + this.recipeRequest.getDiners() + "\n" + "זמן הכנה:\n" + this.recipeRequest.getTime() + "\n" + "מצרכים:\n" + this.recipeRequest.getGroceries() + "\n" + "הוראות הכנה:\n" + this.instructions;

        // Create the share intent
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, recipeMessage);
        shareIntent.setType("text/plain");

        // Check if WhatsApp is installed and direct the user to it, or let them choose another app
        if (shareIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(shareIntent, "Share Recipe via"));
        } else {
            Toast.makeText(this, "No app available to share the recipe", Toast.LENGTH_SHORT).show();
        }
    }


    private void fetchFullRecipe(String recipeTitle, int diners, CookTime time, String groceries) {
        String prompt = RecipePrompts.createGetRecipePrompt(recipeTitle, diners, time, groceries);
        callOpenAI(prompt);
    }

    @Override
    protected void handleOpenAiResponse(String response) {
        try {
            String content = OpenAiJsonService.cleanJsonResponse(response, false);

            JSONObject recipeJson = new JSONObject(content);
            JSONArray groceries = recipeJson.getJSONArray("groceries");
            StringBuilder ingredients = new StringBuilder();
            for (int i = 0; i < groceries.length(); i++) {
                String ingredient = groceries.getString(i);
                ingredients.append("• ").append(ingredient).append("\n");
            }

//            this.recipeRequest = new RecipeRequest(null, this.recipeRequest.getDiners(), this.recipeRequest.getMealType(), this.recipeRequest.getTime(), ingredients.toString());
            JSONArray instructions = recipeJson.getJSONArray("instructions");
            StringBuilder instructionsBuffer = new StringBuilder();
            for (int i = 0; i < instructions.length(); i++) {
                String ingredient = instructions.getString(i);
                instructionsBuffer.append("• ").append(ingredient).append("\n");
            }

            this.instructions = instructionsBuffer.toString();
            int time = recipeJson.getInt("time");

            this.recipe = new Recipe(title, this.recipeRequest.getDiners(), this.recipeRequest.getMealType(), this.recipeRequest.getTime(), ingredients.toString(), this.instructions);
            this.recipe.setActualCookingTime(time);

            // Update the UI on the main thread
            runOnUiThread(this::setupRecipeUI);

        } catch (JSONException e) {
            Log.e("Recipe", "JSON Error: " +e.getMessage() );
        }
    }
}