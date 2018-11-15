package com.ruffian.library.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.CheckBox;
import android.widget.RadioButton;

import com.ruffian.library.widget.helper.RCheckHelper;
import com.ruffian.library.widget.helper.RTextViewHelper;
import com.ruffian.library.widget.iface.RHelper;

/**
 * RCheckBox
 *
 * @author ZhongDaFeng
 */
public class RCheckBox extends CheckBox implements RHelper<RCheckHelper> {

    private RCheckHelper mHelper;

    public RCheckBox(Context context) {
        this(context, null);
    }

    public RCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHelper = new RCheckHelper(context, this, attrs);
    }

    @Override
    public RCheckHelper getHelper() {
        return mHelper;
    }

}
