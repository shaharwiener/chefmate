package com.chefmate;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class FavoriteActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.favorite_activity, "מתכונים מועדפים");
        super.show(true);
    }
}