package com.ruffian.library.widget.helper;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.RequiresApi;
import android.support.annotation.StyleableRes;
import android.support.v4.text.TextUtilsCompat;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;

import com.ruffian.library.widget.R;
import com.ruffian.library.widget.clip.ClipHelper;
import com.ruffian.library.widget.clip.ClipPathManager;
import com.ruffian.library.widget.clip.IClip;
import com.ruffian.library.widget.shadow.ShadowBitmapDrawable;
import com.ruffian.library.widget.utils.RippleDrawableUtils;

import java.util.Locale;


/**
 * BaseHelper
 *
 * @author ZhongDaFeng
 */
public class RBaseHelper<T extends View> implements IClip, ViewTreeObserver.OnGlobalLayoutListener {
    //默认值 _C:颜色 _S:尺寸/宽高
    protected final int _C = 0, _S = -1;

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
    private float mBorderDashWidth = _S;
    private float mBorderDashGap = _S;
    private int mBorderWidthNormal = _S;
    private int mBorderWidthPressed = _S;
    private int mBorderWidthUnable = _S;
    private int mBorderWidthChecked = _S;
    private int mBorderWidthSelected = _S;

    //BorderColor
    private int mBorderColorNormal = _C;
    private int mBorderColorPressed = _C;
    private int mBorderColorUnable = _C;
    private int mBorderColorChecked = _C;
    private int mBorderColorSelected = _C;

    //Background
    private int mBackgroundColorNormal = _C;
    private int mBackgroundColorPressed = _C;
    private int mBackgroundColorUnable = _C;
    private int mBackgroundColorChecked = _C;
    private int mBackgroundColorSelected = _C;

    //BackgroundColorArray
    private int[] mBackgroundColorNormalArray;
    private int[] mBackgroundColorPressedArray;
    private int[] mBackgroundColorUnableArray;
    private int[] mBackgroundColorCheckedArray;
    private int[] mBackgroundColorSelectedArray;
    private GradientDrawable mBackgroundNormal;
    private GradientDrawable mBackgroundPressed;
    private GradientDrawable mBackgroundUnable;
    private GradientDrawable mBackgroundChecked;
    private GradientDrawable mBackgroundSelected;
    private Drawable mBackgroundNormalBmp;
    private Drawable mBackgroundPressedBmp;
    private Drawable mBackgroundUnableBmp;
    private Drawable mBackgroundCheckedBmp;
    private Drawable mBackgroundSelectedBmp;
    //Gradient
    private int mGradientType = 0;
    private float mGradientRadius;
    private float mGradientCenterX, mGradientCenterY;
    private GradientDrawable.Orientation mGradientOrientation = GradientDrawable.Orientation.TOP_BOTTOM;

    //shadow
    private ShadowBitmapDrawable mShadowDrawable;
    private int mShadowDx;
    private int mShadowDy;
    private int mShadowColor;
    private int mShadowRadius;

    //View/ViewGroup是否可用
    private boolean mIsEnabled = true;

    //ripple
    private boolean mUseRipple;
    private int mRippleColor;
    private Drawable mRippleMaskDrawable;
    //Ripple波纹限制样式{null, normal=控件矩形, drawable=自定义drawable}
    private int mRippleMaskStyle;
    //null normal drawable
    private final int MASK_STYLE_NULL = 1, MASK_STYLE_NORMAL = 2, MASK_STYLE_DRAWABLE = 3;

    private Drawable mViewBackground;//控件默认背景Drawable
    private Drawable mBackgroundDrawable;

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
    private boolean mHasSelectedBgColor = false;
    private boolean mHasCheckedBgBmp = false;
    private boolean mHasSelectedBgBmp = false;
    private boolean mHasPressedBorderColor = false;
    private boolean mHasUnableBorderColor = false;
    private boolean mHasCheckedBorderColor = false;
    private boolean mHasSelectedBorderColor = false;
    private boolean mHasPressedBorderWidth = false;
    private boolean mHasUnableBorderWidth = false;
    private boolean mHasCheckedBorderWidth = false;
    private boolean mHasSelectedBorderWidth = false;

    // view
    protected T mView;

