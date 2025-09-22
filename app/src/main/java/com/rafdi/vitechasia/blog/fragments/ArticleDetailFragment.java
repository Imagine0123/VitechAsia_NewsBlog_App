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
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.rafdi.vitechasia.blog.R;
import com.rafdi.vitechasia.blog.models.Article;
import com.rafdi.vitechasia.blog.utils.DummyDataGenerator;

public class ArticleDetailFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    
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
    private String categoryId;
    private String subcategoryId;
    
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
            
            // Set click listeners for category and subcategory
            articleCategory.setOnClickListener(this);
            articleSubcategory.setOnClickListener(this);
            
            // Get article from arguments
            if (getArguments() != null) {
                article = getArguments().getParcelable(ARG_ARTICLE);
                if (article != null) {
                    // Store category and subcategory IDs for navigation
                    categoryId = article.getCategoryId();
                    subcategoryId = article.getSubcategoryId();
                    updateUI();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
                articleCategory.setClickable(true);
                articleCategory.setTextColor(getResources().getColor(R.color.primary, null));
                articleCategory.setPaintFlags(articleCategory.getPaintFlags() | android.graphics.Paint.UNDERLINE_TEXT_FLAG);
            } else {
                articleCategory.setVisibility(View.GONE);
            }
            
            // Set subcategory if available
            String subcategory = article.getSubcategoryDisplayName();
            if (subcategory != null && !subcategory.isEmpty()) {
                articleSubcategory.setText(subcategory);
                articleSubcategory.setVisibility(View.VISIBLE);
                articleSubcategory.setClickable(true);
                articleSubcategory.setTextColor(getResources().getColor(R.color.primary, null));
                articleSubcategory.setPaintFlags(articleSubcategory.getPaintFlags() | android.graphics.Paint.UNDERLINE_TEXT_FLAG);
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
    public void onClick(View v) {
        if (v.getId() == R.id.articleCategory) {
            // Navigate to CategoryFragment
            if (categoryId != null && getActivity() != null) {
                Fragment categoryFragment = CategoryFragment.newInstance(categoryId);
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, categoryFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        } else if (v.getId() == R.id.articleSubcategory) {
            // Navigate to SubcategoryFragment
            if (categoryId != null && subcategoryId != null && getActivity() != null) {
                String categoryName = article != null ? article.getCategory() : "";
                Fragment subcategoryFragment = SubcategoryFragment.newInstance(categoryName, subcategoryId);
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, subcategoryFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
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
