package com.rafdi.vitechasia.blog.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Article implements Parcelable {
    private String id;
    private String title;
    private String content;
    private String imageUrl;
    private String category;
    private String authorId;
    private String authorName;
    private String authorImageUrl;
    private Date publishDate;
    private int viewCount;
    private int likeCount;

    // Empty constructor required for Firestore
    public Article() {
    }

    public Article(String id, String title, String content, String imageUrl, String category,
                  String authorId, String authorName, String authorImageUrl, Date publishDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.category = category;
        this.authorId = authorId;
        this.authorName = authorName;
        this.authorImageUrl = authorImageUrl;
        this.publishDate = publishDate;
    }

    protected Article(Parcel in) {
        id = in.readString();
        title = in.readString();
        content = in.readString();
        imageUrl = in.readString();
        category = in.readString();
        authorId = in.readString();
        authorName = in.readString();
        authorImageUrl = in.readString();
        viewCount = in.readInt();
        likeCount = in.readInt();
        publishDate = new Date(in.readLong());
    }

    public static final Creator<Article> CREATOR = new Creator<Article>() {
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
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
        dest.writeString(category);
        dest.writeString(authorId);
        dest.writeString(authorName);
        dest.writeString(authorImageUrl);
        dest.writeInt(viewCount);
        dest.writeInt(likeCount);
        dest.writeLong(publishDate != null ? publishDate.getTime() : 0);
    }
}
