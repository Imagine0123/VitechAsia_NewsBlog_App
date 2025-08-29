package com.rafdi.vitechasia.blog.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.rafdi.vitechasia.blog.R;
import com.rafdi.vitechasia.blog.HomePage;

public class BottomNavFragment extends Fragment implements BottomNavigationView.OnItemSelectedListener {

    private BottomNavigationView bottomNavView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bottom_nav, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        bottomNavView = view.findViewById(R.id.bottom_navigation);
        bottomNavView.setOnItemSelectedListener(this);
        
        // Set the default selected item
        bottomNavView.setSelectedItemId(R.id.navigation_home);
        
        // Load the home fragment initially
        if (getActivity() != null) {
            ((HomePage) getActivity()).loadHomeFragment();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        
        if (getActivity() instanceof HomePage) {
            HomePage activity = (HomePage) getActivity();
            if (itemId == R.id.navigation_home) {
                activity.loadHomeFragment();
                return true;
            } else if (itemId == R.id.navigation_latest) {
                activity.loadLatestFragment();
                return true;
            } else if (itemId == R.id.navigation_popular) {
                activity.loadPopularFragment();
                return true;
            } else if (itemId == R.id.navigation_bookmark) {
                activity.loadBookmarkFragment();
                return true;
            } else if (itemId == R.id.navigation_profile) {
                activity.loadProfileFragment();
                return true;
            }
        }
        return false;
    }
}
