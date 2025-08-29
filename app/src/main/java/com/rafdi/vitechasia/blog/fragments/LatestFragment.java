package com.rafdi.vitechasia.blog.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class LatestFragment extends Fragment {
    
    private RecyclerView recyclerView;
    private ArticleAdapter articleAdapter;
    private List<Article> articleList;
    
    public LatestFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_latest, container, false);
        
        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recycler_view_articles);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        // Initialize article list and adapter
        articleList = new ArrayList<>();
        articleAdapter = new ArticleAdapter(articleList, this::onArticleClick);
        recyclerView.setAdapter(articleAdapter);
        
        // Load articles (you'll replace this with your actual data loading)
        loadArticles();
        
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
    
    private void loadArticles() {
        // TODO: Replace with your actual data loading logic
        // This is just sample data
        List<Article> sampleArticles = new ArrayList<>();
        sampleArticles.add(new Article(
                "1",
                "Lamp construction: Plant for any anything that",
                "Full article content would go here...",
                "https://example.com/image1.jpg",
                "Tech",
                "user1",
                "Riski hia",
                "https://example.com/avatar1.jpg",
                new Date()
        ));
        
        articleList.addAll(sampleArticles);
        articleAdapter.notifyDataSetChanged();
    }
}
