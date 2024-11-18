package com.chefmate.spinner;

public class SpinnerItem {
    private String id;
    private String displayName;

    public SpinnerItem(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}

