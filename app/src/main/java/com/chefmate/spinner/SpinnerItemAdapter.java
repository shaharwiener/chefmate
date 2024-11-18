package com.chefmate.spinner;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

public class SpinnerItemAdapter extends ArrayAdapter<SpinnerItem> {

    public SpinnerItemAdapter(Context context, List<SpinnerItem> items) {
        super(context, android.R.layout.simple_spinner_item, items);
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }
}

