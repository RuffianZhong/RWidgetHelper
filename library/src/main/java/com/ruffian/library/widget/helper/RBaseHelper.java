package com.ruffian.library.widget.helper;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.StyleableRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;

import com.ruffian.library.widget.R;

/**
 * BaseHelper
 *
 * @author ZhongDaFeng
 */
public class RBaseHelper<T extends View> {
    /**
     * 背景类型{1:单一颜色值 2:颜色数组 3:图片}
     */
    protected int BG_TYPE_COLOR = 1, BG_TYPE_COLOR_ARRAY = 2, BG_TYPE_IMG = 3;

    //corner
    private float mCornerRadius;
    private float mCornerRadiusTopLeft;
    private float mCornerRadiusTopRight;
    private float mCornerRadiusBottomLeft;
    private float mCornerRadiusBottomRight;

    //BorderWidth
    private float mBorderDashWidth = 0;
    private float mBorderDashGap = 0;
    private int mBorderWidthNormal = 0;
    private int mBorderWidthPressed = 0;
    private int mBorderWidthUnable = 0;
    private int mBorderWidthChecked = 0;

    //BorderColor
    private int mBorderColorNormal;
    private int mBorderColorPressed;
    private int mBorderColorUnable;
    private int mBorderColorChecked;

    //Background
    private int mBackgroundColorNormal;
    private int mBackgroundColorPressed;
    private int mBackgroundColorUnable;
    private int mBackgroundColorChecked;
    //BackgroundColorArray
    private int[] mBackgroundColorNormalArray;
    private int[] mBackgroundColorPressedArray;
    private int[] mBackgroundColorUnableArray;
    private int[] mBackgroundColorCheckedArray;
    private GradientDrawable mBackgroundNormal;
    private GradientDrawable mBackgroundPressed;
    private GradientDrawable mBackgroundUnable;
    private GradientDrawable mBackgroundChecked;
    private Drawable mBackgroundNormalBmp;
    private Drawable mBackgroundPressedBmp;
    private Drawable mBackgroundUnableBmp;
    private Drawable mBackgroundCheckedBmp;
    private int mBgNomalType;
    private int mBgPressedType;
    private int mBgUnableType;
    private int mBgCheckedType;
    //Gradient
    private int mGradientType = 0;
    private float mGradientRadius;
    private float mGradientCenterX, mGradientCenterY;
    private GradientDrawable.Orientation mGradientOrientation = GradientDrawable.Orientation.TOP_BOTTOM;

    //View/ViewGroup是否可用
    private boolean mIsEnabled = true;

    private int[][] states = new int[6][];
    private StateListDrawable mStateBackground;
    private float mBorderRadii[] = new float[8];

    /**
     * Cache the touch slop from the context that created the view.
     */
    private int mTouchSlop;
    protected Context mContext;

    /**
     * 是否设置对应的属性
     */
    private boolean mHasPressedBgColor = false;
    private boolean mHasPressedBgBmp = false;
    private boolean mHasUnableBgColor = false;
    private boolean mHasUnableBgBmp = false;
    private boolean mHasCheckedBgColor = false;
    private boolean mHasCheckedBgBmp = false;
    private boolean mHasPressedBorderColor = false;
    private boolean mHasUnableBorderColor = false;
    private boolean mHasCheckedBorderColor = false;
    private boolean mHasPressedBorderWidth = false;
    private boolean mHasUnableBorderWidth = false;
    private boolean mHasCheckedBorderWidth = false;

    // view
    protected T mView;

    //EmptyStateListDrawable
    private StateListDrawable emptyStateListDrawable = new StateListDrawable();

