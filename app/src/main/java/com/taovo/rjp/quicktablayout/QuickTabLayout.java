package com.taovo.rjp.quicktablayout;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import static android.R.attr.scrollY;
import static com.taovo.rjp.quicktablayout.QuickTabLayout.IndicatorMode.EQUAL_TAB;
import static com.taovo.rjp.quicktablayout.QuickTabLayout.TabMode.EQUANT;
import static com.taovo.rjp.quicktablayout.QuickTabLayout.TabMode.WRAPCONTENT;

/**
 * @author Gimpo create on 2017/9/1 14:46
 * @email : jimbo922@163.com
 */

public class QuickTabLayout extends LinearLayout {

    private List<Tab> mTabs;
    private LinearLayout llTabContainer;
    private View indicatorView;
    private int screenWidth;
    /**
     * 默认等分屏幕
     */
    private TabMode tabMode = EQUANT;
    /**
     * 默认和tab一样宽
     */
    private IndicatorMode indicatorMode = EQUAL_TAB;
    private Context mContext;
    private int tabHeight;
    private int tabWidth;
    private int indicatorHeight;
    private int indicatorWidth;
    private int txtSelectedColor;
    private int txtUnselectedColor;
    private int indicatorColor;
    private int txtSize;
    private int selectedIndex = 0;
    private MyHorizontalScrollView horizontalScrollView;

    public QuickTabLayout(Context context) {
        super(context);
        initView(context, null);
    }

