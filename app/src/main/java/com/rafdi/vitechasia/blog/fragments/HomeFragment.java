package com.rafdi.vitechasia.blog.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.media.AudioManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.rafdi.vitechasia.blog.R;
import com.rafdi.vitechasia.blog.adapters.ArticleHorizontalAdapter;
import com.rafdi.vitechasia.blog.models.Article;
import com.rafdi.vitechasia.blog.utils.DataHandler;

import java.util.List;

/**
 * Fragment for the home screen displaying featured articles and categories.
 * Shows latest, popular, and bookmarked content sections.
 */
public class HomeFragment extends Fragment implements ArticleHorizontalAdapter.OnArticleClickListener {
    
    public interface NavigationCallback {
        void onHeaderClicked(int navItemId);
    }
    
    private NavigationCallback navigationCallback;
    

    // RecyclerViews
    private RecyclerView latestArticlesRecyclerView;
    private RecyclerView popularArticlesRecyclerView;
    private RecyclerView bookmarkedArticlesRecyclerView;
    private RecyclerView sportsArticlesRecyclerView;
    private RecyclerView techArticlesRecyclerView;
    private RecyclerView newsArticlesRecyclerView;

    // View All Buttons
    private MaterialButton viewAllSportsButton;
    private MaterialButton viewAllTechButton;
    private MaterialButton viewAllNewsButton;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize DataHandler to enable API-first functionality
        if (getContext() != null) {
            DataHandler.initialize(getContext().getApplicationContext());
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof NavigationCallback) {
            navigationCallback = (NavigationCallback) context;
        }
    }

    // Navigation constants
    private static final String NAV_LATEST = "latest";
    private static final String NAV_POPULAR = "popular";
    private static final String NAV_BOOKMARKS = "bookmarks";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Initialize views
        initializeViews(view);
        setupRecyclerViews();
        setupClickListeners();
    }

    private void initializeViews(View view) {
        // Initialize RecyclerViews
        latestArticlesRecyclerView = view.findViewById(R.id.latestArticlesRecyclerView);
        popularArticlesRecyclerView = view.findViewById(R.id.popularArticlesRecyclerView);
        bookmarkedArticlesRecyclerView = view.findViewById(R.id.bookmarkedArticlesRecyclerView);
        sportsArticlesRecyclerView = view.findViewById(R.id.sportsArticlesRecyclerView);
        techArticlesRecyclerView = view.findViewById(R.id.techArticlesRecyclerView);
        newsArticlesRecyclerView = view.findViewById(R.id.newsArticlesRecyclerView);

        // Initialize View All buttons
        viewAllSportsButton = view.findViewById(R.id.viewAllSportsButton);
        viewAllTechButton = view.findViewById(R.id.viewAllTechButton);
        viewAllNewsButton = view.findViewById(R.id.viewAllNewsButton);
    }

    private void setupRecyclerViews() {
        // Latest Articles
        setupHorizontalRecyclerView(latestArticlesRecyclerView, "latest");
        
        // Popular Articles
        setupHorizontalRecyclerView(popularArticlesRecyclerView, "popular");
        
        // Bookmarked Articles
        setupHorizontalRecyclerView(bookmarkedArticlesRecyclerView, "bookmarked");
        
        // Category Articles
        setupHorizontalRecyclerView(sportsArticlesRecyclerView, DataHandler.CATEGORY_SPORTS);
        setupHorizontalRecyclerView(techArticlesRecyclerView, DataHandler.CATEGORY_TECH);
        setupHorizontalRecyclerView(newsArticlesRecyclerView, DataHandler.CATEGORY_NEWS);
    }
    
    private void setupHorizontalRecyclerView(RecyclerView recyclerView, String type) {
        if (recyclerView == null) return;
        
        recyclerView.setLayoutManager(new LinearLayoutManager(
            requireContext(), 
            LinearLayoutManager.HORIZONTAL, 
            false
        ));
        
        ArticleHorizontalAdapter adapter = new ArticleHorizontalAdapter(this);
        recyclerView.setAdapter(adapter);
        
        // Load appropriate data based on type
        if ("latest".equals(type)) {
           List<Article> latestArticles = DataHandler.getNewestArticles(5);
           adapter.setArticles(latestArticles);
        } else if ("popular".equals(type)) {
           List<Article> popularArticles = DataHandler.getMostViewedArticles(5);
           adapter.setArticles(popularArticles);
        } else if ("bookmarked".equals(type)) {
            List<Article> bookmarkedArticles = DataHandler.getBookmarkedArticles(requireContext());
            if (bookmarkedArticles.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                View bookmarkedHeader = getView().findViewById(R.id.bookmarkedHeader);
                if (bookmarkedHeader != null) {
                    bookmarkedHeader.setVisibility(View.GONE);
                }
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                adapter.setArticles(bookmarkedArticles);
            }
        } else {
            // This is a category view
            DataHandler.getInstance().getArticlesByCategory(type, new DataHandler.DataLoadListener() {
                @Override
                public void onDataLoaded(List<Article> categoryArticles) {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            if (categoryArticles != null && !categoryArticles.isEmpty()) {
                                adapter.setArticles(categoryArticles.subList(0, Math.min(5, categoryArticles.size())));
                            }
                        });
                    }
                }
                
                @Override
                public void onError(String message) {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            Log.e("HomeFragment", "Error loading articles: " + message);
                            Toast.makeText(getContext(), "Error loading articles: " + message, Toast.LENGTH_SHORT).show();
                        });
                    }
                }
            });
        }
    }

    private void setupClickListeners() {
        View rootView = getView();
        if (rootView == null) {
            return;
        }

        // Set up View All button click listeners
        View.OnClickListener categoryClickListener = v -> {
            if (isVibrateMode() && v != null) {
                v.performHapticFeedback(android.view.HapticFeedbackConstants.VIRTUAL_KEY);
            }
            
            int id = v.getId();
            if (id == R.id.viewAllSportsButton) {
                navigateToCategory(DataHandler.CATEGORY_SPORTS);
            } else if (id == R.id.viewAllTechButton) {
                navigateToCategory(DataHandler.CATEGORY_TECH);
            } else if (id == R.id.viewAllNewsButton) {
                navigateToCategory(DataHandler.CATEGORY_NEWS);
            }
        };

        if (viewAllSportsButton != null) {
            viewAllSportsButton.setOnClickListener(categoryClickListener);
        }
        
        if (viewAllTechButton != null) {
            viewAllTechButton.setOnClickListener(categoryClickListener);
        }
        
        if (viewAllNewsButton != null) {
            viewAllNewsButton.setOnClickListener(categoryClickListener);
        }

        // Set up header click listeners to navigate to respective fragments
        setupHeaderClickListener(R.id.latestHeader, () -> navigateToFragment(NAV_LATEST));
        setupHeaderClickListener(R.id.popularHeader, () -> navigateToFragment(NAV_POPULAR));
        setupHeaderClickListener(R.id.bookmarkedHeader, () -> navigateToFragment(NAV_BOOKMARKS));

        // Set up category card click listeners
        View.OnClickListener cardClickListener = v -> {
            if (isVibrateMode() && v != null) {
                v.performHapticFeedback(android.view.HapticFeedbackConstants.VIRTUAL_KEY);
            }
            
            int id = v.getId();
            if (id == R.id.sportsCard) {
                navigateToCategory(DataHandler.CATEGORY_SPORTS);
            } else if (id == R.id.techCard) {
                navigateToCategory(DataHandler.CATEGORY_TECH);
            } else if (id == R.id.newsCard) {
                navigateToCategory(DataHandler.CATEGORY_NEWS);
            }
        };

        View sportsCard = rootView.findViewById(R.id.sportsCard);
        if (sportsCard != null) {
            sportsCard.setOnClickListener(cardClickListener);
        }

        View techCard = rootView.findViewById(R.id.techCard);
        if (techCard != null) {
            techCard.setOnClickListener(cardClickListener);
        }

        View newsCard = rootView.findViewById(R.id.newsCard);
        if (newsCard != null) {
            newsCard.setOnClickListener(cardClickListener);
        }
    }

    
    private boolean isVibrateMode() {
        if (getActivity() == null) return false;
        AudioManager audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        return audioManager != null && audioManager.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE;
    }

    private void setupHeaderClickListener(int viewId, Runnable onClickAction) {
        View view = getView();
        if (view == null) {
            return;
        }
        
        View header = view.findViewById(viewId);
        if (header != null) {
            header.setOnClickListener(v -> {
                if (isVibrateMode()) {
                    v.performHapticFeedback(android.view.HapticFeedbackConstants.VIRTUAL_KEY);
                }
                onClickAction.run();
            });
            header.setClickable(true);
            header.setFocusable(true);
        }
    }

    private void navigateToFragment(String fragmentType) {
        if (getActivity() == null) return;
        
        int navItemId = -1;
        Fragment fragment = null;
        
        switch (fragmentType) {
            case NAV_LATEST:
                fragment = new LatestFragment();
                navItemId = R.id.navigation_latest;
                break;
            case NAV_POPULAR:
                fragment = new PopularFragment();
                navItemId = R.id.navigation_popular;
                break;
            case NAV_BOOKMARKS:
                fragment = new BookmarkFragment();
                navItemId = R.id.navigation_bookmark;
                break;
        }
        
        if (fragment != null) {
            getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
                
            // Update bottom navigation if callback is available
            if (navigationCallback != null) {
                navigationCallback.onHeaderClicked(navItemId);
            }
        }
    }
    
    private void navigateToCategory(String categoryId) {
        if (getActivity() != null) {
            Fragment categoryFragment = CategoryFragment.newInstance(categoryId);
            getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, categoryFragment)
                .addToBackStack(null)
                .commit();
        }
    }

    @Override
    public void onArticleClick(Article article) {
        // Create a new instance of ArticleDetailFragment with the clicked article
        ArticleDetailFragment articleDetailFragment = ArticleDetailFragment.newInstance(article);

        // Get the FragmentManager and start a transaction
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, articleDetailFragment) // Use your container view id
                .addToBackStack(null) // Add to back stack so user can navigate back
                .commit();
    }
}
