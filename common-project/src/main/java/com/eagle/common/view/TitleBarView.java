package com.eagle.common.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eagle.common.base.R;


public class TitleBarView extends RelativeLayout {
    public static final int ID_LEFT_BUTTON = 0;
    public static final int ID_RIGHT_BUTTON = 1;

    private BackClickListener backClickListener;
    private View viewLayout;
    private LinearLayout customTitleLayout;
    private ImageView leftImg;
    private TextView titleTv;
    private RelativeLayout rightLayout;
    private TextView rightTv;
    private ImageView rightImg;
    private View headLineView;

    public TitleBarView(Context context) {
        super(context);
        initView();
    }

    public TitleBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public TitleBarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        viewLayout = LayoutInflater.from(getContext()).inflate(R.layout.layout_titlebar, this, true);
        customTitleLayout = findViewById(R.id.custom_title_ll);
        leftImg = findViewById(R.id.custom_left_iv);
        titleTv = findViewById(R.id.head_title_tv);
        rightLayout = findViewById(R.id.custom_right_rl);
        rightTv = findViewById(R.id.custom_right_tv);
        rightImg = findViewById(R.id.menu_right_iv);
        headLineView = findViewById(R.id.head_line_view);
        setBackListener();
    }

    public void setBackListener() {
        leftImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (backClickListener != null) {
                        if (backClickListener.onBack(ID_LEFT_BUTTON)) {
                            return;
                        }
                    }
                    ((Activity) getContext()).finish();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setViewBackground(int resId) {
        viewLayout.setBackgroundResource(resId);
    }

    public void setLeftImage(int resId) {
        leftImg.setImageResource(resId);
    }

    public void setTitle(String title) {
        titleTv.setText(title);
    }

    public void setTitleColor(int resId) {
        titleTv.setTextColor(resId);
    }

    public void setTitleBackground(int id) {
        titleTv.setBackgroundResource(id);
    }

    public void setRightTitle(String title) {
        rightTv.setText(title);
        rightTv.setVisibility(VISIBLE);
        rightImg.setVisibility(GONE);
    }

    public void setRightTitleColor(int resId) {
        rightTv.setTextColor(getResources().getColor(resId));
    }

    public void setRightImage(int resId) {
        rightImg.setBackgroundResource(resId);
        rightImg.setVisibility(VISIBLE);
        rightTv.setVisibility(GONE);
    }

    public void setRightClickListener(OnClickListener listener) {
        rightLayout.setOnClickListener(listener);
    }

    public String getRightTitle() {
        return rightTv.getText().toString();
    }

    public void setCustomLayoutVisibility(int visibility) {
        customTitleLayout.setVisibility(visibility);
    }

    public void setLeftVisibility(int visibility) {
        leftImg.setVisibility(visibility);
    }

    public void setTitleVisibility(int visibility) {
        titleTv.setVisibility(visibility);
    }

    public void setRightVisibility(int visibility) {
        rightLayout.setVisibility(visibility);
    }

    public void setViewClickListener(BackClickListener backClickListener) {
        this.backClickListener = backClickListener;
    }

    public TextView getTitleTextView() {
        return titleTv;
    }

    public void setLineVisibility(int visibility) {
        headLineView.setVisibility(visibility);
    }

    public interface BackClickListener {
        boolean onBack(int id);
    }
}
