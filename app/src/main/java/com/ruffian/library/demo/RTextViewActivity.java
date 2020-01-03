package com.ruffian.library.demo;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ZhongDaFeng
 */
public class RTextViewActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_textview_main);
        initViews();
        initData();
    }


    private void initData() {

        mFragmentList.add(new FragmentCorner());
        mFragmentList.add(new FragmentState());
        mFragmentList.add(new FragmentVersion());
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mFragmentList.size();
            }

            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }
        };

        mViewPager.setAdapter(mAdapter);
    }

    private void initViews() {
        mViewPager = (ViewPager) findViewById(R.id.vp_content);
    }


}
