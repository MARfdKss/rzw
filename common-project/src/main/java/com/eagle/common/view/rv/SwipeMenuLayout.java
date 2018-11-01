package com.eagle.common.view.rv;


import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.OverScroller;
import android.widget.TextView;
import com.eagle.common.base.R;

import static com.eagle.common.base.R.styleable.recyclerSwipeMenuLayout;


public class SwipeMenuLayout extends FrameLayout implements SwipeSwitch {
    public static final int DEFAULT_SCROLLER_DURATION = 200;
    private int mLeftViewId;
    private int mContentViewId;
    private int mRightViewId;
    private float mOpenPercent;
    private int mScrollerDuration;
    private int mScaledTouchSlop;
    private int mLastX;
    private int mLastY;
    private int mDownX;
    private int mDownY;
    private View mContentView;
    private SwipeLeftHorizontal mSwipeLeftHorizontal;
    private SwipeRightHorizontal mSwipeRightHorizontal;
    private SwipeHorizontal mSwipeCurrentHorizontal;
    private boolean shouldResetSwipe;
    private boolean mDragging;
    private boolean swipeEnable;
    private OverScroller mScroller;
    private VelocityTracker mVelocityTracker;
    private int mScaledMinimumFlingVelocity;
    private int mScaledMaximumFlingVelocity;

