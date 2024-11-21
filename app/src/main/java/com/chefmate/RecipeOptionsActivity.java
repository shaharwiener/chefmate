package com.chefmate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chefmate.ai.OpenAiJsonService;
import com.chefmate.ai.OpenAiService;
import com.chefmate.ai.RecipePrompts;
import com.chefmate.model.CookTime;
import com.chefmate.model.RecipeRequest;
import com.chefmate.speech.SpeechService;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * RecipeOptionsActivity handles displaying a list of recipe options to the user based on their input.
 * It interacts with the OpenAI API to fetch recipe titles and provides options for text-to-speech (TTS)
 * functionality and reloading new recipe suggestions.
 */
public class RecipeOptionsActivity extends OpenAiService {

    private RecipeRequest recipeRequest; // Stores details of the recipe request
    private SpeechService speechService; // Handles text-to-speech functionality
    private ArrayList<String> recipeOptions; // List of recipe titles fetched from OpenAI
    private boolean isSpeaking = false; // Tracks whether TTS is currently speaking

    /**
     * Initializes the activity, sets up the UI components, and triggers the first API call to fetch recipes.
     *
     * @param savedInstanceState if the activity is being re-initialized after being shut down,
     *                           this Bundle contains the most recent data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the content until the page is fully loaded
        super.showPageLayout(false);
        super.setContentLayout(R.layout.recipe_options_activity, "בחר מתכון");

        // Initialize the SpeechService for TTS functionality
        this.speechService = new SpeechService(this);

        // Retrieve the RecipeRequest object from the Intent
        this.recipeRequest = (RecipeRequest) getIntent().getSerializableExtra("recipeRequest");

        // Fetch recipe titles using OpenAI API
        assert this.recipeRequest != null;
        requestRecipeTitles(this.recipeRequest.getDiners(), this.recipeRequest.getTime(), this.recipeRequest.getGroceries(), null);

        // Set up the speaker button to toggle TTS
        ImageButton speakerButton = findViewById(R.id.imageButtonSpeaker);
        speakerButton.setOnClickListener(v -> toggleSpeech());

        // Set up the "Load New Options" button to fetch new recipes
        ImageButton loadNewOptionsButton = findViewById(R.id.newOptions);
        loadNewOptionsButton.setOnClickListener(v -> requestRecipeTitles(this.recipeRequest.getDiners(), this.recipeRequest.getTime(), this.recipeRequest.getGroceries(), this.recipeOptions));
    }

    /**
     * Handles the response from the OpenAI API. Parses the response to extract recipe titles,
     * updates the UI with the fetched titles, or logs errors if parsing fails.
     *
     * @param response The JSON response from OpenAI.
     */
    @Override
    protected void handleOpenAiResponse(String response) {
        try {
            String cleanedJsonArray = OpenAiJsonService.cleanJsonResponse(response, true);
            JSONArray recipesJsonArray = new JSONArray(cleanedJsonArray);

            recipeOptions = new ArrayList<>();
            for (int i = 0; i < recipesJsonArray.length(); i++) {
                recipeOptions.add(recipesJsonArray.getString(i));
            }

            // Update the UI on the main thread
            runOnUiThread(this::setupRecipeOptionsUI);

        } catch (JSONException e) {
            Log.e("OpenAI API", "JSON parsing error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Sends a request to OpenAI to fetch recipe titles based on the user's input.
     *
     * @param diners    Number of diners specified by the user.
     * @param time      Cooking time selected by the user.
     * @param groceries List of grocery items provided by the user.
     * @param options   Previously fetched recipe titles (optional).
     */
    private void requestRecipeTitles(int diners, CookTime time, String groceries, ArrayList<String> options) {
        String prompt = RecipePrompts.createGetRecipeOptionsPrompt(diners, time, groceries, options);
        callOpenAI(prompt);
    }

    /**
     * Toggles the text-to-speech (TTS) functionality. If TTS is currently active, it stops.
     * Otherwise, it starts speaking the recipe options.
     */
    private void toggleSpeech() {
        if (isSpeaking) {
            this.speechService.stop();
            isSpeaking = false;
        } else {
            this.speechService.speak(recipeOptions);
            isSpeaking = true;
        }
    }

    /**
     * Cleans up resources used by the SpeechService when the activity is destroyed.
     */
    @Override
    protected void onDestroy() {
        this.speechService.destroy();
        super.onDestroy();
    }

    /**
     * Sets up the UI to display the fetched recipe options and attach click listeners to navigate
     * to the detailed recipe view.
     */
    private void setupRecipeOptionsUI() {

        TextView title1 = findViewById(R.id.title1);
        TextView title2 = findViewById(R.id.title2);
        TextView title3 = findViewById(R.id.title3);
        TextView title4 = findViewById(R.id.title4);

        if (recipeOptions != null && recipeOptions.size() >= 4) {
            title1.setText(recipeOptions.get(0));
            title2.setText(recipeOptions.get(1));
            title3.setText(recipeOptions.get(2));
            title4.setText(recipeOptions.get(3));

            // Attach click listeners to navigate to detailed recipe activity
            title1.setOnClickListener(v -> navigateToRecipeActivity(recipeOptions.get(0)));
            title2.setOnClickListener(v -> navigateToRecipeActivity(recipeOptions.get(1)));
            title3.setOnClickListener(v -> navigateToRecipeActivity(recipeOptions.get(2)));
            title4.setOnClickListener(v -> navigateToRecipeActivity(recipeOptions.get(3)));
        }

        // Show the content of the page
        super.showPageLayout(true);
    }

    /**
     * Navigates to the RecipeActivity to display details for the selected recipe.
     *
     * @param recipeTitle The title of the recipe selected by the user.
     */
    private void navigateToRecipeActivity(String recipeTitle) {
        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra("title", recipeTitle);
        intent.putExtra("recipeRequest", this.recipeRequest);
        startActivity(intent);
    }
}
