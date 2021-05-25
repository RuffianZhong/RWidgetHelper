package com.ruffian.library.widget.helper;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CompoundButton;

/**
 * Check-Helper
 *
 * @author ZhongDaFeng
 */
public class RCheckHelper extends RTextViewHelper {

    public RCheckHelper(Context context, CompoundButton view, AttributeSet attrs) {
        super(context, view, attrs);
    }

    @Override
    protected boolean isCompoundButtonChecked() {
        if (mView != null) return ((CompoundButton) mView).isChecked();
        return false;
    }

}
