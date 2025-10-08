package com.rafdi.vitechasia.blog.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import androidx.appcompat.app.AppCompatDelegate;

/**
 * Utility class for managing application themes (light/dark mode).
 * Handles theme persistence and application across activities.
 */
public class ThemeManager {
    private static final String THEME_PREF = "theme_pref";
    private static final String THEME_MODE = "theme_mode";

    public enum ThemeMode {
        LIGHT,
        DARK,
        SYSTEM
    }

    public static void applyTheme(ThemeMode themeMode) {
        switch (themeMode) {
            case LIGHT:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case DARK:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case SYSTEM:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }
    }

    public static ThemeMode getCurrentThemeMode(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(THEME_PREF, Context.MODE_PRIVATE);
        int savedTheme = sharedPref.getInt(THEME_MODE, ThemeMode.SYSTEM.ordinal());
        return ThemeMode.values()[savedTheme];
    }

    public static void saveThemeMode(Context context, ThemeMode themeMode) {
        SharedPreferences sharedPref = context.getSharedPreferences(THEME_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(THEME_MODE, themeMode.ordinal());
        editor.apply();
    }

    public static boolean isDarkTheme(Context context) {
        int nightModeFlags = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
    }
}
