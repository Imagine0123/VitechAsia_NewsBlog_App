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

import com.bumptech.glide.Glide;
import com.rafdi.vitechasia.blog.R;
import com.rafdi.vitechasia.blog.models.User;

public class ProfileFragment extends Fragment {
    
    private ImageView profileImage;
    private TextView profileName, profileEmail, profileBio;
    private Button loginButton, logoutButton;
    private ProgressBar progressBar;
    
    // For demo purposes - replace with actual user authentication
    private boolean isLoggedIn = false;

    public ProfileFragment() {
        // Required empty public constructor
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
        
        // Set up click listeners
        loginButton.setOnClickListener(v -> handleLogin());
        logoutButton.setOnClickListener(v -> handleLogout());
        
        // Load profile data
        loadProfileData();
        
        return view;
    }
    
    private void loadProfileData() {
        showLoading(true);
        
        // Simulate network/database delay
        profileImage.postDelayed(() -> {
            //TODO: Replace with actual user data loading logic
            if (isLoggedIn) {
                // Sample user data - replace with actual user data
                User currentUser = new User(
                    "1",
                    "John Doe",
                    "john.doe@example.com",
                    "https://example.com/profile.jpg",
                    "Android Developer | Tech Enthusiast | Coffee Lover"
                );
                displayUserProfile(currentUser);
            } else {
                showGuestView();
            }
            showLoading(false);
        }, 1000);
    }
    
    private void displayUserProfile(User user) {
        // Update UI with user data
        profileName.setText(user.getName());
        profileEmail.setText(user.getEmail());
        
        // Load profile image using Glide
        Glide.with(requireContext())
                .load(user.getPhotoUrl())
                .placeholder(R.drawable.ic_profile_placeholder)
                .circleCrop()
                .into(profileImage);
        
        // Update UI state
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
        //TODO: Implement login logic
        isLoggedIn = true;
        loadProfileData();
        Toast.makeText(getContext(), "Login clicked", Toast.LENGTH_SHORT).show();
    }
    
    private void handleLogout() {
        //TODO: Implement logout logic
        isLoggedIn = false;
        showGuestView();
        Toast.makeText(getContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();
    }
    
    private void showLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
    }
}
