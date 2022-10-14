package com.ruffian.library.widget.shadow;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;

import com.ruffian.library.widget.utils.ReflectUtils;

import java.util.Arrays;

/**
 * ShadowBitmapDrawable
 *
 * @author ZhongDaFeng
 */
public class ShadowBitmapDrawable extends BitmapDrawable {


    private int mShadowColor;//阴影颜色
    private float mShadowRadius;//阴影半径
    private float[] mRoundRadii;//矩形圆角半径
    private int mShadowDx;//阴影x偏移
    private int mShadowDy;//阴影y偏移

    private RectF mBoundsF;

    public ShadowBitmapDrawable() {
        mBoundsF = new RectF();
    }

    /**
     * RBaseHelp中属性修改时调用
     *
     * @param shadowColor
     * @param shadowRadius
     * @param shadowDx
     * @param shadowDy
     * @param roundRadii
     */
    public void updateParameter(int shadowColor, float shadowRadius, int shadowDx, int shadowDy, float[] roundRadii) {
        boolean needUpdate = false;
        if (mShadowColor != shadowColor || mShadowRadius != shadowRadius
                || mShadowDx != shadowDx || mShadowDy != shadowDy
                || !Arrays.equals(mRoundRadii, roundRadii)) {
            needUpdate = true;
        }
        this.mShadowColor = shadowColor;
        this.mRoundRadii = roundRadii;
        this.mShadowRadius = shadowRadius;
        this.mShadowDx = shadowDx;
        this.mShadowDy = shadowDy;

        if (needUpdate) {
            //重新创建bitmap
            Bitmap bitmap = makeShadowBitmap((int) mBoundsF.width(), (int) mBoundsF.height(), mShadowRadius, mShadowDx, mShadowDy, mShadowColor, mRoundRadii);

            //反射调用函数 setBitmap 更新阴影图片
            ReflectUtils.invokeMethod(this, "setBitmap", new Class[]{Bitmap.class}, new Object[]{bitmap});
        }


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

        float left = bounds.left + mShadowRadius + Math.abs(mShadowDx);
        float right = bounds.right - mShadowRadius - Math.abs(mShadowDx);
        float top = bounds.top + mShadowRadius + Math.abs(mShadowDy);
        float bottom = bounds.bottom - mShadowRadius - Math.abs(mShadowDy);

        mBoundsF.set(left, top, right, bottom);

        //重新创建bitmap(不等高列表复用时可能会出现内存抖动，待优化)
        Bitmap bitmap = makeShadowBitmap((int) mBoundsF.width(), (int) mBoundsF.height(), mShadowRadius, mShadowDx, mShadowDy, mShadowColor, mRoundRadii);

        //反射调用函数 setBitmap 更新阴影图片
        ReflectUtils.invokeMethod(this, "setBitmap", new Class[]{Bitmap.class}, new Object[]{bitmap});

    }


    /**
     * 创建阴影Bitmap
     *
     * @param shadowWidth
     * @param shadowHeight
     * @param shadowRadius
     * @param dx
     * @param dy
     * @param shadowColor
     * @param radii
     * @return
     */
    public Bitmap makeShadowBitmap(int shadowWidth, int shadowHeight, float shadowRadius,
                                   float dx, float dy, int shadowColor, float[] radii) {

        //容错处理
        if (shadowWidth <= 0 || shadowHeight <= 0)
            return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_4444);


        //阴影bitmap
        Bitmap output = Bitmap.createBitmap(shadowWidth, shadowHeight, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(output);

        RectF shadowRect = new RectF(shadowRadius, shadowRadius, shadowWidth - shadowRadius, shadowHeight - shadowRadius);


        //存在偏移时重新计算阴影矩形大小
        shadowRect.top += Math.abs(dy);
        shadowRect.bottom -= Math.abs(dy);
        shadowRect.left += Math.abs(dx);
        shadowRect.right -= Math.abs(dx);

        //阴影画笔
        Paint shadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        shadowPaint.setColor(shadowColor);
        shadowPaint.setShadowLayer(shadowRadius, dx, dy, shadowColor);

        //路径
        Path path = new Path();
        path.addRoundRect(shadowRect, radii, Path.Direction.CW);
        canvas.drawPath(path, shadowPaint);

        return output;
    }


}
