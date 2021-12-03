package com.uhb.uhbooks.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class User implements Parcelable {
    private Integer id;
    private String username, password, avatar;
    @SerializedName("level")
    private Level level;
    private List<Item> fav_items;

    // Constructor
    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Parcelable
    protected User(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        username = in.readString();
        password = in.readString();
        avatar = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    // Getter and Setter
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public List<Item> getFav_items() {
        return fav_items;
    }

    public void setFav_items(List<Item> fav_items) {
        this.fav_items = fav_items;
    }

    public enum Level {
        @SerializedName("admin")
        ADMIN,
        @SerializedName("student")
        STUDENT,
        @SerializedName("instructor")
        INSTRUCTOR,
        @SerializedName("none")
        NONE
    }

    // Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(avatar);
    }
}
