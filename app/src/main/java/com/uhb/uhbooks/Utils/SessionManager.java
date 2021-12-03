package com.uhb.uhbooks.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.uhb.uhbooks.models.User;

/**
 * User: jpotts
 * Date: 8/27/13
 * Time: 7:00 PM
 */

public class SessionManager {

    private static SessionManager instance;

    private SharedPreferences pref;
    private Editor editor;
    private Context context;

    private int PRIVATE_MODE = 0;
    private static String PREF_NAME = "";
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String KEY_ID = "id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_LEVEL = "level";
    private static final String KEY_AVATAR = "avatar";
    private static final String KEY_TOKEN = "token";

    public interface SessionListener {
        void onTokenInvalid();
    }

    // Constructor
    private SessionManager(Context context) {
        this.context = context;
        PREF_NAME = context.getPackageName();
        pref = this.context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public static SessionManager getInstance(Context context) {
        if (instance == null) {
            instance = new SessionManager(context);
        }
        return instance;
    }

    public void createLoginSession(String token, Integer id, String name, User.Level level) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_TOKEN, token);
        editor.putInt(KEY_ID, id);
        editor.putString(KEY_USERNAME, name);
        editor.putString(KEY_LEVEL, level.name());
        editor.commit();
    }


    public void redirectOnLogin(Class loginActivity) {
        Intent i = new Intent(context, loginActivity);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    public void redirectOnLogout(Class loginActivity) {

        Intent i = new Intent(context, loginActivity);

        // clear stack and start new activity
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(i);
    }

    public void clearUserSettings() {
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
    }

    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }


    // Getter and Setter
    public String getToken() {
        return pref.getString(KEY_TOKEN, "");
    }

    public void setToken(String token) {
        editor.putString(KEY_TOKEN, token);
        editor.commit();
    }

    public Integer getId() {
        return pref.getInt(KEY_ID, 0);
    }

    public void setId(Integer id) {
        editor.putInt(KEY_ID, id);
        editor.commit();
    }

    public String getName() {
        return pref.getString(KEY_USERNAME, "");

    }

    public void setName(String name) {
        editor.putString(KEY_USERNAME, name);
        editor.commit();
    }

    public User.Level getLevel() {
        String level = pref.getString(KEY_LEVEL, "");
        if (level.isEmpty()) return User.Level.NONE;
        return User.Level.valueOf(level);
    }

    public void setLevel(User.Level level) {
        editor.putString(KEY_LEVEL, level.name());
        editor.commit();
    }

    public String getAvatar() {
        return pref.getString(KEY_AVATAR, "");
    }

    public void setAvatar(String avatar) {
        editor.putString(KEY_AVATAR, avatar);
        editor.commit();
    }
}