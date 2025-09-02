package com.rafdi.vitechasia.blog.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.rafdi.vitechasia.blog.models.User;

public class SessionManager {
    private static final String PREF_NAME = "VitechAsiaBlogPref";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_EMAIL = "userEmail";
    private static final String KEY_USER_PHOTO = "userPhotoUrl";
    private static final String KEY_USER_BIO = "userBio";
    
    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;
    
    public SessionManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }
    
    public void createLoginSession(User user) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_USER_ID, user.getId());
        editor.putString(KEY_USER_NAME, user.getName());
        editor.putString(KEY_USER_EMAIL, user.getEmail());
        editor.putString(KEY_USER_PHOTO, user.getPhotoUrl());
        editor.putString(KEY_USER_BIO, user.getBio());
        editor.apply();
    }
    
    public User getUser() {
        if (!isLoggedIn()) {
            return null;
        }
        
        User user = new User();
        user.setId(pref.getString(KEY_USER_ID, ""));
        user.setName(pref.getString(KEY_USER_NAME, ""));
        user.setEmail(pref.getString(KEY_USER_EMAIL, ""));
        user.setPhotoUrl(pref.getString(KEY_USER_PHOTO, ""));
        user.setBio(pref.getString(KEY_USER_BIO, ""));
        
        return user;
    }
    
    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }
    
    public void logoutUser() {
        editor.clear();
        editor.apply();
    }
}
