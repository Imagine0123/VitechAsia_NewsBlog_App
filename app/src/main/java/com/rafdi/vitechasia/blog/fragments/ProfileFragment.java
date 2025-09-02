package com.rafdi.vitechasia.blog.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.rafdi.vitechasia.blog.R;
import com.rafdi.vitechasia.blog.models.User;
import com.rafdi.vitechasia.blog.utils.SessionManager;

public class ProfileFragment extends Fragment {
    
    private ImageView profileImage;
    private TextView profileName, profileEmail, profileBio;
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
        
        //Initialize views
        profileImage = view.findViewById(R.id.profile_image);
        profileName = view.findViewById(R.id.profile_name);
        profileEmail = view.findViewById(R.id.profile_email);
        loginButton = view.findViewById(R.id.btn_login);
        logoutButton = view.findViewById(R.id.btn_logout);
        progressBar = view.findViewById(R.id.progress_bar);
        
        //Initialize Session Manager
        if (getContext() != null) {
            sessionManager = new SessionManager(getContext());
        }
        
        //Set up click listeners
        loginButton.setOnClickListener(v -> handleLogin());
        logoutButton.setOnClickListener(v -> handleLogout());
        
        //Load profile data
        loadProfileData();
        
        return view;
    }
    
    private void loadProfileData() {
        showLoading(true);
        
        //Simulate network/database delay
        profileImage.postDelayed(() -> {
            if (sessionManager != null && sessionManager.isLoggedIn()) {
                //Load user from session
                User currentUser = sessionManager.getUser();
                if (currentUser != null) {
                    displayUserProfile(currentUser);
                } else {
                    showGuestView();
                }
            } else {
                showGuestView();
            }
            showLoading(false);
        }, 500); //Reduced delay for better UX
    }
    
    private void displayUserProfile(User user) {
        //Load user data
        profileName.setText(user.getName());
        profileEmail.setText(user.getEmail());
        
        //Load profile image
        Glide.with(requireContext())
                .load(user.getPhotoUrl())
                .placeholder(R.drawable.ic_profile_placeholder)
                .circleCrop()
                .into(profileImage);
        
        //Update UI state for logged-in user to allow logging out
        loginButton.setVisibility(View.GONE);
        logoutButton.setVisibility(View.VISIBLE);
    }
    
    private void showGuestView() {
        profileName.setText(R.string.guest_user);
        profileEmail.setText(R.string.guest_email_hint);
        profileImage.setImageResource(R.drawable.ic_profile_placeholder);
        loginButton.setVisibility(View.VISIBLE);
        logoutButton.setVisibility(View.GONE);
    }
    
    private void handleLogin() {
        //Login simulation
        if (getContext() != null) {
            User sampleUser = new User(
                "1",
                "John Doe",
                "john.doe@example.com",
                "https://example.com/profile.jpg",
                "Android Developer | Tech Enthusiast | Coffee Lover"
            );
            
            if (sessionManager != null) {
                sessionManager.createLoginSession(sampleUser);
                loadProfileData();
                Toast.makeText(getContext(), "Successfully logged in", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    private void handleLogout() {
        if (sessionManager != null) {
            sessionManager.logoutUser();
            showGuestView();
            Toast.makeText(getContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void showLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }
}