    public RBaseHelper(Context context, T view, AttributeSet attrs) {
        mView = view;
        mContext = context;
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        //初始化控件属性
        initAttributeSet(context, attrs);
        //监听View大小改变
        addOnGlobalLayoutListener();
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
        TypedArray a = mView.getContext().obtainStyledAttributes(attrs, R.styleable.RBaseView);
        //corner
        mCornerRadius = a.getDimensionPixelSize(R.styleable.RBaseView_corner_radius, -1);
        mCornerRadiusTopLeft = a.getDimensionPixelSize(R.styleable.RBaseView_corner_radius_top_left, 0);
        mCornerRadiusTopRight = a.getDimensionPixelSize(R.styleable.RBaseView_corner_radius_top_right, 0);
        mCornerRadiusBottomLeft = a.getDimensionPixelSize(R.styleable.RBaseView_corner_radius_bottom_left, 0);
        mCornerRadiusBottomRight = a.getDimensionPixelSize(R.styleable.RBaseView_corner_radius_bottom_right, 0);
        //border
        mBorderDashWidth = a.getDimensionPixelSize(R.styleable.RBaseView_border_dash_width, 0);
        mBorderDashGap = a.getDimensionPixelSize(R.styleable.RBaseView_border_dash_gap, 0);
        mBorderWidthNormal = a.getDimensionPixelSize(R.styleable.RBaseView_border_width_normal, 0);
        mBorderWidthPressed = a.getDimensionPixelSize(R.styleable.RBaseView_border_width_pressed, 0);
        mBorderWidthUnable = a.getDimensionPixelSize(R.styleable.RBaseView_border_width_unable, 0);
        mBorderWidthChecked = a.getDimensionPixelSize(R.styleable.RBaseView_border_width_checked, 0);
        mBorderColorNormal = a.getColor(R.styleable.RBaseView_border_color_normal, Color.TRANSPARENT);
        mBorderColorPressed = a.getColor(R.styleable.RBaseView_border_color_pressed, Color.TRANSPARENT);
        mBorderColorUnable = a.getColor(R.styleable.RBaseView_border_color_unable, Color.TRANSPARENT);
        mBorderColorChecked = a.getColor(R.styleable.RBaseView_border_color_checked, Color.TRANSPARENT);
        //background
        //normal
        Object[] bgInfoNormal = getBackgroundInfo(a, R.styleable.RBaseView_background_normal);
        mBgNomalType = (int) bgInfoNormal[0];
        mBackgroundColorNormal = (int) bgInfoNormal[1];
        mBackgroundColorNormalArray = (int[]) bgInfoNormal[2];
        mBackgroundNormalBmp = (Drawable) bgInfoNormal[3];
        //pressed
        Object[] bgInfoPressed = getBackgroundInfo(a, R.styleable.RBaseView_background_pressed);
        mBgPressedType = (int) bgInfoPressed[0];
        mBackgroundColorPressed = (int) bgInfoPressed[1];
        mBackgroundColorPressedArray = (int[]) bgInfoPressed[2];
        mBackgroundPressedBmp = (Drawable) bgInfoPressed[3];
        //unable
        Object[] bgInfoUnable = getBackgroundInfo(a, R.styleable.RBaseView_background_unable);
        mBgUnableType = (int) bgInfoUnable[0];
        mBackgroundColorUnable = (int) bgInfoUnable[1];
        mBackgroundColorUnableArray = (int[]) bgInfoUnable[2];
        mBackgroundUnableBmp = (Drawable) bgInfoUnable[3];
        //checked
        Object[] bgInfoChecked = getBackgroundInfo(a, R.styleable.RBaseView_background_checked);
        mBgCheckedType = (int) bgInfoChecked[0];
        mBackgroundColorChecked = (int) bgInfoChecked[1];
        mBackgroundColorCheckedArray = (int[]) bgInfoChecked[2];
        mBackgroundCheckedBmp = (Drawable) bgInfoChecked[3];
        //gradient
        mGradientType = a.getInt(R.styleable.RBaseView_gradient_type, 0);
        mGradientOrientation = getGradientOrientation(a);
        mGradientRadius = a.getDimensionPixelSize(R.styleable.RBaseView_gradient_radius, -1);
        mGradientCenterX = a.getFloat(R.styleable.RBaseView_gradient_centerX, 0.5f);
        mGradientCenterY = a.getFloat(R.styleable.RBaseView_gradient_centerY, 0.5f);
        //enabled
        mIsEnabled = a.getBoolean(R.styleable.RBaseView_enabled, true);

        a.recycle();

        mHasPressedBgColor = mBackgroundColorPressed != 0 || mBackgroundColorNormalArray != null;
        mHasUnableBgColor = mBackgroundColorUnable != 0 || mBackgroundColorUnableArray != null;
        mHasCheckedBgColor = mBackgroundColorChecked != 0 || mBackgroundColorCheckedArray != null;
        mHasPressedBgBmp = mBackgroundPressedBmp != null;
        mHasUnableBgBmp = mBackgroundUnableBmp != null;
        mHasCheckedBgBmp = mBackgroundCheckedBmp != null;
        mHasPressedBorderColor = mBorderColorPressed != 0;
        mHasUnableBorderColor = mBorderColorUnable != 0;
        mHasCheckedBorderColor = mBorderColorChecked != 0;
        mHasPressedBorderWidth = mBorderWidthPressed != 0;
        mHasUnableBorderWidth = mBorderWidthUnable != 0;
        mHasCheckedBorderWidth = mBorderWidthChecked != 0;

        //setup
        setup();

    }

