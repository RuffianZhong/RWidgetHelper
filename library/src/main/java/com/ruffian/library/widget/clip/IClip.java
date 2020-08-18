package com.ruffian.library.widget.clip;

import android.graphics.Canvas;

/**
 * IClip
 *
 * @author ZhongDaFeng
 */
public interface IClip {
    
    public void dispatchDraw(Canvas canvas);

    public void onLayout(boolean changed, int left, int top, int right, int bottom);
}
