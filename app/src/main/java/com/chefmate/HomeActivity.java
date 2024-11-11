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
import android.widget.TextView;

import com.chefmate.model.RecipeRequest;
import com.google.android.flexbox.FlexboxLayout;


public class HomeActivity extends BaseActivity {

    private EditText groceryInput;
    private FlexboxLayout groceryListContainer;


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
        super.show(true);
    }

    private void navigateToInfo(){
        Intent intent = new Intent(this, InfoActivity.class);
        startActivity(intent);
    }

    private void addGroceryItem(String grocery) {
        if (grocery.isEmpty()) return;

        // Horizontal container for each grocery item
        LinearLayout groceryItemLayout = new LinearLayout(this);
        groceryItemLayout.setOrientation(LinearLayout.HORIZONTAL);
        groceryItemLayout.setBackgroundResource(R.drawable.border_gold);

        // Set fixed margins between items
        FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(
                FlexboxLayout.LayoutParams.WRAP_CONTENT,
                FlexboxLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(16, 8, 16, 30); // Set fixed horizontal and vertical margins
        groceryItemLayout.setGravity(Gravity.CENTER);
        groceryItemLayout.setLayoutParams(layoutParams);



        // Grocery text
        TextView groceryText = new TextView(this);
        groceryText.setText(grocery);
        groceryText.setTextSize(16);
        groceryText.setTextColor(Color.BLACK);
        groceryText.setPadding(20,0,20,0);
        groceryText.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        // Vertical line
        View line = new View(this);
        LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(
                5, LinearLayout.LayoutParams.MATCH_PARENT);
        lineParams.setMargins(0, 0, 0, 0); // Ensure no extra margins
        line.setLayoutParams(lineParams);
        line.setBackgroundColor(Color.parseColor("#219BCC"));
        line.setPadding(0, 0, 0, 0);



        // Remove image button
        ImageView removeImage = new ImageView(this);
        removeImage.setImageResource(android.R.drawable.ic_menu_delete);
        removeImage.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        removeImage.setOnClickListener(v -> groceryListContainer.removeView(groceryItemLayout));
        removeImage.setPadding(20,10,20,10);



        // Add views to grocery item layout
        groceryItemLayout.addView(groceryText);
        groceryItemLayout.addView(line);
        groceryItemLayout.addView(removeImage);


        // Add grocery item layout to the list container
        groceryListContainer.addView(groceryItemLayout);
    }

    private void navigateToRecipeOptions(){

        EditText dinersEdit = findViewById(R.id.editDiners);
        EditText timeEdit = findViewById(R.id.editTime);

        // Ensure inputs are provided
        if (dinersEdit.getText().equals("") || timeEdit.getText().equals("") || groceryListContainer.getChildCount() == 0) {
            // Handle missing input error (e.g., show a Toast message)
            return;
        }

        int diners = Integer.valueOf(((EditText) findViewById(R.id.editDiners)).getText().toString()).intValue();
        int time = Integer.valueOf(((EditText) findViewById(R.id.editTime)).getText().toString()).intValue();

        // Collect grocery list
        StringBuilder groceries = new StringBuilder();
        for (int i = 0; i < groceryListContainer.getChildCount(); i++) {
            LinearLayout itemLayout = (LinearLayout) groceryListContainer.getChildAt(i);
            TextView groceryText = (TextView) itemLayout.getChildAt(0);
            groceries.append(groceryText.getText().toString()).append(", ");
        }
        // Start RecipesOptions activity and pass the recipe list
        Intent intent = new Intent(this, RecipeOptionsActivity.class);
        RecipeRequest request = new RecipeRequest(null, diners, time, groceries.toString());
        intent.putExtra("recipeRequest", request);
        startActivity(intent);
    }

}