    /**
     * 设置
     */
    private void setup() {

        /**
         * 如果本身为true，则由自定义属性决定
         * 如果本身为false，则由原生属性决定
         * ViewGroup Always true
         */
        boolean isEnabled = mView.isEnabled();
        if (isEnabled) mView.setEnabled(mIsEnabled);

        mBackgroundNormal = new GradientDrawable();
        mBackgroundPressed = new GradientDrawable();
        mBackgroundUnable = new GradientDrawable();
        mBackgroundChecked = new GradientDrawable();

        Drawable drawable = mView.getBackground();
        if (drawable != null && drawable instanceof StateListDrawable) {
            mStateBackground = (StateListDrawable) drawable;
        } else {
            mStateBackground = new StateListDrawable();
        }

        /**
         * 设置背景默认值
         */
        if (!mHasPressedBgColor) {
            mBackgroundColorPressed = mBackgroundColorNormal;
            mBackgroundColorPressedArray = mBackgroundColorNormalArray;
        }
        if (!mHasPressedBgBmp) {
            mBackgroundPressedBmp = mBackgroundNormalBmp;
        }
        if (!mHasUnableBgColor) {
            mBackgroundColorUnable = mBackgroundColorNormal;
            mBackgroundColorUnableArray = mBackgroundColorNormalArray;
        }
        if (!mHasUnableBgBmp) {
            mBackgroundUnableBmp = mBackgroundNormalBmp;
        }
        if (!mHasCheckedBgColor) {
            mBackgroundColorChecked = mBackgroundColorNormal;
            mBackgroundColorCheckedArray = mBackgroundColorNormalArray;
        }
        if (!mHasCheckedBgBmp) {
            mBackgroundCheckedBmp = mBackgroundNormalBmp;
        }

        /**
         * 设置背景颜色（包含渐变）
         */
        if (mBackgroundColorNormalArray != null && mBackgroundColorNormalArray.length > 0) {
            mBackgroundNormal = setColors(mBackgroundNormal, mBackgroundColorNormalArray);
        } else {
            mBackgroundNormal.setColor(mBackgroundColorNormal);
        }
        if (mBackgroundColorPressedArray != null && mBackgroundColorPressedArray.length > 0) {
            mBackgroundPressed = setColors(mBackgroundPressed, mBackgroundColorPressedArray);
        } else {
            mBackgroundPressed.setColor(mBackgroundColorPressed);
        }
        if (mBackgroundColorUnableArray != null && mBackgroundColorUnableArray.length > 0) {
            mBackgroundUnable = setColors(mBackgroundUnable, mBackgroundColorUnableArray);
        } else {
            mBackgroundUnable.setColor(mBackgroundColorUnable);
        }
        if (mBackgroundColorCheckedArray != null && mBackgroundColorCheckedArray.length > 0) {
            mBackgroundChecked = setColors(mBackgroundChecked, mBackgroundColorCheckedArray);
        } else {
            mBackgroundChecked.setColor(mBackgroundColorChecked);
        }
        //设置渐变相关Gradient
        setGradient();

        //pressed, focused, normal, unable, checked
        states[0] = new int[]{android.R.attr.state_enabled, android.R.attr.state_pressed};
        states[1] = new int[]{android.R.attr.state_enabled, android.R.attr.state_focused};
        states[2] = new int[]{android.R.attr.state_enabled};
        states[3] = new int[]{-android.R.attr.state_enabled};
        states[4] = new int[]{-android.R.attr.state_checked};
        states[5] = new int[]{android.R.attr.state_checked};

        mStateBackground.addState(states[0], mBackgroundPressedBmp == null ? mBackgroundPressed : mBackgroundPressedBmp);
        mStateBackground.addState(states[1], mBackgroundPressedBmp == null ? mBackgroundPressed : mBackgroundPressedBmp);
        mStateBackground.addState(states[3], mBackgroundUnableBmp == null ? mBackgroundUnable : mBackgroundUnableBmp);
        mStateBackground.addState(states[4], mBackgroundNormalBmp == null ? mBackgroundNormal : mBackgroundNormalBmp);
        mStateBackground.addState(states[5], mBackgroundCheckedBmp == null ? mBackgroundChecked : mBackgroundCheckedBmp);
        mStateBackground.addState(states[2], mBackgroundNormalBmp == null ? mBackgroundNormal : mBackgroundNormalBmp);

        /**
         * 设置边框默认值
         */
        if (!mHasPressedBorderWidth) {
            mBorderWidthPressed = mBorderWidthNormal;
        }
        if (!mHasUnableBorderWidth) {
            mBorderWidthUnable = mBorderWidthNormal;
        }
        if (!mHasCheckedBorderWidth) {
            mBorderWidthChecked = mBorderWidthNormal;
        }
        if (!mHasPressedBorderColor) {
            mBorderColorPressed = mBorderColorNormal;
        }
        if (!mHasUnableBorderColor) {
            mBorderColorUnable = mBorderColorNormal;
        }
        if (!mHasCheckedBorderColor) {
            mBorderColorChecked = mBorderColorNormal;
        }

        //设置背景
        setBackgroundState();

        //设置边框
        setBorder();

        //设置圆角
        setRadiusValue();

    }


    /*********************
     * Gradient
     ********************/
    public float getGradientRadius() {
        return mGradientRadius;
    }

    public float getGradientCenterX() {
        return mGradientCenterX;
    }

    public float getGradientCenterY() {
        return mGradientCenterY;
    }

    public int getGradientType() {
        return mGradientType;
    }

    public RBaseHelper setGradientRadius(float gradientRadius) {
        this.mGradientRadius = gradientRadius;
        setGradient();
        setBackgroundState();
        return this;
    }

    public RBaseHelper setGradientCenterX(float gradientCenterX) {
        this.mGradientCenterX = gradientCenterX;
        setGradient();
        setBackgroundState();
        return this;
    }

    public RBaseHelper setGradientCenterY(float gradientCenterY) {
        this.mGradientCenterY = gradientCenterY;
        setGradient();
        setBackgroundState();
        return this;
    }

    /**
     * 设置渐变样式/类型
     *
     * @param gradientType {LINEAR_GRADIENT=0, RADIAL_GRADIENT=1, SWEEP_GRADIENT=2}
     * @return
     */
    public RBaseHelper setGradientType(int gradientType) {
        if (gradientType < 0 || gradientType > 2) {
            gradientType = 0;
        }
        this.mGradientType = gradientType;
        setGradient();
        setBackgroundState();
        return this;
    }

    public RBaseHelper setGradientOrientation(GradientDrawable.Orientation orientation) {
        this.mGradientOrientation = orientation;
        setGradient();
        setBackgroundState();
        return this;
    }

    private void setGradient() {
        mBackgroundNormal.setGradientType(mGradientType);
        mBackgroundNormal.setGradientRadius(mGradientRadius);
        mBackgroundNormal.setGradientCenter(mGradientCenterX, mGradientCenterY);
        mBackgroundPressed.setGradientType(mGradientType);
        mBackgroundPressed.setGradientRadius(mGradientRadius);
        mBackgroundPressed.setGradientCenter(mGradientCenterX, mGradientCenterY);
        mBackgroundUnable.setGradientType(mGradientType);
        mBackgroundUnable.setGradientRadius(mGradientRadius);
        mBackgroundUnable.setGradientCenter(mGradientCenterX, mGradientCenterY);
        mBackgroundChecked.setGradientType(mGradientType);
        mBackgroundChecked.setGradientRadius(mGradientRadius);
        mBackgroundChecked.setGradientCenter(mGradientCenterX, mGradientCenterY);
    }

