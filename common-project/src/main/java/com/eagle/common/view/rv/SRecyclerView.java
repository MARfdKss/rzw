package com.eagle.common.view.rv;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SRecyclerView extends RecyclerView {
    public static final int LEFT_DIRECTION = 1;
    public static final int RIGHT_DIRECTION = -1;
    private static final int INVALID_POSITION = -1;
    protected int mScaleTouchSlop;
    protected SwipeMenuLayout mOldSwipedLayout;
    protected int mOldTouchedPosition;
    private int mDownX;
    private int mDownY;
    private boolean allowSwipeDelete;
    private SwipeMenuCreator mSwipeMenuCreator;
    private SwipeMenuItemClickListener mSwipeMenuItemClickListener;
    private OnItemClickListener mSwipeItemClickListener;
    private OnItemLongClickListener mSwipeItemLongClickListener;
    private SwipeAdapterWrapper mAdapterWrapper;
    private AdapterDataObserver mAdapterDataObserver;
    private List<View> mHeaderViewList;
    private List<View> mFooterViewList;

    public SRecyclerView(Context context) {
        this(context, (AttributeSet) null);
    }

    public SRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mOldTouchedPosition = -1;
        this.allowSwipeDelete = false;
        this.mAdapterDataObserver = new AdapterDataObserver() {
            public void onChanged() {
                SRecyclerView.this.mAdapterWrapper.notifyDataSetChanged();
            }

            public void onItemRangeChanged(int positionStart, int itemCount) {
                positionStart += SRecyclerView.this.getHeaderItemCount();
                SRecyclerView.this.mAdapterWrapper.notifyItemRangeChanged(positionStart, itemCount);
            }

            public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
                positionStart += SRecyclerView.this.getHeaderItemCount();
                SRecyclerView.this.mAdapterWrapper.notifyItemRangeChanged(positionStart, itemCount, payload);
            }

            public void onItemRangeInserted(int positionStart, int itemCount) {
                positionStart += SRecyclerView.this.getHeaderItemCount();
                SRecyclerView.this.mAdapterWrapper.notifyItemRangeInserted(positionStart, itemCount);
            }

            public void onItemRangeRemoved(int positionStart, int itemCount) {
                positionStart += SRecyclerView.this.getHeaderItemCount();
                SRecyclerView.this.mAdapterWrapper.notifyItemRangeRemoved(positionStart, itemCount);
            }

            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                fromPosition += SRecyclerView.this.getHeaderItemCount();
                toPosition += SRecyclerView.this.getHeaderItemCount();
                SRecyclerView.this.mAdapterWrapper.notifyItemMoved(fromPosition, toPosition);
            }
        };
        this.mHeaderViewList = new ArrayList<>();
        this.mFooterViewList = new ArrayList<>();
        this.mScaleTouchSlop = ViewConfiguration.get(this.getContext()).getScaledTouchSlop();
    }

    private void checkAdapterExist(String message) {
        if (this.mAdapterWrapper != null) {
            throw new IllegalStateException(message);
        }
    }

    /**
     * item的点击监听
     * @param itemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        if (itemClickListener != null) {
            this.checkAdapterExist("Cannot set item click listener, setAdapter has already been called.");
            this.mSwipeItemClickListener = new SRecyclerView.ItemClick(this, itemClickListener);
        }
    }

    /**
     * item的长按监听
     * @param itemLongClickListener
     */
    public void setOnItemLongClickListener(OnItemLongClickListener itemLongClickListener) {
        if (itemLongClickListener != null) {
            this.checkAdapterExist("Cannot set item long click listener, setAdapter has already been called.");
            this.mSwipeItemLongClickListener = new SRecyclerView.ItemLongClick(this, itemLongClickListener);
        }
    }

    /**
     * 菜单创建器
     * @param menuCreator
     */
    public void setSwipeMenuCreator(SwipeMenuCreator menuCreator) {
        if (menuCreator != null) {
            this.checkAdapterExist("Cannot set menu creator, setAdapter has already been called.");
            this.mSwipeMenuCreator = menuCreator;
        }
    }

    /**
     * item的滑动Menu点击监听
     * @param menuItemClickListener
     */
    public void setSwipeMenuItemClickListener(SwipeMenuItemClickListener menuItemClickListener) {
        if (menuItemClickListener != null) {
            this.checkAdapterExist("Cannot set menu item click listener, setAdapter has already been called.");
            this.mSwipeMenuItemClickListener = new SRecyclerView.MenuItemClick(this, menuItemClickListener);
        }
    }

    public void setLayoutManager(LayoutManager layoutManager) {
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup spanSizeLookupHolder = gridLayoutManager.getSpanSizeLookup();
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                public int getSpanSize(int position) {
                    return !SRecyclerView.this.mAdapterWrapper.isHeaderView(position) && !SRecyclerView.this.mAdapterWrapper.isFooterView(position) ? (spanSizeLookupHolder != null ? spanSizeLookupHolder.getSpanSize(position - SRecyclerView.this.getHeaderItemCount()) : 1) : gridLayoutManager.getSpanCount();
                }
            });
        }

        super.setLayoutManager(layoutManager);
    }

    public Adapter getOriginAdapter() {
        return this.mAdapterWrapper == null ? null : this.mAdapterWrapper.getOriginAdapter();
    }

    public void setAdapter(Adapter adapter) {
        if (this.mAdapterWrapper != null) {
            this.mAdapterWrapper.getOriginAdapter().unregisterAdapterDataObserver(this.mAdapterDataObserver);
        }

        if (adapter == null) {
            this.mAdapterWrapper = null;
        } else {
            adapter.registerAdapterDataObserver(this.mAdapterDataObserver);
            this.mAdapterWrapper = new SwipeAdapterWrapper(this.getContext(), adapter);
            this.mAdapterWrapper.setSwipeItemClickListener(this.mSwipeItemClickListener);
            this.mAdapterWrapper.setSwipeItemLongClickListener(this.mSwipeItemLongClickListener);
            this.mAdapterWrapper.setSwipeMenuCreator(this.mSwipeMenuCreator);
            this.mAdapterWrapper.setSwipeMenuItemClickListener(this.mSwipeMenuItemClickListener);
            Iterator var2;
            View view;
            if (this.mHeaderViewList.size() > 0) {
                var2 = this.mHeaderViewList.iterator();

                while (var2.hasNext()) {
                    view = (View) var2.next();
                    this.mAdapterWrapper.addHeaderView(view);
                }
            }

            if (this.mFooterViewList.size() > 0) {
                var2 = this.mFooterViewList.iterator();

                while (var2.hasNext()) {
                    view = (View) var2.next();
                    this.mAdapterWrapper.addFooterView(view);
                }
            }
        }

        super.setAdapter(this.mAdapterWrapper);
    }

    public void addHeaderView(View view) {
        this.mHeaderViewList.add(view);
        if (this.mAdapterWrapper != null) {
            this.mAdapterWrapper.addHeaderViewAndNotify(view);
        }

    }

    public void removeHeaderView(View view) {
        this.mHeaderViewList.remove(view);
        if (this.mAdapterWrapper != null) {
            this.mAdapterWrapper.removeHeaderViewAndNotify(view);
        }

    }

    public void addFooterView(View view) {
        this.mFooterViewList.add(view);
        if (this.mAdapterWrapper != null) {
            this.mAdapterWrapper.addFooterViewAndNotify(view);
        }

    }

    public void removeFooterView(View view) {
        this.mFooterViewList.remove(view);
        if (this.mAdapterWrapper != null) {
            this.mAdapterWrapper.removeFooterViewAndNotify(view);
        }

    }

    public int getHeaderItemCount() {
        return this.mAdapterWrapper == null ? 0 : this.mAdapterWrapper.getHeaderItemCount();
    }

    public int getFooterItemCount() {
        return this.mAdapterWrapper == null ? 0 : this.mAdapterWrapper.getFooterItemCount();
    }

    public int getItemViewType(int position) {
        return this.mAdapterWrapper == null ? 0 : this.mAdapterWrapper.getItemViewType(position);
    }

    public void smoothOpenLeftMenu(int position) {
        this.smoothOpenMenu(position, 1, 200);
    }

    public void smoothOpenLeftMenu(int position, int duration) {
        this.smoothOpenMenu(position, 1, duration);
    }

    public void smoothOpenRightMenu(int position) {
        this.smoothOpenMenu(position, -1, 200);
    }

    public void smoothOpenRightMenu(int position, int duration) {
        this.smoothOpenMenu(position, -1, duration);
    }

    public void smoothOpenMenu(int position, int direction, int duration) {
        if (this.mOldSwipedLayout != null && this.mOldSwipedLayout.isMenuOpen()) {
            this.mOldSwipedLayout.smoothCloseMenu();
        }

        position += this.getHeaderItemCount();
        ViewHolder vh = this.findViewHolderForAdapterPosition(position);
        if (vh != null) {
            View itemView = this.getSwipeMenuView(vh.itemView);
            if (itemView instanceof SwipeMenuLayout) {
                this.mOldSwipedLayout = (SwipeMenuLayout) itemView;
                if (direction == -1) {
                    this.mOldTouchedPosition = position;
                    this.mOldSwipedLayout.smoothOpenRightMenu(duration);
                } else if (direction == 1) {
                    this.mOldTouchedPosition = position;
                    this.mOldSwipedLayout.smoothOpenLeftMenu(duration);
                }
            }
        }

    }

    public void smoothCloseMenu() {
        if (this.mOldSwipedLayout != null && this.mOldSwipedLayout.isMenuOpen()) {
            this.mOldSwipedLayout.smoothCloseMenu();
        }

    }

    public boolean onInterceptTouchEvent(MotionEvent e) {
        boolean isIntercepted = super.onInterceptTouchEvent(e);
        if (this.allowSwipeDelete) {
            return isIntercepted;
        } else if (e.getPointerCount() > 1) {
            return true;
        } else {
            int action = e.getAction();
            int x = (int) e.getX();
            int y = (int) e.getY();
            switch (action) {
                case 0:
                    this.mDownX = x;
                    this.mDownY = y;
                    isIntercepted = false;
                    int viewParent1 = this.getChildAdapterPosition(this.findChildViewUnder((float) x, (float) y));
                    if (viewParent1 != this.mOldTouchedPosition && this.mOldSwipedLayout != null && this.mOldSwipedLayout.isMenuOpen()) {
                        this.mOldSwipedLayout.smoothCloseMenu();
                        isIntercepted = true;
                    }

                    if (isIntercepted) {
                        this.mOldSwipedLayout = null;
                        this.mOldTouchedPosition = -1;
                    } else {
                        ViewHolder disX1 = this.findViewHolderForAdapterPosition(viewParent1);
                        if (disX1 != null) {
                            View showRightCloseLeft1 = this.getSwipeMenuView(disX1.itemView);
                            if (showRightCloseLeft1 instanceof SwipeMenuLayout) {
                                this.mOldSwipedLayout = (SwipeMenuLayout) showRightCloseLeft1;
                                this.mOldTouchedPosition = viewParent1;
                            }
                        }
                    }
                    break;
                case 2:
                    isIntercepted = this.handleUnDown(x, y, isIntercepted);
                    if (this.mOldSwipedLayout == null) {
                        break;
                    }

                    ViewParent viewParent = this.getParent();
                    if (viewParent == null) {
                        break;
                    }

                    int disX = this.mDownX - x;
                    boolean showRightCloseLeft = disX > 0 && (this.mOldSwipedLayout.hasRightMenu() || this.mOldSwipedLayout.isLeftCompleteOpen());
                    boolean showLeftCloseRight = disX < 0 && (this.mOldSwipedLayout.hasLeftMenu() || this.mOldSwipedLayout.isRightCompleteOpen());
                    viewParent.requestDisallowInterceptTouchEvent(showRightCloseLeft || showLeftCloseRight);
                case 1:
                case 3:
                    isIntercepted = this.handleUnDown(x, y, isIntercepted);
            }

            return isIntercepted;
        }
    }

    private boolean handleUnDown(int x, int y, boolean defaultValue) {
        int disX = this.mDownX - x;
        int disY = this.mDownY - y;
        return Math.abs(disX) > this.mScaleTouchSlop && Math.abs(disX) > Math.abs(disY) ? false : (Math.abs(disY) < this.mScaleTouchSlop && Math.abs(disX) < this.mScaleTouchSlop ? false : defaultValue);
    }

    public boolean onTouchEvent(MotionEvent e) {
        int action = e.getAction();
        switch (action) {
            case 2:
                if (this.mOldSwipedLayout != null && this.mOldSwipedLayout.isMenuOpen()) {
                    this.mOldSwipedLayout.smoothCloseMenu();
                }
            case 0:
            case 1:
            case 3:
            default:
                return super.onTouchEvent(e);
        }
    }

    private View getSwipeMenuView(View itemView) {
        if (itemView instanceof SwipeMenuLayout) {
            return itemView;
        } else {
            ArrayList unvisited = new ArrayList();
            unvisited.add(itemView);

            while (true) {
                View child;
                do {
                    if (unvisited.isEmpty()) {
                        return itemView;
                    }

                    child = (View) unvisited.remove(0);
                } while (!(child instanceof ViewGroup));

                if (child instanceof SwipeMenuLayout) {
                    return child;
                }

                ViewGroup group = (ViewGroup) child;
                int childCount = group.getChildCount();

                for (int i = 0; i < childCount; ++i) {
                    unvisited.add(group.getChildAt(i));
                }
            }
        }
    }


    private static class MenuItemClick implements SwipeMenuItemClickListener {
        private SRecyclerView mRecyclerView;
        private SwipeMenuItemClickListener mCallback;

        public MenuItemClick(SRecyclerView recyclerView, SwipeMenuItemClickListener callback) {
            this.mRecyclerView = recyclerView;
            this.mCallback = callback;
        }

        public void onItemClick(SwipeMenuBridge menuBridge) {
            int position = menuBridge.getAdapterPosition();
            position -= this.mRecyclerView.getHeaderItemCount();
            if (position >= 0) {
                menuBridge.mAdapterPosition = position;
                this.mCallback.onItemClick(menuBridge);
            }

        }
    }

    private static class ItemLongClick implements OnItemLongClickListener {
        private SRecyclerView mRecyclerView;
        private OnItemLongClickListener mCallback;

        public ItemLongClick(SRecyclerView recyclerView, OnItemLongClickListener callback) {
            this.mRecyclerView = recyclerView;
            this.mCallback = callback;
        }

        public void onItemLongClick(View itemView, int position) {
            position -= this.mRecyclerView.getHeaderItemCount();
            if (position >= 0) {
                this.mCallback.onItemLongClick(itemView, position);
            }

        }
    }

    private static class ItemClick implements OnItemClickListener {
        private SRecyclerView mRecyclerView;
        private OnItemClickListener mCallback;

        public ItemClick(SRecyclerView recyclerView, OnItemClickListener callback) {
            this.mRecyclerView = recyclerView;
            this.mCallback = callback;
        }

        public void onItemClick(View itemView, int position) {
            position -= this.mRecyclerView.getHeaderItemCount();
            if (position >= 0) {
                this.mCallback.onItemClick(itemView, position);
            }

        }
    }

}
