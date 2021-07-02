package com.ruffian.library.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatTextView;

import com.ruffian.library.widget.helper.RTextViewHelper;
import com.ruffian.library.widget.iface.RHelper;

/**
 * RTextView
 *
 * @author ZhongDaFeng
 */
public class RTextView extends AppCompatTextView implements RHelper<RTextViewHelper> {

    private RTextViewHelper mHelper;

    public RTextView(Context context) {
        this(context, null);
    }

    public RTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHelper = new RTextViewHelper(context, this, attrs);
    }

    @Override
    public RTextViewHelper getHelper() {
        return mHelper;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (mHelper != null) mHelper.setEnabled(enabled);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mHelper != null) mHelper.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public void setSelected(boolean selected) {
        if (mHelper != null) mHelper.setSelected(selected);
        super.setSelected(selected);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mHelper != null) mHelper.drawIconWithText();
    }


}