    //ClipHelper
    protected ClipHelper mClipHelper = new ClipHelper();
    //clipLayout
    private boolean mClipLayout = false;

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
        mBorderDashWidth = a.getDimensionPixelSize(R.styleable.RBaseView_border_dash_width, _S);
        mBorderDashGap = a.getDimensionPixelSize(R.styleable.RBaseView_border_dash_gap, _S);
        mBorderWidthNormal = a.getDimensionPixelSize(R.styleable.RBaseView_border_width_normal, _S);
        mBorderWidthPressed = a.getDimensionPixelSize(R.styleable.RBaseView_border_width_pressed, _S);
        mBorderWidthUnable = a.getDimensionPixelSize(R.styleable.RBaseView_border_width_unable, _S);
        mBorderWidthChecked = a.getDimensionPixelSize(R.styleable.RBaseView_border_width_checked, _S);
        mBorderWidthSelected = a.getDimensionPixelSize(R.styleable.RBaseView_border_width_selected, _S);
        mBorderColorNormal = a.getColor(R.styleable.RBaseView_border_color_normal, _C);
        mBorderColorPressed = a.getColor(R.styleable.RBaseView_border_color_pressed, _C);
        mBorderColorUnable = a.getColor(R.styleable.RBaseView_border_color_unable, _C);
        mBorderColorChecked = a.getColor(R.styleable.RBaseView_border_color_checked, _C);
        mBorderColorSelected = a.getColor(R.styleable.RBaseView_border_color_selected, _C);
        //background
        //normal
        Object[] bgInfoNormal = getBackgroundInfo(a, R.styleable.RBaseView_background_normal);
        mBackgroundColorNormal = (int) bgInfoNormal[1];
        mBackgroundColorNormalArray = (int[]) bgInfoNormal[2];
        mBackgroundNormalBmp = (Drawable) bgInfoNormal[3];
        //pressed
        Object[] bgInfoPressed = getBackgroundInfo(a, R.styleable.RBaseView_background_pressed);
        mBackgroundColorPressed = (int) bgInfoPressed[1];
        mBackgroundColorPressedArray = (int[]) bgInfoPressed[2];
        mBackgroundPressedBmp = (Drawable) bgInfoPressed[3];
        //unable
        Object[] bgInfoUnable = getBackgroundInfo(a, R.styleable.RBaseView_background_unable);
        mBackgroundColorUnable = (int) bgInfoUnable[1];
        mBackgroundColorUnableArray = (int[]) bgInfoUnable[2];
        mBackgroundUnableBmp = (Drawable) bgInfoUnable[3];
        //checked
        Object[] bgInfoChecked = getBackgroundInfo(a, R.styleable.RBaseView_background_checked);
        mBackgroundColorChecked = (int) bgInfoChecked[1];
        mBackgroundColorCheckedArray = (int[]) bgInfoChecked[2];
        mBackgroundCheckedBmp = (Drawable) bgInfoChecked[3];
        //selected
        Object[] bgInfoSelected = getBackgroundInfo(a, R.styleable.RBaseView_background_selected);
        mBackgroundColorSelected = (int) bgInfoSelected[1];
        mBackgroundColorSelectedArray = (int[]) bgInfoSelected[2];
        mBackgroundSelectedBmp = (Drawable) bgInfoSelected[3];
        //gradient
        mGradientType = a.getInt(R.styleable.RBaseView_gradient_type, 0);
        mGradientOrientation = getGradientOrientation(a);
        mGradientRadius = a.getDimensionPixelSize(R.styleable.RBaseView_gradient_radius, -1);
        mGradientCenterX = a.getFloat(R.styleable.RBaseView_gradient_centerX, 0.5f);
        mGradientCenterY = a.getFloat(R.styleable.RBaseView_gradient_centerY, 0.5f);
        //enabled
        mIsEnabled = a.getBoolean(R.styleable.RBaseView_enabled, true);
        //Ripple
        mUseRipple = a.getBoolean(R.styleable.RBaseView_ripple, false);
        mRippleColor = a.getColor(R.styleable.RBaseView_ripple_color, Color.RED);
        mRippleMaskDrawable = a.getDrawable(R.styleable.RBaseView_ripple_mask);
        mRippleMaskStyle = a.getInt(R.styleable.RBaseView_ripple_mask_style, MASK_STYLE_NORMAL);
        //shadow
        mShadowDx = a.getDimensionPixelSize(R.styleable.RBaseView_shadow_dx, 0);
        mShadowDy = a.getDimensionPixelSize(R.styleable.RBaseView_shadow_dy, 0);
        mShadowColor = a.getColor(R.styleable.RBaseView_shadow_color, Color.GRAY);
        mShadowRadius = a.getDimensionPixelSize(R.styleable.RBaseView_shadow_radius, -1);
        //clip
        mClipLayout = a.getBoolean(R.styleable.RBaseView_clip_layout, false);

        a.recycle();

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
        mBackgroundSelected = new GradientDrawable();
        mViewBackground = mView.getBackground();
        mStateBackground = new StateListDrawable();
        //unable,focused,pressed,checked,selected,normal
        states[0] = new int[]{-android.R.attr.state_enabled};//unable
        states[1] = new int[]{android.R.attr.state_focused};//focused
        states[2] = new int[]{android.R.attr.state_pressed};//pressed
        states[3] = new int[]{android.R.attr.state_checked};//checked
        states[4] = new int[]{android.R.attr.state_selected};//selected
        states[5] = new int[]{android.R.attr.state_enabled};//normal

        //设置默认值
        setupDefaultValue(true);

        //设置渐变相关Gradient
        setGradient();

        //设置背景值
        setStateListDrawable();

        //设置边框
        setBorder();

        //设置圆角
        setRadius();

