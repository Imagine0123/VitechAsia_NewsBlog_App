package com.rafdi.vitechasia.blog.models;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Model class representing search filters and sorting options.
 */
public class SearchFilters {
    public enum SortBy {
        RELEVANCE,
        DATE_NEWEST,
        DATE_OLDEST,
        POPULARITY
    }

    public enum DateRange {
        ALL_TIME,
        LAST_24_HOURS,
        LAST_WEEK,
        LAST_MONTH,
        LAST_YEAR
    }

    private SortBy sortBy = SortBy.RELEVANCE;
    private DateRange dateRange = DateRange.ALL_TIME;
    private final List<String> selectedCategories = new ArrayList<>();

    public SortBy getSortBy() {
        return sortBy;
    }

    public void setSortBy(SortBy sortBy) {
        this.sortBy = sortBy != null ? sortBy : SortBy.RELEVANCE;
    }

    public DateRange getDateRange() {
        return dateRange;
    }

    public void setDateRange(DateRange dateRange) {
        this.dateRange = dateRange != null ? dateRange : DateRange.ALL_TIME;
    }

    public List<String> getSelectedCategories() {
        return new ArrayList<>(selectedCategories);
    }

    public void setSelectedCategories(List<String> categories) {
        selectedCategories.clear();
        if (categories != null) {
            selectedCategories.addAll(categories);
        }
    }

    public void addCategory(String category) {
        if (category != null && !selectedCategories.contains(category)) {
            selectedCategories.add(category);
        }
    }

    public void removeCategory(String category) {
        selectedCategories.remove(category);
    }

    public void clear() {
        sortBy = SortBy.RELEVANCE;
        dateRange = DateRange.ALL_TIME;
        selectedCategories.clear();
    }

    public boolean hasActiveFilters() {
        return sortBy != SortBy.RELEVANCE ||
               dateRange != DateRange.ALL_TIME ||
               !selectedCategories.isEmpty();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        SearchFilters that = (SearchFilters) obj;
        return sortBy == that.sortBy &&
               dateRange == that.dateRange &&
               selectedCategories.equals(that.selectedCategories);
    }

    @Override
    public int hashCode() {
        int result = sortBy.hashCode();
        result = 31 * result + dateRange.hashCode();
        result = 31 * result + selectedCategories.hashCode();
        return result;
    }
}
