package com.rafdi.vitechasia.blog.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rafdi.vitechasia.blog.R;
import com.rafdi.vitechasia.blog.adapters.ArticleHorizontalAdapter;
import com.rafdi.vitechasia.blog.models.Article;

import java.util.ArrayList;
import java.util.List;

public class SubcategoryFragment extends Fragment implements ArticleHorizontalAdapter.OnArticleClickListener {
    private static final String ARG_CATEGORY_NAME = "category_name";
    private static final String ARG_SUBCATEGORY_NAME = "subcategory_name";

    private String categoryName;
    private String subcategoryName;
    private RecyclerView articlesRecyclerView;
    private ArticleHorizontalAdapter adapter;

    public static SubcategoryFragment newInstance(String categoryName, String subcategoryName) {
        SubcategoryFragment fragment = new SubcategoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY_NAME, categoryName);
        args.putString(ARG_SUBCATEGORY_NAME, subcategoryName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryName = getArguments().getString(ARG_CATEGORY_NAME);
            subcategoryName = getArguments().getString(ARG_SUBCATEGORY_NAME);
        }
        adapter = new ArticleHorizontalAdapter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subcategory, container, false);

        // Set up toolbar title
        if (getActivity() != null && getActivity().getActionBar() != null) {
            getActivity().setTitle(subcategoryName);
        }

        // Initialize views
        TextView titleText = view.findViewById(R.id.subcategoryTitle);
        titleText.setText(getString(R.string.articles_in_subcategory, categoryName, subcategoryName));

        // Set up RecyclerView
        articlesRecyclerView = view.findViewById(R.id.articlesRecyclerView);
        articlesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        articlesRecyclerView.setAdapter(adapter);

        // Load articles for this subcategory
        loadArticles();

        return view;
    }

    private void loadArticles() {
        // TODO: Replace with actual data loading from your data source
        List<Article> articles = getDummyArticles();
        adapter.setArticles(articles);
    }

    private List<Article> getDummyArticles() {
        // TODO: Replace with actual data loading
        List<Article> articles = new ArrayList<>();
        // Add sample articles here
        return articles;
    }

    @Override
    public void onArticleClick(Article article) {
        // Open article detail
        if (getActivity() != null) {
            ArticleDetailFragment fragment = ArticleDetailFragment.newInstance(article);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
