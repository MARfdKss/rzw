package com.eagle.common.view.rv;


import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public final class SwipeMenuBridge {
    private final int mDirection;
    private final int mPosition;
    private final SwipeSwitch mSwipeSwitch;
    private final View mViewRoot;
    int mAdapterPosition;
    TextView mTextView;
    ImageView mImageView;

    SwipeMenuBridge(int direction, int position, SwipeSwitch swipeSwitch, View viewRoot) {
        this.mDirection = direction;
        this.mPosition = position;
        this.mSwipeSwitch = swipeSwitch;
        this.mViewRoot = viewRoot;
    }

    public SwipeMenuBridge setBackgroundDrawable(@DrawableRes int resId) {
        return this.setBackgroundDrawable(ContextCompat.getDrawable(this.mViewRoot.getContext(), resId));
    }

    public SwipeMenuBridge setBackgroundDrawable(Drawable background) {
        ViewCompat.setBackground(this.mViewRoot, background);
        return this;
    }

    public SwipeMenuBridge setBackgroundColorResource(@ColorRes int color) {
        return this.setBackgroundColor(ContextCompat.getColor(this.mViewRoot.getContext(), color));
    }

    public SwipeMenuBridge setBackgroundColor(@ColorInt int color) {
        this.mViewRoot.setBackgroundColor(color);
        return this;
    }

    public SwipeMenuBridge setImage(int resId) {
        return this.setImage(ContextCompat.getDrawable(this.mViewRoot.getContext(), resId));
    }

    public SwipeMenuBridge setImage(Drawable icon) {
        if (this.mImageView != null) {
            this.mImageView.setImageDrawable(icon);
        }

        return this;
    }

    public SwipeMenuBridge setText(int resId) {
        return this.setText(this.mViewRoot.getContext().getString(resId));
    }

    public SwipeMenuBridge setText(String title) {
        if (this.mTextView != null) {
            this.mTextView.setText(title);
        }

        return this;
    }

    public int getDirection() {
        return this.mDirection;
    }

    public int getPosition() {
        return this.mPosition;
    }

    public int getAdapterPosition() {
        return this.mAdapterPosition;
    }

    public void closeMenu() {
        this.mSwipeSwitch.smoothCloseMenu();
    }
}
