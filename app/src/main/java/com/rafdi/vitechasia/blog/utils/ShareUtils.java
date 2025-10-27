package com.rafdi.vitechasia.blog.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.widget.Toast;

import com.rafdi.vitechasia.blog.models.Article;

import java.util.List;

/**
 * Utility class for handling article sharing functionality
 */
public class ShareUtils {

    /**
     * Share article via Android's built-in sharing system
     */
    public static void shareArticle(Context context, Article article) {
        if (article == null || context == null) {
            return;
        }

        String shareText = buildShareText(context, article);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, article.getTitle());

        // Add article URL if available
        if (article.getImageUrl() != null && !article.getImageUrl().isEmpty()) {
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(article.getImageUrl()));
            shareIntent.setType("image/*");
        }

        // Show sharing options
        Intent chooser = Intent.createChooser(shareIntent, "Share Article");
        context.startActivity(chooser);
    }

    /**
     * Share article specifically to WhatsApp
     */
    public static void shareToWhatsApp(Context context, Article article) {
        if (article == null || context == null) {
            return;
        }

        String shareText = buildShareText(context, article);
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.whatsapp");

        // Check if WhatsApp is installed
        if (isAppInstalled(context, "com.whatsapp")) {
            whatsappIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            context.startActivity(whatsappIntent);
        } else {
            Toast.makeText(context, "WhatsApp is not installed", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Share article specifically to Facebook
     */
    public static void shareToFacebook(Context context, Article article) {
        if (article == null || context == null) {
            return;
        }

        String shareText = buildShareText(context, article);

        try {
            // Try to open Facebook app
            Intent facebookIntent = new Intent(Intent.ACTION_SEND);
            facebookIntent.setType("text/plain");
            facebookIntent.setPackage("com.facebook.katana");

            if (isAppInstalled(context, "com.facebook.katana")) {
                facebookIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                context.startActivity(facebookIntent);
            } else {
                // Fallback to web sharing
                shareToFacebookWeb(context, article);
            }
        } catch (Exception e) {
            // Fallback to web sharing
            shareToFacebookWeb(context, article);
        }
    }

    /**
     * Share article via Facebook web
     */
    private static void shareToFacebookWeb(Context context, Article article) {
        String shareText = buildShareText(context, article);
        String url = "https://www.facebook.com/sharer/sharer.php?u=" +
                    Uri.encode("https://your-blog-url.com/article/" + article.getId()) +
                    "&quote=" + Uri.encode(shareText);

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(browserIntent);
    }

    /**
     * Share article specifically to Twitter
     */
    public static void shareToTwitter(Context context, Article article) {
        if (article == null || context == null) {
            return;
        }

        String shareText = buildShareText(context, article);
        // Truncate text to fit Twitter's character limit
        if (shareText.length() > 280) {
            shareText = shareText.substring(0, 277) + "...";
        }

        String url = "https://twitter.com/intent/tweet?text=" + Uri.encode(shareText);

        Intent twitterIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(twitterIntent);
    }

    /**
     * Share article via email
     */
    public static void shareViaEmail(Context context, Article article) {
        if (article == null || context == null) {
            return;
        }

        String shareText = buildShareText(context, article);
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, article.getTitle());
        emailIntent.putExtra(Intent.EXTRA_TEXT, shareText);

        context.startActivity(Intent.createChooser(emailIntent, "Send Email"));
    }

    /**
     * Copy article link to clipboard
     */
    public static void copyLink(Context context, Article article) {
        if (article == null || context == null) {
            return;
        }

        String link = "https://your-blog-url.com/article/" + article.getId();

        android.content.ClipboardManager clipboard = (android.content.ClipboardManager)
            context.getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("Article Link", link);
        clipboard.setPrimaryClip(clip);

    }

    /**
     * Build share text for the article
     */
    private static String buildShareText(Context context, Article article) {
        StringBuilder shareText = new StringBuilder();

        // Article title
        shareText.append("📖 ").append(article.getTitle()).append("\n\n");
        String content = article.getContent();
        if (content != null && content.length() > 150) {
            content = content.substring(0, 150) + "...";
        }
        shareText.append(content).append("\n\n");

        // Author and category info
        if (article.getAuthorName() != null) {
            shareText.append("By ").append(article.getAuthorName());
        }
        if (article.getCategoryId() != null) {
            shareText.append(" • ").append(capitalizeFirst(article.getCategoryId()));
        }
        shareText.append("\n\n");

        // App branding
        shareText.append("Shared from Sintesis Blog App\n");
        shareText.append("📱 Download: https://play.google.com/store/apps/details?id=")
                .append(context.getPackageName());

        return shareText.toString();
    }

    /**
     * Check if an app is installed
     */
    private static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * Get list of available sharing apps
     */
    public static List<ResolveInfo> getAvailableShareApps(Context context, String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);

        return context.getPackageManager().queryIntentActivities(intent,
            PackageManager.MATCH_DEFAULT_ONLY);
    }

    /**
     * Capitalize first letter of a string
     */
    private static String capitalizeFirst(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }
}
