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

import com.bumptech.glide.Glide;
import com.rafdi.vitechasia.blog.R;
import com.rafdi.vitechasia.blog.models.Article;

public class ArticleDetailFragment extends Fragment {
    
    private static final String ARG_ARTICLE = "article";
    
    private ImageView articleImage;
    private TextView articleCategory;
    private TextView articleSubcategory;
    private TextView articleTitle;
    private ImageView authorImage;
    private TextView articleAuthor;
    private TextView articleDate;
    private TextView articleContent;
    
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
        if (getArguments() != null) {
            article = getArguments().getParcelable(ARG_ARTICLE);
        }
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
        
        //Initialize views
        articleImage = view.findViewById(R.id.articleImage);
        articleCategory = view.findViewById(R.id.articleCategory);
        articleSubcategory = view.findViewById(R.id.articleSubcategory);
        articleTitle = view.findViewById(R.id.articleTitle);
        authorImage = view.findViewById(R.id.authorImage);
        articleAuthor = view.findViewById(R.id.articleAuthor);
        articleDate = view.findViewById(R.id.articleDate);
        articleContent = view.findViewById(R.id.articleContent);
        
        //Populate views with article data
        if (article != null) {
            //Load article image using Glide
            Glide.with(requireContext())
                .load(article.getImageUrl())
                .placeholder(R.drawable.ic_placeholder_image)
                .into(articleImage);
            
            // Set category and subcategory
            articleCategory.setText(article.getCategory());
            String subcategory = article.getSubcategoryDisplayName();
            if (subcategory != null && !subcategory.isEmpty()) {
                articleSubcategory.setText(subcategory);
                articleSubcategory.setVisibility(View.VISIBLE);
            } else {
                articleSubcategory.setVisibility(View.GONE);
            }
            
            articleTitle.setText(article.getTitle());
            
            //Load author image
            Glide.with(requireContext())
                .load(article.getAuthorImageUrl())
                .placeholder(R.drawable.ic_profile_placeholder)
                .circleCrop()
                .into(authorImage);
            
            articleAuthor.setText(article.getAuthorName());
            articleDate.setText(article.getFormattedDate());
            articleContent.setText(article.getContent());
        }
    }
}
