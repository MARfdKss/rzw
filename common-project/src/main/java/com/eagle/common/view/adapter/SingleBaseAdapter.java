package com.eagle.common.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

public abstract class SingleBaseAdapter<T> extends BaseAdapter<T> {


    public SingleBaseAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BaseViewHolder(mInflater.inflate(getLayoutResId(), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        binderData(holder, mData.get(position), position);
    }

    /**
     * 获取layoutId
     */
    protected abstract int getLayoutResId();

    /**
     * 绑定数据源
     */
    protected abstract void binderData(BaseViewHolder baseViewHolder, T item, int position);

}