    public SwipeMenuLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public SwipeMenuLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeMenuLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mLeftViewId = 0;
        this.mContentViewId = 0;
        this.mRightViewId = 0;
        this.mOpenPercent = 0.5F;
        this.mScrollerDuration = 200;
        this.swipeEnable = true;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, recyclerSwipeMenuLayout);
        this.mLeftViewId = typedArray.getResourceId(R.styleable.recyclerSwipeMenuLayout_leftViewId, this.mLeftViewId);
        this.mContentViewId = typedArray.getResourceId(R.styleable.recyclerSwipeMenuLayout_contentViewId, this.mContentViewId);
        this.mRightViewId = typedArray.getResourceId(R.styleable.recyclerSwipeMenuLayout_rightViewId, this.mRightViewId);
        typedArray.recycle();
        ViewConfiguration configuration = ViewConfiguration.get(this.getContext());
        this.mScaledTouchSlop = configuration.getScaledTouchSlop();
        this.mScaledMinimumFlingVelocity = configuration.getScaledMinimumFlingVelocity();
        this.mScaledMaximumFlingVelocity = configuration.getScaledMaximumFlingVelocity();
        this.mScroller = new OverScroller(this.getContext());
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        View errorView;
        if (this.mLeftViewId != 0 && this.mSwipeLeftHorizontal == null) {
            errorView = this.findViewById(this.mLeftViewId);
            this.mSwipeLeftHorizontal = new SwipeLeftHorizontal(errorView);
        }

        if (this.mRightViewId != 0 && this.mSwipeRightHorizontal == null) {
            errorView = this.findViewById(this.mRightViewId);
            this.mSwipeRightHorizontal = new SwipeRightHorizontal(errorView);
        }

        if (this.mContentViewId != 0 && this.mContentView == null) {
            this.mContentView = this.findViewById(this.mContentViewId);
        } else {
            TextView errorView1 = new TextView(this.getContext());
            errorView1.setClickable(true);
            errorView1.setGravity(17);
            errorView1.setTextSize(16.0F);
            errorView1.setText("You may not have set the ContentView.");
            this.mContentView = errorView1;
            this.addView(this.mContentView);
        }

    }

    public void setSwipeEnable(boolean swipeEnable) {
        this.swipeEnable = swipeEnable;
    }

    public boolean isSwipeEnable() {
        return this.swipeEnable;
    }

    public void setOpenPercent(float openPercent) {
        this.mOpenPercent = openPercent;
    }

    public float getOpenPercent() {
        return this.mOpenPercent;
    }

    public void setScrollerDuration(int scrollerDuration) {
        this.mScrollerDuration = scrollerDuration;
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean isIntercepted = super.onInterceptTouchEvent(ev);
        int action = ev.getAction();
        switch (action) {
            case 0:
                this.mDownX = this.mLastX = (int) ev.getX();
                this.mDownY = (int) ev.getY();
                return false;
            case 1:
                boolean isClick1 = this.mSwipeCurrentHorizontal != null && this.mSwipeCurrentHorizontal.isClickOnContentView(this.getWidth(), ev.getX());
                if (this.isMenuOpen() && isClick1) {
                    this.smoothCloseMenu();
                    return true;
                } else {
                    return false;
                }
            case 2:
                int isClick = (int) (ev.getX() - (float) this.mDownX);
                int disY = (int) (ev.getY() - (float) this.mDownY);
                boolean i = Math.abs(isClick) > this.mScaledTouchSlop && Math.abs(isClick) > Math.abs(disY);
                return i;
            case 3:
                if (!this.mScroller.isFinished()) {
                    this.mScroller.abortAnimation();
                }

                return false;
            default:
                return isIntercepted;
        }
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }

        this.mVelocityTracker.addMovement(ev);
        int action = ev.getAction();
        int dx;
        int dy;
        int velocityX;
        int velocity;
        switch (action) {
            case 0:
                this.mLastX = (int) ev.getX();
                this.mLastY = (int) ev.getY();
                break;
            case 1:
                dx = (int) ((float) this.mDownX - ev.getX());
                dy = (int) ((float) this.mDownY - ev.getY());
                this.mDragging = false;
                this.mVelocityTracker.computeCurrentVelocity(1000, (float) this.mScaledMaximumFlingVelocity);
                velocityX = (int) this.mVelocityTracker.getXVelocity();
                velocity = Math.abs(velocityX);
                if (velocity > this.mScaledMinimumFlingVelocity) {
                    if (this.mSwipeCurrentHorizontal != null) {
                        int duration = this.getSwipeDuration(ev, velocity);
                        if (this.mSwipeCurrentHorizontal instanceof SwipeRightHorizontal) {
                            if (velocityX < 0) {
                                this.smoothOpenMenu(duration);
                            } else {
                                this.smoothCloseMenu(duration);
                            }
                        } else if (velocityX > 0) {
                            this.smoothOpenMenu(duration);
                        } else {
                            this.smoothCloseMenu(duration);
                        }

                        ViewCompat.postInvalidateOnAnimation(this);
                    }
                } else {
                    this.judgeOpenClose(dx, dy);
                }

                this.mVelocityTracker.clear();
                this.mVelocityTracker.recycle();
                this.mVelocityTracker = null;
                if (Math.abs((float) this.mDownX - ev.getX()) > (float) this.mScaledTouchSlop || Math.abs((float) this.mDownY - ev.getY()) > (float) this.mScaledTouchSlop || this.isLeftMenuOpen() || this.isRightMenuOpen()) {
                    ev.setAction(3);
                    super.onTouchEvent(ev);
                    return true;
                }
                break;
            case 2:
                if (this.isSwipeEnable()) {
                    velocityX = (int) ((float) this.mLastX - ev.getX());
                    velocity = (int) ((float) this.mLastY - ev.getY());
                    if (!this.mDragging && Math.abs(velocityX) > this.mScaledTouchSlop && Math.abs(velocityX) > Math.abs(velocity)) {
                        this.mDragging = true;
                    }

                    if (this.mDragging) {
                        if (this.mSwipeCurrentHorizontal == null || this.shouldResetSwipe) {
                            if (velocityX < 0) {
                                if (this.mSwipeLeftHorizontal != null) {
                                    this.mSwipeCurrentHorizontal = this.mSwipeLeftHorizontal;
                                } else {
                                    this.mSwipeCurrentHorizontal = this.mSwipeRightHorizontal;
                                }
                            } else if (this.mSwipeRightHorizontal != null) {
                                this.mSwipeCurrentHorizontal = this.mSwipeRightHorizontal;
                            } else {
                                this.mSwipeCurrentHorizontal = this.mSwipeLeftHorizontal;
                            }
                        }

                        this.scrollBy(velocityX, 0);
                        this.mLastX = (int) ev.getX();
                        this.mLastY = (int) ev.getY();
                        this.shouldResetSwipe = false;
                    }
                }
                break;
            case 3:
                this.mDragging = false;
                if (!this.mScroller.isFinished()) {
                    this.mScroller.abortAnimation();
                } else {
                    dx = (int) ((float) this.mDownX - ev.getX());
                    dy = (int) ((float) this.mDownY - ev.getY());
                    this.judgeOpenClose(dx, dy);
                }
        }

        return super.onTouchEvent(ev);
    }

    private int getSwipeDuration(MotionEvent ev, int velocity) {
        int sx = this.getScrollX();
        int dx = (int) (ev.getX() - (float) sx);
        int width = this.mSwipeCurrentHorizontal.getMenuWidth();
        int halfWidth = width / 2;
        float distanceRatio = Math.min(1.0F, 1.0F * (float) Math.abs(dx) / (float) width);
        float distance = (float) halfWidth + (float) halfWidth * this.distanceInfluenceForSnapDuration(distanceRatio);
        int duration;
        if (velocity > 0) {
            duration = 4 * Math.round(1000.0F * Math.abs(distance / (float) velocity));
        } else {
            float pageDelta = (float) Math.abs(dx) / (float) width;
            duration = (int) ((pageDelta + 1.0F) * 100.0F);
        }

        duration = Math.min(duration, this.mScrollerDuration);
        return duration;
    }

    float distanceInfluenceForSnapDuration(float f) {
        f -= 0.5F;
        f = (float) ((double) f * 0.4712389167638204D);
        return (float) Math.sin((double) f);
    }

    private void judgeOpenClose(int dx, int dy) {
        if (this.mSwipeCurrentHorizontal != null) {
            if ((float) Math.abs(this.getScrollX()) >= (float) this.mSwipeCurrentHorizontal.getMenuView().getWidth() * this.mOpenPercent) {
                if (Math.abs(dx) <= this.mScaledTouchSlop && Math.abs(dy) <= this.mScaledTouchSlop) {
                    if (this.isMenuOpen()) {
                        this.smoothCloseMenu();
                    } else {
                        this.smoothOpenMenu();
                    }
                } else if (this.isMenuOpenNotEqual()) {
                    this.smoothCloseMenu();
                } else {
                    this.smoothOpenMenu();
                }
            } else {
                this.smoothCloseMenu();
            }
        }

    }

    public void scrollTo(int x, int y) {
        if (this.mSwipeCurrentHorizontal == null) {
            super.scrollTo(x, y);
        } else {
            SwipeHorizontal.Checker checker = this.mSwipeCurrentHorizontal.checkXY(x, y);
            this.shouldResetSwipe = checker.shouldResetSwipe;
            if (checker.x != this.getScrollX()) {
                super.scrollTo(checker.x, checker.y);
            }
        }

    }

    public void computeScroll() {
        if (this.mScroller.computeScrollOffset() && this.mSwipeCurrentHorizontal != null) {
            if (this.mSwipeCurrentHorizontal instanceof SwipeRightHorizontal) {
                this.scrollTo(Math.abs(this.mScroller.getCurrX()), 0);
                this.invalidate();
            } else {
                this.scrollTo(-Math.abs(this.mScroller.getCurrX()), 0);
                this.invalidate();
            }
        }

    }

    public boolean hasLeftMenu() {
        return this.mSwipeLeftHorizontal != null && this.mSwipeLeftHorizontal.canSwipe();
    }

    public boolean hasRightMenu() {
        return this.mSwipeRightHorizontal != null && this.mSwipeRightHorizontal.canSwipe();
    }

    public boolean isMenuOpen() {
        return this.isLeftMenuOpen() || this.isRightMenuOpen();
    }

    public boolean isLeftMenuOpen() {
        return this.mSwipeLeftHorizontal != null && this.mSwipeLeftHorizontal.isMenuOpen(this.getScrollX());
    }

    public boolean isRightMenuOpen() {
        return this.mSwipeRightHorizontal != null && this.mSwipeRightHorizontal.isMenuOpen(this.getScrollX());
    }

    public boolean isCompleteOpen() {
        return this.isLeftCompleteOpen() || this.isRightMenuOpen();
    }

    public boolean isLeftCompleteOpen() {
        return this.mSwipeLeftHorizontal != null && !this.mSwipeLeftHorizontal.isCompleteClose(this.getScrollX());
    }

    public boolean isRightCompleteOpen() {
        return this.mSwipeRightHorizontal != null && !this.mSwipeRightHorizontal.isCompleteClose(this.getScrollX());
    }

    public boolean isMenuOpenNotEqual() {
        return this.isLeftMenuOpenNotEqual() || this.isRightMenuOpenNotEqual();
    }

    public boolean isLeftMenuOpenNotEqual() {
        return this.mSwipeLeftHorizontal != null && this.mSwipeLeftHorizontal.isMenuOpenNotEqual(this.getScrollX());
    }

    public boolean isRightMenuOpenNotEqual() {
        return this.mSwipeRightHorizontal != null && this.mSwipeRightHorizontal.isMenuOpenNotEqual(this.getScrollX());
    }

    public void smoothOpenMenu() {
        this.smoothOpenMenu(this.mScrollerDuration);
    }

    public void smoothOpenLeftMenu() {
        this.smoothOpenLeftMenu(this.mScrollerDuration);
    }

    public void smoothOpenRightMenu() {
        this.smoothOpenRightMenu(this.mScrollerDuration);
    }

    public void smoothOpenLeftMenu(int duration) {
        if (this.mSwipeLeftHorizontal != null) {
            this.mSwipeCurrentHorizontal = this.mSwipeLeftHorizontal;
            this.smoothOpenMenu(duration);
        }

    }

    public void smoothOpenRightMenu(int duration) {
        if (this.mSwipeRightHorizontal != null) {
            this.mSwipeCurrentHorizontal = this.mSwipeRightHorizontal;
            this.smoothOpenMenu(duration);
        }

    }

    private void smoothOpenMenu(int duration) {
        if (this.mSwipeCurrentHorizontal != null) {
            this.mSwipeCurrentHorizontal.autoOpenMenu(this.mScroller, this.getScrollX(), duration);
            this.invalidate();
        }

    }

    public void smoothCloseMenu() {
        this.smoothCloseMenu(this.mScrollerDuration);
    }

    public void smoothCloseLeftMenu() {
        if (this.mSwipeLeftHorizontal != null) {
            this.mSwipeCurrentHorizontal = this.mSwipeLeftHorizontal;
            this.smoothCloseMenu();
        }

    }

    public void smoothCloseRightMenu() {
        if (this.mSwipeRightHorizontal != null) {
            this.mSwipeCurrentHorizontal = this.mSwipeRightHorizontal;
            this.smoothCloseMenu();
        }

    }

    public void smoothCloseMenu(int duration) {
        if (this.mSwipeCurrentHorizontal != null) {
            this.mSwipeCurrentHorizontal.autoCloseMenu(this.mScroller, this.getScrollX(), duration);
            this.invalidate();
        }

    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int contentViewHeight = 0;
        if (this.mContentView != null) {
            this.measureChildWithMargins(this.mContentView, widthMeasureSpec, 0, heightMeasureSpec, 0);
            contentViewHeight = this.mContentView.getMeasuredHeight();
        }

        View rightMenu;
        int menuViewHeight;
        int menuWidthSpec;
        int menuHeightSpec;
        if (this.mSwipeLeftHorizontal != null) {
            rightMenu = this.mSwipeLeftHorizontal.getMenuView();
            menuViewHeight = contentViewHeight == 0 ? rightMenu.getMeasuredHeightAndState() : contentViewHeight;
            menuWidthSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), -2147483648);
            menuHeightSpec = MeasureSpec.makeMeasureSpec(menuViewHeight, 1073741824);
            rightMenu.measure(menuWidthSpec, menuHeightSpec);
        }

        if (this.mSwipeRightHorizontal != null) {
            rightMenu = this.mSwipeRightHorizontal.getMenuView();
            menuViewHeight = contentViewHeight == 0 ? rightMenu.getMeasuredHeightAndState() : contentViewHeight;
            menuWidthSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), -2147483648);
            menuHeightSpec = MeasureSpec.makeMeasureSpec(menuViewHeight, 1073741824);
            rightMenu.measure(menuWidthSpec, menuHeightSpec);
        }

        if (contentViewHeight > 0) {
            this.setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), contentViewHeight);
        }

    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int menuViewHeight;
        if (this.mContentView != null) {
            int rightMenu = this.mContentView.getMeasuredWidthAndState();
            int contentViewHeight = this.mContentView.getMeasuredHeightAndState();
            LayoutParams menuViewWidth = (LayoutParams) this.mContentView.getLayoutParams();
            menuViewHeight = this.getPaddingLeft();
            int lp = this.getPaddingTop() + menuViewWidth.topMargin;
            this.mContentView.layout(menuViewHeight, lp, menuViewHeight + rightMenu, lp + contentViewHeight);
        }

        int top;
        LayoutParams lp1;
        View rightMenu1;
        int menuViewWidth1;
        if (this.mSwipeLeftHorizontal != null) {
            rightMenu1 = this.mSwipeLeftHorizontal.getMenuView();
            menuViewWidth1 = rightMenu1.getMeasuredWidthAndState();
            menuViewHeight = rightMenu1.getMeasuredHeightAndState();
            lp1 = (LayoutParams) rightMenu1.getLayoutParams();
            top = this.getPaddingTop() + lp1.topMargin;
            rightMenu1.layout(-menuViewWidth1, top, 0, top + menuViewHeight);
        }

        if (this.mSwipeRightHorizontal != null) {
            rightMenu1 = this.mSwipeRightHorizontal.getMenuView();
            menuViewWidth1 = rightMenu1.getMeasuredWidthAndState();
            menuViewHeight = rightMenu1.getMeasuredHeightAndState();
            lp1 = (LayoutParams) rightMenu1.getLayoutParams();
            top = this.getPaddingTop() + lp1.topMargin;
            int parentViewWidth = this.getMeasuredWidthAndState();
            rightMenu1.layout(parentViewWidth, top, parentViewWidth + menuViewWidth1, top + menuViewHeight);
        }

    }
}
