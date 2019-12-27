package com.ruffian.library.widget.rounded;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

/**
 * Round Drawable
 *
 * @author ZhongDaFeng
 */
public class RoundDrawable extends Drawable {


    private Bitmap mBitmap;
    private final int mBitmapWidth;
    private final int mBitmapHeight;
    private final RectF mBitmapRect = new RectF();
    //Bitmap
    private final Paint mBitmapPaint;
    //Border
    private final Paint mBorderPaint;
    private float mBorderWidth = 00;
    private int mBorderColor = Color.RED;
    private final RectF mBorderRect = new RectF();

    private final RectF mBounds = new RectF();
    private ImageView.ScaleType mScaleType = ImageView.ScaleType.CENTER_INSIDE;

    private final Matrix mShaderMatrix = new Matrix();

    private final RectF mDrawableRect = new RectF();

    private boolean mRebuildShader = true;
    private boolean mCircle = false;//是否圆形

    //圆角
    private float mCorner = -1;
    private float mCornerTopLeft = 30;
    private float mCornerTopRight = 30;
    private float mCornerBottomLeft = 30;
    private float mCornerBottomRight = 30;
    private float mCornerRadii[] = new float[8];
    private Path mPath = new Path();
    private RectF mRectF = new RectF();

    public RoundDrawable(Bitmap bitmap) {
        mBitmap = bitmap;
        mBitmapWidth = bitmap.getWidth();
        mBitmapHeight = bitmap.getHeight();
        mBitmapRect.set(0, 0, mBitmapWidth, mBitmapHeight);

        mBitmapPaint = new Paint();
        mBitmapPaint.setStyle(Paint.Style.FILL);
        mBitmapPaint.setAntiAlias(true);

        mBorderPaint = new Paint();
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStrokeWidth(mBorderWidth);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        Log.e("tag", "===================onBoundsChange");
        mBounds.set(bounds);

        //边框矩形
        mBorderRect.set(mBounds);

        updateShaderMatrix();

        updateRound();
    }

    private void updateRound() {
        updateConner();//更新圆角
        updatePath();//更新Path
    }

    private void updateConner() {
        if (mCorner >= 0) {
            for (int i = 0; i < mCornerRadii.length; i++) {
                mCornerRadii[i] = mCorner;
            }
            return;
        }

        if (mCorner < 0) {
            mCornerRadii[0] = mCornerTopLeft;
            mCornerRadii[1] = mCornerTopLeft;
            mCornerRadii[2] = mCornerTopRight;
            mCornerRadii[3] = mCornerTopRight;
            mCornerRadii[4] = mCornerBottomRight;
            mCornerRadii[5] = mCornerBottomRight;
            mCornerRadii[6] = mCornerBottomLeft;
            mCornerRadii[7] = mCornerBottomLeft;
            return;
        }
    }

    private void updatePath() {
        mPath.reset();//must重置
        mPath.addRoundRect(mDrawableRect, mCornerRadii, Path.Direction.CCW);
    }

    @Override
    public void setAlpha(int alpha) {
        mBitmapPaint.setAlpha(alpha);
        invalidateSelf();
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mBitmapPaint.setColorFilter(colorFilter);
        invalidateSelf();
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }


