package com.eagle.common.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eagle.common.base.R;
import com.eagle.common.base.RootApplication;


public class LoadingView extends LinearLayout {

    public interface onLoadingViewClickListener {
        void onClick(View view);
    }

    private onLoadingViewClickListener mLoadingViewClickListener;

    private View viewLayout;

    //"加载中"layout
    private RelativeLayout loadingLayout;
    private ImageView loadingImg;
    private TextView loadingTv;

    //"加载失败"layout
    private RelativeLayout failLoadLayout;
    private ImageView failLoadImg;
    private TextView failLoadTv;

    public LoadingView(Context context) {
        super(context);
        initView();
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        viewLayout = LayoutInflater.from(getContext()).inflate(R.layout.layout_loading_view, this, true);
        loadingLayout = findViewById(R.id.loading_rl);
        loadingImg = findViewById(R.id.progress_iv);
        loadingTv = findViewById(R.id.loading_tv);
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.loading_rotate);
        loadingImg.startAnimation(animation);
        failLoadLayout = findViewById(R.id.fail_loading_rl);
        failLoadImg = findViewById(R.id.fail_load_iv);
        failLoadTv = findViewById(R.id.fail_load_tv);
        setListener();
    }

    private void setListener() {
        failLoadLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLoadingViewClickListener != null) {
                    mLoadingViewClickListener.onClick(v);
                }
            }
        });
    }

    public void setViewBackground(int resId) {
        if (viewLayout != null) {
            viewLayout.setBackgroundResource(resId);
        }
    }

    public void setLoadingTips(String tips) {
        if (loadingTv != null) {
            loadingTv.setText(tips);
        }
    }

    public void setFailLoadImage(int resId) {
        if (failLoadImg != null) {
            failLoadImg.setImageResource(resId);
        }
    }

    public void setFailLoadTips(String tips) {
        if (failLoadTv != null) {
            failLoadTv.setText(tips);
        }
    }

    public void setViewVisibility(int visibility) {
        if (viewLayout != null) {
            viewLayout.setVisibility(visibility);
        }
    }

    /**
     * 正在加载
     */
    public void setLoadingVisibility(int visibility) {
        if (loadingLayout != null) {
            loadingLayout.setVisibility(visibility);

            if (visibility == VISIBLE) {
                failLoadLayout.setVisibility(GONE);
            }
        }
    }

    /**
     * 加载失败
     */
    public void setFailLoadVisibility(int visibility) {
        if (failLoadLayout != null) {
            failLoadLayout.setVisibility(visibility);

            if (visibility == VISIBLE) {
                if (!isConnected(RootApplication.getInstance().getApplicationContext())) {
                    failLoadImg.setImageResource(R.drawable.ic_load_network_fail);
                    failLoadTv.setText("网络连接不可用,请稍候重试");
                } else {
                    failLoadImg.setImageResource(R.drawable.ic_load_data_fail);
                    failLoadTv.setText("数据请求失败，请稍候重试");
                }
                loadingLayout.setVisibility(GONE);
            }
        }
    }

    /**
     * 空界面
     */
    public void setEmptyVisibility(int visibility, String tips) {
        if (failLoadLayout != null) {
            failLoadLayout.setVisibility(visibility);
            if (visibility == VISIBLE) {
                failLoadImg.setImageResource(R.drawable.ic_load_empty);
                failLoadTv.setText(tips);
                loadingLayout.setVisibility(GONE);
            }
        }
    }

    public void setLoadingViewClickListener(onLoadingViewClickListener listener) {
        this.mLoadingViewClickListener = listener;
    }

    private boolean isConnected(Context context) {
        boolean isOk = true;
        try {
            @SuppressLint("WrongConstant")
            ConnectivityManager e = (ConnectivityManager) context.getSystemService("connectivity");
            NetworkInfo mobNetInfo = e.getNetworkInfo(0);
            NetworkInfo wifiNetInfo = e.getNetworkInfo(1);
            if (wifiNetInfo != null && !wifiNetInfo.isConnectedOrConnecting() && mobNetInfo != null && !mobNetInfo.isConnectedOrConnecting()) {
                NetworkInfo info = e.getActiveNetworkInfo();
                if (info == null) {
                    isOk = false;
                }
            }
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        return isOk;
    }

}