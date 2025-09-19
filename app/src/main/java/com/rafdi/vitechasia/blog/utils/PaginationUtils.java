package com.rafdi.vitechasia.blog.utils;

import java.util.ArrayList;
import java.util.List;

public class PaginationUtils<T> {
    private List<T> fullList;
    private final int itemsPerPage;
    private int currentPage = 1;
    private int totalPages = 1;

    public PaginationUtils(List<T> fullList, int itemsPerPage) {
        this.fullList = fullList != null ? new ArrayList<>(fullList) : new ArrayList<>();
        this.itemsPerPage = Math.max(1, itemsPerPage); // Ensure at least 1 item per page
        calculateTotalPages();
    }

    private void calculateTotalPages() {
        if (fullList == null || fullList.isEmpty()) {
            totalPages = 1;
        } else {
            totalPages = (int) Math.ceil((double) fullList.size() / itemsPerPage);
            totalPages = Math.max(1, totalPages); // At least 1 page
        }
    }

    public List<T> getCurrentPageItems() {
        if (fullList == null || fullList.isEmpty()) {
            return new ArrayList<>();
        }

        int fromIndex = (currentPage - 1) * itemsPerPage;
        
        // Ensure fromIndex is within bounds
        if (fromIndex >= fullList.size()) {
            currentPage = totalPages;
            fromIndex = (currentPage - 1) * itemsPerPage;
        }
        
        int toIndex = Math.min(fromIndex + itemsPerPage, fullList.size());
        
        // Ensure fromIndex is not greater than toIndex
        if (fromIndex >= toIndex) {
            return new ArrayList<>();
        }

        return new ArrayList<>(fullList.subList(fromIndex, toIndex));
    }

    public boolean hasNextPage() {
        return currentPage < totalPages;
    }

    public boolean hasPreviousPage() {
        return currentPage > 1;
    }

    public List<T> nextPage() {
        if (hasNextPage()) {
            currentPage++;
            return getCurrentPageItems();
        }
        return new ArrayList<>();
    }
    
    public List<T> previousPage() {
        if (hasPreviousPage()) {
            currentPage--;
            return getCurrentPageItems();
        }
        return new ArrayList<>();
    }

    public void reset() {
        currentPage = 1;
    }

    public int getCurrentPageNumber() {
        return currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }
    
    public void updateData(List<T> newData) {
        this.fullList = newData != null ? new ArrayList<>(newData) : new ArrayList<>();
        calculateTotalPages();
        // Reset to first page when data changes
        reset();
    }
    
    public List<T> loadNextPage() {
        if (hasNextPage()) {
            currentPage++;
            return getCurrentPageItems();
        }
        return new ArrayList<>();
    }
    
    public List<T> getCurrentPage() {
        return getCurrentPageItems();
    }
}
