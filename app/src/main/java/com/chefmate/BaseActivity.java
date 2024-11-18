package com.chefmate;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.view.menu.MenuItemImpl;
import androidx.appcompat.widget.ActionMenuView;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class BaseActivity extends AppCompatActivity {

    private RelativeLayout loadingOverlay;
    private FrameLayout mainContent;
    private LinearLayout headerLayout;
    private int currentFrameLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_activity);
        this.loadingOverlay = findViewById(R.id.loadingOverlay);
        this.mainContent = findViewById(R.id.content_frame);
        this.headerLayout = findViewById(R.id.header_layout);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setItemIconTintList(null);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Intent intent=null;
            int id = item.getItemId();
            if (id == R.id.action_home) {
                if (currentFrameLayout == R.layout.home_activity)
                    return true;
                intent = new Intent(this, HomeActivity.class);
            } else if (id == R.id.action_favorites) {
                if (currentFrameLayout == R.layout.favorite_activity)
                    return true;
                intent = new Intent(this, FavoriteActivity.class);
            } else if (id == R.id.action_profile) {
                if (currentFrameLayout == R.layout.profile_activity)
                    return true;
                intent = new Intent(this, ProfileActivity.class);
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
        TextView titleText = findViewById(R.id.titleText);
        titleText.setText(title);
        FrameLayout contentFrame = findViewById(R.id.content_frame);
        contentFrame.removeAllViews();;
        getLayoutInflater().inflate(layoutResID, contentFrame, true);
        this.currentFrameLayout = layoutResID;
    }

    protected void show(boolean show){
        if(show){
            this.loadingOverlay.setVisibility(View.INVISIBLE);
            this.mainContent.setVisibility(View.VISIBLE);
            this.headerLayout.setVisibility(View.VISIBLE);
        } else{
            this.loadingOverlay.setVisibility(View.VISIBLE);
            this.mainContent.setVisibility(View.INVISIBLE);
            this.headerLayout.setVisibility(View.INVISIBLE);
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