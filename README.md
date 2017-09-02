### 一、先看效果

![QuickTabLayout.gif](http://upload-images.jianshu.io/upload_images/5994029-08ca1a471a3fe263.gif?imageMogr2/auto-orient/strip)

### 二、需求
看上图我们可以看到九种组合，要实现这种，首先看Tab的样式：
**1. 均分手机屏幕；**
**2. wrap_content，也就是等于title的长度；**
**3. 自定义宽度，也就是设定一个值；**
**4. 可以是一个图标（目前没有实现，但是这种需求基本没有）。**
再观察indicator的样式：
**1. 等于Tab的宽度；**
**2. 等于title的宽度；**
**3. 自定义宽度，也就是设定一个值。**
有了这些mode，可以写两个枚举类：
```
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

```
### 三、代码分析
#### 3.1 布局
```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
              android:orientation="vertical"
              android:layout_width="match_parent"
              android:layout_height="match_parent">
    <com.taovo.rjp.quicktablayout.MyHorizontalScrollView
        android:id="@+id/horizontal_scroll_view"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:scrollbars="none"
        >
        <LinearLayout
            android:id="@+id/ll_tab_container"
            android:layout_width="wrap_content"
            android:layout_height="match_parent"
            android:orientation="horizontal"
            >

        </LinearLayout>
    </com.taovo.rjp.quicktablayout.MyHorizontalScrollView>

    <View
        android:id="@+id/indicator"
        android:layout_width="1px"
        android:layout_height="1px"
        android:background="#f00"
        />
</LinearLayout>
```
我们需要用一个自定义的HorizontalScrollView，为什么？因为我们要监听滑动事件，但是HorizontalScrollView没有提供，需要自己去实现，这个代码很多，不细说。然后里面包裹一个LinearLayout作为Tab的容器，下面一个1px * 1px的View就是我们的indicator，为什么要1px的长宽？因为实际操作中发现写wrap_content，后来会造成indicator在界面无法显示的问题。
#### 3.2 自定义属性
```
<declare-styleable name="QuickTabLayout">
        <attr name="tabHeight" format="dimension" />
        <attr name="indicatorHeight" format="dimension" />
        <attr name="indicatorWidth" format="dimension" />
        <attr name="tabWidth" format="dimension" />
        <attr name="txtSelectedColor" format="color" />
        <attr name="txtUnselectedColor" format="color" />
        <attr name="indicatorColor" format="color" />
        <attr name="txtSize" format="integer" />
    </declare-styleable>
```
可以按照需求增加，我只添加了经常需要设置的，比如Tab的高度和宽度，indicator的高度和宽度，选中和未选中的颜色，indicator的颜色，tab文字的大小，还有需要的可以告诉我。
#### 3.3 定义Tab
```
public class Tab {
    private String title;
    private TextView textView;

    private int tabLeft;
    private int tabWidth;

    private int indicatorLeft;
    private int indicatorWidth;
}
```
记录title，为什么要记录textView呢？可能你会疑问，这是为了省事，因为我发现除了记录tab需要一个list，记录textView还需要一个list，这样tab和textView就绑定了，只需要遍历一个list。还需要记录tab的宽度和距离父布局的left，同时indicator的宽度和距离父布局的left也需要记录。一开始肯定想不到记录的这么全，所以这个类是慢慢补全的。
#### 3.4 初始化Tab
```
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
```
很常见的写法，但是下面需要**根据indicator的样式去设置indicator的宽度** 。这里很重要。
#### 3.5 添加Tab到容器里
```
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
                    tab.setIndicatorLeft(i * textViewWidth + ((textViewWidth - tab.getIndicatorWidth()) / 2));
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
```
这一步是**重中之重**，在添加的时候我们不但需要计算Tab的宽度和Tab距离父布局left的距离，同时还要计算indicator的宽度和距父布局left的距离。所以这里需要根据TabMode去差异化计算：
1. EQUANT下，TabWidth是screenWidth除以tabCount，TabLeft就等于 i * TabWidth，IndicatorLeft等于TabLeft加上(TabWidth- IndicatorWidth) / 2)，这里需要思考下为什么？画个图就很清楚了；
2. WRAPCONTENT下，TabWidth我们默认是内容的宽度再加上左右padding，这个padding这里是写死的10dp，可以改成动态的。TabLeft的话需要手动计算，后一个的TabLeft是前一个TabLeft加上前一个TabWidth，IndicatorLeft仍然是TabLeft加上(TabWidth- IndicatorWidth) / 2)；
3. EQUAL下， tabWidth是设定的，一开始传递的，没有设置默认80dp，tabLeft等于 i * TabWidth，IndicatorLeft仍然是TabLeft加上(TabWidth- IndicatorWidth) / 2)。
可能有人注意到WRAPCONTENT和EQUAL下最后还有checkTabTotalWidth()方法，这个方法什么时候触发呢？
```
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
```
当总长度小于screenWidth的时候，就是说，tabMode是WRAPCONTENT或者EQUAL的时候，所有tab加起来总长度还不够一屏幕，这个时候将转换tabMode为EQUANT，均分屏幕的长度，充满屏幕，更加美观。这是一个小技巧。当然返回true之后重新setTabs(mTabs)。
#### 3.6 初始化选中状态
```
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
```
我们有一个selectedIndex记录选中的下标，所以这个方法可以初始化的时候调用，在点击Tab的时候仍然可以调用，更改Tab的状态和indicator的状态。根据indicatorMode设置：
1. EQUAL_TAB下，indicator的宽度等于TabWidth，高度布局文件里指定默认2dp，然后是位置，通过MarginLeft设置，left等于TabLeft；
2. EQUAL_CONTENT下，indicator的宽度等于文字宽度，记录在Tab里，MarginLeft记录在indicatorLeft；
3. EQUAL_VALUE下，需要有一个判断，如果用户设置的太长，让indicatorWidth等于TabWidth，其他同上。
#### 3.7 监听滑动
```
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
```
滑动的时候Tab是随着滑动的，但是indicator不是，所以需要手动设置indicator的MarginLeft，也是根据indicatorMode去判断滑动多少距离：
1. EQUAL_TAB下，滑动tabLeft减去horizontalScrollView的scrollX，为什么减去，可以看第一个Tab的时候，假设滑出去第一个Tab的距离，那么第一个Tab的indicator应该距左-TabWidth，第一个Tab的TabLeft等于0，这个时候scrollX等于TabWidth，所以是0减去TabWidth，通用的也就是tab.getTabLeft() - scrollX；
2. EQUAL_CONTENT和EQUAL_VALUE下，道理和上面的类似，不细说了，总结是tab.getIndicatorLeft() - scrollX。
#### 3.8 点击Tab的时候操作
```
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
```
先将之前的preTab恢复，再设置选中状态setSelectState()，最后indicator加上一个动画效果：
```
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
```
动画有两部分，一部分是位置的动画，还有一个是长度的动画。拆分开来，使用属性动画，也是根据indicatorMode来区分：
1. EQUAL_TAB的时候，位置动画起始值是previousTabLeft到currentTabLeft，长度动画起始值就是previousTabWidth到currentTabWidth；
2. EQUAL_CONTENT和EQUAL_VALUE的时候，位置动画起始值是previousIndicatorLeft到currentIndicatorLeft，长度动画起始值就是previousIndicatorWidth到currentIndicatorWidth。
#### 3.9 检查是否需要滚动
我们点击的如果是已经有一部分超出屏幕的Tab，需要滚动到屏幕中间，方便用户操作：
```
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
```
这个方法在indicator的动画结束之后调用，否则会导致indicator错位。到这整个TabLayout就分析结束了，我给它取名字**QuickTabLayout**，因为真的很方便的调用和智能的选择TabMode，不信可以看demo。
### 四、案例
#### 4.1 布局
```
<com.taovo.rjp.quicktablayout.QuickTabLayout
        android:id="@+id/quick_tab_layout1"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        >

    </com.taovo.rjp.quicktablayout.QuickTabLayout>
```
#### 4.2 activity代码
```
        ArrayList<Tab> tabs1 = new ArrayList<>();
        tabs1.add(new Tab("新闻新闻"));
        tabs1.add(new Tab("热点"));
        tabs1.add(new Tab("视频视频"));
        tabs1.add(new Tab("体育"));
        tabs1.add(new Tab("图片图片"));
        QuickTabLayout quickTabLayout1 = (QuickTabLayout) findViewById(R.id.quick_tab_layout1);
        quickTabLayout1.setTabMode(QuickTabLayout.TabMode.EQUANT);
        quickTabLayout1.setIndicatorMode(QuickTabLayout.IndicatorMode.EQUAL_TAB);
        quickTabLayout1.setTabs(tabs1);
```
设置的Tab均分屏幕，Indicator等于Tab的宽度，效果截图如下：

![image.png](http://upload-images.jianshu.io/upload_images/5994029-932358ffc72d270b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

附上 [简书](http://www.jianshu.com/p/5791f1f5391b) 地址，感兴趣可以和我肛一波。