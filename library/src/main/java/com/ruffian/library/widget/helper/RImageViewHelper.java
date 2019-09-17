package com.ruffian.library.widget.helper;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.v7.content.res.AppCompatResources;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.ruffian.library.widget.R;

/**
 * ImageView-Helper
 *
 * @author ZhongDaFeng
 */
public class RImageViewHelper {

    //Icon
    private Drawable mIconNormal;
    private Drawable mIconPressed;
    private Drawable mIconUnable;
    private Drawable mIconSelected;

    //圆角
    private float mCorner = -1;
    private float mCornerTopLeft = 0;
    private float mCornerTopRight = 0;
    private float mCornerBottomLeft = 0;
    private float mCornerBottomRight = 0;
    private float mCornerBorderRadii[] = new float[8];
    private float mCornerBitmapRadii[] = new float[8];

    //边框
    private int mBorderWidth = 0;
    private int mBorderColor = Color.BLACK;

    //是否为普通ImageView(不是圆形或者圆角)
    private boolean mIsNormal = true;

    //是否圆形
    private boolean mIsCircle = false;

    private final RectF mDrawableRect = new RectF();
    private final RectF mBorderRect = new RectF();
    private Paint mBitmapPaint;
    private Paint mBorderPaint;


    protected int[][] states = new int[4][];
    private StateListDrawable mStateDrawable;

    private ImageView mView;

    public RImageViewHelper(Context context, ImageView view, AttributeSet attrs) {
        mView = view;
        initAttributeSet(context, attrs);
    }

