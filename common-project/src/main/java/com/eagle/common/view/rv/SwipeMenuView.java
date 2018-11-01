package com.eagle.common.view.rv;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class SwipeMenuView extends LinearLayout implements OnClickListener {
    private ViewHolder mAdapterVIewHolder;
    private SwipeSwitch mSwipeSwitch;
    private SwipeMenuItemClickListener mItemClickListener;
    private int mDirection;

    public SwipeMenuView(Context context) {
        this(context, (AttributeSet) null);
    }

    public SwipeMenuView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void createMenu(SwipeMenu swipeMenu, SwipeSwitch swipeSwitch, SwipeMenuItemClickListener swipeMenuItemClickListener, int direction) {
        this.removeAllViews();
        this.mSwipeSwitch = swipeSwitch;
        this.mItemClickListener = swipeMenuItemClickListener;
        this.mDirection = direction;
        List items = swipeMenu.getMenuItems();

        for (int i = 0; i < items.size(); ++i) {
            SwipeMenuItem item = (SwipeMenuItem) items.get(i);
            LayoutParams params = new LayoutParams(item.getWidth(), item.getHeight());
            params.weight = (float) item.getWeight();
            LinearLayout parent = new LinearLayout(this.getContext());
            parent.setId(i);
            parent.setGravity(17);
            parent.setOrientation(1);
            parent.setLayoutParams(params);
            ViewCompat.setBackground(parent, item.getBackground());
            parent.setOnClickListener(this);
            this.addView(parent);
            SwipeMenuBridge menuBridge = new SwipeMenuBridge(this.mDirection, i, this.mSwipeSwitch, parent);
            parent.setTag(menuBridge);
            if (item.getImage() != null) {
                ImageView tv = this.createIcon(item);
                menuBridge.mImageView = tv;
                parent.addView(tv);
            }

            if (!TextUtils.isEmpty(item.getText())) {
                TextView var12 = this.createTitle(item);
                menuBridge.mTextView = var12;
                parent.addView(var12);
            }
        }

    }

    public void bindViewHolder(ViewHolder adapterVIewHolder) {
        this.mAdapterVIewHolder = adapterVIewHolder;
    }

    private ImageView createIcon(SwipeMenuItem item) {
        ImageView imageView = new ImageView(this.getContext());
        imageView.setImageDrawable(item.getImage());
        return imageView;
    }

    private TextView createTitle(SwipeMenuItem item) {
        TextView textView = new TextView(this.getContext());
        textView.setText(item.getText());
        textView.setGravity(17);
        int textSize = item.getTextSize();
        if (textSize > 0) {
            textView.setTextSize(2, (float) textSize);
        }

        ColorStateList textColor = item.getTitleColor();
        if (textColor != null) {
            textView.setTextColor(textColor);
        }

        int textAppearance = item.getTextAppearance();
        if (textAppearance != 0) {
            TextViewCompat.setTextAppearance(textView, textAppearance);
        }

        Typeface typeface = item.getTextTypeface();
        if (typeface != null) {
            textView.setTypeface(typeface);
        }

        return textView;
    }

    public void onClick(View v) {
        if (this.mItemClickListener != null && this.mSwipeSwitch.isMenuOpen()) {
            SwipeMenuBridge menuBridge = (SwipeMenuBridge) v.getTag();
            menuBridge.mAdapterPosition = this.mAdapterVIewHolder.getAdapterPosition();
            this.mItemClickListener.onItemClick(menuBridge);
        }

    }
}