    @Override
    public void draw(@NonNull Canvas canvas) {

        if (mRebuildShader) {
            BitmapShader bitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            bitmapShader.setLocalMatrix(mShaderMatrix);// Shader.TileMode.CLAMP
            mBitmapPaint.setShader(bitmapShader);
            mRebuildShader = false;
        }

        if (mCircle) {

            // mDrawableRect 宽高是实际显示图片的宽高，类似于 marginTop 了  mDrawableRect.left
            float cx = mDrawableRect.width() / 2f + mDrawableRect.left;
            float cy = mDrawableRect.height() / 2f + mDrawableRect.top;

            float radiusX = mDrawableRect.width() / 2f;//- mBorderWidth;
            float radiusY = mDrawableRect.height() / 2f;//- mBorderWidth;

            float radiusDrawable = Math.min(radiusX, radiusY);
            float radiusBitmap = Math.min(mBitmapHeight, mBitmapWidth);
            float radius = Math.min(radiusBitmap, radiusDrawable);

            canvas.drawCircle(cx, cy, radius, mBitmapPaint);

            if (mBorderWidth > 0) {
                cx = mBorderRect.width() / 2f + mBorderRect.left;
                cy = mBorderRect.height() / 2f + mBorderRect.top;

                radiusX = mBorderRect.width() / 2f;//- mBorderWidth;
                radiusY = mBorderRect.height() / 2f;// - mBorderWidth;

                radiusDrawable = Math.min(radiusX, radiusY);
                radiusBitmap = Math.min(mBitmapHeight, mBitmapWidth);
                radius = Math.min(radiusBitmap, radiusDrawable);
                 canvas.drawCircle(cx, cy, radius, mBorderPaint);
            }
        } else {
            mPath.reset();//must重置
            mPath.addRoundRect(mDrawableRect, mCornerRadii, Path.Direction.CCW);
            Log.e("tag", "======DrawableRect==width:" + mDrawableRect.width() + "  height: " + mDrawableRect.height());
            canvas.drawPath(mPath, mBitmapPaint);
            if (mBorderWidth > 0) {
                mPath.reset();
                mPath.addRoundRect(mBorderRect, mCornerRadii, Path.Direction.CCW);
                  canvas.drawPath(mPath, mBorderPaint);
            }
        }
    }


    private void updateDrawablePath(){
        mPath.reset();//must重置
        mPath.addRoundRect(mDrawableRect, mCornerRadii, Path.Direction.CCW);
    }

    private void updateBorderPath(){
        mPath.reset();//must重置
        mPath.addRoundRect(mBorderRect, mCornerRadii, Path.Direction.CCW);
    }