    /**
     * 初始化控件属性
     *
     * @param context
     * @param attrs
     */
    private void initAttributeSet(Context context, AttributeSet attrs) {
        if (context == null || attrs == null) {
            setup();
            return;
        }
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RImageView);
        //icon
        //Vector兼容处理
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mIconNormal = a.getDrawable(R.styleable.RImageView_icon_src_normal);
            mIconPressed = a.getDrawable(R.styleable.RImageView_icon_src_pressed);
            mIconUnable = a.getDrawable(R.styleable.RImageView_icon_src_unable);
            mIconSelected = a.getDrawable(R.styleable.RImageView_icon_src_selected);
        } else {
            int normalId = a.getResourceId(R.styleable.RImageView_icon_src_normal, -1);
            int pressedId = a.getResourceId(R.styleable.RImageView_icon_src_pressed, -1);
            int unableId = a.getResourceId(R.styleable.RImageView_icon_src_unable, -1);
            int selectedId = a.getResourceId(R.styleable.RImageView_icon_src_selected, -1);

            if (normalId != -1)
                mIconNormal = AppCompatResources.getDrawable(context, normalId);
            if (pressedId != -1)
                mIconPressed = AppCompatResources.getDrawable(context, pressedId);
            if (unableId != -1)
                mIconUnable = AppCompatResources.getDrawable(context, unableId);
            if (selectedId != -1)
                mIconSelected = AppCompatResources.getDrawable(context, selectedId);
        }
        //基础属性
        mIsCircle = a.getBoolean(R.styleable.RImageView_is_circle, false);
        mCorner = a.getDimensionPixelSize(R.styleable.RImageView_corner_radius, -1);
        mCornerTopLeft = a.getDimensionPixelSize(R.styleable.RImageView_corner_radius_top_left, 0);
        mCornerTopRight = a.getDimensionPixelSize(R.styleable.RImageView_corner_radius_top_right, 0);
        mCornerBottomLeft = a.getDimensionPixelSize(R.styleable.RImageView_corner_radius_bottom_left, 0);
        mCornerBottomRight = a.getDimensionPixelSize(R.styleable.RImageView_corner_radius_bottom_right, 0);
        mBorderWidth = a.getDimensionPixelSize(R.styleable.RImageView_border_width, 0);
        mBorderColor = a.getColor(R.styleable.RImageView_border_color, Color.BLACK);

        a.recycle();

        //未使用自定义属性容错处理
        if (mIconNormal == null) mIconNormal = mView.getDrawable();

        //init
        init();

        //setup
        setup();

    }

    /**
     * 设置
     */
    private void setup() {

        mStateDrawable = new StateListDrawable();

        /**
         * 设置背景默认值
         */
        if (mIconPressed == null) {
            mIconPressed = mIconNormal;
        }
        if (mIconUnable == null) {
            mIconUnable = mIconNormal;
        }
        if (mIconSelected == null) {
            mIconSelected = mIconNormal;
        }

        //unable,focused,pressed,selected,normal
        states[0] = new int[]{-android.R.attr.state_enabled};//unable
        states[1] = new int[]{android.R.attr.state_pressed};//pressed
        states[2] = new int[]{android.R.attr.state_selected};//selected
        states[3] = new int[]{android.R.attr.state_enabled};//normal

        mStateDrawable.addState(states[0], mIconUnable);
        mStateDrawable.addState(states[1], mIconPressed);
        mStateDrawable.addState(states[2], mIconSelected);
        mStateDrawable.addState(states[3], mIconNormal);

        setIcon();
    }

    /**
     * 初始化设置
     */
    private void init() {

        //统一设置圆角弧度优先
        updateCornerBorderRadii();
        updateCornerBitmapRadii();

        //设置圆形，或者某个角度圆角则认为不是普通imageView
        if (mIsCircle || mCorner > 0 || mCornerTopLeft != 0 || mCornerTopRight != 0 || mCornerBottomRight != 0 || mCornerBottomLeft != 0) {
            mIsNormal = false;
        }


        //border
        if (mBorderPaint == null) {
            mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        }
        mBorderPaint.setStyle(Paint.Style.STROKE);
        //bitmap
        if (mBitmapPaint == null) {
            mBitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        }

        //圆形imageView强制设置类型
        if (!mIsNormal) {
            //border
          /*  if (mBorderPaint == null) {
                mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            }
            mBorderPaint.setStyle(Paint.Style.STROKE);
            //bitmap
            if (mBitmapPaint == null) {
                mBitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            }*/

            //setLayerType(View.LAYER_TYPE_SOFTWARE, mBitmapPaint);//禁止硬件加速
            /*if (mIsCircle) {
                super.setScaleType(ScaleType.CENTER_CROP);
            } else {
                super.setScaleType(getScaleType());
            }*/
        }
    }

    /**
     * 绘制
     *
     * @param canvas
     */
    public void onDraw(Canvas canvas) {
        /**
         * 绘制图片
         */
        drawBitmap(canvas);
        /**
         * 绘制边框
         */
        drawBorder(canvas);
    }

    /**
     * 绘制bitmap
     *
     * @param canvas
     */
    private void drawBitmap(Canvas canvas) {
        if (mView.getDrawable() != null) {
            //setLayerType(View.LAYER_TYPE_SOFTWARE, mBitmapPaint);//禁止硬件加速
            int layerId = canvas.saveLayer(0, 0, getWidth(), getHeight(), mBitmapPaint, Canvas.ALL_SAVE_FLAG);//离屏绘制

            //drawable
            Drawable drawable = mView.getDrawable();
            int bmpW = drawable.getIntrinsicWidth();
            int bmpH = drawable.getIntrinsicHeight();
            //matrix
            Matrix matrix;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                matrix = mView.getMatrix();
            } else {
                matrix = new Matrix();
                matrix.set(mView.getMatrix());
            }

            //ScaleType
            ImageView.ScaleType scaleType = mView.getScaleType();

            //图形轮廓
            Bitmap dst = makeDst(getWidth(), getHeight());//创建
            canvas.drawBitmap(dst, 0, 0, mBitmapPaint);//绘制

            //设置混合模型
            mBitmapPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

            //绘制展示图
            drawBitmapSrc(canvas, drawable, matrix, scaleType, bmpW, bmpH, getWidth(), getHeight());

            mBitmapPaint.setXfermode(null);
            canvas.restoreToCount(layerId); //离屏绘制
        }
    }


    /**
     * 绘制图片
     * 参考源码 configureBounds()
     *
     * @param canvas
     * @param drawable
     * @param matrix
     * @param scaleType
     * @param bmpW      图片宽
     * @param bmpH      图片高
     * @param w         控件宽
     * @param h         控件高
     */
    private void drawBitmapSrc(Canvas canvas, Drawable drawable, Matrix matrix, ImageView.ScaleType scaleType, int bmpW, int bmpH, int w, int h) {

        /**
         * 支持padding 考虑边框宽度
         */
        int paddingLeft = mView.getPaddingLeft() + mBorderWidth;
        int paddingTop = mView.getPaddingTop() + mBorderWidth;
        int paddingRight = mView.getPaddingRight() + mBorderWidth;
        int paddingBottom = mView.getPaddingBottom() + mBorderWidth;

        /**
         * 实际宽高
         */
        float actualW = w - paddingLeft - paddingRight;
        float actualH = h - paddingTop - paddingBottom;

        /**
         * 宽高缩放比例
         */
        float scaleW = actualW / w;
        float scaleH = actualH / h;

        Bitmap viewBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);//根据view大小创建bitmap
        Canvas viewCanvas = new Canvas(viewBitmap);//根据 viewBitmap 大小创建 canvas 画布
        viewCanvas.translate(paddingLeft, paddingTop);//移动画布,必须先于缩放，避免误差
        viewCanvas.scale(scaleW, scaleH);//缩放画布

        /**
         * 根据 scaleType 处理图片 参考 ImageView 源码 configureBounds()
         */
        float scale;
        float dx = 0, dy = 0;
        switch (scaleType) {
            default:
            case CENTER:

                matrix.setTranslate(Math.round((w - bmpW) * 0.5f), Math.round((h - bmpH) * 0.5f));
                break;
            case FIT_START:
            case FIT_END:
            case FIT_CENTER:

                RectF mTempSrc = new RectF(0, 0, bmpW, bmpH);
                RectF mTempDst = new RectF(0, 0, w, h);
                matrix.setRectToRect(mTempSrc, mTempDst, scaleTypeToScaleToFit(scaleType));
                break;
            case FIT_XY:

                drawable.setBounds(0, 0, w, h);
                matrix = null;
                break;
            case CENTER_CROP:

                if (bmpW * h > w * bmpH) {
                    scale = (float) h / (float) bmpH;
                    dx = (w - bmpW * scale) * 0.5f;
                } else {
                    scale = (float) w / (float) bmpW;
                    dy = (h - bmpH * scale) * 0.5f;
                }

                matrix.setScale(scale, scale);
                matrix.postTranslate(Math.round(dx), Math.round(dy));
                break;
            case CENTER_INSIDE:

                if (bmpW <= w && bmpH <= h) {
                    scale = 1.0f;
                } else {
                    scale = Math.min((float) w / (float) bmpW, (float) h / (float) bmpH);
                }

                dx = Math.round((w - bmpW * scale) * 0.5f);
                dy = Math.round((h - bmpH * scale) * 0.5f);

                matrix.setScale(scale, scale);
                matrix.postTranslate(dx, dy);
                break;
            case MATRIX:

                if (matrix.isIdentity()) {
                    matrix = null;
                }
                break;
        }

        viewCanvas.concat(matrix);//设置变化矩阵
        drawable.draw(viewCanvas);//绘制drawable
        canvas.drawBitmap(viewBitmap, 0, 0, mBitmapPaint);
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
            default:
                return Matrix.ScaleToFit.CENTER;
        }
    }

    /**
     * 绘制边框
     *
     * @param canvas
     */
    private void drawBorder(Canvas canvas) {
        if (mBorderWidth > 0) {
            //重新设置 color & width
            mBorderPaint.setColor(mBorderColor);
            mBorderPaint.setStrokeWidth(mBorderWidth);
            if (mIsCircle) {
                float borderRadiusX = (mBorderRect.width() - mBorderWidth) / 2;
                float borderRadiusY = (mBorderRect.height() - mBorderWidth) / 2;
                canvas.drawCircle(getWidth() / 2, getHeight() / 2, Math.min(borderRadiusX, borderRadiusY), mBorderPaint);
            } else {
                updateCornerBorderRadii();
                Path path = new Path();
                path.addRoundRect(mBorderRect, mCornerBorderRadii, Path.Direction.CW);
                canvas.drawPath(path, mBorderPaint);
            }
        }
    }

    /**
     * 获取目标资源bitmap(形状)
     *
     * @param w
     * @param h
     * @return
     */
    private Bitmap makeDst(int w, int h) {

        updateCornerBitmapRadii();
        updateDrawableAndBorderRect();

        if (mIsCircle) {//圆形
            return makeDstCircle(w, h, mDrawableRect);
        } else {//圆角
            return makeDstRound(w, h, mDrawableRect, mCornerBitmapRadii);
        }
    }

    /**
     * 获取目标资源bitmap-圆形
     *
     * @param w
     * @param h
     * @param rect
     * @return
     */
    private Bitmap makeDstCircle(int w, int h, RectF rect) {
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);//抗锯齿
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, Math.min(rect.width() / 2, rect.width() / 2), paint);
        return bitmap;
    }

    /**
     * 获取目标资源bitmap-圆角
     *
     * @param w
     * @param h
     * @param rect
     * @param radii
     * @return
     */
    private Bitmap makeDstRound(int w, int h, RectF rect, float[] radii) {
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);//抗锯齿
        Path path = new Path();
        path.addRoundRect(rect, radii, Path.Direction.CW);
        canvas.drawPath(path, paint);
        return bitmap;
    }

    /**
     * 更新drawable和border Rect
     */
    private void updateDrawableAndBorderRect() {

        float half = mBorderWidth / 2;
        if (mIsCircle) {//圆形

            mBorderRect.set(0, 0, getWidth(), getHeight());//边框Rect圆形
            mDrawableRect.set(mBorderWidth, mBorderWidth, mBorderRect.width() - mBorderWidth, mBorderRect.height() - mBorderWidth);//drawableRect
        } else {//圆角

            mBorderRect.set(half, half, getWidth() - half, getHeight() - half);//边框Rect圆角
            mDrawableRect.set(mBorderRect.left + half, mBorderRect.top + half, mBorderRect.right - half, mBorderRect.bottom - half);//drawableRect
        }
    }

    /**
     * 更新bitmap圆角弧度
     */
    private void updateCornerBitmapRadii() {
        if (mCorner >= 0) {
            for (int i = 0; i < mCornerBitmapRadii.length; i++) {
                mCornerBitmapRadii[i] = mCorner;
            }
            return;
        }

        if (mCorner < 0) {
            mCornerBitmapRadii[0] = mCornerTopLeft;
            mCornerBitmapRadii[1] = mCornerTopLeft;
            mCornerBitmapRadii[2] = mCornerTopRight;
            mCornerBitmapRadii[3] = mCornerTopRight;
            mCornerBitmapRadii[4] = mCornerBottomRight;
            mCornerBitmapRadii[5] = mCornerBottomRight;
            mCornerBitmapRadii[6] = mCornerBottomLeft;
            mCornerBitmapRadii[7] = mCornerBottomLeft;
            return;
        }
    }

    /**
     * 更新border圆角弧度
     */
    private void updateCornerBorderRadii() {

        if (mCorner >= 0) {
            for (int i = 0; i < mCornerBorderRadii.length; i++) {
                mCornerBorderRadii[i] = mCorner == 0 ? mCorner : mCorner + mBorderWidth;
            }
            return;
        }

        if (mCorner < 0) {
            mCornerBorderRadii[0] = mCornerTopLeft == 0 ? mCornerTopLeft : mCornerTopLeft + mBorderWidth;
            mCornerBorderRadii[1] = mCornerTopLeft == 0 ? mCornerTopLeft : mCornerTopLeft + mBorderWidth;
            mCornerBorderRadii[2] = mCornerTopRight == 0 ? mCornerTopRight : mCornerTopRight + mBorderWidth;
            mCornerBorderRadii[3] = mCornerTopRight == 0 ? mCornerTopRight : mCornerTopRight + mBorderWidth;
            mCornerBorderRadii[4] = mCornerBottomRight == 0 ? mCornerBottomRight : mCornerBottomRight + mBorderWidth;
            mCornerBorderRadii[5] = mCornerBottomRight == 0 ? mCornerBottomRight : mCornerBottomRight + mBorderWidth;
            mCornerBorderRadii[6] = mCornerBottomLeft == 0 ? mCornerBottomLeft : mCornerBottomLeft + mBorderWidth;
            mCornerBorderRadii[7] = mCornerBottomLeft == 0 ? mCornerBottomLeft : mCornerBottomLeft + mBorderWidth;
            return;
        }

    }

    private int getWidth() {
        return mView.getWidth();
    }

    private int getHeight() {
        return mView.getHeight();
    }

    private void invalidate() {
        mView.invalidate();
    }


    /************************
     * Icon
     ************************/

    public RImageViewHelper setIconNormal(Drawable icon) {
        this.mIconNormal = icon;
        if (mIconPressed == null) {
            mIconPressed = mIconNormal;
        }
        if (mIconUnable == null) {
            mIconUnable = mIconNormal;
        }
        if (mIconSelected == null) {
            mIconSelected = mIconNormal;
        }
        setIcon();
        return this;
    }

    public Drawable getIconNormal() {
        return mIconNormal;
    }

    public RImageViewHelper setIconPressed(Drawable icon) {
        this.mIconPressed = icon;
        setIcon();
        return this;
    }

    public Drawable getIconPressed() {
        return mIconPressed;
    }

    public RImageViewHelper setIconUnable(Drawable icon) {
        this.mIconUnable = icon;
        setIcon();
        return this;
    }

    public Drawable getIconUnable() {
        return mIconUnable;
    }

    public RImageViewHelper setIconSelected(Drawable icon) {
        this.mIconSelected = icon;
        setIcon();
        return this;
    }

    public Drawable getIconSelected() {
        return mIconSelected;
    }

    private void setIcon() {
        mView.setImageDrawable(mStateDrawable);
        mView.invalidate();
    }

    /************************
     * Border
     ************************/

    public int getBorderWidth() {
        return mBorderWidth;
    }

    public RImageViewHelper setBorderWidth(int borderWidth) {
        this.mBorderWidth = borderWidth;
        invalidate();
        return this;
    }

    public int getBorderColor() {
        return mBorderColor;
    }

    public RImageViewHelper setBorderColor(@ColorInt int borderColor) {
        this.mBorderColor = borderColor;
        invalidate();
        return this;
    }

    /************************
     * Corner
     ************************/
    public float getCorner() {
        return mCorner;
    }

    public RImageViewHelper setCorner(float corner) {
        this.mCorner = corner;
        init();
        invalidate();
        return this;
    }

    public float getCornerTopLeft() {
        return mCornerTopLeft;
    }

    public RImageViewHelper setCornerTopLeft(float cornerTopLeft) {
        this.mCorner = -1;
        this.mCornerTopLeft = cornerTopLeft;
        init();
        invalidate();
        return this;
    }

    public float getCornerTopRight() {
        return mCornerTopRight;
    }

    public RImageViewHelper setCornerTopRight(float cornerTopRight) {
        this.mCorner = -1;
        this.mCornerTopRight = cornerTopRight;
        init();
        invalidate();
        return this;
    }

    public float getCornerBottomLeft() {
        return mCornerBottomLeft;
    }

    public RImageViewHelper setCornerBottomLeft(float cornerBottomLeft) {
        this.mCorner = -1;
        this.mCornerBottomLeft = cornerBottomLeft;
        init();
        invalidate();
        return this;
    }

    public float getCornerBottomRight() {
        return mCornerBottomRight;
    }

    public RImageViewHelper setCornerBottomRight(float cornerBottomRight) {
        this.mCorner = -1;
        this.mCornerBottomRight = cornerBottomRight;
        init();
        invalidate();
        return this;
    }

    public RImageViewHelper setCorner(float topLeft, float topRight, float bottomRight, float bottomLeft) {
        this.mCorner = -1;
        this.mCornerTopLeft = topLeft;
        this.mCornerTopRight = topRight;
        this.mCornerBottomRight = bottomRight;
        this.mCornerBottomLeft = bottomLeft;
        init();
        invalidate();
        return this;
    }

    /**
     * 是否默认ImageView
     *
     * @return
     */
    public boolean isNormal() {
        return mIsNormal;
    }
}
