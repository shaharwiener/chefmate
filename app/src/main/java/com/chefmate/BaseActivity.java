package com.chefmate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BaseActivity extends AppCompatActivity {

    private RelativeLayout loadingOverlay;
    private FrameLayout mainContent;
    private LinearLayout headerLayout;
    private LinearLayout menu;
    private View line;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);
        this.loadingOverlay = findViewById(R.id.loadingOverlay);
        this.mainContent = findViewById(R.id.content_frame);
        this.headerLayout = findViewById(R.id.header_layout);
        this.menu = findViewById(R.id.menu);
        this.line = findViewById(R.id.line);

        ImageButton home =  findViewById(R.id.imageHome);
        home.setOnClickListener(v -> goHome());
    }

    // Method to set the content of the FrameLayout
    protected void setContentLayout(int layoutResID, String title) {
        TextView titleText = findViewById(R.id.titleText);
        System.out.println("title: " + title);
        titleText.setText(title);
        FrameLayout contentFrame = findViewById(R.id.content_frame);
        contentFrame.removeAllViews();;
        getLayoutInflater().inflate(layoutResID, contentFrame, true);
    }

    protected void show(boolean show){
        if(show){
            this.loadingOverlay.setVisibility(View.INVISIBLE);
            this.mainContent.setVisibility(View.VISIBLE);
            this.menu.setVisibility(View.VISIBLE);
            this.headerLayout.setVisibility(View.VISIBLE);
            this.line.setVisibility(View.VISIBLE);
        } else{
            this.loadingOverlay.setVisibility(View.VISIBLE);
            this.mainContent.setVisibility(View.INVISIBLE);
            this.menu.setVisibility(View.INVISIBLE);
            this.headerLayout.setVisibility(View.INVISIBLE);
            this.line.setVisibility(View.INVISIBLE );
        }
    }

    private void goHome(){
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}