package com.rafdi.vitechasia.blog;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.rafdi.vitechasia.blog.fragments.BottomNavFragment;
import com.rafdi.vitechasia.blog.fragments.HomeFragment;
import com.rafdi.vitechasia.blog.fragments.LatestFragment;
import com.rafdi.vitechasia.blog.fragments.PopularFragment;
import com.rafdi.vitechasia.blog.fragments.BookmarkFragment;
import com.rafdi.vitechasia.blog.fragments.ProfileFragment;

public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        
        // Set up edge-to-edge display
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set up Vitech text styling
        TextView vitechText = findViewById(R.id.vitechText);
        EditText searchText = findViewById(R.id.searchText);
        ImageButton searchButton = findViewById(R.id.searchButton);
        
        setupSearch();

        String fullText = vitechText.getText().toString();
        SpannableString spannable = new SpannableString(fullText);
        int colorVitech = Color.BLACK;
        int colorAsia = Color.parseColor("#E94B64");
        String vitechPart = "Vitech";

        spannable.setSpan(
                new ForegroundColorSpan(colorVitech),
                0,
                vitechPart.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        spannable.setSpan(
                new ForegroundColorSpan(colorAsia),
                vitechPart.length(),
                fullText.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        vitechText.setText(spannable);
        
        // Set click listener for vitechText to load home fragment and update bottom nav
        vitechText.setOnClickListener(v -> {
            loadHomeFragment();
            updateBottomNavSelection(R.id.navigation_home);
        });
        
        // Add Bottom Navigation Fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.bottom_nav_container, new BottomNavFragment())
                .commit();
        
        // Load home fragment by default
        loadHomeFragment();
    }

    public void loadHomeFragment() {
        // Only replace if the current fragment is not already HomeFragment
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (currentFragment == null || !(currentFragment instanceof HomeFragment)) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        }
    }

    public void loadLatestFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new LatestFragment())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    public void loadPopularFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new PopularFragment())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    public void loadBookmarkFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new BookmarkFragment())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    public void loadProfileFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new ProfileFragment())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }
    
    private void setupSearch() {
        EditText searchText = findViewById(R.id.searchText);
        ImageButton searchButton = findViewById(R.id.searchButton);
        
        searchText.setHint(R.string.search_hint);
        
        searchButton.setOnClickListener(v -> {
            String query = searchText.getText().toString().trim();
            if (!query.isEmpty()) {
                // TODO: Implement search functionality
                android.widget.Toast.makeText(this, 
                    getString(R.string.searching_for, query), 
                    android.widget.Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    /**
     * Updates the selected item in the bottom navigation
     * @param itemId The ID of the menu item to select
     */
    public void updateBottomNavSelection(int itemId) {
        BottomNavigationView bottomNavView = findViewById(R.id.bottom_navigation);
        if (bottomNavView != null) {
            bottomNavView.setSelectedItemId(itemId);
        }
    }
    }