    /*********************
     * BackgroundColor
     ********************/

    public RBaseHelper setStateBackgroundColor(@ColorInt int normal, @ColorInt int pressed, @ColorInt int unable, @ColorInt int checked) {
        mBackgroundColorNormal = normal;
        mBackgroundColorPressed = pressed;
        mBackgroundColorUnable = unable;
        mBackgroundColorChecked = checked;
        mHasPressedBgColor = true;
        mHasUnableBgColor = true;
        mHasCheckedBgColor = true;
        mBackgroundNormal.setColor(mBackgroundColorNormal);
        mBackgroundPressed.setColor(mBackgroundColorPressed);
        mBackgroundUnable.setColor(mBackgroundColorUnable);
        mBackgroundChecked.setColor(mBackgroundColorChecked);
        setBackgroundState();
        return this;
    }

    public RBaseHelper setStateBackgroundColorArray(int[] normalArray, int[] pressedArray, int[] unableArray, int[] checkedArray) {
        mBackgroundColorNormalArray = normalArray;
        mBackgroundColorPressedArray = pressedArray;
        mBackgroundColorUnableArray = unableArray;
        mBackgroundColorCheckedArray = checkedArray;
        mHasPressedBgColor = true;
        mHasUnableBgColor = true;
        mHasCheckedBgColor = true;
        mBackgroundNormal = setColors(mBackgroundNormal, mBackgroundColorNormalArray);
        mBackgroundPressed = setColors(mBackgroundPressed, mBackgroundColorPressedArray);
        mBackgroundUnable = setColors(mBackgroundUnable, mBackgroundColorUnableArray);
        mBackgroundChecked = setColors(mBackgroundChecked, mBackgroundColorCheckedArray);
        setBorder();
        setRadiusUI();
        setGradient();
        setBackgroundState();
        return this;
    }

    public RBaseHelper setStateBackgroundColor(Drawable normal, Drawable pressed, Drawable unable, Drawable checked) {
        mBackgroundNormalBmp = normal;
        mBackgroundPressedBmp = pressed;
        mBackgroundUnableBmp = unable;
        mBackgroundCheckedBmp = checked;
        mHasPressedBgBmp = true;
        mHasUnableBgBmp = true;
        mHasCheckedBgBmp = true;
        refreshStateListDrawable();
        setBackgroundState();
        return this;
    }

    public int getBackgroundColorNormal() {
        return mBackgroundColorNormal;
    }

    public int getBackgroundColorPressed() {
        return mBackgroundColorPressed;
    }

    public int getBackgroundColorUnable() {
        return mBackgroundColorUnable;
    }

    public int getBackgroundColorChecked() {
        return mBackgroundColorChecked;
    }

    public int[] getBackgroundColorNormalArray() {
        return mBackgroundColorNormalArray;
    }

    public int[] getBackgroundColorPressedArray() {
        return mBackgroundColorPressedArray;
    }

    public int[] getBackgroundColorUnableArray() {
        return mBackgroundColorUnableArray;
    }

    public int[] getBackgroundColorCheckedArray() {
        return mBackgroundColorCheckedArray;
    }

    public Drawable getBackgroundDrawableNormal() {
        return mBackgroundNormalBmp;
    }

    public Drawable getBackgroundDrawablePressed() {
        return mBackgroundPressedBmp;
    }

    public Drawable getBackgroundDrawableUnable() {
        return mBackgroundUnableBmp;
    }

    public Drawable getBackgroundDrawableChecked() {
        return mBackgroundCheckedBmp;
    }

    public RBaseHelper setBackgroundColorNormal(@ColorInt int colorNormal) {
        this.mBackgroundColorNormal = colorNormal;
        /**
         * 设置背景默认值
         */
        if (!mHasPressedBgColor) {
            mBackgroundColorPressed = mBackgroundColorNormal;
            mBackgroundPressed.setColor(mBackgroundColorPressed);
        }
        if (!mHasUnableBgColor) {
            mBackgroundColorUnable = mBackgroundColorNormal;
            mBackgroundUnable.setColor(mBackgroundColorUnable);
        }
        if (!mHasCheckedBgColor) {
            mBackgroundColorChecked = mBackgroundColorNormal;
            mBackgroundChecked.setColor(mBackgroundColorChecked);
        }
        mBackgroundNormal.setColor(mBackgroundColorNormal);
        setBackgroundState();
        return this;
    }

    public RBaseHelper setBackgroundColorNormalArray(int[] colorNormalArray) {
        this.mBackgroundColorNormalArray = colorNormalArray;
        /**
         * 设置背景默认值
         */
        if (!mHasPressedBgColor) {
            mBackgroundColorPressedArray = mBackgroundColorNormalArray;
            mBackgroundPressed = setColors(mBackgroundPressed, mBackgroundColorPressedArray);
        }
        if (!mHasUnableBgColor) {
            mBackgroundColorUnableArray = mBackgroundColorNormalArray;
            mBackgroundUnable = setColors(mBackgroundUnable, mBackgroundColorUnableArray);
        }
        if (!mHasCheckedBgColor) {
            mBackgroundColorCheckedArray = mBackgroundColorNormalArray;
            mBackgroundChecked = setColors(mBackgroundChecked, mBackgroundColorCheckedArray);
        }
        mBackgroundNormal = setColors(mBackgroundNormal, mBackgroundColorNormalArray);

        setBorder();
        setRadiusUI();
        setGradient();
        setBackgroundState();
        return this;
    }

