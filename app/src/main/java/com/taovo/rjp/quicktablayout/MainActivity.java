package com.taovo.rjp.quicktablayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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


        ArrayList<Tab> tabs2 = new ArrayList<>();
        tabs2.add(new Tab("新闻"));
        tabs2.add(new Tab("热点"));
        tabs2.add(new Tab("视频视频"));
        tabs2.add(new Tab("体育"));
        tabs2.add(new Tab("体育体育"));
        tabs2.add(new Tab("体育"));
        QuickTabLayout quickTabLayout2 = (QuickTabLayout) findViewById(R.id.quick_tab_layout2);
        quickTabLayout2.setTabMode(QuickTabLayout.TabMode.EQUANT);
        quickTabLayout2.setIndicatorMode(QuickTabLayout.IndicatorMode.EQUAL_CONTENT);
        quickTabLayout2.setTabs(tabs2);

        ArrayList<Tab> tabs3 = new ArrayList<>();
        tabs3.add(new Tab("新闻"));
        tabs3.add(new Tab("热点"));
        tabs3.add(new Tab("视频"));
        tabs3.add(new Tab("体育"));
        tabs3.add(new Tab("体育"));
        tabs3.add(new Tab("体育"));
        tabs3.add(new Tab("图片"));
        QuickTabLayout quickTabLayout3 = (QuickTabLayout) findViewById(R.id.quick_tab_layout3);
        quickTabLayout3.setTabMode(QuickTabLayout.TabMode.EQUANT);
        quickTabLayout3.setIndicatorMode(QuickTabLayout.IndicatorMode.EQUAL_VALUE);
        quickTabLayout3.setTabs(tabs3);

        ArrayList<Tab> tabs4 = new ArrayList<>();
        tabs4.add(new Tab("新闻新闻"));
        tabs4.add(new Tab("热点新"));
        tabs4.add(new Tab("视频新闻新闻"));
        tabs4.add(new Tab("体育"));
        tabs4.add(new Tab("图片新闻新闻新闻新闻"));
        tabs4.add(new Tab("图片新闻"));
        tabs4.add(new Tab("图片新闻新闻新闻新闻新闻新闻新闻"));
        tabs4.add(new Tab("图片新闻"));
        QuickTabLayout quickTabLayout4 = (QuickTabLayout) findViewById(R.id.quick_tab_layout4);
        quickTabLayout4.setTabMode(QuickTabLayout.TabMode.WRAPCONTENT);
        quickTabLayout4.setIndicatorMode(QuickTabLayout.IndicatorMode.EQUAL_TAB);
        quickTabLayout4.setTabs(tabs4);


        ArrayList<Tab> tabs5 = new ArrayList<>();
        tabs5.add(new Tab("新闻"));
        tabs5.add(new Tab("热点"));
        tabs5.add(new Tab("视频"));
        tabs5.add(new Tab("体育"));
        tabs5.add(new Tab("图片"));
        QuickTabLayout quickTabLayout5 = (QuickTabLayout) findViewById(R.id.quick_tab_layout5);
        quickTabLayout5.setTabMode(QuickTabLayout.TabMode.WRAPCONTENT);
        quickTabLayout5.setIndicatorMode(QuickTabLayout.IndicatorMode.EQUAL_CONTENT);
        quickTabLayout5.setTabs(tabs5);

        ArrayList<Tab> tabs6 = new ArrayList<>();
        tabs6.add(new Tab("新闻"));
        tabs6.add(new Tab("热点"));
        tabs6.add(new Tab("视频"));
        tabs6.add(new Tab("体育"));
        tabs6.add(new Tab("图片"));
        QuickTabLayout quickTabLayout6 = (QuickTabLayout) findViewById(R.id.quick_tab_layout6);
        quickTabLayout6.setTabMode(QuickTabLayout.TabMode.WRAPCONTENT);
        quickTabLayout6.setIndicatorMode(QuickTabLayout.IndicatorMode.EQUAL_VALUE);
        quickTabLayout6.setTabs(tabs6);

        ArrayList<Tab> tabs7 = new ArrayList<>();
        tabs7.add(new Tab("新闻"));
        tabs7.add(new Tab("热点"));
        tabs7.add(new Tab("视频"));
        tabs7.add(new Tab("体育"));
        tabs7.add(new Tab("图片"));
        tabs7.add(new Tab("图片"));
        tabs7.add(new Tab("图片"));
        tabs7.add(new Tab("图片"));
        QuickTabLayout quickTabLayout7 = (QuickTabLayout) findViewById(R.id.quick_tab_layout7);
        quickTabLayout7.setTabMode(QuickTabLayout.TabMode.EQUAL);
        quickTabLayout7.setIndicatorMode(QuickTabLayout.IndicatorMode.EQUAL_TAB);
        quickTabLayout7.setTabs(tabs7);


        ArrayList<Tab> tabs8 = new ArrayList<>();
        tabs8.add(new Tab("新闻"));
        tabs8.add(new Tab("热点"));
        tabs8.add(new Tab("视频"));
        tabs8.add(new Tab("体育"));
        tabs8.add(new Tab("图片"));
        tabs8.add(new Tab("图片"));
        tabs8.add(new Tab("图片"));
        QuickTabLayout quickTabLayout8 = (QuickTabLayout) findViewById(R.id.quick_tab_layout8);
        quickTabLayout8.setTabMode(QuickTabLayout.TabMode.EQUAL);
        quickTabLayout8.setIndicatorMode(QuickTabLayout.IndicatorMode.EQUAL_CONTENT);
        quickTabLayout8.setTabs(tabs8);

        ArrayList<Tab> tabs9 = new ArrayList<>();
        tabs9.add(new Tab("新闻"));
        tabs9.add(new Tab("热点"));
        tabs9.add(new Tab("视频"));
        tabs9.add(new Tab("体育"));
        tabs9.add(new Tab("体育"));
        tabs9.add(new Tab("体育"));
        tabs9.add(new Tab("体育"));
        tabs9.add(new Tab("图片"));
        QuickTabLayout quickTabLayout9 = (QuickTabLayout) findViewById(R.id.quick_tab_layout9);
        quickTabLayout9.setTabMode(QuickTabLayout.TabMode.EQUAL);
        quickTabLayout9.setIndicatorMode(QuickTabLayout.IndicatorMode.EQUAL_VALUE);
        quickTabLayout9.setTabs(tabs9);
    }
}
