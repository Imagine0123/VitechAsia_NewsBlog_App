package com.rafdi.vitechasia.blog.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.rafdi.vitechasia.blog.utils.DataHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Model class representing a blog category with subcategories.
 * Implements Parcelable for Android component communication.
 */
public class Category implements Parcelable {
    private String id;
    private String name;
    private List<String> subcategories;

    public Category() {
        subcategories = new ArrayList<>();
    }

    public Category(String id, String name, List<String> subcategories) {
        this.id = id;
        this.name = name;
        this.subcategories = subcategories != null ? subcategories : new ArrayList<>();
    }

    protected Category(Parcel in) {
        id = in.readString();
        name = in.readString();
        subcategories = in.createStringArrayList();
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<String> subcategories) {
        this.subcategories = subcategories != null ? subcategories : new ArrayList<>();
    }
    
    public void addSubcategory(String subcategory) {
        if (subcategory != null && !subcategory.trim().isEmpty()) {
            if (subcategories == null) {
                subcategories = new ArrayList<>();
            }
            subcategories.add(subcategory);
        }
    }
    
    public boolean hasSubcategories() {
        return subcategories != null && !subcategories.isEmpty();
    }
    
    /**
     * Get articles for a specific subcategory asynchronously
     * @param subcategory The subcategory to get articles for
     * @param callback Callback to handle the result or error
     */
    public void getArticlesForSubcategory(String subcategory, DataHandler.DataLoadListener callback) {
        DataHandler.getInstance().getArticlesBySubcategory(subcategory, new DataHandler.DataLoadListener() {
            @Override
            public void onDataLoaded(List<Article> articles) {
                if (callback != null) {
                    callback.onDataLoaded(articles);
                }
            }

            @Override
            public void onError(String message) {
                if (callback != null) {
                    callback.onError(message);
                }
            }
        });
    }
    
    /**
     * @deprecated Use {@link #getArticlesForSubcategory(String, DataHandler.DataLoadListener)} instead
     */
    @Deprecated
    public List<Article> getArticlesForSubcategorySync(String subcategory) {
        // This is kept for backward compatibility but should be avoided in new code
        final List<Article>[] result = new List[1];
        final Object lock = new Object();
        
        getArticlesForSubcategory(subcategory, new DataHandler.DataLoadListener() {
            @Override
            public void onDataLoaded(List<Article> articles) {
                synchronized (lock) {
                    result[0] = articles;
                    lock.notify();
                }
            }

            @Override
            public void onError(String message) {
                synchronized (lock) {
                    result[0] = new ArrayList<>();
                    lock.notify();
                }
            }
        });
        
        // Wait for the async operation to complete (not recommended on main thread!)
        synchronized (lock) {
            try {
                lock.wait(5000); // 5 second timeout
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return new ArrayList<>();
            }
        }
        
        return result[0] != null ? result[0] : new ArrayList<>();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeStringList(subcategories);
    }
}
