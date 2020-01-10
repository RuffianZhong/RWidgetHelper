package com.ruffian.library.demo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 示例FragmentCorner
 * 备注:各种圆角使用示例
 *
 * @author ZhongDaFeng
 */

public class FragmentCorner extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return LayoutInflater.from(getActivity()).inflate(R.layout.fmt_textview_corner, null);
    }
}
