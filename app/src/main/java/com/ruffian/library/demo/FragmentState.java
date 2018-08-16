package com.ruffian.library.demo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 示例FragmentState
 * 备注:各种边框，背景状态使用示例
 *
 * @author ZhongDaFeng
 */

public class FragmentState extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fmt_textview_state, null);
        return view;
    }

}