    public RBaseHelper setBackgroundDrawableNormal(Drawable drawableNormal) {
        this.mBackgroundNormalBmp = drawableNormal;
        /**
         * 设置背景默认值
         */
        if (!mHasPressedBgBmp) {
            mBackgroundPressedBmp = mBackgroundNormalBmp;
        }
        if (!mHasUnableBgBmp) {
            mBackgroundUnableBmp = mBackgroundNormalBmp;
        }
        if (!mHasCheckedBgBmp) {
            mBackgroundCheckedBmp = mBackgroundNormalBmp;
        }
        refreshStateListDrawable();
        setBackgroundState();
        return this;
    }

    public RBaseHelper setBackgroundColorPressed(@ColorInt int colorPressed) {
        this.mBackgroundColorPressed = colorPressed;
        this.mHasPressedBgColor = true;
        mBackgroundPressed.setColor(mBackgroundColorPressed);
        setBackgroundState();
        return this;
    }

    public RBaseHelper setBackgroundColorPressedArray(int[] colorPressedArray) {
        this.mBackgroundColorPressedArray = colorPressedArray;
        this.mHasPressedBgColor = true;
        mBackgroundPressed = setColors(mBackgroundPressed, mBackgroundColorPressedArray);
        setBorder();
        setRadiusUI();
        setGradient();
        setBackgroundState();
        return this;
    }

    public RBaseHelper setBackgroundDrawablePressed(Drawable drawablePressed) {
        this.mBackgroundPressedBmp = drawablePressed;
        this.mHasPressedBgBmp = true;
        refreshStateListDrawable();
        setBackgroundState();
        return this;
    }

    public RBaseHelper setBackgroundColorUnable(@ColorInt int colorUnable) {
        this.mBackgroundColorUnable = colorUnable;
        this.mHasUnableBgColor = true;
        mBackgroundUnable.setColor(mBackgroundColorUnable);
        setBackgroundState();
        return this;
    }

    public RBaseHelper setBackgroundDrawableUnable(Drawable drawableUnable) {
        this.mBackgroundUnableBmp = drawableUnable;
        this.mHasUnableBgBmp = true;
        refreshStateListDrawable();
        setBackgroundState();
        return this;
    }

    public RBaseHelper setBackgroundColorUnableArray(int[] colorUnableArray) {
        this.mBackgroundColorUnableArray = colorUnableArray;
        this.mHasUnableBgColor = true;
        mBackgroundUnable = setColors(mBackgroundUnable, mBackgroundColorUnableArray);
        setBorder();
        setRadiusUI();
        setGradient();
        setBackgroundState();
        return this;
    }

    public RBaseHelper setBackgroundColorChecked(@ColorInt int colorChecked) {
        this.mBackgroundColorChecked = colorChecked;
        this.mHasCheckedBgColor = true;
        mBackgroundChecked.setColor(mBackgroundColorChecked);
        setBackgroundState();
        return this;
    }

    public RBaseHelper setBackgroundColorCheckedArray(int[] colorCheckedArray) {
        this.mBackgroundColorCheckedArray = colorCheckedArray;
        this.mHasCheckedBgColor = true;
        mBackgroundChecked = setColors(mBackgroundChecked, mBackgroundColorCheckedArray);
        setBorder();
        setRadiusUI();
        setGradient();
        setBackgroundState();
        return this;
    }

    public RBaseHelper setBackgroundDrawableChecked(Drawable drawableChecked) {
        this.mBackgroundCheckedBmp = drawableChecked;
        this.mHasCheckedBgBmp = true;
        refreshStateListDrawable();
        setBackgroundState();
        return this;
    }

    /**
     * 刷新StateListDrawable状态
     * 更新drawable背景时时候刷新
     */
    private void refreshStateListDrawable() {
        mStateBackground = emptyStateListDrawable;
        mStateBackground.addState(states[0], mBackgroundPressedBmp == null ? mBackgroundPressed : mBackgroundPressedBmp);
        mStateBackground.addState(states[1], mBackgroundPressedBmp == null ? mBackgroundPressed : mBackgroundPressedBmp);
        mStateBackground.addState(states[3], mBackgroundUnableBmp == null ? mBackgroundUnable : mBackgroundUnableBmp);
        mStateBackground.addState(states[4], mBackgroundNormalBmp == null ? mBackgroundNormal : mBackgroundNormalBmp);
        mStateBackground.addState(states[5], mBackgroundCheckedBmp == null ? mBackgroundChecked : mBackgroundCheckedBmp);
        mStateBackground.addState(states[2], mBackgroundNormalBmp == null ? mBackgroundNormal : mBackgroundNormalBmp);
    }

