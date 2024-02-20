package com.example.go4lunch.utils;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;

/**
 * Class with some useful function for view
 */
public class ViewUtils {

    /**
     * Change drawable color for text view
     *
     * @param textView Text view
     * @param color Color for the drawable
     */
    public static void setTextViewDrawableColor(@NonNull TextView textView, @ColorRes int color) {
        for (Drawable drawable : textView.getCompoundDrawables()) {
            if (drawable != null) {
                drawable.setColorFilter(new PorterDuffColorFilter(textView.getResources().getColor(color), PorterDuff.Mode.SRC_IN));
            }
        }
    }

    /**
     * Change drawable and text color for text view
     *
     * @param textView Text view
     * @param color Color for the drawable and the text
     */
    public static void setTextViewColor(@NonNull TextView textView, @ColorRes int color) {
        textView.setTextColor(textView.getResources().getColor(color));
        setTextViewDrawableColor(textView,color);
    }

}
