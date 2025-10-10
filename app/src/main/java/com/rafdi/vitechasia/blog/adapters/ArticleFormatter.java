package com.rafdi.vitechasia.blog.adapters;

/**
 * Utility class for formatting article-related text and data.
 * Provides consistent formatting across all article adapters.
 */
public class ArticleFormatter {

    /**
     * Format category name with proper capitalization.
     * @param categoryId The category ID to format
     * @return Formatted category name or empty string if null
     */
    public static String formatCategory(String categoryId) {
        if (categoryId == null) return "";

        // Handle special cases and ensure proper formatting
        String formatted = categoryId.trim().toLowerCase();

        // Capitalize first letter
        if (!formatted.isEmpty()) {
            formatted = formatted.substring(0, 1).toUpperCase() + formatted.substring(1);
        }

        return formatted;
    }

    /**
     * Format subcategory name with proper capitalization.
     * @param subcategoryId The subcategory ID to format
     * @return Formatted subcategory name or empty string if null
     */
    public static String formatSubcategory(String subcategoryId) {
        if (subcategoryId == null) return "";

        // Handle special cases and ensure proper formatting
        String formatted = subcategoryId.trim().toLowerCase();

        // Capitalize first letter
        if (!formatted.isEmpty()) {
            formatted = formatted.substring(0, 1).toUpperCase() + formatted.substring(1);
        }

        return formatted;
    }

    /**
     * Format author name with fallback to "Unknown Author".
     * @param authorName The author name to format
     * @return Formatted author name or "Unknown Author" if null/empty
     */
    public static String formatAuthorName(String authorName) {
        if (authorName == null || authorName.trim().isEmpty()) {
            return "Unknown Author";
        }
        return authorName.trim();
    }

    /**
     * Format date with fallback to current date if null.
     * @param date The date to format
     * @return Formatted date string or empty string if date is null
     */
    public static String formatDate(String date) {
        if (date == null || date.trim().isEmpty()) {
            return "";
        }
        return date.trim();
    }

    /**
     * Format reading time in a human-readable format.
     * @param minutes Reading time in minutes
     * @return Formatted reading time string
     */
    public static String formatReadingTime(int minutes) {
        if (minutes <= 0) {
            return "Quick read";
        }

        if (minutes < 60) {
            return minutes + " min read";
        } else {
            int hours = minutes / 60;
            int remainingMinutes = minutes % 60;

            if (remainingMinutes == 0) {
                return hours + "h read";
            } else {
                return hours + "h " + remainingMinutes + "m read";
            }
        }
    }

    /**
     * Format view count with appropriate suffix (K for thousands, M for millions).
     * @param viewCount Number of views
     * @return Formatted view count string
     */
    public static String formatViewCount(int viewCount) {
        if (viewCount < 1000) {
            return String.valueOf(viewCount);
        } else if (viewCount < 1000000) {
            double thousands = viewCount / 1000.0;
            return String.format("%.1fK", thousands);
        } else {
            double millions = viewCount / 1000000.0;
            return String.format("%.1fM", millions);
        }
    }

    /**
     * Format content preview by truncating to specified length.
     * @param content Original content
     * @param maxLength Maximum length of preview
     * @return Truncated content with ellipsis if needed
     */
    public static String formatContentPreview(String content, int maxLength) {
        if (content == null) return "";

        if (content.length() <= maxLength) {
            return content;
        }

        // Find the last complete word within the limit
        int endIndex = maxLength;
        while (endIndex > 0 && !Character.isWhitespace(content.charAt(endIndex - 1))) {
            endIndex--;
        }

        // If no space found, just truncate at maxLength
        if (endIndex == 0) {
            endIndex = maxLength;
        }

        return content.substring(0, endIndex).trim() + "...";
    }

    /**
     * Format article title with ellipsis if too long.
     * @param title Original title
     * @param maxLength Maximum length
     * @return Truncated title if needed
     */
    public static String formatTitle(String title, int maxLength) {
        if (title == null) return "";

        if (title.length() <= maxLength) {
            return title;
        }

        return title.substring(0, maxLength - 3).trim() + "...";
    }
}
