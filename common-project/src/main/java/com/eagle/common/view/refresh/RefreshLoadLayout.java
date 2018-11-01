package com.eagle.common.view.refresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import com.eagle.common.base.R;
import com.eagle.common.view.rv.SRecyclerView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import static com.eagle.common.base.R.styleable.refreshLoadLayout;

public class RefreshLoadLayout extends LinearLayout implements OnRefreshLoadMoreListener{

    public OnRefreshListener onRefreshListener;
    public OnLoadMoreListener onLoadmoreListener;

    private SmartRefreshLayout refreshLayout;
    private SRecyclerView recyclerView;

    public RefreshLoadLayout(Context context) {
        super(context);
        initView(context,null);
    }

    public RefreshLoadLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context,attrs);
    }

    public RefreshLoadLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context,attrs);
    }

    private void initView(Context context, AttributeSet attrs){
        TypedArray ta = context.obtainStyledAttributes(attrs, refreshLoadLayout);
        boolean enableRefresh = ta.getBoolean(R.styleable.refreshLoadLayout_enableRefresh, false);
        boolean enableLoadMore = ta.getBoolean(R.styleable.refreshLoadLayout_enableLoadMore, false);
        ta.recycle();
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_refresh_load, this, true);
        refreshLayout = view.findViewById(R.id.refresh_layout);
        recyclerView = view.findViewById(R.id.recycler_view);
        refreshLayout.setEnableRefresh(enableRefresh);
        refreshLayout.setEnableLoadMore(enableLoadMore);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
    }

    public SRecyclerView getRecyclerView(){
        return recyclerView;
    }

    public void setOnDownRefreshListener(OnRefreshListener onRefreshListener){
        this.onRefreshListener = onRefreshListener;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener){
        this.onLoadmoreListener = onLoadMoreListener;
    }

    public void onRefreshComplete(){
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        if(onLoadmoreListener != null){
            onLoadmoreListener.onLoadMore();
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        if(onRefreshListener != null){
            onRefreshListener.onRefresh();
        }
    }

    public interface OnRefreshListener{
        void onRefresh();
    }

    public interface OnLoadMoreListener{
        void onLoadMore();
    }
}