    public QuickTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public QuickTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.layout_quick_tab_layout, this);
        horizontalScrollView = (MyHorizontalScrollView) findViewById(R.id.horizontal_scroll_view);
        llTabContainer = (LinearLayout) findViewById(R.id.ll_tab_container);
        indicatorView = findViewById(R.id.indicator);
        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        horizontalScrollView.setOnHorizontalScrollListener(new MyHorizontalScrollView.OnHorizontalScrollListener() {
            @Override
            public void onScroll(int scrollX, int scrollY) {
                LinearLayout.LayoutParams layoutParams = (LayoutParams) indicatorView.getLayoutParams();
                Tab tab = mTabs.get(selectedIndex);
                switch (indicatorMode) {
                    case EQUAL_TAB:
                        layoutParams.setMargins(tab.getTabLeft() - scrollX, 0, 0, 0);
                        break;
                    case EQUAL_CONTENT:
                    case EQUAL_VALUE:
                        layoutParams.setMargins(tab.getIndicatorLeft() - scrollX, 0, 0, 0);
                        break;
                }
                indicatorView.setLayoutParams(layoutParams);
            }
        });

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.QuickTabLayout);
            tabHeight = (int) a.getDimension(R.styleable.QuickTabLayout_tabHeight, dp2px(mContext, 42));
            tabWidth = (int) a.getDimension(R.styleable.QuickTabLayout_tabWidth, dp2px(mContext, 80));
            indicatorHeight = (int) a.getDimension(R.styleable.QuickTabLayout_indicatorHeight, dp2px(mContext, 2));
            indicatorWidth = (int) a.getDimension(R.styleable.QuickTabLayout_indicatorWidth, 1);
            txtSelectedColor = a.getColor(R.styleable.QuickTabLayout_txtSelectedColor, Color.RED);
            txtUnselectedColor = a.getColor(R.styleable.QuickTabLayout_txtUnselectedColor, Color.GRAY);
            indicatorColor = a.getColor(R.styleable.QuickTabLayout_indicatorColor, Color.RED);
            txtSize = a.getInteger(R.styleable.QuickTabLayout_txtSize, 14);
        }
    }

    /**
     * 设置tab
     *
     * @param tabs
     */
    public void setTabs(List<Tab> tabs) {
        mTabs = tabs;
        selectedIndex = 0;
        llTabContainer.removeAllViews();
        initTabs(tabs);
        addTabInContainer();
        setSelectState();
    }

    /**
     * 添加Tab进容器
     */
    private void addTabInContainer() {
        int tabCount = mTabs.size();
        int textViewWidth = 0;
        switch (tabMode) {
            case EQUANT: //等分的情况下，过长或者tab过多都不去变换mode，有可能造成死循环
                textViewWidth = screenWidth / tabCount;
                for (int i = 0; i < tabCount; i++) {
                    Tab tab = mTabs.get(i);
                    tab.setTabWidth(textViewWidth);
                    llTabContainer.addView(tab.getTextView());
                    tab.setTabLeft(i * textViewWidth);
                    tab.setIndicatorLeft((i * textViewWidth + ((textViewWidth - tab.getIndicatorWidth()) / 2)));
                }
                break;
            case WRAPCONTENT:
                int tabLeft = 0;
                for (int i = 0; i < tabCount; i++) {
                    Tab tab = mTabs.get(i);
                    float titleWidth = tab.getIndicatorWidth();
                    textViewWidth = (int) (titleWidth + dp2px(mContext, 20));
                    tab.setTabWidth(textViewWidth);
                    llTabContainer.addView(tab.getTextView());
                    tab.setTabLeft(tabLeft);
                    tab.setIndicatorLeft((tabLeft + ((textViewWidth - tab.getIndicatorWidth()) / 2)));
                    tabLeft += textViewWidth;
                }
                if (checkTabTotalWidth()) {
                    setTabs(mTabs);
                    return;
                }
                break;
            case EQUAL:
                textViewWidth = tabWidth;
                for (int i = 0; i < tabCount; i++) {
                    Tab tab = mTabs.get(i);
                    tab.setTabWidth(textViewWidth);
                    llTabContainer.addView(tab.getTextView());
                    tab.setTabLeft(i * textViewWidth);
                    tab.setIndicatorLeft((i * textViewWidth + ((textViewWidth - tab.getIndicatorWidth()) / 2)));
                }
                if (checkTabTotalWidth()) {
                    setTabs(mTabs);
                    return;
                }
                break;
        }
    }

    /**
     * 检查总长度
     *
     * @return
     */
    private boolean checkTabTotalWidth() {
        int totalWidth = 0;
        for (Tab tab : mTabs) {
            totalWidth += tab.getTabWidth();
        }
        if (totalWidth < screenWidth) {
            tabMode = EQUANT;
            return true;
        }
        return false;
    }

    /**
     * 选中tab的一系列操作
     */
    private void setSelectState() {
        Tab tab = mTabs.get(selectedIndex);
        TextView selectedTextView = tab.getTextView();
        selectedTextView.setTextColor(txtSelectedColor);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) indicatorView.getLayoutParams();
        switch (indicatorMode) {
            case EQUAL_TAB:
                layoutParams.width = tab.getTabWidth();
                layoutParams.height = indicatorHeight;
                layoutParams.setMargins(tab.getTabLeft(), 0, 0, 0);
                break;
            case EQUAL_CONTENT:
                layoutParams.width = tab.getIndicatorWidth();
                layoutParams.height = indicatorHeight;
                layoutParams.setMargins(tab.getIndicatorLeft(), 0, 0, 0);
                break;
            case EQUAL_VALUE:
                if (indicatorWidth > tab.getTabWidth()) {
                    indicatorWidth = tab.getTabWidth();
                }
                layoutParams.width = indicatorWidth;
                layoutParams.height = indicatorHeight;
                layoutParams.setMargins(tab.getIndicatorLeft(), 0, 0, 0);
                break;
        }
        indicatorView.setLayoutParams(layoutParams);
    }

    /**
     * 首先初始化所有的tab
     *
     * @param tabs
     * @return
     */
    private void initTabs(List<Tab> tabs) {
        int size = tabs.size();
        Paint paint = new Paint();
        for (int i = 0; i < size; i++) {
            Tab tab = tabs.get(i);
            TextView textView = new TextView(mContext);
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, tabHeight);
            textView.setLayoutParams(params);
            textView.setGravity(Gravity.CENTER);
            textView.setTextSize(txtSize);
            textView.setTextColor(txtUnselectedColor);
            textView.setText(tab.getTitle());
            textView.setTag(i);
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Tab preTab = mTabs.get(selectedIndex);
                    TextView preTextView = preTab.getTextView();
                    preTextView.setTextColor(txtUnselectedColor);
                    selectedIndex = (Integer) v.getTag();
                    setSelectState();
                    indicatorAnim(mTabs.get(selectedIndex), preTab);
                }
            });
            switch (indicatorMode) {
                case EQUAL_TAB:
                case EQUAL_CONTENT:
                    paint.setTextSize(textView.getTextSize());
                    float titleWidth = paint.measureText(textView.getText().toString());
                    tab.setIndicatorWidth((int) titleWidth);
                    break;
                case EQUAL_VALUE:
                    tab.setIndicatorWidth(indicatorWidth);
                    break;
            }

            tab.setTextView(textView);
        }
    }

    /**
     * 检查是否需要滑动
     */
    private void checkScroll() {
        View view = llTabContainer.getChildAt(selectedIndex);
        int left = view.getLeft();
        if (left > screenWidth / 2) {
            horizontalScrollView.smoothScrollTo(left - screenWidth / 2, 0);
        } else {
            horizontalScrollView.smoothScrollTo(0, 0);
        }
    }

    /**
     * 下面小角标的动画
     */
    private void indicatorAnim(Tab currentTab, Tab previousTab) {
        int scrollX = horizontalScrollView.getScrollX();
        int startValue = 0, endValue = 0;
        int startWidth = 0, endWidth = 0;

        switch (indicatorMode) {
            case EQUAL_TAB:
                startValue = previousTab.getTabLeft() - scrollX;
                endValue = currentTab.getTabLeft() - scrollX;
                startWidth = previousTab.getTabWidth();
                endWidth = currentTab.getTabWidth();
                break;
            case EQUAL_CONTENT:
            case EQUAL_VALUE:
                startValue = previousTab.getIndicatorLeft() - scrollX;
                endValue = currentTab.getIndicatorLeft() - scrollX;
                startWidth = previousTab.getIndicatorWidth();
                endWidth = currentTab.getIndicatorWidth();
                break;
        }
        ObjectAnimator anim1 = ObjectAnimator.ofInt(indicatorView, "rjp-left", startValue, endValue).setDuration(300);
        ObjectAnimator anim2 = ObjectAnimator.ofInt(indicatorView, "rjp-width", startWidth, endWidth).setDuration(300);
        anim1.start();
        anim2.start();
        anim1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) indicatorView.getLayoutParams();
                layoutParams.setMargins((Integer) animation.getAnimatedValue(), 0, 0, 0);
                indicatorView.setLayoutParams(layoutParams);
            }
        });
        anim2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) indicatorView.getLayoutParams();
                int width = (Integer) animation.getAnimatedValue();
                layoutParams.width = width;
                indicatorView.setLayoutParams(layoutParams);
            }
        });
        anim1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                checkScroll();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public void setIndicatorMode(IndicatorMode indicatorMode) {
        this.indicatorMode = indicatorMode;
    }

    public void setTabMode(TabMode tabMode) {
        this.tabMode = tabMode;
    }

    public enum TabMode {
        /**
         * 等分的
         */
        EQUANT,
        /**
         * 适应的
         */
        WRAPCONTENT,
        /**
         * 相等的  设定值
         */
        EQUAL
    }

    public enum IndicatorMode {
        /**
         * 和tab等宽的
         */
        EQUAL_TAB,
        /**
         * 和内容等宽的
         */
        EQUAL_CONTENT,
        /**
         * 设定值
         */
        EQUAL_VALUE
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param context 上下文
     * @param dpValue 单位dip的数据
     */
    public int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     */
    public int sp2px(Context context, float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
