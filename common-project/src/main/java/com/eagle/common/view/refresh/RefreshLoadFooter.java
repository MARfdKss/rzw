package com.eagle.common.view.refresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eagle.common.base.R;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.internal.pathview.PathsDrawable;
import com.scwang.smartrefresh.layout.util.DensityUtil;

public class RefreshLoadFooter extends RelativeLayout implements RefreshFooter {

    public static String REFRESH_FOOTER_PULLUP = "上拉加载更多";
    public static String REFRESH_FOOTER_RELEASE = "释放立即加载";
    public static String REFRESH_FOOTER_LOADING = "正在加载...";
    public static String REFRESH_FOOTER_REFRESHING = "正在刷新...";
    public static String REFRESH_FOOTER_FINISH = "加载完成";
    public static String REFRESH_FOOTER_FAILED = "加载失败";
    public static String REFRESH_FOOTER_ALLLOADED = "全部加载完成";

    protected TextView mTitleText;
    protected ImageView mArrowView;
    protected ImageView mProgressView;
    protected ImageView mStaticIconView;
    protected PathsDrawable mArrowDrawable;
    protected SpinnerStyle mSpinnerStyle = SpinnerStyle.Translate;
    protected RefreshKernel mRefreshKernel;
    protected int mFinishDuration = 500;
    protected int mBackgroundColor = 0;
    protected boolean mNoMoreData = false;
    protected int mPaddingTop = 20;
    protected int mPaddingBottom = 20;

    public RefreshLoadFooter(Context context) {
        super(context);
        this.initView(context, null);
    }

