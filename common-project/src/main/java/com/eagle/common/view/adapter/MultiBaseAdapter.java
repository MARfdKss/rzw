package com.eagle.common.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.SparseIntArray;
import android.view.ViewGroup;

public abstract class MultiBaseAdapter<T> extends BaseAdapter<T> {

    private SparseIntArray mLayoutMap = new SparseIntArray();

    public MultiBaseAdapter(Context context){
        super(context);
        initLayoutMap();
    }

    private void initLayoutMap(){
        int[] layoutIds = getLayoutResIds();
        if(layoutIds != null) {
            for (int key = 0; key < layoutIds.length; ++key) {
                mLayoutMap.put(key, layoutIds[key]);
            }
        }
    }

    /**
     * 根据viewType获取layoutId
     */
    private int getLayoutId(int viewType){
        return mLayoutMap.get(viewType);
    }

    /**
     * 根据layoutId获取viewType
     */
    private int getViewType(int layoutId){
        int index = mLayoutMap.indexOfValue(layoutId);

        if(index >= 0){
            return mLayoutMap.keyAt(index);
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        int layoutId = bindItemLayout(mData.get(position), position);
        return getViewType(layoutId);
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BaseViewHolder(mInflater.inflate(getLayoutId(viewType), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        int layoutId = bindItemLayout(mData.get(position), position);
        binderData(holder, mData.get(position), layoutId, position);
    }

    /**
     * 获取多个layoutId
     */
    protected abstract int[] getLayoutResIds();

    /**
     * 获取每个item的layoutId
     */
    protected abstract int bindItemLayout(T item, int position);

    /**
     * 绑定数据源
     */
    protected abstract void binderData(BaseViewHolder baseViewHolder, T item, int layoutId, int position);


}
