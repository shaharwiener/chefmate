package com.chefmate.model;

import java.io.Serializable;

public class Identity implements Serializable {
    private String id;

    public Identity(String id){
        this.id = id;
    }

    public String getId(){
        return this.id;
    }
}
