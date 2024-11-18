package com.chefmate.spinner;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.content.Context;


import com.chefmate.model.MealType;

import java.util.ArrayList;
import java.util.List;

public class MealTypeSpinnerHandler {

    public static void handle(Spinner spinner, Context context){
        // Get enum values and convert them to a list of strings in Hebrew
        MealType[] mealTypes = MealType.values();

        List<SpinnerItem> mealTypeValues = new ArrayList<>();
        for (int i = 0; i < mealTypes.length; i++) {
            mealTypeValues.add(new SpinnerItem(mealTypes[i].name(),mealTypes[i].getValue()));
        }

        // Create an ArrayAdapter using a simple spinner layout and your items list
        SpinnerItemAdapter adapter = new SpinnerItemAdapter(context, mealTypeValues);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Attach the adapter to the spinner
        spinner.setAdapter(adapter);

        // Optional: Handle item selection
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                // Do something with the selected item
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Optional action if nothing is selected
            }
        });
    }
}
