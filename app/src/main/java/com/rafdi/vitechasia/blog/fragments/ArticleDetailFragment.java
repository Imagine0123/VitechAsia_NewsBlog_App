package com.rafdi.vitechasia.blog.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.rafdi.vitechasia.blog.R;
import com.rafdi.vitechasia.blog.models.Article;

public class ArticleDetailFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    
    private static final String ARG_ARTICLE = "article";
    
    private ImageView articleImage;
    private TextView articleCategory;
    private TextView articleSubcategory;
    private TextView articleTitle;
    private ImageView authorImage;
    private TextView articleAuthor;
    private TextView articleDate;
    private TextView articleContent;
    private SwipeRefreshLayout swipeRefreshLayout;
    
    private Article article;
    
    public static ArticleDetailFragment newInstance(Article article) {
        ArticleDetailFragment fragment = new ArticleDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_ARTICLE, article);
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_article_detail, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        try {
            // Initialize views
            articleImage = view.findViewById(R.id.articleImage);
            articleCategory = view.findViewById(R.id.articleCategory);
            articleSubcategory = view.findViewById(R.id.articleSubcategory);
            articleTitle = view.findViewById(R.id.articleTitle);
            authorImage = view.findViewById(R.id.authorImage);
            articleAuthor = view.findViewById(R.id.articleAuthor);
            articleDate = view.findViewById(R.id.articleDate);
            articleContent = view.findViewById(R.id.articleContent);
            
            // Set up swipe to refresh
            swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
            if (swipeRefreshLayout != null) {
                swipeRefreshLayout.setOnRefreshListener(this);
            }
            
            // Get article from arguments
            if (getArguments() != null) {
                article = getArguments().getParcelable(ARG_ARTICLE);
                if (article != null) {
                    updateUI();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle error state if needed
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        }
    }
    
    // Simple method to get current article
    public Article getCurrentArticle() {
        return article;
    }
    
    // Safe method to update UI with null checks
    private void updateUI() {
        if (getView() == null || article == null) return;
        
        try {
            // Set title
            if (article.getTitle() != null) {
                articleTitle.setText(article.getTitle());
            }
            
            // Set content
            if (article.getContent() != null) {
                articleContent.setText(article.getContent());
            }
            
            // Set author name
            if (article.getAuthorName() != null) {
                articleAuthor.setText(article.getAuthorName());
            }
            
            // Set category
            if (article.getCategory() != null) {
                articleCategory.setText(article.getCategory());
                articleCategory.setVisibility(View.VISIBLE);
            } else {
                articleCategory.setVisibility(View.GONE);
            }
            
            // Set subcategory if available
            String subcategory = article.getSubcategoryDisplayName();
            if (subcategory != null && !subcategory.isEmpty()) {
                articleSubcategory.setText(subcategory);
                articleSubcategory.setVisibility(View.VISIBLE);
            } else {
                articleSubcategory.setVisibility(View.GONE);
            }
            
            // Set date
            if (article.getPublishDate() != null) {
                articleDate.setText(article.getFormattedDate());
            }
            
            // Load article image
            if (article.getImageUrl() != null && !article.getImageUrl().isEmpty()) {
                Glide.with(requireContext())
                    .load(article.getImageUrl())
                    .placeholder(R.drawable.ic_placeholder_image)
                    .error(R.drawable.ic_placeholder_image)
                    .into(articleImage);
            }
            
            // Load author image
            if (article.getAuthorImageUrl() != null && !article.getAuthorImageUrl().isEmpty()) {
                Glide.with(requireContext())
                    .load(article.getAuthorImageUrl())
                    .placeholder(R.drawable.ic_profile_placeholder)
                    .error(R.drawable.ic_profile_placeholder)
                    .circleCrop()
                    .into(authorImage);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Add a method to refresh the article data
    public void refreshArticle(Article updatedArticle) {
        if (updatedArticle != null && getView() != null) {
            this.article = updatedArticle;
            updateUI();
        }
    }
    
    @Override
    public void onRefresh() {
        // Implement pull-to-refresh if needed
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
