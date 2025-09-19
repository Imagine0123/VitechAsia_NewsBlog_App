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

import java.util.List;

public class ArticleVerticalAdapter extends RecyclerView.Adapter<ArticleVerticalAdapter.ArticleViewHolder> {

    private final List<Article> articleList;
    private final OnArticleClickListener listener;

    public interface OnArticleClickListener {
        void onArticleClick(Article article);
    }

    public ArticleVerticalAdapter(List<Article> articleList, OnArticleClickListener listener) {
        this.articleList = articleList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_article_vertical, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        Article article = articleList.get(position);
        
        // Set article data to views
        holder.articleTitle.setText(article.getTitle());
        
        // Format category and subcategory for display
        String category = article.getCategoryId() != null ? 
                         article.getCategoryId().substring(0, 1).toUpperCase() + 
                         article.getCategoryId().substring(1) : "";
        
        String subcategory = article.getSubcategoryId() != null ? 
                            article.getSubcategoryId().substring(0, 1).toUpperCase() + 
                            article.getSubcategoryId().substring(1) : "";
        
        holder.articleCategory.setText(category);
        holder.articleSubcategory.setText(subcategory);
        
        // Set author and formatted date
        holder.articleAuthor.setText(article.getAuthorName());
        holder.articleDate.setText(article.getFormattedDate());
        
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
        return articleList.size();
    }

    public void setArticles(List<Article> articles) {
        this.articleList.clear();
        if (articles != null) {
            this.articleList.addAll(articles);
        }
        notifyDataSetChanged();
    }

    public void appendArticles(List<Article> articles) {
        if (articles != null && !articles.isEmpty()) {
            int startPosition = articleList.size();
            this.articleList.addAll(articles);
            notifyItemRangeInserted(startPosition, articles.size());
        }
    }

    public static class ArticleViewHolder extends RecyclerView.ViewHolder {
        final ImageView articleImage;
        final TextView articleTitle;
        final TextView articleCategory;
        final TextView articleSubcategory;
        final TextView articleAuthor;
        final TextView articleDate;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            articleImage = itemView.findViewById(R.id.article_image);
            articleTitle = itemView.findViewById(R.id.article_title);
            articleCategory = itemView.findViewById(R.id.article_category);
            articleSubcategory = itemView.findViewById(R.id.article_subcategory);
            articleAuthor = itemView.findViewById(R.id.article_author);
            articleDate = itemView.findViewById(R.id.article_date);
        }
    }
}
