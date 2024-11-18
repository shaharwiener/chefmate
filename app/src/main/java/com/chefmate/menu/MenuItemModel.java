package com.chefmate.menu;

public class MenuItemModel {
    private final int iconResId;
    private final String title;

    public MenuItemModel(int iconResId, String title) {
        this.iconResId = iconResId;
        this.title = title;
    }

    public int getIconResId() {
        return iconResId;
    }

    public String getTitle() {
        return title;
    }
}

