package com.rafdi.vitechasia.blog.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.rafdi.vitechasia.blog.models.Article;

/**
 * Utility class for managing social interactions (likes, shares, bookmarks)
 */
public class SocialInteractionManager {
    private static final String PREFS_NAME = "social_interactions";
    private static final String KEY_LIKED_PREFIX = "liked_";
    private static final String KEY_SHARED_PREFIX = "shared_";
    private static final String KEY_LIKES_COUNT_PREFIX = "likes_count_";
    private static final String KEY_SHARES_COUNT_PREFIX = "shares_count_";
    private static final String KEY_COMMENTS_COUNT_PREFIX = "comments_count_";

    private static SocialInteractionManager instance;
    private SharedPreferences preferences;

    private SocialInteractionManager(Context context) {
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static void initialize(Context context) {
        if (instance == null) {
            instance = new SocialInteractionManager(context.getApplicationContext());
        }
    }

    public static SocialInteractionManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("SocialInteractionManager must be initialized first");
        }
        return instance;
    }

    /**
     * Toggle like status for an article
     */
    public boolean toggleLike(Article article) {
        if (article == null) return false;

        String articleId = article.getId();
        boolean wasLiked = isLiked(articleId);
        boolean isLikedNow = !wasLiked;

        // Update preferences
        preferences.edit()
            .putBoolean(KEY_LIKED_PREFIX + articleId, isLikedNow)
            .apply();

        // Update article like count
        int currentLikes = getLikeCount(articleId);
        if (isLikedNow) {
            currentLikes++;
        } else {
            currentLikes = Math.max(0, currentLikes - 1);
        }

        setLikeCount(articleId, currentLikes);

        // Update article object
        article.setLikedByUser(isLikedNow);
        article.setLikeCount(currentLikes);

        return isLikedNow;
    }

    /**
     * Record a share action
     */
    public void recordShare(Article article) {
        if (article == null) return;

        String articleId = article.getId();
        boolean wasShared = isShared(articleId);

        if (!wasShared) {
            // Mark as shared
            preferences.edit()
                .putBoolean(KEY_SHARED_PREFIX + articleId, true)
                .apply();

            // Increment share count
            int currentShares = getShareCount(articleId) + 1;
            setShareCount(articleId, currentShares);

            // Update article object
            article.setSharedByUser(true);
            article.setShareCount(currentShares);
        }
    }

    /**
     * Check if article is liked by user
     */
    public boolean isLiked(String articleId) {
        return preferences.getBoolean(KEY_LIKED_PREFIX + articleId, false);
    }

    /**
     * Check if article is shared by user
     */
    public boolean isShared(String articleId) {
        return preferences.getBoolean(KEY_SHARED_PREFIX + articleId, false);
    }

    /**
     * Get like count for article
     */
    public int getLikeCount(String articleId) {
        return preferences.getInt(KEY_LIKES_COUNT_PREFIX + articleId, 0);
    }

    /**
     * Set like count for article
     */
    public void setLikeCount(String articleId, int count) {
        preferences.edit()
            .putInt(KEY_LIKES_COUNT_PREFIX + articleId, count)
            .apply();
    }

    /**
     * Get share count for article
     */
    public int getShareCount(String articleId) {
        return preferences.getInt(KEY_SHARES_COUNT_PREFIX + articleId, 0);
    }

    /**
     * Set share count for article
     */
    public void setShareCount(String articleId, int count) {
        preferences.edit()
            .putInt(KEY_SHARES_COUNT_PREFIX + articleId, count)
            .apply();
    }

    /**
     * Get comment count for article
     */
    public int getCommentCount(String articleId) {
        return preferences.getInt(KEY_COMMENTS_COUNT_PREFIX + articleId, 0);
    }

    /**
     * Set comment count for article
     */
    public void setCommentCount(String articleId, int count) {
        preferences.edit()
            .putInt(KEY_COMMENTS_COUNT_PREFIX + articleId, count)
            .apply();
    }

    /**
     * Initialize article social state from preferences
     */
    public void initializeArticleSocialState(Article article) {
        if (article == null || article.getId() == null) return;

        String articleId = article.getId();

        // Set user interaction states
        article.setLikedByUser(isLiked(articleId));
        article.setSharedByUser(isShared(articleId));

        // Set counts from preferences (or use defaults if not set)
        int likeCount = getLikeCount(articleId);
        int shareCount = getShareCount(articleId);
        int commentCount = getCommentCount(articleId);

        if (likeCount > 0) article.setLikeCount(likeCount);
        if (shareCount > 0) article.setShareCount(shareCount);
        if (commentCount > 0) article.setCommentCount(commentCount);
    }

    /**
     * Reset all social interactions (for testing or user reset)
     */
    public void resetAllInteractions() {
        preferences.edit().clear().apply();
    }
}
