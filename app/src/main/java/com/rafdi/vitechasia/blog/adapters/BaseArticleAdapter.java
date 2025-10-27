package com.rafdi.vitechasia.blog.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import android.util.Log;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.rafdi.vitechasia.blog.R;
import androidx.annotation.Nullable;
import com.rafdi.vitechasia.blog.models.Article;

import java.util.ArrayList;
import java.util.List;

/**
 * Base adapter class for article adapters that provides common functionality.
 * Reduces code duplication between ArticleVerticalAdapter and ArticleHorizontalAdapter.
 */
public abstract class BaseArticleAdapter<VH extends RecyclerView.ViewHolder>
    extends RecyclerView.Adapter<VH> {
    
    private static final String TAG = "BaseArticleAdapter";
    private static final int DEFAULT_IMAGE_PLACEHOLDER = R.drawable.ic_placeholder_image;
    private static final int DEFAULT_IMAGE_ERROR = R.drawable.error_image;

    protected List<Article> articles = new ArrayList<>();
    protected OnArticleClickListener listener;

    public interface OnArticleClickListener {
        void onArticleClick(Article article);
    }

    public BaseArticleAdapter(OnArticleClickListener listener) {
        this.listener = listener;
    }

    /**
     * Updates the list of articles and notifies the adapter of the change
     * @param articles New list of articles to display
     */
    public void setArticles(List<Article> articles) {
        if (articles == null) {
            this.articles = new ArrayList<>();
        } else {
            this.articles = new ArrayList<>(articles);
        }
        notifyDataSetChanged();
    }

    /**
     * Common method to bind article data to view holder.
     * Subclasses should call this and then add their specific binding logic.
     */
    protected void bindArticleData(ArticleViewHolder holder, Article article) {
        if (article == null || holder == null) {
            Log.w(TAG, "Article or holder is null in bindArticleData");
            return;
        }
        
        // Bind text views
        bindTextViews(holder, article);
        
        // Load and bind image
        bindImageView(holder, article);
        
        // Set category and subcategory with proper formatting
        bindCategories(holder, article);
        
        // Bind social stats
        bindSocialStats(holder, article);
    }
    
    /**
     * Bind text-related views for the article
     */
    private void bindTextViews(ArticleViewHolder holder, Article article) {
        // Safe text setting with null checks
        holder.articleTitle.setText(article.getTitle() != null ? article.getTitle() : "");
        holder.articleAuthor.setText(article.getAuthorName() != null ? article.getAuthorName() : "");
        
        if (article.getPublishDate() != null) {
            holder.articleDate.setText(article.getFormattedDate());
        } else {
            holder.articleDate.setText("");
        }
    }
    
    /**
     * Bind image view with the article's image
     */
    private void bindImageView(ArticleViewHolder holder, Article article) {
        if (holder.articleImage != null) {
            loadArticleImage(holder.articleImage, article.getImageUrl());
        }
    }

    /**
     * Enhanced image loading with caching and error handling.
     */
    protected void loadArticleImage(ImageView imageView, String imageUrl) {
        if (imageView == null) {
            Log.w(TAG, "ImageView is null, cannot load image");
            return;
        }
        
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(imageView.getContext())
                    .load(imageUrl)
                    .placeholder(DEFAULT_IMAGE_PLACEHOLDER)
                    .error(DEFAULT_IMAGE_ERROR)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .skipMemoryCache(false)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imageView);
        } else {
            Glide.with(imageView.getContext())
                    .load(DEFAULT_IMAGE_PLACEHOLDER)
                    .into(imageView);
        }
    }

    /**
     * Bind social statistics views
     */
    private void bindSocialStats(ArticleViewHolder holder, Article article) {
        if (article == null || holder == null) {
            return;
        }
        // Bind like count
        if (holder.likeCount != null) {
            int likes = article.getLikeCount();
            holder.likeCount.setText(String.valueOf(likes));
        }

        // Bind share count
        if (holder.shareCount != null) {
            int shares = article.getShareCount();
            holder.shareCount.setText(String.valueOf(shares));
        }

        // Bind view count
        if (holder.viewCount != null) {
            int views = article.getViewCount();
            holder.viewCount.setText(String.valueOf(views));
        }
    }

    /**
     * Set category and subcategory text with null safety
     */
    private void bindCategories(ArticleViewHolder holder, Article article) {
        if (article == null || holder == null) {
            return;
        }

        String category = "";
        String subcategory = "";

        try {
            if (article.getCategoryId() != null) {
                category = ArticleFormatter.formatCategory(article.getCategoryId());
            }
            if (article.getSubcategoryId() != null) {
                subcategory = ArticleFormatter.formatSubcategory(article.getSubcategoryId());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error formatting categories", e);
        }

        if (holder.articleCategory != null) {
            holder.articleCategory.setText(category);
        }
        if (holder.articleSubcategory != null) {
            holder.articleSubcategory.setText(subcategory);
            holder.articleSubcategory.setVisibility(subcategory.isEmpty() ? View.GONE : View.VISIBLE);
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
     * ViewHolder class for article items that holds references to all the views.
     * All view fields are marked as @Nullable since they might not be present in all layouts.
     */
    public static class ArticleViewHolder extends RecyclerView.ViewHolder {
        @Nullable public final ImageView articleImage;
        @Nullable public final TextView articleTitle;
        @Nullable public final TextView articleAuthor;
        @Nullable public final TextView articleDate;
        @Nullable public final TextView articleCategory;
        @Nullable public final TextView articleSubcategory;
        // Social stats views
        @Nullable public final TextView likeCount;
        @Nullable public final TextView shareCount;
        @Nullable public final TextView viewCount;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize all views with null checks
            articleImage = safeFindView(itemView, R.id.articleImage, ImageView.class);
            articleTitle = safeFindView(itemView, R.id.articleTitle, TextView.class);
            articleAuthor = safeFindView(itemView, R.id.articleAuthor, TextView.class);
            articleDate = safeFindView(itemView, R.id.articleDate, TextView.class);
            articleCategory = safeFindView(itemView, R.id.articleCategory, TextView.class);
            articleSubcategory = safeFindView(itemView, R.id.articleSubcategory, TextView.class);

            // Initialize social stats views
            likeCount = safeFindView(itemView, R.id.likeCount, TextView.class);
            shareCount = safeFindView(itemView, R.id.shareCount, TextView.class);
            viewCount = safeFindView(itemView, R.id.viewCount, TextView.class);
        }
        
        /**
         * Safely find a view by ID and cast it to the specified type.
         * @param <T> The type of view to find
         * @param root The root view to search in
         * @param id The ID of the view to find
         * @param viewClass The class of the view to find
         * @return The view if found, null otherwise
         */
        @Nullable
        private <T extends View> T safeFindView(@NonNull View root, int id, Class<T> viewClass) {
            try {
                View view = root.findViewById(id);
                if (view != null && viewClass.isInstance(view)) {
                    return viewClass.cast(view);
                }
                return null;
            } catch (Exception e) {
                Log.w(TAG, "Error finding view with ID: " + root.getResources().getResourceName(id), e);
                return null;
            }
        }
    }
}