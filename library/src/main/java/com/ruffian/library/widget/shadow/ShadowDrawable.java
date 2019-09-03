package com.ruffian.library.widget.shadow;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;

/**
 * Shadow Drawable
 *
 * @author ZhongDaFeng
 */
public class ShadowDrawable extends Drawable {

    private int mShadowColor;//阴影颜色
    private float mShadowRadius;//阴影半径
    private float mRoundRadii[];//矩形圆角半径
    private int mShadowDx;//阴影x偏移
    private int mShadowDy;//阴影y偏移

    private Path mPath;
    private Paint mPaint;
    private RectF mBoundsF;

    public ShadowDrawable() {
        mPath = new Path();
        mBoundsF = new RectF();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
    }

    public void updateParameter(int shadowColor, float shadowRadius, int shadowDx, int shadowDy, float roundRadii[]) {
        this.mShadowColor = shadowColor;
        this.mRoundRadii = roundRadii;
        this.mShadowRadius = shadowRadius;
        this.mShadowDx = shadowDx;
        this.mShadowDy = shadowDy;
        /**
         * 设置阴影
         */
        mPaint.setColor(mShadowColor);
        mPaint.setShadowLayer(mShadowRadius, mShadowDx, mShadowDy, mShadowColor);
    }


    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        if (bounds.right - bounds.left > 0 && bounds.bottom - bounds.top > 0) {
            updateBounds(bounds);
        }
    }

    /**
     * 更新Bounds
     *
     * @param bounds
     */
    private void updateBounds(Rect bounds) {
        if (bounds == null) bounds = getBounds();
        float offset = 1f;//存在边框时偏移兼容
        /**
         * 算法1 => 存在便宜的方向增加宽度 （效果不是很好）
         */
       /* float left = bounds.left + getShadowOffsetHalf() + offset;
        left = mShadowDx < 0 ? left + Math.abs(mShadowDx) : left;

        float right = bounds.right - getShadowOffsetHalf() - offset;
        right = mShadowDx > 0 ? right - Math.abs(mShadowDx) : right;

        float top = bounds.top + getShadowOffsetHalf() + offset;
        top = mShadowDy < 0 ? top + Math.abs(mShadowDy) : top;

        float bottom = bounds.bottom - getShadowOffsetHalf() - offset;
        bottom = mShadowDy > 0 ? bottom - Math.abs(mShadowDy) : bottom;*/

        /**
         * 算法2 => 水平/垂直存在偏移，对应方向增加间距
         */
        float left = bounds.left + getShadowOffsetHalf() + offset + Math.abs(mShadowDx);
        float right = bounds.right - getShadowOffsetHalf() - offset - Math.abs(mShadowDx);
        float top = bounds.top + getShadowOffsetHalf() + offset + Math.abs(mShadowDy);
        float bottom = bounds.bottom - getShadowOffsetHalf() - offset - Math.abs(mShadowDy);
        mBoundsF.set(left, top, right, bottom);
        mPath.addRoundRect(mBoundsF, mRoundRadii, Path.Direction.CW);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }


    public float getShadowOffset() {
        return getShadowOffsetHalf() * 2f;
    }

    public float getShadowOffsetHalf() {
       // return 0 >= mShadowRadius ? 0 :Math.max( Math.abs(mShadowDx),  Math.abs(mShadowDy)) + mShadowRadius;
        return mShadowRadius;
    }
}
