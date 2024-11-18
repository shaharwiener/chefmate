package com.chefmate.model;

public enum CookTime {
    SHORT("קצר",0, 10),
    MEDIUM("בינוני", 10, 60),
    LONG( "ארוך", 60, 3000);

    private String title;
    private int minTime;
    private int maxTime;

    CookTime (String title, int minTime, int maxTime){
        this.title = title;
        this.minTime = minTime;
        this.maxTime = maxTime;
    }

    public String getValue(){
        StringBuilder builder = new StringBuilder();
        builder.append(this.title).append(" - ").append(this.minTime).append(" עד ").append(this.maxTime).append(" דקות");
        return builder.toString();
    }

    public int getMinTime(){
        return this.minTime;
    }

    public int getMaxTime(){
        return this.maxTime;
    }
}
