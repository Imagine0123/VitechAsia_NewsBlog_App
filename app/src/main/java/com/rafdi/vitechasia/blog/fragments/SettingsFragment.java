package com.rafdi.vitechasia.blog.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.rafdi.vitechasia.blog.R;
import com.rafdi.vitechasia.blog.utils.ThemeManager;
import com.rafdi.vitechasia.blog.utils.ThemeManager.ThemeMode;

public class SettingsFragment extends PreferenceFragmentCompat {
    
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Load the preferences from an XML resource
        setPreferencesFromResource(R.xml.preferences, rootKey);
        
        // Set up theme preference change listener
        String themeKey = getString(R.string.pref_theme_key);
        if (themeKey != null) {
            ListPreference themePreference = findPreference(themeKey);
            if (themePreference != null) {
                themePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        if (getContext() == null) return false;
                        
                        String themeValue = (String) newValue;
                        ThemeManager.ThemeMode themeMode;
                        
                        if (getString(R.string.pref_theme_light).equals(themeValue)) {
                            themeMode = ThemeManager.ThemeMode.LIGHT;
                        } else if (getString(R.string.pref_theme_dark).equals(themeValue)) {
                            themeMode = ThemeManager.ThemeMode.DARK;
                        } else {
                            themeMode = ThemeManager.ThemeMode.SYSTEM;
                        }
                        
                        ThemeManager.saveThemeMode(requireContext(), themeMode);
                        ThemeManager.applyTheme(themeMode);
                        
                        // Recreate activity to apply theme changes
                        if (getActivity() != null) {
                            getActivity().recreate();
                        }
                        return true;
                    }
                });
            }
        }
    }
    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                           @Nullable Bundle savedInstanceState) {
        // Apply theme before creating the view
        Context context = getContext();
        if (context != null) {
            ThemeMode themeMode = ThemeManager.getCurrentThemeMode(context);
            ThemeManager.applyTheme(themeMode);
        }
        
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
