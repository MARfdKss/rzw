package com.eagle.common.base;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.eagle.common.bean.LoadingStatus;
import com.eagle.common.mvp.BaseMvpActivity;
import com.eagle.common.mvp.BasePresenter;
import com.eagle.common.mvp.IModel;
import com.eagle.common.mvp.IView;
import com.eagle.common.view.LoadingView;
import com.eagle.common.view.TitleBarView;
import com.eagle.common.view.dialog.LoadingDialog;


public abstract class BaseActivity<M extends IModel, V extends IView, P extends BasePresenter> extends BaseMvpActivity<M, V, P> {

    public String TAG = getClass().getSimpleName();
    private TitleBarView titleBarView;
    private LoadingView loadingView;
    private LoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        initBaseView();
        findById();
        setListener();
        initView();
    }

    private void initBaseView() {
        titleBarView = findViewById(R.id.title_bar_view);
        FrameLayout contentContain = findViewById(R.id.ll_container);
        View contentView = View.inflate(this, getContentView(), null);
        contentContain.addView(contentView);
        loadingView = getLoadingView();
        contentContain.addView(loadingView);
    }

    private LoadingView getLoadingView() {
        LoadingView loadingView = new LoadingView(this);
        loadingView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        loadingView.setLoadingViewClickListener(new LoadingView.onLoadingViewClickListener() {
            @Override
            public void onClick(View view) {
                onRefreshLoadView();
            }
        });
        loadingView.setViewVisibility(View.GONE);
        return loadingView;
    }

    public void setLoadingView(int status) {
        setLoadingView(status, "");
    }

    public void setLoadingView(int status, String tips) {
        if (loadingView == null) {
            return;
        }
        if (status == LoadingStatus.LOADING.value()) {
            loadingView.setVisibility(View.VISIBLE);
            loadingView.setLoadingVisibility(View.VISIBLE);
        } else if (status == LoadingStatus.FAIL_LOADING.value()) {
            loadingView.setVisibility(View.VISIBLE);
            loadingView.setFailLoadVisibility(View.VISIBLE);
        } else if (status == LoadingStatus.EMPTY.value()) {
            loadingView.setVisibility(View.VISIBLE);
            loadingView.setEmptyVisibility(View.VISIBLE, tips);
        } else {
            loadingView.setVisibility(View.GONE);
        }
    }

    public TitleBarView getTitleBarView() {
        return titleBarView;
    }

    public void showLoadingDialog(String msg) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
        }
        if (mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
        mLoadingDialog.setCancelable(true);
        mLoadingDialog.show(msg);

    }

    public void showLoadingDialog(int msg) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
        }
        if (mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
        mLoadingDialog.setCancelable(true);
        mLoadingDialog.show(getString(msg));

    }

    public void hideLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    protected void onRefreshLoadView() {
        //刷新页面
    }


    protected void setImmersionBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            //5.0及以上
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            window.getDecorView().setSystemUiVisibility(option);

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);

        } else if (Build.VERSION.SDK_INT >= 19) {
            //4.4及以上
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //虚拟键盘也透明
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    protected void setFitSystemWindow(boolean fitSystemWindow) {
        View contentViewGroup = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(1);
        if (contentViewGroup != null) {
            contentViewGroup.setFitsSystemWindows(fitSystemWindow);
        }
    }

}
