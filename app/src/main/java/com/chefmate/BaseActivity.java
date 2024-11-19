package com.chefmate;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class BaseActivity extends AppCompatActivity {

    private RelativeLayout loadingOverlay;
    private static int currentMenuItemId;
    private ScrollView scrollArea;
    private LinearLayout menuView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);

        // Store the views for visible and invisible when loading
        this.scrollArea = findViewById(R.id.scroll_area);
        this.menuView = findViewById(R.id.menu_view);
        this.loadingOverlay = findViewById(R.id.loading_view);

        // Setup of menu and defines the actions on selected items
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setItemIconTintList(null);
        // Temporarily disable the listener to prevent double-triggering
        bottomNavigationView.setOnNavigationItemSelectedListener(null);
        bottomNavigationView.setSelectedItemId(this.currentMenuItemId);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Intent intent=null;
            int id = item.getItemId();
            if (id == R.id.action_home) {
                if (currentMenuItemId == R.layout.home_activity)
                    return true;
                currentMenuItemId = R.id.action_home;
                intent = new Intent(this, HomeActivity.class);
            } else if (id == R.id.action_favorites) {
                if (currentMenuItemId == R.layout.favorite_activity)
                    return true;
                currentMenuItemId = R.id.action_favorites;
                intent = new Intent(this, FavoriteActivity.class);
            } else if (id == R.id.action_profile) {
                if (currentMenuItemId == R.layout.profile_activity)
                    return true;
                currentMenuItemId = R.id.action_profile;
                intent = new Intent(this, ProfileActivity.class);
            } else if (id == R.id.action_info){
                if (currentMenuItemId == R.layout.info_activity)
                    return true;
                currentMenuItemId = R.id.action_info;
                intent = new Intent(this, InfoActivity.class);
            }

            if(intent != null){
                startActivity(intent);
                return  true;
            }

            return super.onOptionsItemSelected(item);
        });
    }




    // Method to set the content of the FrameLayout
    protected void setContentLayout(int layoutResID, String title) {
        TextView titleText = findViewById(R.id.page_title);
        titleText.setText(title);
        FrameLayout contentFrame = findViewById(R.id.page_content);
        contentFrame.removeAllViews();;
        getLayoutInflater().inflate(layoutResID, contentFrame, true);
        this.currentMenuItemId = layoutResID;
    }

    protected void showPageLayout(boolean show){
        if(show){
            this.loadingOverlay.setVisibility(View.INVISIBLE);
            this.scrollArea.setVisibility(View.VISIBLE);
            this.menuView.setVisibility(View.VISIBLE);
        } else{
            this.loadingOverlay.setVisibility(View.VISIBLE);
            this.scrollArea.setVisibility(View.INVISIBLE);
            this.menuView.setVisibility(View.INVISIBLE);
        }
    }

    private void goHome(){
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

}