    private void updateShaderMatrix() {
        float dx;
        float dy;
        float scale;
        float half = mBorderWidth / 2f;
        switch (mScaleType) {
            case CENTER_INSIDE:
                /**
                 * 找出View中比较小的一边（宽/高）
                 * 将bitmap的宽或者高按照View的小的一边缩放比例，另一边空余
                 * 然后位移
                 */
                float height, width;
                //比例处理
                float scaleH, scaleW;
                float wPadding = 0, hPadding = 0;
                if (mBitmapWidth <= mBounds.width() && mBitmapHeight <= mBounds.height()) {

                    scaleH = scaleW = 1.0f;//原比例缩放
                    dx = mBounds.width() - mBitmapWidth;
                    dy = mBounds.height() - mBitmapHeight;

                    height = mBitmapHeight * scaleH;
                    width = mBitmapWidth * scaleW;

                    wPadding = (mBounds.width() - width) / 2f;
                    hPadding = (mBounds.height() - height) / 2f;

                } else {//bitmap > drawable

                    if (mBounds.height() <= mBounds.width()) {//高<宽

                        scaleH = mBounds.height() / mBitmapHeight;
                        scaleW = scaleH;

                        height = mBounds.height();
                        width = mBitmapWidth * scaleW;

                        dy = (mBitmapHeight - height) / 2f;
                        dx = (mBitmapWidth - width) / 2f;

                        wPadding = (mBounds.width() - width) / 2f;
                        hPadding = 0;

                    } else {//宽<高
                        Log.e("tag", "=============================");
                        scaleW = mBounds.width() / mBitmapWidth;
                        scaleH = scaleW;

                        width = mBounds.width();
                        height = mBitmapHeight * scaleH;

                        dx = (mBitmapWidth - width) / 2f;
                        dy = (mBitmapHeight - height) / 2f;

                        wPadding = 0;
                        hPadding = (mBounds.height() - height) / 2f;
                    }
                }
                Log.e("tag", "=============mBounds.height():" + mBounds.height() + " mBounds.width():" + mBounds.width());
                Log.e("tag", "=============height:" + height + " width:" + width);
                Log.e("tag", "=============scaleH:" + scaleH + " scaleW:" + scaleW);
                // Log.e("tag", "=============dx:" + dx + " dy:" + dy);

                RectF rectF = new RectF(wPadding, hPadding, width + wPadding, height + hPadding);
                if (mCircle) {
                    rectF.inset(mBorderWidth, mBorderWidth);//圆形 padding BorderWidth
                } else {
                    rectF.inset(half, half);//非圆 padding 1/2兼容圆角
                }
                mRectF.set(rectF);

                //位移处理
                mShaderMatrix.reset();
                mShaderMatrix.setScale(scaleW, scaleH);
                mShaderMatrix.postTranslate(wPadding, hPadding);
                break;
            case CENTER:

                //  RectF rectF = new RectF();
                //  float height = Math.min(mBounds.height(), mBitmapRect.height());
                //   float width = Math.min(mBounds.width(), mBitmapRect.width());

                rectF = new RectF();
                height = Math.min(mBounds.height(), mBitmapRect.height());
                width = Math.min(mBounds.width(), mBitmapRect.width());
                //裁剪或者Margin（如果View大，则 margin Bitmap，如果View小则裁剪Bitmap）
                float cutOrMarginH = mBounds.height() - mBitmapRect.height();
                float cutOrMarginW = mBounds.width() - mBitmapRect.width();
                float halfH = cutOrMarginH / 2f, halfW = cutOrMarginW / 2f;
                float top = halfH > 0 ? halfH : 0;
                float left = halfW > 0 ? halfW : 0;
                //   dx = halfW ;
                //     dy = halfH ;
                dx = halfW;// < 0 ? halfW : 0;
                dy = halfH;//< 0 ? halfH : 0;
                //     dx = left > 0 ? left : 0;
                //    dy = top > 0 ? top : 0;

                Log.e("tag", "=============cutOrMarginH:" + cutOrMarginH + " cutOrMarginW:" + cutOrMarginW);
                Log.e("tag", "=============top:" + top + " left:" + left);
                Log.e("tag", "=============dx:" + dx + " dy:" + dy);

                rectF.set(left, top, left + width, top + height);


                float insetDx = dx < 0 ? dx : 0;
                float insetDy = dy < 0 ? dy : 0;
                // rectF.inset(insetDx, insetDy);
                Log.e("tag", "=============insetDx:" + insetDx + " insetDy:" + insetDy);

                mShaderMatrix.reset();
                mShaderMatrix.postTranslate((int) (dx + 0.5f) + half, (int) (dy + 0.5f) + half);

                if (mCircle) {
                    rectF.inset(mBorderWidth, mBorderWidth);//圆形 padding BorderWidth
                } else {
                    rectF.inset(half, half);//非圆 padding 1/2兼容圆角
                }

                mRectF.set(rectF);

                break;
            case CENTER_CROP:
                if (mCircle) {
                    mBounds.inset(mBorderWidth, mBorderWidth);//圆形 padding BorderWidth
                } else {
                    mBounds.inset(half, half);//非圆 padding 1/2兼容圆角
                }

                mRectF.set(mBounds);
                mShaderMatrix.reset();
                dx = 0;
                dy = 0;
                if (mBitmapWidth * mRectF.height() > mRectF.width() * mBitmapHeight) {
                    scale = mRectF.height() / (float) mBitmapHeight;
                    dx = (mRectF.width() - mBitmapWidth * scale) * 0.5f;
                } else {
                    scale = mRectF.width() / (float) mBitmapWidth;
                    dy = (mRectF.height() - mBitmapHeight * scale) * 0.5f;
                }

                mShaderMatrix.setScale(scale, scale);
                mShaderMatrix.postTranslate((int) (dx + 0.5f) + half, (int) (dy + 0.5f) + half);
                break;
            default:
            case FIT_CENTER:
            case FIT_END:
            case FIT_START:

                //为了不改变mBounds引入临时变量
                //控制源bitmap
                if (mCircle) {
                    mBounds.inset(mBorderWidth, mBorderWidth);//圆形 padding BorderWidth
                } else {
                    mBounds.inset(half, half);//非圆 padding 1/2兼容圆角
                }

                mRectF.set(mBitmapRect);
                mShaderMatrix.setRectToRect(mBitmapRect, mBounds, scaleTypeToScaleToFit(mScaleType));
                mShaderMatrix.mapRect(mRectF);
                mShaderMatrix.setRectToRect(mBitmapRect, mRectF, Matrix.ScaleToFit.FILL);
                break;
            case FIT_XY:

                if (mCircle) {
                    mBounds.inset(mBorderWidth, mBorderWidth);
                } else {
                    mBounds.inset(half, half);
                }
                mRectF.set(mBounds);
                mShaderMatrix.reset();
                mShaderMatrix.setRectToRect(mBitmapRect, mRectF, Matrix.ScaleToFit.FILL);
                break;
        }
        if (mCircle) {
            mBorderRect.set(mRectF.left - half, mRectF.top - half, mRectF.right + half, mRectF.bottom + half);//还原
        } else {
            //   mBorderRect.set(drawableRect);//前面赋值
            mBorderRect.inset(half, half);
        }
        mDrawableRect.set(mRectF);
        mRebuildShader = true;
    }


