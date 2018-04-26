package com.ruffian.library.demo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ruffian.library.widget.RTextView;

/**
 * 示例FragmentState
 * 备注:各种边框，背景状态使用示例
 *
 * @author ZhongDaFeng
 */

public class FragmentState extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fmt_textview_state, null);
        view.findViewById(R.id.text1).setOnClickListener(this);
        view.findViewById(R.id.text2).setOnClickListener(this);
        view.findViewById(R.id.text3).setOnClickListener(this);
        view.findViewById(R.id.text4).setOnClickListener(this);
        view.findViewById(R.id.text5).setOnClickListener(this);
        view.findViewById(R.id.text6).setOnClickListener(this);
        view.findViewById(R.id.text7).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
    }
}
