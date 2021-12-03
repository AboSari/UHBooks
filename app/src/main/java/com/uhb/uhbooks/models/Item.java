package com.uhb.uhbooks.models;


import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Item {
    private Type type;
    private String name, path, thumb;
    private Level level;
    @SerializedName("children")
    private List<Item> items;

    public Item() {
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public enum Level {
        STUDENT,
        INSTRUCTOR,
        FAVORITE
    }

    public enum Type {
        FOLDER,
        FILE
    }
}
