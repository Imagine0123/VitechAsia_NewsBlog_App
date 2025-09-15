package com.rafdi.vitechasia.blog.utils;

import java.util.List;

public class PaginationUtils<T> {
    private final List<T> fullList;
    private final int itemsPerPage;
    private int currentPage = 1;
    private int totalPages = 1;

    public PaginationUtils(List<T> fullList, int itemsPerPage) {
        this.fullList = fullList;
        this.itemsPerPage = itemsPerPage;
        calculateTotalPages();
    }

    private void calculateTotalPages() {
        if (fullList == null || fullList.isEmpty()) {
            totalPages = 1;
        } else {
            totalPages = (int) Math.ceil((double) fullList.size() / itemsPerPage);
        }
    }

    public List<T> getCurrentPageItems() {
        if (fullList == null || fullList.isEmpty()) {
            return fullList;
        }

        int fromIndex = (currentPage - 1) * itemsPerPage;
        int toIndex = Math.min(fromIndex + itemsPerPage, fullList.size());

        if (fromIndex >= fullList.size()) {
            currentPage = totalPages;
            return getCurrentPageItems();
        }

        return fullList.subList(fromIndex, toIndex);
    }

    public boolean hasNextPage() {
        return currentPage < totalPages;
    }

    public void nextPage() {
        if (hasNextPage()) {
            currentPage++;
        }
    }

    public void reset() {
        currentPage = 1;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }
}
