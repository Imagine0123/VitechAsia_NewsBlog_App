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

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    private final List<Article> articleList;
    private final OnArticleClickListener listener;

    public interface OnArticleClickListener {
        void onArticleClick(Article article);
    }

    public ArticleAdapter(List<Article> articleList, OnArticleClickListener listener) {
        this.articleList = articleList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_article, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        Article article = articleList.get(position);
        
        // Set article data to views
        holder.articleTitle.setText(article.getTitle());
        holder.articleCategory.setText(article.getCategory());
        holder.articleAuthor.setText(article.getAuthorName());
        holder.articleDate.setText(article.getFormattedDate());
        
        // Load article image using Glide
        Glide.with(holder.itemView.getContext())
                .load(article.getImageUrl())
                .placeholder(R.drawable.ic_placeholder_image)
                .into(holder.articleImage);
        
        // Set click listener
        holder.itemView.setOnClickListener(v -> listener.onArticleClick(article));
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    static class ArticleViewHolder extends RecyclerView.ViewHolder {
        ImageView articleImage;
        TextView articleTitle;
        TextView articleCategory;
        TextView articleAuthor;
        TextView articleDate;

        ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            articleImage = itemView.findViewById(R.id.article_image);
            articleTitle = itemView.findViewById(R.id.article_title);
            articleCategory = itemView.findViewById(R.id.article_category);
            articleAuthor = itemView.findViewById(R.id.article_author);
            articleDate = itemView.findViewById(R.id.article_date);
        }
    }
}
