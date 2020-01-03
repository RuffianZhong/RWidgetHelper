package com.ruffian.library.widget;

import android.content.Context;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.util.AttributeSet;

import com.ruffian.library.widget.helper.RBaseHelper;
import com.ruffian.library.widget.iface.RHelper;

/**
 * RConstraintLayout
 *
 * @author ZhongDaFeng
 */
public class RConstraintLayout extends ConstraintLayout implements RHelper<RBaseHelper> {

    private RBaseHelper mHelper;

    public RConstraintLayout(Context context) {
        this(context, null);
    }

    public RConstraintLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHelper = new RBaseHelper(context, this, attrs);
    }

    @Override
    public RBaseHelper getHelper() {
        return mHelper;
    }
}
