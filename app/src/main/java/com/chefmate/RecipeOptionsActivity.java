package com.chefmate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chefmate.ai.OpenAiJsonService;
import com.chefmate.ai.OpenAiService;
import com.chefmate.ai.RecipePrompts;
import com.chefmate.menu.BottomNavigationViewHandler;
import com.chefmate.model.CookTime;
import com.chefmate.model.RecipeRequest;
import com.chefmate.speech.SpeechService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecipeOptionsActivity extends OpenAiService {

     private RecipeRequest recipeRequest;
     private SpeechService speechService;
    private ArrayList<String> recipeOptions;

    private boolean isSpeaking = false; // Flag to track if TTS is speaking


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the content of the page until everything is loaded
        super.show(false);
        super.setContentLayout(R.layout.recipe_options_activity, "בחר מתכון");
        this.speechService = new SpeechService(this);
        this.recipeRequest = (RecipeRequest)getIntent().getSerializableExtra("recipeRequest");
        BottomNavigationViewHandler.clearMenu(this);

        requestRecipeTitles(this.recipeRequest.getDiners(), this.recipeRequest.getTime(), this.recipeRequest.getGroceries(), null);

        // Setup the speaker button
        ImageButton speakerButton = findViewById(R.id.imageButtonSpeaker);
        speakerButton.setOnClickListener(v -> toggleSpeech());

        // Setup the "Load New Options" button
        ImageButton loadNewOptionsButton = findViewById(R.id.newOptions);
        loadNewOptionsButton.setOnClickListener(v -> requestRecipeTitles(this.recipeRequest.getDiners(), this.recipeRequest.getTime(), this.recipeRequest.getGroceries(), this.recipeOptions));
    }

    protected void handleOpenAiResponse(String response) {
        try {
            String cleanedJsonArray = OpenAiJsonService.cleanJsonResponse(response, true);
            // Parse the cleaned JSON array
            JSONArray recipesJsonArray = new JSONArray(cleanedJsonArray);
            recipeOptions = new ArrayList<>();
            // Extract each title from the JSON array and add to recipes list
            for (int i = 0; i < recipesJsonArray.length(); i++) {
                String title = recipesJsonArray.getString(i);
                recipeOptions.add(title);
            }

            // Update the UI on the main thread
            runOnUiThread(this::setupRecipeOptionsUI);

        } catch (JSONException e) {
            Log.e("OpenAI API", "JSON parsing error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void requestRecipeTitles(int diners, CookTime time, String groceries, ArrayList<String> options) {
        String prompt = RecipePrompts.createGetRecipeOptionsPrompt(diners, time, groceries, options);
        callOpenAI(prompt);
    }

    // Method to toggle TTS speech
    private void toggleSpeech() {
        if (isSpeaking) {
            // If already speaking, stop the TTS
            this.speechService.stop();
            isSpeaking = false;
        } else {
            // If not speaking, start speaking the recipe options
            this.speechService.speak(recipeOptions);
            isSpeaking = true;
        }
    }

    @Override
    protected void onDestroy() {
       this.speechService.destroy();
        super.onDestroy();
    }

    private void setupRecipeOptionsUI() {

        TextView title1 = findViewById(R.id.title1);
        TextView title2 = findViewById(R.id.title2);
        TextView title3 = findViewById(R.id.title3);
        TextView title4 = findViewById(R.id.title4);

        // Set button text with recipes, handle cases with fewer than 4 recipes
        if (recipeOptions != null && recipeOptions.size() >= 4) {
            title1.setText(recipeOptions.get(0));
            title2.setText(recipeOptions.get(1));
            title3.setText(recipeOptions.get(2));
            title4.setText(recipeOptions.get(3));

//             Set click listeners to fetch full recipe details
            title1.setOnClickListener(v -> navigateToRecipeActivity(recipeOptions.get(0)));
            title2.setOnClickListener(v -> navigateToRecipeActivity(recipeOptions.get(1)));
            title3.setOnClickListener(v -> navigateToRecipeActivity(recipeOptions.get(2)));
            title4.setOnClickListener(v -> navigateToRecipeActivity(recipeOptions.get(3)));

        }

        //Show the content of the page
        super.show(true);
    }

    private void navigateToRecipeActivity(String recipeTitle) {
        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra("title", recipeTitle);
        intent.putExtra("recipeRequest", this.recipeRequest);
        startActivity(intent);
    }


}