    private static Matrix.ScaleToFit scaleTypeToScaleToFit(ImageView.ScaleType st) {
        /**
         * 根据源码改造  sS2FArray[st.nativeInt - 1]
         */
        switch (st) {
            case FIT_XY:
                return Matrix.ScaleToFit.FILL;
            case FIT_START:
                return Matrix.ScaleToFit.START;
            case FIT_END:
                return Matrix.ScaleToFit.END;
            case FIT_CENTER:
                return Matrix.ScaleToFit.CENTER;
            default:
                return Matrix.ScaleToFit.CENTER;
        }
    }


    public Bitmap getSourceBitmap() {
        return mBitmap;
    }

    public ImageView.ScaleType getScaleType() {
        return mScaleType;
    }

    public RoundDrawable setScaleType(ImageView.ScaleType scaleType) {
        if (scaleType == null) {
            scaleType = ImageView.ScaleType.FIT_CENTER;
        }
        if (mScaleType != scaleType) {
            mScaleType = scaleType;
            updateShaderMatrix();
        }
        return this;
    }

    public boolean isCircle() {
        return mCircle;
    }

    public RoundDrawable setCircle(boolean circle) {
        mCircle = circle;
        return this;
    }

    public RoundDrawable setBorderWidth(float borderWidth) {
        mBorderWidth = borderWidth;
        return this;
    }

    public float getBorderWidth() {
        return mBorderWidth;
    }

    public RoundDrawable setBorderColor(int borderColor) {
        mBorderColor = borderColor;
        return this;
    }

    public float getBorderColor() {
        return mBorderColor;
    }

    public static RoundDrawable fromBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            return new RoundDrawable(bitmap);
        } else {
            return null;
        }
    }

    public static Drawable fromDrawable(Drawable drawable) {
        if (drawable != null) {
            if (drawable instanceof RoundDrawable) {
                // just return if it's already a RoundedDrawable
                return drawable;
            } else if (drawable instanceof LayerDrawable) {
                ConstantState cs = drawable.mutate().getConstantState();
                LayerDrawable ld = (LayerDrawable) (cs != null ? cs.newDrawable() : drawable);

                int num = ld.getNumberOfLayers();

                // loop through layers to and change to RoundedDrawables if possible
                for (int i = 0; i < num; i++) {
                    Drawable d = ld.getDrawable(i);
                    ld.setDrawableByLayerId(ld.getId(i), fromDrawable(d));
                }
                return ld;
            }

            // try to get a bitmap from the drawable and
            Bitmap bm = drawableToBitmap(drawable);
            if (bm != null) {
                return new RoundDrawable(bm);
            }
        }
        return drawable;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap;
        int width = Math.max(drawable.getIntrinsicWidth(), 2);
        int height = Math.max(drawable.getIntrinsicHeight(), 2);
        try {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        } catch (Throwable e) {
            e.printStackTrace();
            //  Log.w(TAG, "Failed to create bitmap from drawable!");
            bitmap = null;
        }

        return bitmap;
    }

}
