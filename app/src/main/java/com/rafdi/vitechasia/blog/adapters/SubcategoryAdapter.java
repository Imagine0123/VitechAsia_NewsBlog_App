package com.rafdi.vitechasia.blog.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.rafdi.vitechasia.blog.R;
import com.rafdi.vitechasia.blog.models.Article;
import com.rafdi.vitechasia.blog.utils.DummyDataGenerator;

import java.util.ArrayList;
import java.util.List;

public class SubcategoryAdapter extends RecyclerView.Adapter<SubcategoryAdapter.SubcategoryViewHolder> {

    private final List<String> subcategoryNames;
    private final String categoryId;
    private final OnViewAllClickListener viewAllListener;
    private final OnArticleClickListener articleClickListener;

    public interface OnViewAllClickListener {
        void onViewAllClick(String categoryId, String subcategoryName);
    }

    public interface OnArticleClickListener {
        void onArticleClick(Article article);
    }

    public SubcategoryAdapter(String categoryId, OnViewAllClickListener viewAllListener, 
                            OnArticleClickListener articleClickListener) {
        this.categoryId = categoryId;
        this.subcategoryNames = new ArrayList<>();
        this.viewAllListener = viewAllListener;
        this.articleClickListener = articleClickListener;
    }

    public void setSubcategories(List<String> subcategories) {
        this.subcategoryNames.clear();
        if (subcategories != null) {
            this.subcategoryNames.addAll(subcategories);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SubcategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_subcategory_card, parent, false);
        return new SubcategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubcategoryViewHolder holder, int position) {
        String subcategoryName = subcategoryNames.get(position);
        holder.bind(subcategoryName, categoryId, viewAllListener, articleClickListener);
    }

    @Override
    public int getItemCount() {
        return subcategoryNames.size();
    }

    static class SubcategoryViewHolder extends RecyclerView.ViewHolder {
        private final TextView subcategoryTitle;
        private final MaterialButton viewAllButton;
        private final RecyclerView articlesRecyclerView;
        private final ArticleHorizontalAdapter articlesAdapter;
        private OnArticleClickListener articleClickListener;

        public SubcategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            subcategoryTitle = itemView.findViewById(R.id.subcategoryTitle);
            viewAllButton = itemView.findViewById(R.id.viewAllButton);
            articlesRecyclerView = itemView.findViewById(R.id.articlesRecyclerView);

            // Set up horizontal layout manager for articles
            articlesRecyclerView.setLayoutManager(
                    new LinearLayoutManager(itemView.getContext(),
                            LinearLayoutManager.HORIZONTAL, false));

            // Initialize articles adapter
            articlesAdapter = new ArticleHorizontalAdapter(article -> {
                if (this.articleClickListener != null) {
                    this.articleClickListener.onArticleClick(article);
                }
            });
            articlesRecyclerView.setAdapter(articlesAdapter);
        }

        public void bind(String subcategoryName, String categoryId,
                        OnViewAllClickListener viewAllListener,
                        OnArticleClickListener articleClickListener) {
            // Store the click listener
            this.articleClickListener = articleClickListener;
            
            // Set subcategory title with proper capitalization
            String formattedSubcategory = subcategoryName != null && !subcategoryName.isEmpty() ?
                    subcategoryName.substring(0, 1).toUpperCase() + 
                    (subcategoryName.length() > 1 ? subcategoryName.substring(1).toLowerCase() : "") : "";
            subcategoryTitle.setText(formattedSubcategory);

            // Set up view all button
            viewAllButton.setOnClickListener(v -> {
                if (viewAllListener != null) {
                    viewAllListener.onViewAllClick(categoryId, subcategoryName);
                }
            });

            // Set up articles (first 5)
            List<Article> articles = DummyDataGenerator.getDummyArticlesBySubcategory(subcategoryName);
            if (articles != null && !articles.isEmpty()) {
                int count = Math.min(5, articles.size());
                articlesAdapter.setArticles(articles.subList(0, count));
            } else {
                articlesAdapter.setArticles(new ArrayList<>());
            }

            // Set button color based on category
            int colorResId = getCategoryColor(categoryId);
            viewAllButton.setTextColor(ContextCompat.getColor(itemView.getContext(), colorResId));
            viewAllButton.setIconTint(
                    ContextCompat.getColorStateList(itemView.getContext(), colorResId));
        }

        private int getCategoryColor(String categoryId) {
            if (categoryId == null) return R.color.tech_category_color;

            // Convert to lowercase for case-insensitive comparison
            String lowerCategoryId = categoryId.toLowerCase();
            switch (lowerCategoryId) {
                case "sports":
                    return R.color.sports_category_color;
                case "tech":
                    return R.color.tech_category_color;
                case "news":
                    return R.color.news_category_color;
                default:
                    return R.color.tech_category_color;
            }
        }
    }
}
