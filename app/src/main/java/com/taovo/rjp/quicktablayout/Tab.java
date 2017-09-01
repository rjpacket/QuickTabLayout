package com.taovo.rjp.quicktablayout;

import android.widget.TextView;

/**
 * @author Gimpo create on 2017/9/1 14:58
 * @email : jimbo922@163.com
 */

public class Tab {
    private String title;
    private TextView textView;

    private int tabLeft;
    private int tabWidth;

    private int indicatorLeft;
    private int indicatorWidth;

    public Tab(String title){
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTabLeft() {
        return tabLeft;
    }

    public void setTabLeft(int tabLeft) {
        this.tabLeft = tabLeft;
    }

    public int getIndicatorLeft() {
        return indicatorLeft;
    }

    public void setIndicatorLeft(int indicatorLeft) {
        this.indicatorLeft = indicatorLeft;
    }

    public int getIndicatorWidth() {
        return indicatorWidth;
    }

    public void setIndicatorWidth(int indicatorWidth) {
        this.indicatorWidth = indicatorWidth;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public int getTabWidth() {
        return tabWidth;
    }

    public void setTabWidth(int tabWidth) {
        this.tabWidth = tabWidth;
        textView.setWidth(tabWidth);
    }
}
