package com.chefmate.spinner;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.chefmate.model.MealType;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for handling the setup and interaction of a spinner (dropdown menu)
 * with meal type options. This class populates the spinner with values derived
 * from the {@link MealType} enum and sets up an adapter and listener for user interaction.
 */
public class MealTypeSpinnerHandler {

    /**
     * Configures a spinner with meal type options.
     * Populates the spinner with values from the {@link MealType} enum and sets up
     * a listener for item selection.
     *
     * @param spinner The {@link Spinner} view to be configured.
     * @param context The {@link Context} required for creating the adapter.
     */
    public static void handle(Spinner spinner, Context context) {
        // Get enum values and convert them to a list of SpinnerItem objects
        MealType[] mealTypes = MealType.values();
        List<SpinnerItem> mealTypeValues = new ArrayList<>();
        for (MealType mealType : mealTypes) {
            mealTypeValues.add(new SpinnerItem(mealType.name(), mealType.getValue()));
        }

        // Create an adapter for the spinner
        SpinnerItemAdapter adapter = new SpinnerItemAdapter(context, mealTypeValues);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Attach the adapter to the spinner
        spinner.setAdapter(adapter);

        // Set a listener to handle user interaction with the spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * Called when an item is selected in the spinner.
             *
             * @param parent   The parent view of the spinner.
             * @param view     The view within the spinner that was selected.
             * @param position The position of the selected item in the adapter.
             * @param id       The row ID of the selected item.
             */
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                // Optional: Add action based on selected item
            }

            /**
             * Called when no item is selected in the spinner.
             *
             * @param parent The parent view of the spinner.
             */
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Optional: Add action when nothing is selected
            }
        });
    }
}
