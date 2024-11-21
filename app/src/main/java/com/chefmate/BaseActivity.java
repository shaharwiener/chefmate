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

/**
 * BaseActivity provides common functionality for all activities in the ChefMate application.
 * This includes managing a bottom navigation menu, dynamically setting content, and handling
 * a loading overlay. Activities that extend this class inherit these features.
 */
public class BaseActivity extends AppCompatActivity {

    private RelativeLayout loadingOverlay; // Overlay to indicate loading
    private static int currentMenuItemId; // ID of the currently selected menu item
    private ScrollView scrollArea;        // Scrollable content area
    private LinearLayout menuView;        // Bottom navigation menu view

    /**
     * Called when the activity is created. Initializes the layout, sets up the bottom navigation menu,
     * and links key views to their respective UI components.
     *
     * @param savedInstanceState if the activity is being re-initialized after being shut down,
     *                           this Bundle contains the most recent data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Sets the BaseActivity layout in the parent
        setContentView(R.layout.base_activity);

        // Link UI components
        this.scrollArea = findViewById(R.id.scroll_area);
        this.menuView = findViewById(R.id.menu_view);
        this.loadingOverlay = findViewById(R.id.loading_view);

        // Set up bottom navigation menu
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setItemIconTintList(null); // Preserve original icon colors
        bottomNavigationView.setOnNavigationItemSelectedListener(null);
        bottomNavigationView.setSelectedItemId(currentMenuItemId);

        // Set a listener for navigation menu item selection
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Intent intent = null;
            int id = item.getItemId();

            // Handle menu actions
            if (id == R.id.action_home) {
                if (currentMenuItemId == R.layout.home_activity) return true;
                currentMenuItemId = R.id.action_home;
                intent = new Intent(this, HomeActivity.class);
            } else if (id == R.id.action_favorites) {
                if (currentMenuItemId == R.layout.favorite_activity) return true;
                currentMenuItemId = R.id.action_favorites;
                intent = new Intent(this, FavoriteActivity.class);
            } else if (id == R.id.action_profile) {
                if (currentMenuItemId == R.layout.profile_activity) return true;
                currentMenuItemId = R.id.action_profile;
                intent = new Intent(this, ProfileActivity.class);
            } else if (id == R.id.action_info) {
                if (currentMenuItemId == R.layout.info_activity) return true;
                currentMenuItemId = R.id.action_info;
                intent = new Intent(this, InfoActivity.class);
            }

            // Start the selected activity
            if (intent != null) {
                startActivity(intent);
                return true;
            }

            return super.onOptionsItemSelected(item);
        });
    }

    /**
     * Sets the content of the FrameLayout in the BaseActivity and updates the title of the page.
     *
     * @param layoutResID The layout resource ID to be loaded in the content area.
     * @param title       The title to be displayed at the top of the page.
     */
    protected void setContentLayout(int layoutResID, String title) {
        TextView titleText = findViewById(R.id.page_title);
        titleText.setText(title); // Set the title text
        FrameLayout contentFrame = findViewById(R.id.page_content);
        contentFrame.removeAllViews();
        getLayoutInflater().inflate(layoutResID, contentFrame, true); // Inflate the new layout
        this.currentMenuItemId = layoutResID; // Update the current menu item ID
    }

    /**
     * Shows or hides the main page layout, including the scrollable area and bottom navigation menu,
     * depending on the loading state.
     *
     * @param show true to show the main page layout, false to display the loading overlay.
     */
    protected void showPageLayout(boolean show) {
        if (show) {
            this.loadingOverlay.setVisibility(View.INVISIBLE);
            this.scrollArea.setVisibility(View.VISIBLE);
            this.menuView.setVisibility(View.VISIBLE);
        } else {
            this.loadingOverlay.setVisibility(View.VISIBLE);
            this.scrollArea.setVisibility(View.INVISIBLE);
            this.menuView.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Inflates the main menu options for the activity.
     *
     * @param menu The menu to be inflated.
     * @return true if the menu is successfully created.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
}
