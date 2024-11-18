package com.chefmate.menu;

import android.app.Activity;
import android.content.Context;

import com.chefmate.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavigationViewHandler {

    public static void clearMenu(Activity activity){
        BottomNavigationView bottomNavigationView = activity.findViewById(R.id.bottom_navigation);

        // Deselect all menu items to remove glam effect
        for (int i = 0; i < bottomNavigationView.getMenu().size(); i++) {
            bottomNavigationView.getMenu().getItem(i).setChecked(false);
        }
        bottomNavigationView.getMenu().setGroupCheckable(0, false, true);
        // Force the UI to refresh
        bottomNavigationView.invalidate();
    }
}