        //更新背景状态
        setBackgroundState();
    }


    /**
     * 设置默认值
     * 备注: Pressed 未设置时 赋值逻辑
     * 1.优先设置  Pressed ==  Checked 状态的值
     * 2.其次设置  Pressed ==  Selected 状态的值
     * 3.最终设置  Pressed ==  Normal 状态的值
     *
     * @param init 初始化
     */
    private void setupDefaultValue(boolean init) {

        //基础判断
        if (init) {//初始化时需要做基础判断
            mHasPressedBgColor = mBackgroundColorPressed != _C || mBackgroundColorPressedArray != null;
            mHasUnableBgColor = mBackgroundColorUnable != _C || mBackgroundColorUnableArray != null;
            mHasCheckedBgColor = mBackgroundColorChecked != _C || mBackgroundColorCheckedArray != null;
            mHasSelectedBgColor = mBackgroundColorSelected != _C || mBackgroundColorSelectedArray != null;
            mHasPressedBgBmp = mBackgroundPressedBmp != null;
            mHasUnableBgBmp = mBackgroundUnableBmp != null;
            mHasCheckedBgBmp = mBackgroundCheckedBmp != null;
            mHasSelectedBgBmp = mBackgroundSelectedBmp != null;
            mHasPressedBorderColor = mBorderColorPressed != _C;
            mHasUnableBorderColor = mBorderColorUnable != _C;
            mHasCheckedBorderColor = mBorderColorChecked != _C;
            mHasSelectedBorderColor = mBorderColorSelected != _C;
            mHasPressedBorderWidth = mBorderWidthPressed != _S;
            mHasUnableBorderWidth = mBorderWidthUnable != _S;
            mHasCheckedBorderWidth = mBorderWidthChecked != _S;
            mHasSelectedBorderWidth = mBorderWidthSelected != _S;
        }

        //设置背景默认值
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
        if (!mHasSelectedBgColor) {
            mBackgroundColorSelected = mBackgroundColorNormal;
            mBackgroundColorSelectedArray = mBackgroundColorNormalArray;
        }
        if (!mHasCheckedBgBmp) {
            mBackgroundCheckedBmp = mBackgroundNormalBmp;
        }
        if (!mHasSelectedBgBmp) {
            mBackgroundSelectedBmp = mBackgroundNormalBmp;
        }

        //设置背景颜色（包含渐变）
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
        if (mBackgroundColorSelectedArray != null && mBackgroundColorSelectedArray.length > 0) {
            mBackgroundSelected = setColors(mBackgroundSelected, mBackgroundColorSelectedArray);
        } else {
            mBackgroundSelected.setColor(mBackgroundColorSelected);
        }

        //设置边框默认值
        if (!mHasPressedBorderWidth) {
            mBorderWidthPressed = mBorderWidthNormal;
        }
        if (!mHasUnableBorderWidth) {
            mBorderWidthUnable = mBorderWidthNormal;
        }
        if (!mHasCheckedBorderWidth) {
            mBorderWidthChecked = mBorderWidthNormal;
        }
        if (!mHasSelectedBorderWidth) {
            mBorderWidthSelected = mBorderWidthNormal;
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
        if (!mHasSelectedBorderColor) {
            mBorderColorSelected = mBorderColorNormal;
        }
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
        mBackgroundSelected.setGradientType(mGradientType);
        mBackgroundSelected.setGradientRadius(mGradientRadius);
        mBackgroundSelected.setGradientCenter(mGradientCenterX, mGradientCenterY);
    }

    /*********************
     * BackgroundColor
     ********************/

    public RBaseHelper setStateBackgroundColor(@ColorInt int normal, @ColorInt int pressed, @ColorInt int unable, @ColorInt int checked, @ColorInt int selected) {
        mBackgroundColorNormal = normal;
        mBackgroundColorPressed = pressed;
        mBackgroundColorUnable = unable;
        mBackgroundColorChecked = checked;
        mBackgroundColorSelected = selected;

        mBackgroundColorNormalArray = null;
        mBackgroundColorPressedArray = null;
        mBackgroundColorUnableArray = null;
        mBackgroundColorCheckedArray = null;
        mBackgroundColorSelectedArray = null;

        mBackgroundNormalBmp = null;
        mBackgroundPressedBmp = null;
        mBackgroundSelectedBmp = null;
        mBackgroundCheckedBmp = null;

        mHasPressedBgColor = true;
        mHasCheckedBgColor = true;
        mHasSelectedBgColor = true;
        mHasUnableBgColor = true;

        mHasPressedBgBmp = false;
        mHasCheckedBgBmp = false;
        mHasSelectedBgBmp = false;
        mHasUnableBgBmp = false;

        updateBackgroundValue();
        return this;
    }

    public RBaseHelper setStateBackgroundColorArray(int[] normalArray, int[] pressedArray, int[] unableArray, int[] checkedArray, int[] selectedArray) {
        mBackgroundColorNormalArray = normalArray;
        mBackgroundColorPressedArray = pressedArray;
        mBackgroundColorUnableArray = unableArray;
        mBackgroundColorCheckedArray = checkedArray;
        mBackgroundColorSelectedArray = selectedArray;

        mBackgroundColorNormal = _C;
        mBackgroundColorPressed = _C;
        mBackgroundColorUnable = _C;
        mBackgroundColorChecked = _C;
        mBackgroundColorSelected = _C;

        mBackgroundNormalBmp = null;
        mBackgroundPressedBmp = null;
        mBackgroundSelectedBmp = null;
        mBackgroundCheckedBmp = null;

        mHasPressedBgColor = true;
        mHasCheckedBgColor = true;
        mHasSelectedBgColor = true;
        mHasUnableBgColor = true;

        mHasPressedBgBmp = false;
        mHasCheckedBgBmp = false;
        mHasSelectedBgBmp = false;
        mHasUnableBgBmp = false;

        updateBackgroundValue();
        return this;
    }

    public RBaseHelper setStateBackgroundColor(Drawable normal, Drawable pressed, Drawable unable, Drawable checked, Drawable selected) {
        mBackgroundNormalBmp = normal;
        mBackgroundPressedBmp = pressed;
        mBackgroundUnableBmp = unable;
        mBackgroundCheckedBmp = checked;
        mBackgroundSelectedBmp = selected;

        mBackgroundColorNormalArray = null;
        mBackgroundColorPressedArray = null;
        mBackgroundColorUnableArray = null;
        mBackgroundColorCheckedArray = null;
        mBackgroundColorSelectedArray = null;

        mBackgroundColorNormal = _C;
        mBackgroundColorPressed = _C;
        mBackgroundColorUnable = _C;
        mBackgroundColorChecked = _C;
        mBackgroundColorSelected = _C;

        mHasPressedBgColor = false;
        mHasCheckedBgColor = false;
        mHasSelectedBgColor = false;
        mHasUnableBgColor = false;

        mHasPressedBgBmp = true;
        mHasCheckedBgBmp = true;
        mHasSelectedBgBmp = true;
        mHasUnableBgBmp = true;

        updateBackgroundValue();
        return this;
    }

    public RBaseHelper setBackgroundColorNormal(@ColorInt int colorNormal) {
        this.mBackgroundColorNormal = colorNormal;
        mBackgroundColorNormalArray = null;
        mBackgroundNormalBmp = null;
        updateBackgroundValue();
        return this;
    }

    public RBaseHelper setBackgroundColorNormalArray(int[] colorNormalArray) {
        this.mBackgroundColorNormalArray = colorNormalArray;
        mBackgroundColorNormal = _C;
        mBackgroundNormalBmp = null;
        updateBackgroundValue();
        return this;
    }

    public RBaseHelper setBackgroundDrawableNormal(Drawable drawableNormal) {
        this.mBackgroundNormalBmp = drawableNormal;
        this.mBackgroundColorNormalArray = null;
        mBackgroundColorNormal = _C;
        updateBackgroundValue();
        return this;
    }

    public RBaseHelper setBackgroundColorPressed(@ColorInt int colorPressed) {
        this.mBackgroundColorPressed = colorPressed;
        mBackgroundColorPressedArray = null;
        mBackgroundPressedBmp = null;
        mHasPressedBgColor = true;
        mHasPressedBgBmp = false;
        updateBackgroundValue();
        return this;
    }

    public RBaseHelper setBackgroundColorPressedArray(int[] colorPressedArray) {
        this.mBackgroundColorPressedArray = colorPressedArray;
        mBackgroundColorPressed = _C;
        mBackgroundPressedBmp = null;
        mHasPressedBgColor = true;
        mHasPressedBgBmp = false;
        updateBackgroundValue();
        return this;
    }

    public RBaseHelper setBackgroundDrawablePressed(Drawable drawablePressed) {
        this.mBackgroundPressedBmp = drawablePressed;
        this.mBackgroundColorPressedArray = null;
        mBackgroundColorPressed = _C;
        mHasPressedBgColor = false;
        mHasPressedBgBmp = true;
        updateBackgroundValue();
        return this;
    }

    public RBaseHelper setBackgroundColorUnable(@ColorInt int colorUnable) {
        this.mBackgroundColorUnable = colorUnable;
        mBackgroundColorUnableArray = null;
        mBackgroundUnableBmp = null;
        mHasUnableBgColor = true;
        mHasUnableBgBmp = false;
        updateBackgroundValue();
        return this;
    }

    public RBaseHelper setBackgroundDrawableUnable(Drawable drawableUnable) {
        this.mBackgroundUnableBmp = drawableUnable;
        this.mBackgroundColorUnable = _C;
        mBackgroundColorUnableArray = null;
        mHasUnableBgColor = false;
        mHasUnableBgBmp = true;
        updateBackgroundValue();
        return this;
    }

    public RBaseHelper setBackgroundColorUnableArray(int[] colorUnableArray) {
        this.mBackgroundColorUnableArray = colorUnableArray;
        this.mBackgroundColorUnable = _C;
        mBackgroundUnableBmp = null;
        mHasUnableBgColor = true;
        mHasUnableBgBmp = false;
        updateBackgroundValue();
        return this;
    }

    public RBaseHelper setBackgroundColorChecked(@ColorInt int colorChecked) {
        this.mBackgroundColorChecked = colorChecked;
        mBackgroundColorCheckedArray = null;
        mBackgroundCheckedBmp = null;
        mHasCheckedBgColor = true;
        mHasCheckedBgBmp = false;
        updateBackgroundValue();
        return this;
    }

    public RBaseHelper setBackgroundColorSelected(@ColorInt int colorSelected) {
        this.mBackgroundColorSelected = colorSelected;
        mBackgroundColorSelectedArray = null;
        mBackgroundSelectedBmp = null;
        mHasSelectedBgColor = true;
        mHasSelectedBgBmp = false;
        updateBackgroundValue();
        return this;
    }

    public RBaseHelper setBackgroundColorCheckedArray(int[] colorCheckedArray) {
        this.mBackgroundColorCheckedArray = colorCheckedArray;
        mBackgroundColorChecked = _C;
        mBackgroundCheckedBmp = null;
        mHasCheckedBgColor = true;
        mHasCheckedBgBmp = false;
        updateBackgroundValue();
        return this;
    }

    public RBaseHelper setBackgroundColorSelectedArray(int[] colorSelectedArray) {
        this.mBackgroundColorSelectedArray = colorSelectedArray;
        mBackgroundColorSelected = _C;
        mBackgroundSelectedBmp = null;
        mHasSelectedBgColor = true;
        mHasSelectedBgBmp = false;
        updateBackgroundValue();
        return this;
    }

    public RBaseHelper setBackgroundDrawableChecked(Drawable drawableChecked) {
        this.mBackgroundCheckedBmp = drawableChecked;
        mBackgroundColorChecked = _C;
        mBackgroundColorCheckedArray = null;
        mHasCheckedBgColor = false;
        mHasCheckedBgBmp = true;
        updateBackgroundValue();
        return this;
    }

    public RBaseHelper setBackgroundDrawableSelected(Drawable drawableSelected) {
        this.mBackgroundSelectedBmp = drawableSelected;
        mBackgroundColorSelected = _C;
        mBackgroundColorSelectedArray = null;
        mHasSelectedBgColor = false;
        mHasSelectedBgBmp = true;
        updateBackgroundValue();
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

    public int getBackgroundColorSelected() {
        return mBackgroundColorSelected;
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

    public int[] getBackgroundColorSelectedArray() {
        return mBackgroundColorSelectedArray;
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

    public Drawable getBackgroundDrawableSelected() {
        return mBackgroundSelectedBmp;
    }


    /**
     * 更新背景值(单一颜色，渐变，背景图)
     */
    private void updateBackgroundValue() {
        setupDefaultValue(false);
        setStateListDrawable();
        setBackgroundState();
    }

    /**
     * 刷新StateListDrawable状态
     * 更新drawable背景时时候刷新
     */
    private void setStateListDrawable() {
        mStateBackground = new StateListDrawable();


        //unable,focused,pressed,checked,selected,normal
        //android studio 预览：不支持直接三目运算赋值

        if (mBackgroundUnableBmp == null) {
            mStateBackground.addState(states[0], mBackgroundUnable);
        } else {
            mStateBackground.addState(states[0], mBackgroundUnableBmp);
        }

        if (mBackgroundPressedBmp == null) {
            mStateBackground.addState(states[1], mBackgroundPressed);
        } else {
            mStateBackground.addState(states[1], mBackgroundPressedBmp);
        }

        if (mBackgroundPressedBmp == null) {
            mStateBackground.addState(states[2], mBackgroundPressed);
        } else {
            mStateBackground.addState(states[2], mBackgroundPressedBmp);
        }

        if (mBackgroundCheckedBmp == null) {
            mStateBackground.addState(states[3], mBackgroundChecked);
        } else {
            mStateBackground.addState(states[3], mBackgroundCheckedBmp);
        }

        if (mBackgroundSelectedBmp == null) {
            mStateBackground.addState(states[4], mBackgroundSelected);
        } else {
            mStateBackground.addState(states[4], mBackgroundSelectedBmp);
        }

        if (mBackgroundNormalBmp == null) {
            mStateBackground.addState(states[5], mBackgroundNormal);
        } else {
            mStateBackground.addState(states[5], mBackgroundNormalBmp);
        }
    }

    private void setBackgroundState() {

        boolean hasCustom = false;//是否存在自定义
        boolean hasCusBg, hasCusBorder = false, hasCusCorner = false;//存在自定义相关属性
        boolean unHasBgColor = mBackgroundColorNormal == _C && mBackgroundColorUnable == _C && mBackgroundColorPressed == _C && mBackgroundColorChecked == _C && mBackgroundColorSelected == _C;
        boolean unHasBgColorArray = mBackgroundColorNormalArray == null && mBackgroundColorUnableArray == null && mBackgroundColorPressedArray == null && mBackgroundColorCheckedArray == null && mBackgroundColorSelectedArray == null;
        boolean unHasBgDrawable = mBackgroundNormalBmp == null && mBackgroundPressedBmp == null && mBackgroundUnableBmp == null && mBackgroundCheckedBmp == null && mBackgroundSelectedBmp == null;

        //是否自定义了背景
        if (unHasBgColor && unHasBgColorArray && unHasBgDrawable) {//未设置自定义背景
            hasCusBg = false;
        } else {
            hasCusBg = true;
        }

        //是否自定义了边框
        if (mBorderDashWidth != _S || mBorderDashGap != _S
                || mBorderWidthNormal != _S || mBorderWidthPressed != _S || mBorderWidthUnable != _S || mBorderWidthChecked != _S || mBorderWidthSelected != _S
                || mBorderColorNormal != _C || mBorderColorPressed != _C || mBorderColorUnable != _C || mBorderColorChecked != _C || mBorderColorSelected != _C) {
            hasCusBorder = true;
        }

        //是否自定义了圆角
        if (mCornerRadius != -1 || mCornerRadiusTopLeft != 0 || mCornerRadiusTopRight != 0 || mCornerRadiusBottomLeft != 0 || mCornerRadiusBottomRight != 0) {
            hasCusCorner = true;
        }

        if (hasCusBg || hasCusCorner || hasCusBorder) {
            hasCustom = true;
        }

        /**
         * 未设置自定义属性,获取原生背景并且设置
         */
        if (!hasCustom && !useShadow() && !useRipple()) {
            mBackgroundDrawable = mViewBackground;//使用原生背景
        } else {
            //获取drawable
            mBackgroundDrawable = getBackgroundDrawable(hasCustom, mRippleColor);
            if (useShadow()) {

                //1.早期实现方式禁止了硬件加速，并且由于 LAYER_TYPE_SOFTWARE 的特性导致在列表中卡顿（频繁创建缓存再渲染）(超过限制的view创建缓存过大导致无法展示背景)
                //mView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);//禁止硬件加速,must,导致绘制耗时(寻找优化路径)
                //if (mShadowDrawable == null) mShadowDrawable = new ShadowDrawable();
                //mShadowDrawable.updateParameter(mShadowColor, mShadowRadius, mShadowDx, mShadowDy, mBorderRadii);
                //int shadowOffset = (int) mShadowDrawable.getShadowOffset();
                //int left = shadowOffset + Math.abs(mShadowDx);
                //int right = shadowOffset + Math.abs(mShadowDx);
                //int top = shadowOffset + Math.abs(mShadowDy);
                //int bottom = shadowOffset + Math.abs(mShadowDy);

                //2.将阴影背景绘制成bitmap渲染，没有禁止硬件加速，和 LAYER_TYPE_SOFTWARE 的特性，大大提升性能。（在列表中大量使用阴影时，可能导致内存较高）
                if (mShadowDrawable == null) mShadowDrawable = new ShadowBitmapDrawable();
                mShadowDrawable.updateParameter(mShadowColor, mShadowRadius, mShadowDx, mShadowDy, mBorderRadii);

                int left = mShadowRadius + Math.abs(mShadowDx);
                int right = mShadowRadius + Math.abs(mShadowDx);
                int top = mShadowRadius + Math.abs(mShadowDy);
                int bottom = mShadowRadius + Math.abs(mShadowDy);

                //设置背景
                LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{mShadowDrawable, mBackgroundDrawable});
                layerDrawable.setLayerInset(1, left, top, right, bottom);//设置第二层drawable四周偏移量
                mBackgroundDrawable = layerDrawable;
            }
        }

        /**
         * 存在自定义属性，使用自定义属性设置
         */
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            mView.setBackgroundDrawable(mBackgroundDrawable);
        } else {
            mView.setBackground(mBackgroundDrawable);
        }

    }

    /**
     * 获取 BackgroundDrawable
     *
     * @param hasCustom   是否存在自定义背景
     * @param rippleColor 水波纹颜色
     */
    private Drawable getBackgroundDrawable(boolean hasCustom, int rippleColor) {
        if (!isUseRipple()) {
            return mStateBackground;
        } else {//使用Ripple
            Object[] objects = getRippleDrawableWithTag(hasCustom, rippleColor);
            RippleDrawable rippleDrawable = (RippleDrawable) objects[0];
            boolean isMaskNull = (boolean) objects[1];
            if (isMaskNull) {
                return rippleDrawable;//仅仅使用 RippleDrawable(需要无限制的跨越效果)
            } else {
                /**
                 * 使用Ripple时兼容Pressed之外的状态
                 * 备注:水波纹效果受限于控件
                 * 构建一种新的状态，兼容存在 RippleDrawable 和其他状态
                 */
                StateListDrawable stateBackground = new StateListDrawable();
                int[][] states = new int[4][];
                //unable,checked,selected,normal
                states[0] = new int[]{-android.R.attr.state_enabled};//unable
                states[1] = new int[]{android.R.attr.state_checked};//checked
                states[2] = new int[]{android.R.attr.state_selected};//selected
                states[3] = new int[]{android.R.attr.state_enabled};//normal

                //unable,checked,normal
                //android studio 预览：不支持直接三目运算赋值
                if (mBackgroundUnableBmp == null) {
                    stateBackground.addState(states[0], mBackgroundUnable);
                } else {
                    stateBackground.addState(states[0], mBackgroundUnableBmp);
                }

                if (mBackgroundCheckedBmp == null) {
                    stateBackground.addState(states[1], mBackgroundChecked);
                } else {
                    stateBackground.addState(states[1], mBackgroundCheckedBmp);
                }

                if (mBackgroundSelectedBmp == null) {
                    stateBackground.addState(states[2], mBackgroundSelected);
                } else {
                    stateBackground.addState(states[2], mBackgroundSelectedBmp);
                }
                stateBackground.addState(states[3], rippleDrawable);

                return stateBackground;
            }
        }
    }

    /**
     * 获取 RippleDrawable 和 特殊标记
     *
     * @param hasCustom   是否存在自定义背景
     * @param rippleColor 水波纹颜色
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private Object[] getRippleDrawableWithTag(boolean hasCustom, int rippleColor) {

        /**
         * RippleDrawable -> 内容默认drawable
         * 1. 存在自定义背景 提取 mBackgroundNormalBmp || mBackgroundNormal
         * 2. null
         */
        Drawable contentDrawable = null;
        if (hasCustom) {
            //存在背景图片，优先使用背景图片，不存在使用默认背景
            //android studio 预览：不支持直接三目运算赋值
            if (mBackgroundNormalBmp == null) {
                contentDrawable = mBackgroundNormal;
            } else {
                contentDrawable = mBackgroundNormalBmp;
            }
        }

        /**
         * RippleDrawable -> 纹范围限drawable
         * 1. null 不设置范围
         * 2. 不设置情况 -> 提取 mBackgroundNormalBmp || mBackgroundNormal
         * 3. 自定义设置drawable
         */
        Drawable maskDrawable = null;
        switch (mRippleMaskStyle) {
            case MASK_STYLE_NULL:// null 不设置范围
                maskDrawable = null;
                break;
            case MASK_STYLE_NORMAL:// 不设置情况 -> 提取 mBackgroundNormalBmp || mBackgroundNormal || null
                if (hasCustom) {
                    if (mBackgroundNormalBmp != null) {//背景图片
                        maskDrawable = mBackgroundNormalBmp;
                    } else {
                        //允许圆角的shape
                        RoundRectShape roundShape = new RoundRectShape(mBorderRadii, null, null);
                        ShapeDrawable shapeDrawable = new ShapeDrawable(roundShape);
                        maskDrawable = shapeDrawable;
                    }
                } else {//控件默认形状->矩形
                    maskDrawable = new ShapeDrawable(new RectShape());
                }
                break;
            case MASK_STYLE_DRAWABLE:// 自定义设置drawable
                maskDrawable = mRippleMaskDrawable;
                break;
        }


        /**
         * 水波纹颜色值
         * 可以有多个状态，暂时支持一个
         */
        int[][] stateList = new int[][]{
                new int[]{android.R.attr.state_pressed},
                new int[]{android.R.attr.state_focused},
                new int[]{android.R.attr.state_activated},
                new int[]{}
        };
        int[] stateColorList = new int[]{
                rippleColor,
                rippleColor,
                rippleColor,
                rippleColor
        };


        ColorStateList colorStateList = new ColorStateList(stateList, stateColorList);

        //水波纹drawable
        //android studio 预览：不支持给 drawable 赋值 null
        RippleDrawable rippleDrawable = new RippleDrawableUtils(contentDrawable, maskDrawable).getRippleDrawable(colorStateList);

        //是否不受限制的水波纹效果{ contentDrawable == null && maskDrawable == null }
        boolean isMaskNull = contentDrawable == null && maskDrawable == null;

        return new Object[]{rippleDrawable, isMaskNull};
    }


    /*********************
     * Shadow
     *********************/

    public boolean useShadow() {
        return mShadowRadius >= 0;
    }

    public RBaseHelper setShadowRadius(int shadowRadius) {
        this.mShadowRadius = shadowRadius;
        setBackgroundState();
        return this;
    }

    public int getShadowRadius() {
        return mShadowRadius;
    }

    public RBaseHelper setShadowColor(int shadowColor) {
        this.mShadowColor = shadowColor;
        setBackgroundState();
        return this;
    }

    public int getShadowColor() {
        return mShadowColor;
    }

    public RBaseHelper setShadowDx(int shadowDx) {
        this.mShadowDx = shadowDx;
        setBackgroundState();
        return this;
    }

    public int getShadowDx() {
        return mShadowDx;
    }

    public RBaseHelper setShadowDy(int shadowDy) {
        this.mShadowDy = shadowDy;
        setBackgroundState();
        return this;
    }

    public int getShadowDy() {
        return mShadowDy;
    }


    /*********************
     * Ripple
     *********************/

    public RBaseHelper setUseRipple(boolean useRipple) {
        this.mUseRipple = useRipple;
        setBackgroundState();
        return this;
    }

    public boolean useRipple() {
        return mUseRipple;
    }

    public RBaseHelper setRippleColor(@ColorInt int rippleColor) {
        this.mRippleColor = rippleColor;
        this.mUseRipple = true;
        setBackgroundState();
        return this;
    }

    public int getRippleColor() {
        return mRippleColor;
    }

    public RBaseHelper setRippleMaskDrawable(Drawable rippleMaskDrawable) {
        this.mRippleMaskDrawable = rippleMaskDrawable;
        this.mUseRipple = true;
        this.mRippleMaskStyle = MASK_STYLE_DRAWABLE;
        setBackgroundState();
        return this;
    }

    public Drawable getRippleMaskDrawable() {
        return mRippleMaskDrawable;
    }


    /*********************
     * border
     *********************/

    public RBaseHelper setBorderWidthNormal(int width) {
        this.mBorderWidthNormal = width;
        updateBorderValue();
        return this;
    }

    public int getBorderWidthNormal() {
        return mBorderWidthNormal;
    }

    public RBaseHelper setBorderColorNormal(@ColorInt int color) {
        this.mBorderColorNormal = color;
        updateBorderValue();
        return this;
    }

    public int getBorderColorNormal() {
        return mBorderColorNormal;
    }

    public RBaseHelper setBorderWidthPressed(int width) {
        this.mBorderWidthPressed = width;
        mHasPressedBorderWidth = true;
        updateBorderValue();
        return this;
    }

    public int getBorderWidthPressed() {
        return mBorderWidthPressed;
    }

    public RBaseHelper setBorderColorPressed(@ColorInt int color) {
        this.mBorderColorPressed = color;
        mHasPressedBorderColor = true;
        updateBorderValue();
        return this;
    }

    public int getBorderColorPressed() {
        return mBorderColorPressed;
    }

    public RBaseHelper setBorderColorChecked(@ColorInt int color) {
        this.mBorderColorChecked = color;
        mHasCheckedBorderColor = true;
        updateBorderValue();
        return this;
    }

    public int getBorderColorChecked() {
        return mBorderColorChecked;
    }

    public RBaseHelper setBorderColorSelected(@ColorInt int color) {
        this.mBorderColorSelected = color;
        mHasSelectedBorderColor = true;
        updateBorderValue();
        return this;
    }

    public int getBorderColorSelected() {
        return mBorderColorSelected;
    }

    public RBaseHelper setBorderWidthChecked(int width) {
        this.mBorderWidthChecked = width;
        mHasCheckedBorderWidth = true;
        updateBorderValue();
        return this;
    }

    public int getBorderWidthChecked() {
        return mBorderWidthChecked;
    }

    public RBaseHelper setBorderWidthSelected(int width) {
        this.mBorderWidthSelected = width;
        mHasSelectedBorderWidth = true;
        updateBorderValue();
        return this;
    }

    public int getBorderWidthSelected() {
        return mBorderWidthSelected;
    }

    public RBaseHelper setBorderWidthUnable(int width) {
        this.mBorderWidthUnable = width;
        mHasUnableBorderWidth = true;
        updateBorderValue();
        return this;
    }

    public int getBorderWidthUnable() {
        return mBorderWidthUnable;
    }

    public RBaseHelper setBorderColorUnable(@ColorInt int color) {
        this.mBorderColorUnable = color;
        mHasUnableBorderColor = true;
        updateBorderValue();
        return this;
    }

    public int getBorderColorUnable() {
        return mBorderColorUnable;
    }

    public RBaseHelper setBorderWidth(int normal, int pressed, int unable, int checked,
                                      int selected) {
        this.mBorderWidthNormal = normal;
        this.mBorderWidthPressed = pressed;
        this.mBorderWidthUnable = unable;
        this.mBorderWidthChecked = checked;
        this.mBorderWidthSelected = selected;

        mHasPressedBorderWidth = true;
        mHasUnableBorderWidth = true;
        mHasCheckedBorderWidth = true;
        mHasSelectedBorderWidth = true;
        updateBorderValue();
        return this;
    }

    public RBaseHelper setBorderColor(@ColorInt int normal, @ColorInt int pressed,
                                      @ColorInt int unable, @ColorInt int checked, @ColorInt int selected) {
        this.mBorderColorNormal = normal;
        this.mBorderColorPressed = pressed;
        this.mBorderColorUnable = unable;
        this.mBorderColorChecked = checked;
        this.mBorderColorSelected = selected;

        mHasPressedBorderColor = true;
        mHasUnableBorderColor = true;
        mHasCheckedBorderColor = true;
        mHasSelectedBorderColor = true;
        updateBorderValue();
        return this;
    }

    public RBaseHelper setBorderDashWidth(float dashWidth) {
        this.mBorderDashWidth = dashWidth;
        updateBorderValue();
        return this;
    }

    public float getBorderDashWidth() {
        return mBorderDashWidth;
    }

    public RBaseHelper setBorderDashGap(float dashGap) {
        this.mBorderDashGap = dashGap;
        updateBorderValue();
        return this;
    }

    public float getBorderDashGap() {
        return mBorderDashGap;
    }

    public RBaseHelper setBorderDash(float dashWidth, float dashGap) {
        this.mBorderDashWidth = dashWidth;
        this.mBorderDashGap = dashGap;
        updateBorderValue();
        return this;
    }

    private void setBorder() {
        mBackgroundNormal.setStroke(mBorderWidthNormal, mBorderColorNormal, mBorderDashWidth, mBorderDashGap);
        mBackgroundPressed.setStroke(mBorderWidthPressed, mBorderColorPressed, mBorderDashWidth, mBorderDashGap);
        mBackgroundUnable.setStroke(mBorderWidthUnable, mBorderColorUnable, mBorderDashWidth, mBorderDashGap);
        mBackgroundChecked.setStroke(mBorderWidthChecked, mBorderColorChecked, mBorderDashWidth, mBorderDashGap);
        mBackgroundSelected.setStroke(mBorderWidthSelected, mBorderColorSelected, mBorderDashWidth, mBorderDashGap);
    }

    /**
     * 更新边框值
     */
    private void updateBorderValue() {
        setupDefaultValue(false);
        setBorder();
        setBackgroundState();
    }

    /*********************
     * radius
     ********************/

    public void setCornerRadius(float radius) {
        this.mCornerRadius = radius;
        updateRadiusValue();
    }

    public float getCornerRadius() {
        return mCornerRadius;
    }

    public RBaseHelper setCornerRadiusTopLeft(float topLeft) {
        this.mCornerRadius = -1;
        this.mCornerRadiusTopLeft = topLeft;
        updateRadiusValue();
        return this;
    }

    public float getCornerRadiusTopLeft() {
        return mCornerRadiusTopLeft;
    }

    public RBaseHelper setCornerRadiusTopRight(float topRight) {
        this.mCornerRadius = -1;
        this.mCornerRadiusTopRight = topRight;
        updateRadiusValue();
        return this;
    }

    public float getCornerRadiusTopRight() {
        return mCornerRadiusTopRight;
    }

    public RBaseHelper setCornerRadiusBottomRight(float bottomRight) {
        this.mCornerRadius = -1;
        this.mCornerRadiusBottomRight = bottomRight;
        updateRadiusValue();
        return this;
    }

    public float getCornerRadiusBottomRight() {
        return mCornerRadiusBottomRight;
    }

    public RBaseHelper setCornerRadiusBottomLeft(float bottomLeft) {
        this.mCornerRadius = -1;
        this.mCornerRadiusBottomLeft = bottomLeft;
        updateRadiusValue();
        return this;
    }

    public float getCornerRadiusBottomLeft() {
        return mCornerRadiusBottomLeft;
    }

    public RBaseHelper setCornerRadius(float topLeft, float topRight, float bottomRight,
                                       float bottomLeft) {
        this.mCornerRadius = -1;
        this.mCornerRadiusTopLeft = topLeft;
        this.mCornerRadiusTopRight = topRight;
        this.mCornerRadiusBottomRight = bottomRight;
        this.mCornerRadiusBottomLeft = bottomLeft;
        updateRadiusValue();
        return this;
    }

    /**
     * 更新圆角值
     */
    private void updateRadiusValue() {
        setRadius();
        setBackgroundState();
    }

    /**
     * 设置圆角数值
     */
    private void setRadius() {
        if (mCornerRadius >= 0) {
            mBorderRadii[0] = mCornerRadius;
            mBorderRadii[1] = mCornerRadius;
            mBorderRadii[2] = mCornerRadius;
            mBorderRadii[3] = mCornerRadius;
            mBorderRadii[4] = mCornerRadius;
            mBorderRadii[5] = mCornerRadius;
            mBorderRadii[6] = mCornerRadius;
            mBorderRadii[7] = mCornerRadius;
        } else {
            boolean isRtl = isRtl();
            mBorderRadii[0] = isRtl ? mCornerRadiusTopRight : mCornerRadiusTopLeft;
            mBorderRadii[1] = isRtl ? mCornerRadiusTopRight : mCornerRadiusTopLeft;
            mBorderRadii[2] = isRtl ? mCornerRadiusTopLeft : mCornerRadiusTopRight;
            mBorderRadii[3] = isRtl ? mCornerRadiusTopLeft : mCornerRadiusTopRight;
            mBorderRadii[4] = isRtl ? mCornerRadiusBottomLeft : mCornerRadiusBottomRight;
            mBorderRadii[5] = isRtl ? mCornerRadiusBottomLeft : mCornerRadiusBottomRight;
            mBorderRadii[6] = isRtl ? mCornerRadiusBottomRight : mCornerRadiusBottomLeft;
            mBorderRadii[7] = isRtl ? mCornerRadiusBottomRight : mCornerRadiusBottomLeft;
        }
        mBackgroundNormal.setCornerRadii(mBorderRadii);
        mBackgroundPressed.setCornerRadii(mBorderRadii);
        mBackgroundUnable.setCornerRadii(mBorderRadii);
        mBackgroundChecked.setCornerRadii(mBorderRadii);
        mBackgroundSelected.setCornerRadii(mBorderRadii);
    }

    /**
     * 设置View大小变化监听,用来更新渐变半径
     */
    private void addOnGlobalLayoutListener() {
        if (mView == null) return;
        mView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                ViewTreeObserver observer = v.getViewTreeObserver();
                observer.addOnGlobalLayoutListener(RBaseHelper.this);
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                ViewTreeObserver observer = v.getViewTreeObserver();
                observer.removeGlobalOnLayoutListener(RBaseHelper.this);
                v.removeOnAttachStateChangeListener(this);
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {//>=16
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

    /**
     * 是否使用Ripple
     *
     * @return
     */
    private boolean isUseRipple() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && mUseRipple;
    }

    /**
     * 初始化Clip
     */
    private void initClip() {
        //初始化clip
        mClipHelper.initClip(mView, mClipLayout, new ClipPathManager.ClipPathCreator() {
            @Override
            public Path createClipPath(int width, int height) {
                Path path = new Path();
                path.addRoundRect(new RectF(0, 0, width, height), mBorderRadii, Path.Direction.CCW);
                return path;
            }
        });
    }

    @Override
    public void dispatchDraw(Canvas canvas) {
        mClipHelper.dispatchDraw(canvas);
    }

    @Override
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        mClipHelper.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public void onGlobalLayout() {
        //移除监听
        ViewTreeObserver observer = mView.getViewTreeObserver();
        observer.removeGlobalOnLayoutListener(RBaseHelper.this);

        //5.0以下圆角兼容
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            float half = mView.getHeight() / 2f;
            for (int i = 0; i < mBorderRadii.length; i++) {
                if (mBorderRadii[i] > half) mBorderRadii[i] = half;
            }
        }
        if (mGradientRadius <= 0) {
            int width = mView.getWidth();
            int height = mView.getHeight();
            float radius = Math.min(width, height) / 2f;
            setGradientRadius(radius);
        }
        //初始化clip
        initClip();
    }


    /**
     * 是否从右到左布局
     *
     * @return
     */
    protected static boolean isRtl() {
        return TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault()) == ViewCompat.LAYOUT_DIRECTION_RTL;

    }
}
