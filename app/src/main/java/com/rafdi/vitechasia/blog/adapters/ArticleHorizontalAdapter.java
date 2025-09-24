package com.rafdi.vitechasia.blog.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rafdi.vitechasia.blog.R;
import com.rafdi.vitechasia.blog.models.Article;

import java.util.ArrayList;
import java.util.List;

public class ArticleHorizontalAdapter extends RecyclerView.Adapter<ArticleHorizontalAdapter.ArticleViewHolder> {
    private List<Article> articles = new ArrayList<>();
    private OnArticleClickListener listener;

    public interface OnArticleClickListener {
        void onArticleClick(Article article);
    }

    public ArticleHorizontalAdapter(OnArticleClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_article_horizontal, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        Article article = articles.get(position);
        
        // Set article data to views
        holder.articleTitle.setText(article.getTitle());
        
        // Format category and subcategory for display (first letter uppercase, rest lowercase)
        String category = article.getCategoryId() != null ? 
                         article.getCategoryId().substring(0, 1).toUpperCase() + 
                         article.getCategoryId().substring(1).toLowerCase() : "";
        
        String subcategory = article.getSubcategoryId() != null ? 
                           article.getSubcategoryId().substring(0, 1).toUpperCase() + 
                           article.getSubcategoryId().substring(1).toLowerCase() : "";
        
        holder.articleCategory.setText(category);
        holder.articleSubcategory.setText(subcategory);
        holder.articleAuthor.setText(article.getAuthorName());
        
        // Format and set date
        String formattedDate = article.getFormattedDate();
        holder.articleDate.setText(formattedDate);
        
        // Load image using Glide
        if (article.getImageUrl() != null && !article.getImageUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                 .load(article.getImageUrl())
                 .placeholder(R.drawable.placeholder_image)
                 .error(R.drawable.error_image)
                 .into(holder.articleImage);
        } else {
            holder.articleImage.setImageResource(R.drawable.placeholder_image);
        }
        
        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onArticleClick(article);
            }
        });
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles != null ? articles : new ArrayList<>();
        notifyDataSetChanged();
    }

    static class ArticleViewHolder extends RecyclerView.ViewHolder {
        private final ImageView articleImage;
        private final TextView articleTitle;
        private final TextView articleCategory;
        private final TextView articleSubcategory;
        private final TextView articleAuthor;
        private final TextView articleDate;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            articleImage = itemView.findViewById(R.id.articleImage);
            articleTitle = itemView.findViewById(R.id.articleTitle);
            articleCategory = itemView.findViewById(R.id.articleCategory);
            articleSubcategory = itemView.findViewById(R.id.articleSubcategory);
            articleAuthor = itemView.findViewById(R.id.articleAuthor);
            articleDate = itemView.findViewById(R.id.articleDate);
        }
    }
}
