package com.rafdi.vitechasia.blog.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.rafdi.vitechasia.blog.R;
import com.rafdi.vitechasia.blog.models.Article;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView adapter for displaying articles in horizontal layout.
 * Now extends BaseArticleAdapter to reduce code duplication and improve maintainability.
 */
public class ArticleHorizontalAdapter extends BaseArticleAdapter<ArticleHorizontalAdapter.ArticleViewHolder> {

    public ArticleHorizontalAdapter(OnArticleClickListener listener) {
        super(listener);
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

        // Use base adapter's common binding
        bindArticleData(holder, article);

        // Set click listener for the entire item
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onArticleClick(article);
            }
        });
    }

    static class ArticleViewHolder extends BaseArticleAdapter.ArticleViewHolder {
        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
