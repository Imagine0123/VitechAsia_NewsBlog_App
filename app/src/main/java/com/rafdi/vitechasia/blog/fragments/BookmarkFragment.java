package com.rafdi.vitechasia.blog.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rafdi.vitechasia.blog.R;
import com.rafdi.vitechasia.blog.adapters.ArticleAdapter;
import com.rafdi.vitechasia.blog.models.Article;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookmarkFragment extends Fragment {
    
    private RecyclerView recyclerView;
    private ArticleAdapter articleAdapter;
    private List<Article> bookmarkedArticles;
    private TextView emptyView;
    
    public BookmarkFragment() {
        //Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);
        
        //Initialize views and recyclerView
        recyclerView = view.findViewById(R.id.recycler_view_articles);
        emptyView = view.findViewById(R.id.empty_view);
        
        //Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        bookmarkedArticles = new ArrayList<>();
        articleAdapter = new ArticleAdapter(bookmarkedArticles, this::onArticleClick);
        recyclerView.setAdapter(articleAdapter);
        
        //Load bookmarked articles
        loadBookmarkedArticles();
        
        return view;
    }
    
    private void onArticleClick(Article article) {
        // Open ArticleDetailFragment when an article is clicked
        ArticleDetailFragment fragment = ArticleDetailFragment.newInstance(article);
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
    
    private void loadBookmarkedArticles() {
        //TODO: Replace with actual data loading logic for bookmarked articles from user
        //This is just sample data
        List<Article> sampleBookmarks = new ArrayList<>();
        sampleBookmarks.add(new Article(
                "3",
                "Bookmarked Article: Android Development Tips",
                "This is a sample bookmarked article with useful Android development tips...",
                "https://example.com/bookmark1.jpg",
                "Android",
                "user3",
                "Jane Smith",
                "https://example.com/avatar3.jpg",
                new Date()
        ));
        
        bookmarkedArticles.clear();
        bookmarkedArticles.addAll(sampleBookmarks);
        articleAdapter.notifyDataSetChanged();
        
        // Show empty view if no bookmarks
        updateEmptyView();
    }
    
    private void updateEmptyView() {
        if (bookmarkedArticles.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }
}