    public RefreshLoadFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context, attrs);
    }

    public RefreshLoadFooter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_refresh_footer, this, true);
        mTitleText = view.findViewById(R.id.title_tv);
        mArrowView = view.findViewById(R.id.arrow_iv);
        mStaticIconView = view.findViewById(R.id.static_logo_iv);
        mProgressView = view.findViewById(R.id.progress_iv);
        mProgressView.animate().setInterpolator(new LinearInterpolator());
        DensityUtil density = new DensityUtil();
        mTitleText.setText(REFRESH_FOOTER_PULLUP);
        mArrowDrawable = new PathsDrawable();
        mArrowDrawable.parserColors(0xff666666);
        mArrowDrawable.parserPaths("M20,12l-1.41,-1.41L13,16.17V4h-2v12.17l-5.58,-5.59L4,12l8,8 8,-8z");
        mArrowView.setImageDrawable(mArrowDrawable);

        if (!isInEditMode()) {
            mProgressView.setVisibility(GONE);
            mStaticIconView.setVisibility(GONE);
        } else {
            mArrowView.setVisibility(GONE);
        }
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ClassicsFooter);
        mFinishDuration = ta.getInt(R.styleable.ClassicsFooter_srlFinishDuration, mFinishDuration);
        mSpinnerStyle = SpinnerStyle.values()[ta.getInt(R.styleable.ClassicsFooter_srlClassicsSpinnerStyle, mSpinnerStyle.ordinal())];

        ta.recycle();

        if (getPaddingTop() == 0) {
            if (getPaddingBottom() == 0) {
                setPadding(getPaddingLeft(), mPaddingTop = density.dip2px(20), getPaddingRight(), mPaddingBottom = density.dip2px(20));
            } else {
                setPadding(getPaddingLeft(), mPaddingTop = density.dip2px(20), getPaddingRight(), mPaddingBottom = getPaddingBottom());
            }
        } else {
            if (getPaddingBottom() == 0) {
                setPadding(getPaddingLeft(), mPaddingTop = getPaddingTop(), getPaddingRight(), mPaddingBottom = density.dip2px(20));
            } else {
                mPaddingTop = getPaddingTop();
                mPaddingBottom = getPaddingBottom();
            }
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
            setPadding(getPaddingLeft(), 0, getPaddingRight(), 0);
        } else {
            setPadding(getPaddingLeft(), mPaddingTop, getPaddingRight(), mPaddingBottom);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onInitialized(RefreshKernel kernel, int height, int extendHeight) {
        mRefreshKernel = kernel;
        mRefreshKernel.requestDrawBackgroundForFooter(mBackgroundColor);
    }

    @Override
    public void onPulling(float percent, int offset, int height, int extendHeight) {

    }

    @Override
    public void onReleasing(float percent, int offset, int height, int extendHeight) {

    }

    @Override
    public void onReleased(RefreshLayout refreshLayout, int height, int extendHeight) {
        onStartAnimator(refreshLayout, height, extendHeight);
    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public void onStartAnimator(RefreshLayout layout, int headHeight, int extendHeight) {
        if (!mNoMoreData) {
            View progressView = mProgressView;
            if (progressView.getVisibility() != VISIBLE) {
                progressView.setVisibility(VISIBLE);
                mStaticIconView.setVisibility(VISIBLE);
                Drawable drawable = mProgressView.getDrawable();
                if (drawable instanceof Animatable) {
                    ((Animatable) drawable).start();
                } else {
                    progressView.animate().rotation(36000).setDuration(100000);
                }
            }
        }
    }

    @Override
    public int onFinish(RefreshLayout layout, boolean success) {
        if (!mNoMoreData) {
            mProgressView.animate().rotation(0).setDuration(300);
            mProgressView.setVisibility(GONE);
            mStaticIconView.setVisibility(GONE);
            if (success) {
                mTitleText.setText(REFRESH_FOOTER_FINISH);
            } else {
                mTitleText.setText(REFRESH_FOOTER_FAILED);
            }
            return mFinishDuration;
        }
        return 0;
    }

    /**
     * ClassicsFooter 在(SpinnerStyle.FixedBehind)时才有主题色
     */
    @Override
    @Deprecated
    public void setPrimaryColors(@ColorInt int ... colors) {
        if (mSpinnerStyle == SpinnerStyle.FixedBehind) {
            if (colors.length > 0) {
                if (!(getBackground() instanceof BitmapDrawable)) {
                    setPrimaryColor(colors[0]);
                }
                if (colors.length > 1) {
                    setAccentColor(colors[1]);
                } else {
                    setAccentColor(colors[0] == 0xffffffff ? 0xff666666 : 0xffffffff);
                }
            }
        }
    }

    @NonNull
    public View getView() {
        return this;
    }

    @Override
    public SpinnerStyle getSpinnerStyle() {
        return mSpinnerStyle;
    }

    @Override
    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
        if (!mNoMoreData) {
            switch (newState) {
                case None:
                    mArrowView.setVisibility(VISIBLE);
                case PullUpToLoad:
                    mTitleText.setText(REFRESH_FOOTER_PULLUP);
                    mArrowView.animate().rotation(180);
                    break;
                case Loading:
                case LoadReleased:
                    mArrowView.setVisibility(GONE);
                    mTitleText.setText(REFRESH_FOOTER_LOADING);
                    break;
                case ReleaseToLoad:
                    mTitleText.setText(REFRESH_FOOTER_RELEASE);
                    mArrowView.animate().rotation(0);
                    break;
                case Refreshing:
                    mTitleText.setText(REFRESH_FOOTER_REFRESHING);
                    mProgressView.setVisibility(GONE);
                    mStaticIconView.setVisibility(GONE);
                    mArrowView.setVisibility(GONE);
                    break;
            }
        }
    }

    public RefreshLoadFooter setAccentColor(@ColorInt int accentColor) {
        mTitleText.setTextColor(accentColor);
        if (mArrowDrawable != null) {
            mArrowDrawable.parserColors(accentColor);
        }
        return this;
    }
    public RefreshLoadFooter setPrimaryColor(@ColorInt int primaryColor) {
        setBackgroundColor(mBackgroundColor = primaryColor);
        if (mRefreshKernel != null) {
            mRefreshKernel.requestDrawBackgroundForFooter(mBackgroundColor);
        }
        return this;
    }

    @Override
    public boolean setNoMoreData(boolean noMoreData) {
        if (mNoMoreData != noMoreData) {
            mNoMoreData = noMoreData;
            if (noMoreData) {
                mTitleText.setText(REFRESH_FOOTER_ALLLOADED);
            } else {
                mTitleText.setText(REFRESH_FOOTER_PULLUP);
            }
            mProgressView.animate().rotation(0).setDuration(300);
            mProgressView.setVisibility(GONE);
            mStaticIconView.setVisibility(GONE);
            mArrowView.setVisibility(GONE);
        }
        return true;
    }
}