    private void setBackgroundState() {

        boolean hasCustom = false;//是否存在自定义
        boolean hasCusBg, hasCusBorder = false, hasCusCorner = false;//存在自定义相关属性
        boolean unHasBgColor = mBackgroundColorNormal == 0 && mBackgroundColorUnable == 0 && mBackgroundColorPressed == 0 && mBackgroundColorChecked == 0;
        boolean unHasBgColorArray = mBackgroundColorNormalArray == null && mBackgroundColorUnableArray == null && mBackgroundColorPressedArray == null && mBackgroundColorCheckedArray == null;
        boolean unHasBgDrawable = mBackgroundNormalBmp == null && mBackgroundPressedBmp == null && mBackgroundUnableBmp == null && mBackgroundCheckedBmp == null;

        //是否自定义了背景
        if (unHasBgColor && unHasBgColorArray && unHasBgDrawable) {//未设置自定义背景
            hasCusBg = false;
        } else {
            hasCusBg = true;
        }

        //是否自定义了边框
        if (mBorderDashWidth != 0 || mBorderDashGap != 0 || mBorderWidthNormal != 0 || mBorderWidthPressed != 0 || mBorderWidthUnable != 0 || mBorderWidthChecked != 0
                || mBorderColorNormal != 0 || mBorderColorPressed != 0 || mBorderColorUnable != 0 || mBorderColorChecked != 0) {
            hasCusBorder = true;
        }

        //是否自定义了圆角
        if (mCornerRadius != 0 || mCornerRadiusTopLeft != 0 || mCornerRadiusTopRight != 0 || mCornerRadiusBottomLeft != 0 || mCornerRadiusBottomRight != 0) {
            hasCusCorner = true;
        }

        if (hasCusBg || hasCusCorner || hasCusBorder) {
            hasCustom = true;
        }


        /**
         * 未设置自定义属性,获取原生背景并且设置
         */
        Drawable drawable = mView.getBackground();
        if (!hasCustom && drawable instanceof ColorDrawable) {
            int color = ((ColorDrawable) drawable).getColor();
            setStateBackgroundColor(color, color, color, color);//获取背景颜色值设置 StateListDrawable
        }

        /**
         * 存在自定义属性，使用自定义属性设置
         */
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            mView.setBackgroundDrawable(hasCustom ? mStateBackground : drawable);
        } else {
            mView.setBackground(hasCustom ? mStateBackground : drawable);
        }
    }


    /*********************
     * border
     *********************/

    public RBaseHelper setBorderWidthNormal(int width) {
        this.mBorderWidthNormal = width;
        if (!mHasPressedBorderWidth) {
            mBorderWidthPressed = mBorderWidthNormal;
            setBorderPressed();
        }
        if (!mHasUnableBorderWidth) {
            mBorderWidthUnable = mBorderWidthNormal;
            setBorderUnable();
        }
        if (!mHasCheckedBorderWidth) {
            mBorderWidthChecked = mBorderWidthNormal;
            setBorderChecked();
        }
        setBorderNormal();
        return this;
    }

    public int getBorderWidthNormal() {
        return mBorderWidthNormal;
    }

    public RBaseHelper setBorderColorNormal(@ColorInt int color) {
        this.mBorderColorNormal = color;
        if (!mHasPressedBorderColor) {
            mBorderColorPressed = mBorderColorNormal;
            setBorderPressed();
        }
        if (!mHasUnableBorderColor) {
            mBorderColorUnable = mBorderColorNormal;
            setBorderUnable();
        }
        if (!mHasCheckedBorderColor) {
            mBorderColorChecked = mBorderColorNormal;
            setBorderChecked();
        }
        setBorderNormal();
        return this;
    }

    public int getBorderColorNormal() {
        return mBorderColorNormal;
    }

    public RBaseHelper setBorderWidthPressed(int width) {
        this.mBorderWidthPressed = width;
        this.mHasPressedBorderWidth = true;
        setBorderPressed();
        return this;
    }

    public int getBorderWidthPressed() {
        return mBorderWidthPressed;
    }

    public RBaseHelper setBorderColorPressed(@ColorInt int color) {
        this.mBorderColorPressed = color;
        this.mHasPressedBorderColor = true;
        setBorderPressed();
        return this;
    }

    public int getBorderColorPressed() {
        return mBorderColorPressed;
    }

    public RBaseHelper setBorderColorChecked(@ColorInt int color) {
        this.mBorderColorChecked = color;
        this.mHasCheckedBorderColor = true;
        setBorderChecked();
        return this;
    }

    public int getBorderColorChecked() {
        return mBorderColorChecked;
    }

    public RBaseHelper setBorderWidthChecked(int width) {
        this.mBorderWidthChecked = width;
        this.mHasCheckedBorderWidth = true;
        setBorderChecked();
        return this;
    }

    public int getBorderWidthChecked() {
        return mBorderWidthChecked;
    }

    public RBaseHelper setBorderWidthUnable(int width) {
        this.mBorderWidthUnable = width;
        this.mHasUnableBorderWidth = true;
        setBorderUnable();
        return this;
    }

    public int getBorderWidthUnable() {
        return mBorderWidthUnable;
    }

    public RBaseHelper setBorderColorUnable(@ColorInt int color) {
        this.mBorderColorUnable = color;
        this.mHasUnableBorderColor = true;
        setBorderUnable();
        return this;
    }

    public int getBorderColorUnable() {
        return mBorderColorUnable;
    }

    public RBaseHelper setBorderWidth(int normal, int pressed, int unable, int checked) {
        this.mBorderWidthNormal = normal;
        this.mBorderWidthPressed = pressed;
        this.mBorderWidthUnable = unable;
        this.mBorderWidthChecked = checked;
        this.mHasPressedBorderWidth = true;
        this.mHasUnableBorderWidth = true;
        this.mHasCheckedBorderWidth = true;
        setBorder();
        return this;
    }

    public RBaseHelper setBorderColor(@ColorInt int normal, @ColorInt int pressed, @ColorInt int unable, @ColorInt int checked) {
        this.mBorderColorNormal = normal;
        this.mBorderColorPressed = pressed;
        this.mBorderColorUnable = unable;
        this.mBorderColorChecked = checked;
        this.mHasPressedBorderColor = true;
        this.mHasUnableBorderColor = true;
        this.mHasCheckedBorderColor = true;
        setBorder();
        return this;
    }

    public RBaseHelper setBorderDashWidth(float dashWidth) {
        this.mBorderDashWidth = dashWidth;
        setBorder();
        return this;
    }

    public float getBorderDashWidth() {
        return mBorderDashWidth;
    }

    public RBaseHelper setBorderDashGap(float dashGap) {
        this.mBorderDashGap = dashGap;
        setBorder();
        return this;
    }

    public float getBorderDashGap() {
        return mBorderDashGap;
    }

    public RBaseHelper setBorderDash(float dashWidth, float dashGap) {
        this.mBorderDashWidth = dashWidth;
        this.mBorderDashGap = dashGap;
        setBorder();
        return this;
    }

    private void setBorder() {
        mBackgroundNormal.setStroke(mBorderWidthNormal, mBorderColorNormal, mBorderDashWidth, mBorderDashGap);
        mBackgroundPressed.setStroke(mBorderWidthPressed, mBorderColorPressed, mBorderDashWidth, mBorderDashGap);
        mBackgroundUnable.setStroke(mBorderWidthUnable, mBorderColorUnable, mBorderDashWidth, mBorderDashGap);
        mBackgroundChecked.setStroke(mBorderWidthChecked, mBorderColorChecked, mBorderDashWidth, mBorderDashGap);
        setBackgroundState();
    }

    private void setBorderNormal() {
        mBackgroundNormal.setStroke(mBorderWidthNormal, mBorderColorNormal, mBorderDashWidth, mBorderDashGap);
        setBackgroundState();
    }

    private void setBorderPressed() {
        mBackgroundPressed.setStroke(mBorderWidthPressed, mBorderColorPressed, mBorderDashWidth, mBorderDashGap);
        setBackgroundState();
    }

    private void setBorderUnable() {
        mBackgroundUnable.setStroke(mBorderWidthUnable, mBorderColorUnable, mBorderDashWidth, mBorderDashGap);
        setBackgroundState();
    }

    private void setBorderChecked() {
        mBackgroundChecked.setStroke(mBorderWidthChecked, mBorderColorChecked, mBorderDashWidth, mBorderDashGap);
        setBackgroundState();
    }

    /*********************
     * radius
     ********************/

    public void setCornerRadius(float radius) {
        this.mCornerRadius = radius;
        setRadiusValue();
    }

    public float getCornerRadius() {
        return mCornerRadius;
    }

    public RBaseHelper setCornerRadiusTopLeft(float topLeft) {
        this.mCornerRadius = -1;
        this.mCornerRadiusTopLeft = topLeft;
        setRadiusValue();
        return this;
    }

    public float getCornerRadiusTopLeft() {
        return mCornerRadiusTopLeft;
    }

    public RBaseHelper setCornerRadiusTopRight(float topRight) {
        this.mCornerRadius = -1;
        this.mCornerRadiusTopRight = topRight;
        setRadiusValue();
        return this;
    }

    public float getCornerRadiusTopRight() {
        return mCornerRadiusTopRight;
    }

    public RBaseHelper setCornerRadiusBottomRight(float bottomRight) {
        this.mCornerRadius = -1;
        this.mCornerRadiusBottomRight = bottomRight;
        setRadiusValue();
        return this;
    }

    public float getCornerRadiusBottomRight() {
        return mCornerRadiusBottomRight;
    }

    public RBaseHelper setCornerRadiusBottomLeft(float bottomLeft) {
        this.mCornerRadius = -1;
        this.mCornerRadiusBottomLeft = bottomLeft;
        setRadiusValue();
        return this;
    }

    public float getCornerRadiusBottomLeft() {
        return mCornerRadiusBottomLeft;
    }

    public RBaseHelper setCornerRadius(float topLeft, float topRight, float bottomRight, float bottomLeft) {
        this.mCornerRadius = -1;
        this.mCornerRadiusTopLeft = topLeft;
        this.mCornerRadiusTopRight = topRight;
        this.mCornerRadiusBottomRight = bottomRight;
        this.mCornerRadiusBottomLeft = bottomLeft;
        setRadiusValue();
        return this;
    }

    /**
     * 设置圆角UI
     */
    private void setRadiusUI() {
        mBackgroundNormal.setCornerRadii(mBorderRadii);
        mBackgroundPressed.setCornerRadii(mBorderRadii);
        mBackgroundUnable.setCornerRadii(mBorderRadii);
        mBackgroundChecked.setCornerRadii(mBorderRadii);
        setBackgroundState();
    }

    /**
     * 设置圆角数值
     */
    private void setRadiusValue() {
        if (mCornerRadius >= 0) {
            mBorderRadii[0] = mCornerRadius;
            mBorderRadii[1] = mCornerRadius;
            mBorderRadii[2] = mCornerRadius;
            mBorderRadii[3] = mCornerRadius;
            mBorderRadii[4] = mCornerRadius;
            mBorderRadii[5] = mCornerRadius;
            mBorderRadii[6] = mCornerRadius;
            mBorderRadii[7] = mCornerRadius;
            setRadiusUI();
            return;
        }

        if (mCornerRadius < 0) {
            mBorderRadii[0] = mCornerRadiusTopLeft;
            mBorderRadii[1] = mCornerRadiusTopLeft;
            mBorderRadii[2] = mCornerRadiusTopRight;
            mBorderRadii[3] = mCornerRadiusTopRight;
            mBorderRadii[4] = mCornerRadiusBottomRight;
            mBorderRadii[5] = mCornerRadiusBottomRight;
            mBorderRadii[6] = mCornerRadiusBottomLeft;
            mBorderRadii[7] = mCornerRadiusBottomLeft;
            setRadiusUI();
            return;
        }
    }

    /**
     * 设置View大小变化监听,用来更新渐变半径
     */
    private void addOnGlobalLayoutListener() {
        if (mView == null) return;
        mView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                if (mGradientRadius <= 0) {
                    int width = mView.getWidth();
                    int height = mView.getHeight();
                    float radius = Math.min(width, height) / 2f;
                    setGradientRadius(radius);
                }
            }
        });
    }

    /**
     * 获取背景颜色
     * 备注:
     * 数组[0]:背景类型(1:单一颜色 2:颜色数组 3:图片资源)
     * 数组[1]:单一颜色,可能为0
     * 数组[2]:颜色数组,可能为null
     * 数组[3]:图片资源drawable,可能为null
     *
     * @param a
     * @param styleableRes
     * @return
     */
    private Object[] getBackgroundInfo(TypedArray a, @StyleableRes int styleableRes) {
        int bgType = BG_TYPE_COLOR;//背景类型
        int bgColor = 0;//单一颜色
        int[] bgColorArray = null;//多个颜色
        Drawable drawable = null;//图片资源

        //resId==0表示直接使用#cccccc这种模式，不为0说明可以获取到资源类型
        int resId = a.getResourceId(styleableRes, 0);
        if (resId == 0) {//#cccccc
            bgColor = a.getColor(styleableRes, 0);
            bgType = BG_TYPE_COLOR;
        } else {//其他资源类型
            String typeName = mContext.getResources().getResourceTypeName(resId);
            if ("array".equals(typeName)) {//color-array
                bgType = BG_TYPE_COLOR_ARRAY;
                String[] strArray = mContext.getResources().getStringArray(resId);
                int[] intArray = mContext.getResources().getIntArray(resId);
                int length = Math.min(intArray.length, strArray.length);
                bgColorArray = new int[length];
                String strIndex;
                int intIndex;
                for (int i = 0; i < length; i++) {
                    strIndex = strArray[i];
                    intIndex = intArray[i];
                    bgColorArray[i] = !TextUtils.isEmpty(strIndex) ? Color.parseColor(strIndex) : intIndex;
                }
            } else if ("color".equals(typeName)) {//color
                bgColor = a.getColor(styleableRes, 0);
                bgType = BG_TYPE_COLOR;
            } else if ("mipmap".equals(typeName) || "drawable".equals(typeName)) {//image
                bgType = BG_TYPE_IMG;
                drawable = a.getDrawable(styleableRes);
            }
        }
        return new Object[]{bgType, bgColor, bgColorArray, drawable};
    }

    /**
     * 获取渐变方向
     *
     * @param a
     * @return
     */
    private GradientDrawable.Orientation getGradientOrientation(TypedArray a) {
        GradientDrawable.Orientation orientation = GradientDrawable.Orientation.BL_TR;
        int gradientOrientation = a.getInt(R.styleable.RBaseView_gradient_orientation, 0);
        switch (gradientOrientation) {
            case 0:
                orientation = GradientDrawable.Orientation.TOP_BOTTOM;
                break;
            case 1:
                orientation = GradientDrawable.Orientation.TR_BL;
                break;
            case 2:
                orientation = GradientDrawable.Orientation.RIGHT_LEFT;
                break;
            case 3:
                orientation = GradientDrawable.Orientation.BR_TL;
                break;
            case 4:
                orientation = GradientDrawable.Orientation.BOTTOM_TOP;
                break;
            case 5:
                orientation = GradientDrawable.Orientation.BL_TR;
                break;
            case 6:
                orientation = GradientDrawable.Orientation.LEFT_RIGHT;
                break;
            case 7:
                orientation = GradientDrawable.Orientation.TL_BR;
                break;
        }
        return orientation;
    }

    /**
     * 设置GradientDrawable颜色数组,兼容版本
     *
     * @param drawable GradientDrawable
     * @param colors   颜色数组
     * @return
     */
    private GradientDrawable setColors(GradientDrawable drawable, int[] colors) {
        if (drawable == null) drawable = new GradientDrawable();
        //版本兼容
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {//>=16
            drawable.setOrientation(mGradientOrientation);
            drawable.setColors(colors);
        } else {//<16
            drawable = new GradientDrawable(mGradientOrientation, colors);
        }
        return drawable;
    }

    /**
     * 是否移出view
     *
     * @param x
     * @param y
     * @return
     */
    protected boolean isOutsideView(int x, int y) {
        boolean flag = false;
        // Be lenient about moving outside of buttons
        if ((x < 0 - mTouchSlop) || (x >= mView.getWidth() + mTouchSlop) ||
                (y < 0 - mTouchSlop) || (y >= mView.getHeight() + mTouchSlop)) {
            // Outside button
            flag = true;
        }
        return flag;
    }

    /**
     * 单位转换dp2px
     *
     * @param dp dp
     * @return
     */
    protected float dp2px(int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, mContext.getResources().getDisplayMetrics());
    }

}
