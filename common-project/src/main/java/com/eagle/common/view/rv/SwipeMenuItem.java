package com.eagle.common.view.rv;


import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v4.content.ContextCompat;

public class SwipeMenuItem {
    private Context mContext;
    private Drawable background;
    private Drawable icon;
    private String title;
    private ColorStateList titleColor;
    private int titleSize;
    private Typeface textTypeface;
    private int textAppearance;
    private int width = -2;
    private int height = -2;
    private int weight = 0;

    public SwipeMenuItem(Context context) {
        this.mContext = context;
    }

    public SwipeMenuItem setBackground(@DrawableRes int resId) {
        return this.setBackground(ContextCompat.getDrawable(this.mContext, resId));
    }

    public SwipeMenuItem setBackground(Drawable background) {
        this.background = background;
        return this;
    }

    public SwipeMenuItem setBackgroundColorResource(@ColorRes int color) {
        return this.setBackgroundColor(ContextCompat.getColor(this.mContext, color));
    }

    public SwipeMenuItem setBackgroundColor(@ColorInt int color) {
        this.background = new ColorDrawable(color);
        return this;
    }

    public Drawable getBackground() {
        return this.background;
    }

    public SwipeMenuItem setImage(@DrawableRes int resId) {
        return this.setImage(ContextCompat.getDrawable(this.mContext, resId));
    }

    public SwipeMenuItem setImage(Drawable icon) {
        this.icon = icon;
        return this;
    }

    public Drawable getImage() {
        return this.icon;
    }

    public SwipeMenuItem setText(@StringRes int resId) {
        return this.setText(this.mContext.getString(resId));
    }

    public SwipeMenuItem setText(String title) {
        this.title = title;
        return this;
    }

    public String getText() {
        return this.title;
    }

    public SwipeMenuItem setTextColorResource(@ColorRes int titleColor) {
        return this.setTextColor(ContextCompat.getColor(this.mContext, titleColor));
    }

    public SwipeMenuItem setTextColor(@ColorInt int titleColor) {
        this.titleColor = ColorStateList.valueOf(titleColor);
        return this;
    }

    public ColorStateList getTitleColor() {
        return this.titleColor;
    }

    public SwipeMenuItem setTextSize(int titleSize) {
        this.titleSize = titleSize;
        return this;
    }

    public int getTextSize() {
        return this.titleSize;
    }

    public SwipeMenuItem setTextAppearance(@StyleRes int textAppearance) {
        this.textAppearance = textAppearance;
        return this;
    }

    public int getTextAppearance() {
        return this.textAppearance;
    }

    public SwipeMenuItem setTextTypeface(Typeface textTypeface) {
        this.textTypeface = textTypeface;
        return this;
    }

    public Typeface getTextTypeface() {
        return this.textTypeface;
    }

    public SwipeMenuItem setWidth(int width) {
        this.width = width;
        return this;
    }

    public int getWidth() {
        return this.width;
    }

    public SwipeMenuItem setHeight(int height) {
        this.height = height;
        return this;
    }

    public int getHeight() {
        return this.height;
    }

    public SwipeMenuItem setWeight(int weight) {
        this.weight = weight;
        return this;
    }

    public int getWeight() {
        return this.weight;
    }
}
