package com.chefmate.spinner;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.chefmate.model.CookTime;

import java.util.ArrayList;
import java.util.List;

public class CookTimeSpinnerHandler {

    public static void handle(Spinner spinner, Context context){
        // Get enum values and convert them to a list of strings in Hebrew
        CookTime[] cookTimeTypes = CookTime.values();

        List<SpinnerItem> cookTimeValues = new ArrayList<>();
        for (int i = 0; i < cookTimeTypes.length; i++) {
            cookTimeValues.add(new SpinnerItem(cookTimeTypes[i].name(),cookTimeTypes[i].getValue()));
        }

        // Create an ArrayAdapter using a simple spinner layout and your items list
        SpinnerItemAdapter adapter = new SpinnerItemAdapter(context, cookTimeValues);
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
