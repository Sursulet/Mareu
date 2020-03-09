package com.openclassrooms.mareu.model;

public class RoomItem {

    private String name;
    private int color;

    public RoomItem(String name, int color) {
        this.name = name;
        this.color = color;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public void setImgColor(int color) { this.color = color; }
    public int getImgColor() { return color; }
}
