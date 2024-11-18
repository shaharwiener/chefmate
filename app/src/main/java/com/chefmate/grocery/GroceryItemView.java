package com.chefmate.grocery;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chefmate.R;
import com.google.android.flexbox.FlexboxLayout;

public class GroceryItemView extends LinearLayout {

    private TextView groceryText;

    public GroceryItemView(Context context, String grocery, OnClickListener onRemoveListener) {
        super(context);
        init(context, grocery, onRemoveListener);
    }

    public GroceryItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void init(Context context, String grocery, OnClickListener onRemoveListener) {
        setOrientation(HORIZONTAL);
        setBackgroundResource(R.drawable.border_gold);

        FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(
                FlexboxLayout.LayoutParams.WRAP_CONTENT,
                FlexboxLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(16, 8, 16, 30); // Adjust margins as needed
        setGravity(Gravity.CENTER);
        setLayoutParams(layoutParams);

        // Grocery Text
        groceryText = new TextView(context);
        groceryText.setText(grocery);
        groceryText.setTextSize(16);
        groceryText.setTextColor(Color.BLACK);
        groceryText.setPadding(20, 0, 20, 0);
        groceryText.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1));

        // Vertical Line
        View line = new View(context);
        LayoutParams lineParams = new LayoutParams(5, LayoutParams.MATCH_PARENT);
        line.setLayoutParams(lineParams);
        line.setBackgroundColor(Color.parseColor("#219BCC"));

        // Remove Image Button
        ImageView removeImage = new ImageView(context);
        removeImage.setImageResource(android.R.drawable.ic_menu_delete);
        removeImage.setLayoutParams(new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        ));
        removeImage.setPadding(20, 10, 20, 10);
        removeImage.setOnClickListener(onRemoveListener);

        // Add Views
        addView(groceryText);
        addView(line);
        addView(removeImage);
    }

    public String getGroceryText() {
        return groceryText.getText().toString();
    }
}

