package com.ruffian.library.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioButton;

import com.ruffian.library.widget.helper.RCheckHelper;
import com.ruffian.library.widget.iface.RHelper;

/**
 * RRadioButton
 *
 * @author ZhongDaFeng
 */
public class RRadioButton extends RadioButton implements RHelper<RCheckHelper> {

    private RCheckHelper mHelper;

    public RRadioButton(Context context) {
        this(context, null);
    }

    public RRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHelper = new RCheckHelper(context, this, attrs);
    }

    @Override
    public RCheckHelper getHelper() {
        return mHelper;
    }

}
