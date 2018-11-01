package com.eagle.common.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.eagle.common.bean.LoadingStatus;
import com.eagle.common.mvp.BaseMvpFragment;
import com.eagle.common.mvp.BasePresenter;
import com.eagle.common.mvp.IModel;
import com.eagle.common.mvp.IView;
import com.eagle.common.view.LoadingView;
import com.eagle.common.view.dialog.LoadingDialog;


public abstract class BaseFragment<M extends IModel, V extends IView, P extends BasePresenter> extends BaseMvpFragment<M, V, P> implements View.OnTouchListener {

    private boolean isVisible;                  //是否可见状态
    private boolean isPrepared;                 //标志位，View已经初始化完成。
    private boolean isFirstLoad = true;         //是否第一次加载
    protected LayoutInflater inflater;
    private LoadingView loadingView;
    private LoadingDialog mLoadingDialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        isFirstLoad = true;
        View view = initView(inflater, container, savedInstanceState);
        isPrepared = true;
        lazyLoad();
        return view;
    }

    /**
     * 如果是与ViewPager一起使用，调用的是setUserVisibleHint
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    /**
     * 如果是通过FragmentTransaction的show和hide的方法来控制显示，调用的是onHiddenChanged.
     * 若是初始就show的Fragment 为了触发该事件 需要先hide再show
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    protected void onVisible() {
        lazyLoad();
    }

    protected void onInvisible() {
    }

    protected void lazyLoad() {
        if (!isPrepared || !isVisible || !isFirstLoad) {
            return;
        }
        isFirstLoad = false;
        initData();
    }

    protected abstract View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    protected abstract void initData();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 拦截触摸事件，防止泄露下去
        view.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return true;
    }

    private LoadingView getLoadingView() {
        LoadingView loadingView = new LoadingView(getContext());
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

    protected void onRefreshLoadView() {
        //刷新页面
    }

    public void showLoadingDialog(String msg) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(getActivity());
        }
        if (mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
        mLoadingDialog.setCancelable(true);
        mLoadingDialog.show(msg);

    }

    public void showLoadingDialog(int msg) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(getActivity());
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
}
