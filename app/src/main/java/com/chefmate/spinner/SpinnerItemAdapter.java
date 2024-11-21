package com.chefmate.spinner;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Custom adapter for populating a spinner with {@link SpinnerItem} objects.
 * Extends the {@link ArrayAdapter} to display spinner items using a simple layout.
 */
public class SpinnerItemAdapter extends ArrayAdapter<SpinnerItem> {

    /**
     * Constructs a new {@code SpinnerItemAdapter}.
     *
     * @param context The {@link Context} of the application or activity using this adapter.
     * @param items   The list of {@link SpinnerItem} objects to be displayed in the spinner.
     */
    public SpinnerItemAdapter(Context context, List<SpinnerItem> items) {
        super(context, android.R.layout.simple_spinner_item, items);
        // Sets the layout for dropdown items in the spinner
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }
}
