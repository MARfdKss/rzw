package com.eagle.common.view.rv;


import android.content.Context;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eagle.common.base.R;

import java.lang.reflect.Field;
import java.util.List;

public class SwipeAdapterWrapper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int BASE_ITEM_TYPE_HEADER = 100000;
    private static final int BASE_ITEM_TYPE_FOOTER = 200000;
    private SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat<>();
    private SparseArrayCompat<View> mFootViews = new SparseArrayCompat<>();
    private RecyclerView.Adapter mAdapter;
    private LayoutInflater mInflater;
    private SwipeMenuCreator mSwipeMenuCreator;
    private SwipeMenuItemClickListener mSwipeMenuItemClickListener;
    private OnItemClickListener mSwipeItemClickListener;
    private OnItemLongClickListener mSwipeItemLongClickListener;

    SwipeAdapterWrapper(Context context, RecyclerView.Adapter adapter) {
        this.mInflater = LayoutInflater.from(context);
        this.mAdapter = adapter;
    }

    public RecyclerView.Adapter getOriginAdapter() {
        return this.mAdapter;
    }

    void setSwipeMenuCreator(SwipeMenuCreator swipeMenuCreator) {
        this.mSwipeMenuCreator = swipeMenuCreator;
    }

    void setSwipeMenuItemClickListener(SwipeMenuItemClickListener swipeMenuItemClickListener) {
        this.mSwipeMenuItemClickListener = swipeMenuItemClickListener;
    }

    void setSwipeItemClickListener(OnItemClickListener swipeItemClickListener) {
        this.mSwipeItemClickListener = swipeItemClickListener;
    }

    void setSwipeItemLongClickListener(OnItemLongClickListener swipeItemLongClickListener) {
        this.mSwipeItemLongClickListener = swipeItemLongClickListener;
    }

    public int getItemCount() {
        return this.getHeaderItemCount() + this.getContentItemCount() + this.getFooterItemCount();
    }

    private int getContentItemCount() {
        return this.mAdapter.getItemCount();
    }

    public int getItemViewType(int position) {
        return this.isHeaderView(position) ? this.mHeaderViews.keyAt(position) : (this.isFooterView(position) ? this.mFootViews.keyAt(position - this.getHeaderItemCount() - this.getContentItemCount()) : this.mAdapter.getItemViewType(position - this.getHeaderItemCount()));
    }

    public android.support.v7.widget.RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (this.mHeaderViews.get(viewType) != null) {
            return new SwipeAdapterWrapper.ViewHolder((View) this.mHeaderViews.get(viewType));
        } else if (this.mFootViews.get(viewType) != null) {
            return new SwipeAdapterWrapper.ViewHolder((View) this.mFootViews.get(viewType));
        } else {
            final android.support.v7.widget.RecyclerView.ViewHolder viewHolder = this.mAdapter.onCreateViewHolder(parent, viewType);
            if (this.mSwipeItemClickListener != null) {
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        SwipeAdapterWrapper.this.mSwipeItemClickListener.onItemClick(v, viewHolder.getAdapterPosition());
                    }
                });
            }

            if (this.mSwipeItemLongClickListener != null) {
                viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    public boolean onLongClick(View v) {
                        SwipeAdapterWrapper.this.mSwipeItemLongClickListener.onItemLongClick(v, viewHolder.getAdapterPosition());
                        return true;
                    }
                });
            }

            if (this.mSwipeMenuCreator == null) {
                return viewHolder;
            } else {
                SwipeMenuLayout swipeMenuLayout = (SwipeMenuLayout) this.mInflater.inflate(R.layout.layout_swipe_view_item, parent, false);
                SwipeMenu swipeLeftMenu = new SwipeMenu(swipeMenuLayout, viewType);
                SwipeMenu swipeRightMenu = new SwipeMenu(swipeMenuLayout, viewType);
                this.mSwipeMenuCreator.onCreateMenu(swipeLeftMenu, swipeRightMenu, viewType);
                int leftMenuCount = swipeLeftMenu.getMenuItems().size();
                if (leftMenuCount > 0) {
                    SwipeMenuView rightMenuCount = (SwipeMenuView) swipeMenuLayout.findViewById(R.id.swipe_left);
                    rightMenuCount.setOrientation(swipeLeftMenu.getOrientation());
                    rightMenuCount.createMenu(swipeLeftMenu, swipeMenuLayout, this.mSwipeMenuItemClickListener, 1);
                }

                int rightMenuCount1 = swipeRightMenu.getMenuItems().size();
                if (rightMenuCount1 > 0) {
                    SwipeMenuView viewGroup = (SwipeMenuView) swipeMenuLayout.findViewById(R.id.swipe_right);
                    viewGroup.setOrientation(swipeRightMenu.getOrientation());
                    viewGroup.createMenu(swipeRightMenu, swipeMenuLayout, this.mSwipeMenuItemClickListener, -1);
                }

                ViewGroup viewGroup1 = (ViewGroup) swipeMenuLayout.findViewById(R.id.swipe_content);
                viewGroup1.addView(viewHolder.itemView);

                try {
                    Field e = this.getSupperClass(viewHolder.getClass()).getDeclaredField("itemView");
                    if (!e.isAccessible()) {
                        e.setAccessible(true);
                    }

                    e.set(viewHolder, swipeMenuLayout);
                } catch (Exception var11) {
                    var11.printStackTrace();
                }

                return viewHolder;
            }
        }
    }

    private Class<?> getSupperClass(Class<?> aClass) {
        Class supperClass = aClass.getSuperclass();
        return supperClass != null && !supperClass.equals(Object.class) ? this.getSupperClass(supperClass) : aClass;
    }

    public final void onBindViewHolder(android.support.v7.widget.RecyclerView.ViewHolder holder, int position) {
    }

    public final void onBindViewHolder(android.support.v7.widget.RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        if (!this.isHeaderView(position) && !this.isFooterView(position)) {
            View itemView = holder.itemView;
            if (itemView instanceof SwipeMenuLayout) {
                SwipeMenuLayout swipeMenuLayout = (SwipeMenuLayout) itemView;
                int childCount = swipeMenuLayout.getChildCount();

                for (int i = 0; i < childCount; ++i) {
                    View childView = swipeMenuLayout.getChildAt(i);
                    if (childView instanceof SwipeMenuView) {
                        ((SwipeMenuView) childView).bindViewHolder(holder);
                    }
                }
            }

            this.mAdapter.onBindViewHolder(holder, position - this.getHeaderItemCount(), payloads);
        }
    }

    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        this.mAdapter.onAttachedToRecyclerView(recyclerView);
    }

    public void onViewAttachedToWindow(android.support.v7.widget.RecyclerView.ViewHolder holder) {
        int position = holder.getAdapterPosition();
        if (!this.isHeaderView(position) && !this.isFooterView(position)) {
            this.mAdapter.onViewAttachedToWindow(holder);
        } else {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null && lp instanceof android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams) {
                android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams p = (android.support.v7.widget.StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }

    }

    public boolean isHeaderView(int position) {
        return position >= 0 && position < this.getHeaderItemCount();
    }

    public boolean isFooterView(int position) {
        return position >= this.getHeaderItemCount() + this.getContentItemCount();
    }

    public void addHeaderView(View view) {
        this.mHeaderViews.put(this.getHeaderItemCount() + 100000, view);
    }

    public void addHeaderViewAndNotify(View view) {
        this.mHeaderViews.put(this.getHeaderItemCount() + 100000, view);
        this.notifyItemInserted(this.getHeaderItemCount() - 1);
    }

    public void removeHeaderViewAndNotify(View view) {
        int headerIndex = this.mHeaderViews.indexOfValue(view);
        this.mHeaderViews.removeAt(headerIndex);
        this.notifyItemRemoved(headerIndex);
    }

    public void addFooterView(View view) {
        this.mFootViews.put(this.getFooterItemCount() + 200000, view);
    }

    public void addFooterViewAndNotify(View view) {
        this.mFootViews.put(this.getFooterItemCount() + 200000, view);
        this.notifyItemInserted(this.getHeaderItemCount() + this.getContentItemCount() + this.getFooterItemCount() - 1);
    }

    public void removeFooterViewAndNotify(View view) {
        int footerIndex = this.mFootViews.indexOfValue(view);
        this.mFootViews.removeAt(footerIndex);
        this.notifyItemRemoved(this.getHeaderItemCount() + this.getContentItemCount() + footerIndex);
    }

    public int getHeaderItemCount() {
        return this.mHeaderViews.size();
    }

    public int getFooterItemCount() {
        return this.mFootViews.size();
    }

    public void setHasStableIds(boolean hasStableIds) {
        this.mAdapter.setHasStableIds(hasStableIds);
    }

    public long getItemId(int position) {
        return !this.isHeaderView(position) && !this.isFooterView(position) ? this.mAdapter.getItemId(position) : super.getItemId(position);
    }

    public void onViewRecycled(android.support.v7.widget.RecyclerView.ViewHolder holder) {
        int position = holder.getAdapterPosition();
        if (!this.isHeaderView(position) && !this.isFooterView(position)) {
            this.mAdapter.onViewRecycled(holder);
        }

    }

    public boolean onFailedToRecycleView(android.support.v7.widget.RecyclerView.ViewHolder holder) {
        int position = holder.getAdapterPosition();
        return !this.isHeaderView(position) && !this.isFooterView(position) ? this.mAdapter.onFailedToRecycleView(holder) : false;
    }

    public void onViewDetachedFromWindow(android.support.v7.widget.RecyclerView.ViewHolder holder) {
        int position = holder.getAdapterPosition();
        if (!this.isHeaderView(position) && !this.isFooterView(position)) {
            this.mAdapter.onViewDetachedFromWindow(holder);
        }

    }

    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
    }

    public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.unregisterAdapterDataObserver(observer);
    }

    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        this.mAdapter.onDetachedFromRecyclerView(recyclerView);
    }

    static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
