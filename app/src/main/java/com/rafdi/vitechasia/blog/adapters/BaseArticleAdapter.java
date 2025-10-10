package com.rafdi.vitechasia.blog.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rafdi.vitechasia.blog.R;
import com.rafdi.vitechasia.blog.models.Article;

import java.util.ArrayList;
import java.util.List;

/**
 * Base adapter class for article adapters that provides common functionality.
 * Reduces code duplication between ArticleVerticalAdapter and ArticleHorizontalAdapter.
 */
public abstract class BaseArticleAdapter<VH extends RecyclerView.ViewHolder>
    extends RecyclerView.Adapter<VH> {

    protected List<Article> articles = new ArrayList<>();
    protected OnArticleClickListener listener;

    public interface OnArticleClickListener {
        void onArticleClick(Article article);
    }

    public BaseArticleAdapter(OnArticleClickListener listener) {
        this.listener = listener;
    }

    /**
     * Common method to bind article data to view holder.
     * Subclasses should call this and then add their specific binding logic.
     */
    protected void bindArticleData(ArticleViewHolder holder, Article article) {
        // Set basic article information
        holder.articleTitle.setText(article.getTitle());
        holder.articleAuthor.setText(article.getAuthorName());

        if (article.getPublishDate() != null) {
            holder.articleDate.setText(article.getFormattedDate());
        }

        // Load article image with enhanced error handling and caching
        loadArticleImage(holder.articleImage, article.getImageUrl());

        // Set category and subcategory with proper formatting
        setCategoryAndSubcategory(holder, article);
    }

    /**
     * Enhanced image loading with caching and error handling.
     */
    protected void loadArticleImage(ImageView imageView, String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(imageView.getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_placeholder_image)
                    .error(R.drawable.error_image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
        } else {
            imageView.setImageResource(R.drawable.ic_placeholder_image);
        }
    }

    /**
     * Format and set category and subcategory text.
     */
    protected void setCategoryAndSubcategory(ArticleViewHolder holder, Article article) {
        String category = ArticleFormatter.formatCategory(article.getCategoryId());
        String subcategory = ArticleFormatter.formatSubcategory(article.getSubcategoryId());

        holder.articleCategory.setText(category);
        holder.articleSubcategory.setText(subcategory);
    }

    /**
     * Set articles and notify adapter of data change.
     */
    public void setArticles(List<Article> articles) {
        this.articles.clear();
        if (articles != null) {
            this.articles.addAll(articles);
        }
        notifyDataSetChanged();
    }

    /**
     * Add articles to the existing list and notify adapter.
     */
    public void addArticles(List<Article> articles) {
        if (articles != null && !articles.isEmpty()) {
            int startPosition = this.articles.size();
            this.articles.addAll(articles);
            notifyItemRangeInserted(startPosition, articles.size());
        }
    }

    /**
     * Remove an article from the list.
     */
    public void removeArticle(Article article) {
        int position = articles.indexOf(article);
        if (position != -1) {
            articles.remove(position);
            notifyItemRemoved(position);
        }
    }

    /**
     * Update a specific article in the list.
     */
    public void updateArticle(Article updatedArticle) {
        for (int i = 0; i < articles.size(); i++) {
            Article article = articles.get(i);
            if (article.getId().equals(updatedArticle.getId())) {
                articles.set(i, updatedArticle);
                notifyItemChanged(i);
                break;
            }
        }
    }

    /**
     * Clear all articles from the adapter.
     */
    public void clearArticles() {
        int size = articles.size();
        articles.clear();
        notifyItemRangeRemoved(0, size);
    }

    /**
     * Get the current list of articles.
     */
    public List<Article> getArticles() {
        return new ArrayList<>(articles);
    }

    /**
     * Get article at specific position.
     */
    public Article getArticle(int position) {
        if (position >= 0 && position < articles.size()) {
            return articles.get(position);
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    /**
     * Common ViewHolder class that contains views shared by article adapters.
     */
    public static class ArticleViewHolder extends RecyclerView.ViewHolder {
        public final ImageView articleImage;
        public final TextView articleTitle;
        public final TextView articleAuthor;
        public final TextView articleDate;
        public final TextView articleCategory;
        public final TextView articleSubcategory;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            // All views now use camelCase IDs
            articleImage = itemView.findViewById(R.id.articleImage);
            articleTitle = itemView.findViewById(R.id.articleTitle);
            articleAuthor = itemView.findViewById(R.id.articleAuthor);
            articleDate = itemView.findViewById(R.id.articleDate);
            articleCategory = itemView.findViewById(R.id.articleCategory);
            articleSubcategory = itemView.findViewById(R.id.articleSubcategory);
        }
    }
}