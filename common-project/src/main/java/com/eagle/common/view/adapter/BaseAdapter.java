package com.eagle.common.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {

    protected Context mContext;
    protected LayoutInflater mInflater;
    protected List<T> mData = new ArrayList<>();

    public BaseAdapter(Context context){
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public List<T> getData() {
        return mData;
    }

    public void setData(List<T> list){
        if(list != null) {
            mData.clear();
            mData.addAll(list);
            notifyDataSetChanged();
        }
    }

    public void addItemLast(List<T> list) {
        mData.addAll(list);
        notifyDataSetChanged();
    }

    public void addItem(T item){
        mData.add(item);
        notifyDataSetChanged();
    }

    public void addItem(int position, T item){
        mData.add(position,item);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        mData.remove(position);
        notifyDataSetChanged();
    }

    public void removeAll() {
        mData.clear();
        notifyDataSetChanged();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }
}
