package com.eagle.widget.fixtab;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import test.com.myapplication.R;

/**
 * @author renzhiwen
 * @time 2018/10/19 上午9:59
 * @describe 指示器不自动滑动
 */
public class STabLayout extends FrameLayout {
    private TabLayout mTabLayout;
    private List<View> mCustomViewList;
    private int mSelectIndicatorColor;
    private int mSelectTextColor;
    private int mUnSelectTextColor;
    private int mIndicatorHeight;
    private int mIndicatorWidth;
    private int mTabMode;
    private int mTabTextSize;
    private int mTabHeight;
    private int mTabMargin;

    public STabLayout(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public STabLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public STabLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public STabLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void readAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.STabLayout);
        mSelectIndicatorColor = typedArray.getColor(R.styleable.STabLayout_tabIndicatorColor, context.getResources().getColor(R.color.colorAccent));
        mUnSelectTextColor = typedArray.getColor(R.styleable.STabLayout_tabTextColor, Color.parseColor("#666666"));
        mSelectTextColor = typedArray.getColor(R.styleable.STabLayout_tabSelectTextColor, context.getResources().getColor(R.color.colorAccent));
        mIndicatorHeight = typedArray.getDimensionPixelSize(R.styleable.STabLayout_tabIndicatorHeight, 1);
        mIndicatorWidth = typedArray.getDimensionPixelSize(R.styleable.STabLayout_tabIndicatorWidth, 0);
        mTabHeight = typedArray.getDimensionPixelSize(R.styleable.STabLayout_tabHeight, dp2px(context, 40));
        mTabTextSize = typedArray.getDimensionPixelSize(R.styleable.STabLayout_tabTextSize, sp2px(context, 13));
        mTabMargin = typedArray.getDimensionPixelSize(R.styleable.STabLayout_tabMargin, sp2px(context, 6));
        mTabMode = typedArray.getInt(R.styleable.STabLayout_tab_Mode, 2);
        typedArray.recycle();
    }

    private void init(Context context, AttributeSet attrs) {
        readAttr(context, attrs);

        mCustomViewList = new ArrayList<>();
        View view = View.inflate(context, R.layout.layout_stab, this);
        mTabLayout = (TabLayout) view.findViewById(R.id.tab_view);

        // 添加属性
        mTabLayout.setTabMode(mTabMode == 1 ? TabLayout.MODE_FIXED : TabLayout.MODE_SCROLLABLE);
        FrameLayout.LayoutParams lp = (LayoutParams) mTabLayout.getLayoutParams();
        lp.height = mTabHeight + mIndicatorHeight;
        mTabLayout.setLayoutParams(lp);
    }

    public List<View> getCustomViewList() {
        return mCustomViewList;
    }

    public void addOnTabSelectedListener(TabLayout.OnTabSelectedListener onTabSelectedListener) {
        mTabLayout.addOnTabSelectedListener(onTabSelectedListener);
    }

    /**
     * 与TabLayout 联动
     */
    public void setupWithViewPager(@Nullable ViewPager viewPager) {
        mTabLayout.addOnTabSelectedListener(new ViewPagerOnTabSelectedListener(viewPager));
    }


    /**
     * retrive TabLayout Instance
     */
    public TabLayout getTabLayout() {
        return mTabLayout;
    }

    /**
     * 添加tab
     */
    public void addTab(String tab) {
        View customView = getTabView(getContext(), tab, mIndicatorWidth, mIndicatorHeight, mTabTextSize);
        mCustomViewList.add(customView);
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(customView));
    }

    public class ViewPagerOnTabSelectedListener implements TabLayout.OnTabSelectedListener {

        private final ViewPager mViewPager;

        ViewPagerOnTabSelectedListener(ViewPager viewPager) {
            mViewPager = viewPager;
            switchTab(mViewPager, 0);
        }

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            int position = tab.getPosition();
            mViewPager.setCurrentItem(position);
            updateTabView(position);
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            // No-op
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
            // No-op
        }
    }

    public void switchTab(ViewPager mViewPager, int position) {
        mViewPager.setCurrentItem(position);
        updateTabView(position);
    }

    /**
     * 更新tab view
     *
     * @param position 当前位置
     */
    public void updateTabView(int position) {
        if (mTabLayout != null) {
            List<View> customViewList = getCustomViewList();
            if (customViewList == null || customViewList.size() == 0) {
                return;
            }
            for (int i = 0; i < customViewList.size(); i++) {
                View view = customViewList.get(i);
                if (view == null) {
                    return;
                }
                TextView text = (TextView) view.findViewById(R.id.tab_item_text);
                View indicator = view.findViewById(R.id.tab_item_indicator);
                if (i == position) { // 选中状态
                    text.setTextColor(mSelectTextColor);
                    indicator.setBackgroundColor(mSelectIndicatorColor);
                    indicator.setVisibility(View.VISIBLE);
                } else {// 未选中状态
                    text.setTextColor(mUnSelectTextColor);
                    indicator.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    /**
     * 获取Tab 显示的内容
     */
    private View getTabView(Context context, String text, int indicatorWidth, int indicatorHeight, int textSize) {
        View view = View.inflate(context, R.layout.layout_stab_item, null);
        setIndicator(view, indicatorWidth, indicatorHeight);
        setTabText(view, text, textSize);
        return view;
    }

    /**
     * 设置指示器
     *
     * @param view            rootView
     * @param indicatorWidth  指示器宽度
     * @param indicatorHeight 指示器高度
     */
    private void setIndicator(View view, int indicatorWidth, int indicatorHeight) {
        if (indicatorWidth > 0) {//固定导航条宽度
            View indicator = view.findViewById(R.id.tab_item_indicator);
            ViewGroup.LayoutParams layoutParams = indicator.getLayoutParams();
            layoutParams.width = indicatorWidth;
            layoutParams.height = indicatorHeight;
            indicator.setLayoutParams(layoutParams);
        }
    }

    /**
     * 设置tab内容
     *
     * @param view     rootView
     * @param text     文本
     * @param textSize 文本字体大小
     */
    private void setTabText(View view, String text, int textSize) {
        TextView tabText = (TextView) view.findViewById(R.id.tab_item_text);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tabText.getLayoutParams();
        layoutParams.height = mTabHeight;
        layoutParams.setMargins(mTabMargin, 0, mTabMargin, 0);
        tabText.setLayoutParams(layoutParams);
        tabText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        tabText.setText(text);
    }

    private int sp2px(Context mContext, float sp) {
        final float scale = mContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * scale + 0.5f);
    }

    private int dp2px(Context mContext, float dp) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
