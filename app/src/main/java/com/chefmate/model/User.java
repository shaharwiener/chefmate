package com.chefmate.model;

public class User extends Identity{

    private String name;
    private String imagePath;

    public User(String id, String name, String imagePath) {
        super(id);
        this.name = name;
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public String getImagePath() {
        return imagePath;
    }
}
