package com.rafdi.vitechasia.blog.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup;
import android.text.style.ForegroundColorSpan;
import android.graphics.drawable.ColorDrawable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.rafdi.vitechasia.blog.utils.SearchHistoryManager;
import com.rafdi.vitechasia.blog.adapters.SearchHistoryAdapter;
import java.util.List;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.rafdi.vitechasia.blog.R;
import com.rafdi.vitechasia.blog.fragments.ArticleDetailFragment;
import com.rafdi.vitechasia.blog.fragments.BottomNavFragment;
import com.rafdi.vitechasia.blog.fragments.BookmarkFragment;
import com.rafdi.vitechasia.blog.fragments.HomeFragment;
import com.rafdi.vitechasia.blog.fragments.LatestFragment;
import com.rafdi.vitechasia.blog.fragments.PopularFragment;
import com.rafdi.vitechasia.blog.fragments.ProfileFragment;
import com.rafdi.vitechasia.blog.fragments.SearchResultsFragment;
import com.rafdi.vitechasia.blog.models.Article;
import com.rafdi.vitechasia.blog.utils.SessionManager;
import com.rafdi.vitechasia.blog.utils.ThemeManager;
import com.rafdi.vitechasia.blog.adapters.ArticleVerticalAdapter;

/**
 * Main activity for the blog application home page.
 * Handles navigation, search, and fragment management.
 */
public class HomePage extends AppCompatActivity implements ArticleVerticalAdapter.OnArticleClickListener, HomeFragment.NavigationCallback {

    private static final String TAG = "HomePage";
    private SessionManager sessionManager;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Article currentArticle;

    // Search components
    private EditText searchText;
    private ImageButton searchButton;
    private PopupWindow searchDropdown;
    private SearchHistoryManager searchHistoryManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initialize session manager
        sessionManager = new SessionManager(this);

