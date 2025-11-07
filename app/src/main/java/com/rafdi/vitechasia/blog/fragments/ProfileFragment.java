package com.rafdi.vitechasia.blog.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.rafdi.vitechasia.blog.R;
import com.rafdi.vitechasia.blog.activities.LoginActivity;
import com.rafdi.vitechasia.blog.models.User;
import com.rafdi.vitechasia.blog.utils.SessionManager;
import com.rafdi.vitechasia.blog.utils.ThemeManager;

/**
 * Fragment for displaying user profile information and settings.
 * Shows user details and provides logout functionality.
 */
public class ProfileFragment extends Fragment {
    
    private ImageView profileImage;
    private TextView profileName, profileEmail;
    private TextView themeModeSummary;
    private com.google.android.material.materialswitch.MaterialSwitch darkThemeSwitch;
    private com.google.android.material.materialswitch.MaterialSwitch systemThemeSwitch;
    private Button loginButton, logoutButton;
    private ProgressBar progressBar;
    
    private SessionManager sessionManager;
    private boolean isUpdatingTheme = false;

    public ProfileFragment() {
        //Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        
        // Initialize views
        profileImage = view.findViewById(R.id.profile_image);
        profileName = view.findViewById(R.id.profile_name);
        profileEmail = view.findViewById(R.id.profile_email);
        loginButton = view.findViewById(R.id.btn_login);
        logoutButton = view.findViewById(R.id.btn_logout);
        progressBar = view.findViewById(R.id.progress_bar);
        darkThemeSwitch = view.findViewById(R.id.switch_dark_theme);
        systemThemeSwitch = view.findViewById(R.id.switch_system_theme);
        themeModeSummary = view.findViewById(R.id.theme_mode_summary);
        
        // Initialize Session Manager
        if (getContext() != null) {
            sessionManager = new SessionManager(getContext());
        }
        
        // Set up click listeners
        loginButton.setOnClickListener(v -> handleLogin());
        logoutButton.setOnClickListener(v -> handleLogout());
        
        // Set up theme switch listeners
        setupThemeSwitches();
        
        // Load current theme settings
        loadThemeSettings();
        
        return view;
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Load profile data when view is created
        loadProfileData();
    }
    
    private void loadProfileData() {
        showLoading(true);
        
        if (sessionManager != null && sessionManager.isLoggedIn()) {
            // Load user from session
            User user = sessionManager.getUserDetails();
            if (user != null) {
                // Update UI with user data
                profileName.setText(user.getName());
                profileEmail.setText(user.getEmail());
                
                // Load profile image if available
                if (user.getPhotoUrl() != null && !user.getPhotoUrl().isEmpty()) {
                    Glide.with(this)
                         .load(user.getPhotoUrl())
                         .circleCrop()
                         .placeholder(R.drawable.ic_profile_placeholder)
                         .into(profileImage);
                } else {
                    profileImage.setImageResource(R.drawable.ic_profile_placeholder);
                }
                
                // Show user info and hide login button
                loginButton.setVisibility(View.GONE);
                logoutButton.setVisibility(View.VISIBLE);
                profileName.setVisibility(View.VISIBLE);
                profileEmail.setVisibility(View.VISIBLE);
            } else {
                // User data is null, treat as not logged in
                handleNotLoggedIn();
            }
        } else {
            // User is not logged in
            handleNotLoggedIn();
        }
        
        showLoading(false);
    }
    
    private void handleLogin() {
        // Open LoginActivity
        if (getActivity() != null) {
            startActivity(new Intent(getActivity(), LoginActivity.class));
        }
    }
    
    private void handleLogout() {
        if (sessionManager != null) {
            sessionManager.logoutUser();
            // Update UI
            handleNotLoggedIn();
            // Show login screen
            if (getActivity() != null) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        }
    }
    
    private void handleNotLoggedIn() {
        // User is not logged in, show login button and hide profile info
        loginButton.setVisibility(View.VISIBLE);
        logoutButton.setVisibility(View.GONE);
        profileName.setVisibility(View.GONE);
        profileEmail.setVisibility(View.GONE);
        profileName.setText("");
        profileEmail.setText("");
        profileImage.setImageResource(R.drawable.ic_profile_placeholder);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        // Reload profile data when fragment is resumed
        loadProfileData();
    }
    
    private void showLoading(boolean isLoading) {
        if (progressBar != null) {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        }
    }
    
    private void setupThemeSwitches() {
        darkThemeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isUpdatingTheme) return;
            
            if (isChecked) {
                // If dark theme is enabled, make sure system theme is disabled
                systemThemeSwitch.setChecked(false);
                applyTheme(ThemeManager.ThemeMode.DARK);
            } else if (!systemThemeSwitch.isChecked()) {
                // If both switches are off, default to light theme
                applyTheme(ThemeManager.ThemeMode.LIGHT);
            }
        });
        
        systemThemeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isUpdatingTheme) return;
            
            if (isChecked) {
                // If system theme is enabled, disable dark theme switch
                darkThemeSwitch.setChecked(false);
                applyTheme(ThemeManager.ThemeMode.SYSTEM);
            } else if (!darkThemeSwitch.isChecked()) {
                // If both switches are off, default to light theme
                applyTheme(ThemeManager.ThemeMode.LIGHT);
            }
        });
    }
    
    private void loadThemeSettings() {
        if (getContext() == null) return;
        
        isUpdatingTheme = true;
        
        ThemeManager.ThemeMode currentTheme = ThemeManager.getCurrentThemeMode(getContext());
        
        switch (currentTheme) {
            case LIGHT:
                darkThemeSwitch.setChecked(false);
                systemThemeSwitch.setChecked(false);
                themeModeSummary.setText(getString(R.string.theme_set_to, getString(R.string.theme_light)));
                break;
            case DARK:
                darkThemeSwitch.setChecked(true);
                systemThemeSwitch.setChecked(false);
                themeModeSummary.setText(getString(R.string.theme_set_to, getString(R.string.theme_dark)));
                break;
            case SYSTEM:
                darkThemeSwitch.setChecked(false);
                systemThemeSwitch.setChecked(true);
                themeModeSummary.setText(getString(R.string.theme_set_to, getString(R.string.theme_system)));
                break;
        }
        
        isUpdatingTheme = false;
    }
    
    private void applyTheme(ThemeManager.ThemeMode themeMode) {
        if (getContext() == null) return;
        
        // Save the theme preference
        ThemeManager.saveThemeMode(getContext(), themeMode);
        
        // Apply the theme
        ThemeManager.applyTheme(themeMode);
        
        // Update the UI to reflect the current theme
        updateThemeSummary(themeMode);
        
        // Show a toast to confirm the theme change
        String themeName = "";
        switch (themeMode) {
            case LIGHT:
                themeName = getString(R.string.theme_light);
                break;
            case DARK:
                themeName = getString(R.string.theme_dark);
                break;
            case SYSTEM:
                themeName = getString(R.string.theme_system);
                break;
        }
        
        if (getContext() != null) {
            Toast.makeText(getContext(), 
                         getString(R.string.theme_set_to, themeName), 
                         Toast.LENGTH_SHORT).show();
        }
    }
    
    private void updateThemeSummary(ThemeManager.ThemeMode themeMode) {
        String themeName = "";
        switch (themeMode) {
            case LIGHT:
                themeName = getString(R.string.theme_light);
                break;
            case DARK:
                themeName = getString(R.string.theme_dark);
                break;
            case SYSTEM:
                themeName = getString(R.string.theme_system);
                break;
        }
        themeModeSummary.setText(getString(R.string.theme_set_to, themeName));
    }
}
