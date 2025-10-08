package com.rafdi.vitechasia.blog.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.rafdi.vitechasia.blog.R;
import com.rafdi.vitechasia.blog.activities.LoginActivity;
import com.rafdi.vitechasia.blog.models.User;
import com.rafdi.vitechasia.blog.utils.SessionManager;

/**
 * Fragment for displaying user profile information and settings.
 * Shows user details and provides logout functionality.
 */
public class ProfileFragment extends Fragment {
    
    private ImageView profileImage;
    private TextView profileName, profileEmail;
    private Button loginButton, logoutButton;
    private ProgressBar progressBar;
    
    private SessionManager sessionManager;

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
        
        // Initialize Session Manager
        if (getContext() != null) {
            sessionManager = new SessionManager(getContext());
        }
        
        // Set up click listeners
        loginButton.setOnClickListener(v -> handleLogin());
        logoutButton.setOnClickListener(v -> handleLogout());
        
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
}