        // Check if user is logged in, if not redirect to LoginActivity
        if (!sessionManager.isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // Apply theme before setting content view
        ThemeManager.applyTheme(ThemeManager.getCurrentThemeMode(this));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        // Initialize search components
        searchText = findViewById(R.id.searchText);
        searchButton = findViewById(R.id.searchButton);
        searchHistoryManager = SearchHistoryManager.getInstance(this);
        searchDropdown = createSearchDropdown();

        // Enable edge-to-edge display
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            // Update status bar icons color based on theme
            boolean isDark = ThemeManager.isDarkTheme(this);
            WindowInsetsControllerCompat windowInsetsController = ViewCompat.getWindowInsetsController(getWindow().getDecorView());
            if (windowInsetsController != null) {
                windowInsetsController.setAppearanceLightStatusBars(!isDark);
            }

            return insets;
        });

        // Initialize SwipeRefreshLayout
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this::reloadCurrentFragment);

        // Set colors for the refresh indicator
        swipeRefreshLayout.setColorSchemeResources(
                R.color.primary,
                R.color.colorAccent,
                R.color.primary_variant
        );

        //Text Styling
        TextView appNameText = findViewById(R.id.app_name);
        
        // Setup search after all components are initialized
        if (searchText != null && searchButton != null) {
            setupSearch();
        } else {
            // Log error if search components are not found
            if (searchText == null) {
                android.util.Log.e("HomePage", "Search EditText not found in layout");
            }
            if (searchButton == null) {
                android.util.Log.e("HomePage", "Search Button not found in layout");
            }
        }

        String fullText = getString(R.string.app_name);
        SpannableString spannable = new SpannableString(fullText);
        int colorSin = ThemeManager.isDarkTheme(this) ? Color.WHITE : Color.BLACK;
        int colorTesis = getColor(R.color.colorAccent);
        String sinPart = "Sin";

        spannable.setSpan(
                new ForegroundColorSpan(colorSin),
                0,
                sinPart.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        spannable.setSpan(
                new ForegroundColorSpan(colorTesis),
                sinPart.length(),
                fullText.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        appNameText.setText(spannable);

        appNameText.setOnClickListener(v -> {
            loadHomeFragment();
            updateBottomNavSelection(R.id.navigation_home);
        });

        //Bottom navigation fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.bottom_nav_container, new BottomNavFragment())
                .commit();

        //Default load home
        loadHomeFragment();
    }

    public void loadHomeFragment() {
        this.currentArticle = null;
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (!(currentFragment instanceof HomeFragment)) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        }
    }

    public void loadLatestFragment() {
        this.currentArticle = null;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new LatestFragment())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    public void loadPopularFragment() {
        this.currentArticle = null;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new PopularFragment())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    public void loadBookmarkFragment() {
        this.currentArticle = null;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new BookmarkFragment())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    public void loadProfileFragment() {
        this.currentArticle = null;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new ProfileFragment())
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    public void loadArticleDetailFragment(Article article) {
        if (article == null) return;

        try {
            this.currentArticle = article;

            // Create new instance with the article
            ArticleDetailFragment fragment = ArticleDetailFragment.newInstance(article);

            // Perform the fragment transaction
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(null)
                    .commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void reloadCurrentFragment() {
        if (swipeRefreshLayout == null) return;

        try {
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

            if (currentFragment != null) {
                if (currentFragment instanceof ArticleDetailFragment) {
                    Article currentArticle = ((ArticleDetailFragment) currentFragment).getCurrentArticle();
                    if (currentArticle != null) {
                        // Reload the article detail fragment
                        loadArticleDetailFragment(currentArticle);
                    }
                } else if (currentFragment instanceof HomeFragment) {
                    loadHomeFragment();
                } else if (currentFragment instanceof LatestFragment) {
                    loadLatestFragment();
                } else if (currentFragment instanceof PopularFragment) {
                    loadPopularFragment();
                } else if (currentFragment instanceof BookmarkFragment) {
                    loadBookmarkFragment();
                } else if (currentFragment instanceof ProfileFragment) {
                    loadProfileFragment();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Always stop the refresh indicator
            if (swipeRefreshLayout != null) {
                swipeRefreshLayout.postDelayed(() -> {
                    if (swipeRefreshLayout != null) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 500);
            }
        }
    }

    private void setupSearch() {
        if (searchButton == null || searchText == null) {
            android.util.Log.e("HomePage", "Search components not properly initialized");
            return;
        }
        
        // Handle search button click
        searchButton.setOnClickListener(v -> {
            String query = searchText.getText().toString().trim();
            if (!query.isEmpty()) {
                performSearch(query);
                searchDropdown.dismiss(); // Hide dropdown when performing search
            }
        });

        // Handle search on keyboard's search/enter key
        searchText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String query = searchText.getText().toString().trim();
                if (!query.isEmpty()) {
                    performSearch(query);
                    searchDropdown.dismiss(); // Hide dropdown when performing search
                    // Hide the keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchText.getWindowToken(), 0);
                    return true;
                }
            }
            return false;
        });

        // Show search history dropdown when EditText gains focus
        searchText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                List<String> recentSearches = searchHistoryManager.getSearchHistory();
                if (recentSearches != null && !recentSearches.isEmpty()) {
                    showSearchDropdown(recentSearches);
                }
            } else {
                searchDropdown.dismiss();
            }
        });

        // Hide dropdown when clicking outside
        searchText.setOnClickListener(v -> {
            List<String> recentSearches = searchHistoryManager.getSearchHistory();
            if (recentSearches != null && !recentSearches.isEmpty()) {
                showSearchDropdown(recentSearches);
            }
        });

        // Clear search and return to home when search text is empty
        searchText.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().isEmpty() &&
                    getSupportFragmentManager().findFragmentById(R.id.fragment_container) instanceof SearchResultsFragment) {
                    // If search is cleared and we're on search results, go back to home
                    loadHomeFragment();
                }
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });
    }
    
    private PopupWindow createSearchDropdown() {
        // Inflate the dropdown layout
        View dropdownView = getLayoutInflater().inflate(R.layout.layout_search_dropdown, null);

        // Create the popup window
        PopupWindow popupWindow = new PopupWindow(
                dropdownView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );

        // Set background drawable for proper shadow
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Set elevation for Material Design shadow
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            popupWindow.setElevation(8);
        }

        // Set outside touchable to dismiss popup
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(false);

        return popupWindow;
    }

    private void showSearchDropdown(List<String> recentSearches) {
        if (recentSearches.isEmpty()) {
            searchDropdown.dismiss();
            return;
        }

        // Get the dropdown content view
        View dropdownView = searchDropdown.getContentView();
        RecyclerView recyclerView = dropdownView.findViewById(R.id.recyclerView);

        // Limit to 5 recent searches as requested
        List<String> limitedSearches = recentSearches.size() > 5 ?
                recentSearches.subList(0, 5) : recentSearches;

        // Setup adapter
        SearchHistoryAdapter adapter = new SearchHistoryAdapter(limitedSearches,
                query -> {
                    // Set the search text and perform search
                    searchText.setText(query);
                    performSearch(query);
                    searchDropdown.dismiss();

                    // Hide keyboard after selection
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchText.getWindowToken(), 0);
                });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Show popup below the search bar if not already showing
        if (!searchDropdown.isShowing()) {
            searchDropdown.showAsDropDown(searchText);
        }

        // Set on dismiss listener to handle cleanup
        searchDropdown.setOnDismissListener(() -> {
            // Any cleanup if needed
        });
    }
    
    public void performSearch(String query) {
        if (query.trim().isEmpty()) {
            loadHomeFragment();
            return;
        }

        // Add search to history before performing search
        SearchHistoryManager searchHistoryManager = SearchHistoryManager.getInstance(this);
        searchHistoryManager.addSearchQuery(query);

        // Create and show the search results fragment
        SearchResultsFragment fragment = SearchResultsFragment.newInstance(query);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack("search")
                .commit();
    }

    public void updateBottomNavSelection(int itemId) {
        BottomNavigationView bottomNavView = findViewById(R.id.bottom_navigation);
        if (bottomNavView != null) {
            bottomNavView.setSelectedItemId(itemId);
        }
    }

    @Override
    public void onArticleClick(Article article) {
        loadArticleDetailFragment(article);
    }

    @Override
    public void onHeaderClicked(int navItemId) {
        updateBottomNavSelection(navItemId);
    }

    @Override
    protected void onDestroy() {
        // Clean up any pending callbacks to prevent memory leaks
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
            swipeRefreshLayout.destroyDrawingCache();
            swipeRefreshLayout.clearAnimation();
        }
        super.onDestroy();
    }
}