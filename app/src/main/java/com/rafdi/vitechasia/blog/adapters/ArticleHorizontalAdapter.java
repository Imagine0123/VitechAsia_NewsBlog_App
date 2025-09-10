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

import java.util.ArrayList;
import java.util.List;

public class ArticleHorizontalAdapter extends RecyclerView.Adapter<ArticleHorizontalAdapter.ArticleViewHolder> {
    private List<Article> articles = new ArrayList<>();
    private OnArticleClickListener listener;

    public interface OnArticleClickListener {
        void onArticleClick(Article article);
    }

    public ArticleHorizontalAdapter(OnArticleClickListener listener) {
        this.listener = listener;
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
        holder.bind(article, listener);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles != null ? articles : new ArrayList<>();
        notifyDataSetChanged();
    }

    static class ArticleViewHolder extends RecyclerView.ViewHolder {
        private final ImageView articleImage;
        private final TextView articleTitle;
        private final TextView articleCategory;
        private final TextView articleSubcategory;
        private final TextView articleAuthor;
        private final TextView articleDate;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            articleImage = itemView.findViewById(R.id.articleImage);
            articleTitle = itemView.findViewById(R.id.articleTitle);
            articleCategory = itemView.findViewById(R.id.articleCategory);
            articleSubcategory = itemView.findViewById(R.id.articleSubcategory);
            articleAuthor = itemView.findViewById(R.id.articleAuthor);
            articleDate = itemView.findViewById(R.id.articleDate);
        }

        public void bind(Article article, OnArticleClickListener listener) {
            // Load article image
            Glide.with(itemView.getContext())
                    .load(article.getImageUrl())
                    .placeholder(R.drawable.ic_placeholder_image)
                    .into(articleImage);

            // Set article details
            articleTitle.setText(article.getTitle());
            articleCategory.setText(article.getCategory());

            // Set subcategory if available
            String subcategory = article.getSubcategoryDisplayName();
            if (subcategory != null && !subcategory.isEmpty()) {
                articleSubcategory.setText(subcategory);
                articleSubcategory.setVisibility(View.VISIBLE);
            } else {
                articleSubcategory.setVisibility(View.GONE);
            }

            articleAuthor.setText(article.getAuthorName());
            articleDate.setText(article.getFormattedDate());

            // Set click listener
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onArticleClick(article);
                }
            });
        }
    }
}
