package com.rafdi.vitechasia.blog.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.rafdi.vitechasia.blog.models.Article;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BookmarkManager {
    private static final String PREF_NAME = "BookmarkPreferences";
    private static final String KEY_BOOKMARKS = "bookmarked_article_ids";
    private static BookmarkManager instance;
    private SharedPreferences preferences;

    private BookmarkManager(Context context) {
        preferences = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized BookmarkManager getInstance(Context context) {
        if (instance == null) {
            instance = new BookmarkManager(context);
        }
        return instance;
    }

    public boolean isBookmarked(String articleId) {
        Set<String> bookmarks = preferences.getStringSet(KEY_BOOKMARKS, new HashSet<>());
        return bookmarks.contains(articleId);
    }

    public void toggleBookmark(Article article) {
        if (article == null || article.getId() == null) return;
        
        Set<String> bookmarks = new HashSet<>(preferences.getStringSet(KEY_BOOKMARKS, new HashSet<>()));
        
        if (bookmarks.contains(article.getId())) {
            bookmarks.remove(article.getId());
            article.setBookmarked(false);
        } else {
            bookmarks.add(article.getId());
            article.setBookmarked(true);
        }
        
        preferences.edit().putStringSet(KEY_BOOKMARKS, bookmarks).apply();
    }

    public void syncArticleBookmarkStatus(List<Article> articles) {
        if (articles == null) return;
        
        Set<String> bookmarks = preferences.getStringSet(KEY_BOOKMARKS, new HashSet<>());
        for (Article article : articles) {
            if (article.getId() != null) {
                article.setBookmarked(bookmarks.contains(article.getId()));
            }
        }
    }

    public void syncArticleBookmarkStatus(Article article) {
        if (article == null || article.getId() == null) return;
        article.setBookmarked(isBookmarked(article.getId()));
    }
}
