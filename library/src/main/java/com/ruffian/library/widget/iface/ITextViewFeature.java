package com.ruffian.library.widget.iface;

import android.view.MotionEvent;
import android.view.View;

/**
 * TextView特性功能接口
 *
 * @author ZhongDaFeng
 */
public interface ITextViewFeature {
    public void setEnabled(boolean enabled);

    public void onTouchEvent(MotionEvent event);

    public void setSelected(boolean selected);

    public void onVisibilityChanged(View changedView, int visibility);

}