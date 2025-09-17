package com.rafdi.vitechasia.blog.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    
    // Helper method to get dummy articles for a subcategory
    // In a real app, this would fetch from your data source
    public List<Article> getArticlesForSubcategory(String subcategory) {
        List<Article> articles = new ArrayList<>();
        
        // Add sample articles based on subcategory
        // Replace this with your actual data loading logic
        if (subcategory != null && !subcategory.isEmpty()) {
            // Example: Create 3 sample articles for the subcategory
            for (int i = 1; i <= 3; i++) {
                Article article = new Article();
                article.setId("article_" + subcategory.toLowerCase() + "_" + i);
                article.setTitle(subcategory + " Article " + i);
                article.setContent("This is a sample article about " + subcategory + ". This is article number " + i + ".");
                article.setCategory(name);
                article.setSubcategoryId(subcategory);
                article.setAuthorName("Author " + (i % 3 + 1));
                article.setPublishDate(new Date());
                
                // Add some sample image URLs (replace with your actual image loading)
                if (i % 3 == 1) {
                    article.setImageUrl("https://picsum.photos/400/300?random=" + System.currentTimeMillis());
                } else if (i % 3 == 2) {
                    article.setImageUrl("https://picsum.photos/400/300?random=" + (System.currentTimeMillis() + 1));
                } else {
                    article.setImageUrl("https://picsum.photos/400/300?random=" + (System.currentTimeMillis() + 2));
                }
                
                articles.add(article);
            }
        }
        
        return articles;
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
