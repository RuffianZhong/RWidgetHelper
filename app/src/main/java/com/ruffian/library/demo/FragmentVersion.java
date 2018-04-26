package com.ruffian.library.demo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 示例FragmentCorner
 *
 * @author ZhongDaFeng
 */

public class FragmentVersion extends Fragment {

    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = LayoutInflater.from(getActivity()).inflate(R.layout.fmt_textview_version, null);
        return mView;
    }
}
