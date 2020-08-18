package com.ruffian.library.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.ruffian.library.widget.helper.RBaseHelper;
import com.ruffian.library.widget.iface.RHelper;

/**
 * RLinearLayout
 *
 * @author ZhongDaFeng
 */
public class RLinearLayout extends LinearLayout implements RHelper<RBaseHelper> {

    private RBaseHelper mHelper;

    public RLinearLayout(Context context) {
        this(context, null);
    }

    public RLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHelper = new RBaseHelper(context, this, attrs);
    }

    @Override
    public RBaseHelper getHelper() {
        return mHelper;
    }

    @Override
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        mHelper.dispatchDraw(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mHelper.onLayout(changed, left, top, right, bottom);
    }
}
