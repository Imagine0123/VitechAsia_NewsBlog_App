package com.rafdi.vitechasia.blog.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.rafdi.vitechasia.blog.utils.CategoryManager;

public class Article implements Parcelable {
    private String id;
    private String title;
    private String content;
    private String imageUrl;
    private String categoryId;
    private String subcategoryId;
    private String authorId;
    private String authorName;
    private String authorImageUrl;
    private Date publishDate;
    private int viewCount;
    private int likeCount;
    private boolean isBookmarked;

    public Article() {
    }

    public Article(String id, String title, String content, String imageUrl,
                  String categoryId, String subcategoryId, String authorId, String authorName, 
                  String authorImageUrl, Date publishDate, int viewCount, int likeCount) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
        this.subcategoryId = subcategoryId;
        this.authorId = authorId;
        this.authorName = authorName;
        this.authorImageUrl = authorImageUrl;
        this.publishDate = publishDate;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
    }

    public boolean isBookmarked() {
        return isBookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        isBookmarked = bookmarked;
    }

    protected Article(Parcel in) {
        id = in.readString();
        title = in.readString();
        content = in.readString();
        imageUrl = in.readString();
        categoryId = in.readString();
        subcategoryId = in.readString();
        authorId = in.readString();
        authorName = in.readString();
        authorImageUrl = in.readString();
        viewCount = in.readInt();
        likeCount = in.readInt();
        isBookmarked = in.readByte() != 0;
        // Handle potential null date
        long dateMillis = in.readLong();
        publishDate = dateMillis == -1 ? null : new Date(dateMillis);
    }

    public static final Creator<Article> CREATOR = new Creator<>() {
        @Override
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return categoryId != null ? CategoryManager.getCategoryDisplayName(categoryId) : null;
    }
    
    public String getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
    
    public String getSubcategoryId() {
        return subcategoryId;
    }
    
    public void setSubcategoryId(String subcategoryId) {
        this.subcategoryId = subcategoryId;
    }
    
    public String getSubcategoryDisplayName() {
        if (subcategoryId == null || categoryId == null) return null;
        return subcategoryId; // In a real app, you might want to map this to a display name
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorImageUrl() {
        return authorImageUrl;
    }

    public void setAuthorImageUrl(String authorImageUrl) {
        this.authorImageUrl = authorImageUrl;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public String getFormattedDate() {
        if (publishDate == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
        return sdf.format(publishDate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(imageUrl);
        dest.writeString(categoryId);
        dest.writeString(subcategoryId);
        dest.writeString(authorId);
        dest.writeString(authorName);
        dest.writeString(authorImageUrl);
        dest.writeInt(viewCount);
        dest.writeInt(likeCount);
        dest.writeByte((byte) (isBookmarked ? 1 : 0));
        dest.writeLong(publishDate != null ? publishDate.getTime() : -1);
    }
}
