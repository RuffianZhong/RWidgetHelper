package com.ruffian.library.widget.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.v7.content.res.AppCompatResources;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

import com.ruffian.library.widget.R;
import com.ruffian.library.widget.iface.ITextViewFeature;
import com.ruffian.library.widget.utils.TextViewUtils;

/**
 * TextView-Helper
 *
 * @author ZhongDaFeng
 */
public class RTextViewHelper extends RBaseHelper<TextView> implements ITextViewFeature {

    //default value
    public static final int ICON_DIR_LEFT = 1, ICON_DIR_TOP = 2, ICON_DIR_RIGHT = 3, ICON_DIR_BOTTOM = 4;

    //Icon（兼容老版本）
    private Drawable mIcon = null;
    private Drawable mIconNormal;
    private Drawable mIconPressed;
    private Drawable mIconUnable;
    private Drawable mIconSelected;
    private Drawable mIconChecked;
    private int mIconHeight;
    private int mIconWidth;
    private int mIconDirection;

    //icon
    private int mIconHeightLeft;
    private int mIconWidthLeft;
    private int mIconHeightRight;
    private int mIconWidthRight;

    private int mIconHeightTop;
    private int mIconWidthTop;
    private int mIconHeightBottom;
    private int mIconWidthBottom;

    private Drawable mIconLeft = null;
    private Drawable mIconNormalLeft;
    private Drawable mIconPressedLeft;
    private Drawable mIconUnableLeft;
    private Drawable mIconSelectedLeft;
    private Drawable mIconCheckedLeft;

    private Drawable mIconTop = null;
    private Drawable mIconNormalTop;
    private Drawable mIconPressedTop;
    private Drawable mIconUnableTop;
    private Drawable mIconSelectedTop;
    private Drawable mIconCheckedTop;

    private Drawable mIconBottom = null;
    private Drawable mIconNormalBottom;
    private Drawable mIconPressedBottom;
    private Drawable mIconUnableBottom;
    private Drawable mIconSelectedBottom;
    private Drawable mIconCheckedBottom;

    private Drawable mIconRight = null;
    private Drawable mIconNormalRight;
    private Drawable mIconPressedRight;
    private Drawable mIconUnableRight;
    private Drawable mIconSelectedRight;
    private Drawable mIconCheckedRight;

    // Text
    protected int mTextColorNormal = _C;
    protected int mTextColorPressed = _C;
    protected int mTextColorUnable = _C;
    protected int mTextColorSelected = _C;
    protected int mTextColorChecked = _C;
    protected ColorStateList mTextColorStateList;
    protected int[][] states = new int[6][];

    //typeface
    private String mTypefacePath;

    //drawable和Text居中
    private boolean mDrawableWithText = false;

    /**
     * 是否设置对应的属性
     */
    protected boolean mHasPressedTextColor = false;
    protected boolean mHasUnableTextColor = false;
    protected boolean mHasSelectedTextColor = false;
    protected boolean mHasCheckedTextColor = false;

    //TextView本身设置的padding
    protected int mPaddingLeft, mPaddingRight, mPaddingTop, mPaddingBottom;

    //缓存的Padding数据，是否更新iconWithText的依据
    private String mCacheSingleIconPaddingVale;
    private String mCacheMultipleIconPaddingVale;


    public RTextViewHelper(Context context, TextView view, AttributeSet attrs) {
        super(context, view, attrs);
        initAttributeSet(context, attrs);
    }

    /**
     * 初始化控件属性
     *
     * @param context
     * @param attrs
     */
    @SuppressLint("NewApi")
    private void initAttributeSet(Context context, AttributeSet attrs) {
        if (context == null || attrs == null) {
            setup();
            return;
        }
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RTextView);
        //icon
        //Vector兼容处理
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mIconNormalLeft = a.getDrawable(R.styleable.RTextView_icon_normal_left);
            mIconPressedLeft = a.getDrawable(R.styleable.RTextView_icon_pressed_left);
            mIconUnableLeft = a.getDrawable(R.styleable.RTextView_icon_unable_left);
            mIconSelectedLeft = a.getDrawable(R.styleable.RTextView_icon_selected_left);
            mIconCheckedLeft = a.getDrawable(R.styleable.RTextView_icon_checked_left);
            mIconNormalRight = a.getDrawable(R.styleable.RTextView_icon_normal_right);
            mIconPressedRight = a.getDrawable(R.styleable.RTextView_icon_pressed_right);
            mIconUnableRight = a.getDrawable(R.styleable.RTextView_icon_unable_right);
            mIconSelectedRight = a.getDrawable(R.styleable.RTextView_icon_selected_right);
            mIconCheckedRight = a.getDrawable(R.styleable.RTextView_icon_checked_right);
            mIconNormalTop = a.getDrawable(R.styleable.RTextView_icon_normal_top);
            mIconPressedTop = a.getDrawable(R.styleable.RTextView_icon_pressed_top);
            mIconUnableTop = a.getDrawable(R.styleable.RTextView_icon_unable_top);
            mIconSelectedTop = a.getDrawable(R.styleable.RTextView_icon_selected_top);
            mIconCheckedTop = a.getDrawable(R.styleable.RTextView_icon_checked_top);
            mIconNormalBottom = a.getDrawable(R.styleable.RTextView_icon_normal_bottom);
            mIconPressedBottom = a.getDrawable(R.styleable.RTextView_icon_pressed_bottom);
            mIconUnableBottom = a.getDrawable(R.styleable.RTextView_icon_unable_bottom);
            mIconSelectedBottom = a.getDrawable(R.styleable.RTextView_icon_selected_bottom);
            mIconCheckedBottom = a.getDrawable(R.styleable.RTextView_icon_checked_bottom);
            //版本兼容代码
            mIconNormal = a.getDrawable(R.styleable.RTextView_icon_src_normal);
            mIconPressed = a.getDrawable(R.styleable.RTextView_icon_src_pressed);
            mIconUnable = a.getDrawable(R.styleable.RTextView_icon_src_unable);
            mIconSelected = a.getDrawable(R.styleable.RTextView_icon_src_selected);
            mIconChecked = a.getDrawable(R.styleable.RTextView_icon_src_checked);

        } else {
            int normalIdLeft = a.getResourceId(R.styleable.RTextView_icon_normal_left, -1);
            int pressedIdLeft = a.getResourceId(R.styleable.RTextView_icon_pressed_left, -1);
            int unableIdLeft = a.getResourceId(R.styleable.RTextView_icon_unable_left, -1);
            int selectedIdLeft = a.getResourceId(R.styleable.RTextView_icon_selected_left, -1);
            int checkedIdLeft = a.getResourceId(R.styleable.RTextView_icon_checked_left, -1);
            int normalIdRight = a.getResourceId(R.styleable.RTextView_icon_normal_right, -1);
            int pressedIdRight = a.getResourceId(R.styleable.RTextView_icon_pressed_right, -1);
            int unableIdRight = a.getResourceId(R.styleable.RTextView_icon_unable_right, -1);
            int selectedIdRight = a.getResourceId(R.styleable.RTextView_icon_selected_right, -1);
            int checkedIdRight = a.getResourceId(R.styleable.RTextView_icon_checked_right, -1);
            int normalIdTop = a.getResourceId(R.styleable.RTextView_icon_normal_top, -1);
            int pressedIdTop = a.getResourceId(R.styleable.RTextView_icon_pressed_top, -1);
            int unableIdTop = a.getResourceId(R.styleable.RTextView_icon_unable_top, -1);
            int selectedIdTop = a.getResourceId(R.styleable.RTextView_icon_selected_top, -1);
            int checkedIdTop = a.getResourceId(R.styleable.RTextView_icon_checked_top, -1);
            int normalIdBottom = a.getResourceId(R.styleable.RTextView_icon_normal_bottom, -1);
            int pressedIdBottom = a.getResourceId(R.styleable.RTextView_icon_pressed_bottom, -1);
            int unableIdBottom = a.getResourceId(R.styleable.RTextView_icon_unable_bottom, -1);
            int selectedIdBottom = a.getResourceId(R.styleable.RTextView_icon_selected_bottom, -1);
            int checkedIdBottom = a.getResourceId(R.styleable.RTextView_icon_checked_bottom, -1);
            if (normalIdLeft != -1)
                mIconNormalLeft = AppCompatResources.getDrawable(context, normalIdLeft);
            if (pressedIdLeft != -1)
                mIconPressedLeft = AppCompatResources.getDrawable(context, pressedIdLeft);
            if (unableIdLeft != -1)
                mIconUnableLeft = AppCompatResources.getDrawable(context, unableIdLeft);
            if (selectedIdLeft != -1)
                mIconSelectedLeft = AppCompatResources.getDrawable(context, selectedIdLeft);
            if (checkedIdLeft != -1)
                mIconCheckedLeft = AppCompatResources.getDrawable(context, checkedIdLeft);
            if (normalIdRight != -1)
                mIconNormalRight = AppCompatResources.getDrawable(context, normalIdRight);
            if (pressedIdRight != -1)
                mIconPressedRight = AppCompatResources.getDrawable(context, pressedIdRight);
            if (unableIdRight != -1)
                mIconUnableRight = AppCompatResources.getDrawable(context, unableIdRight);
            if (selectedIdRight != -1)
                mIconSelectedRight = AppCompatResources.getDrawable(context, selectedIdRight);
            if (checkedIdRight != -1)
                mIconCheckedRight = AppCompatResources.getDrawable(context, checkedIdRight);
            if (normalIdTop != -1)
                mIconNormalTop = AppCompatResources.getDrawable(context, normalIdTop);
            if (pressedIdTop != -1)
                mIconPressedTop = AppCompatResources.getDrawable(context, pressedIdTop);
            if (unableIdTop != -1)
                mIconUnableTop = AppCompatResources.getDrawable(context, unableIdTop);
            if (selectedIdTop != -1)
                mIconSelectedTop = AppCompatResources.getDrawable(context, selectedIdTop);
            if (checkedIdTop != -1)
                mIconCheckedTop = AppCompatResources.getDrawable(context, checkedIdTop);
            if (normalIdBottom != -1)
                mIconNormalBottom = AppCompatResources.getDrawable(context, normalIdBottom);
            if (pressedIdBottom != -1)
                mIconPressedBottom = AppCompatResources.getDrawable(context, pressedIdBottom);
            if (unableIdBottom != -1)
                mIconUnableBottom = AppCompatResources.getDrawable(context, unableIdBottom);
            if (selectedIdBottom != -1)
                mIconSelectedBottom = AppCompatResources.getDrawable(context, selectedIdBottom);
            if (checkedIdBottom != -1)
                mIconCheckedBottom = AppCompatResources.getDrawable(context, checkedIdBottom);
            //版本兼容代码
            int normalId = a.getResourceId(R.styleable.RTextView_icon_src_normal, -1);
            int pressedId = a.getResourceId(R.styleable.RTextView_icon_src_pressed, -1);
            int unableId = a.getResourceId(R.styleable.RTextView_icon_src_unable, -1);
            int selectedId = a.getResourceId(R.styleable.RTextView_icon_src_selected, -1);
            int checkedId = a.getResourceId(R.styleable.RTextView_icon_src_checked, -1);
            if (normalId != -1)
                mIconNormal = AppCompatResources.getDrawable(context, normalId);
            if (pressedId != -1)
                mIconPressed = AppCompatResources.getDrawable(context, pressedId);
            if (unableId != -1)
                mIconUnable = AppCompatResources.getDrawable(context, unableId);
            if (selectedId != -1)
                mIconSelected = AppCompatResources.getDrawable(context, selectedId);
            if (checkedId != -1)
                mIconChecked = AppCompatResources.getDrawable(context, checkedId);
        }
        mIconWidthLeft = a.getDimensionPixelSize(R.styleable.RTextView_icon_width_left, 0);
        mIconHeightLeft = a.getDimensionPixelSize(R.styleable.RTextView_icon_height_left, 0);
        mIconWidthRight = a.getDimensionPixelSize(R.styleable.RTextView_icon_width_right, 0);
        mIconHeightRight = a.getDimensionPixelSize(R.styleable.RTextView_icon_height_right, 0);
        mIconWidthBottom = a.getDimensionPixelSize(R.styleable.RTextView_icon_width_bottom, 0);
        mIconHeightBottom = a.getDimensionPixelSize(R.styleable.RTextView_icon_height_bottom, 0);
        mIconWidthTop = a.getDimensionPixelSize(R.styleable.RTextView_icon_width_top, 0);
        mIconHeightTop = a.getDimensionPixelSize(R.styleable.RTextView_icon_height_top, 0);
        //老版本兼容代码
        mIconWidth = a.getDimensionPixelSize(R.styleable.RTextView_icon_width, 0);
        mIconHeight = a.getDimensionPixelSize(R.styleable.RTextView_icon_height, 0);
        mIconDirection = a.getInt(R.styleable.RTextView_icon_direction, ICON_DIR_LEFT);
        //兼容系统原生drawableLeft
        String namespace = "http://schemas.android.com/apk/res/android";//android的命名空间
        int drawableLeft = attrs.getAttributeResourceValue(namespace, "drawableLeft", 0);
        if (drawableLeft != 0) mIconNormalLeft = context.getResources().getDrawable(drawableLeft);
        int drawableTop = attrs.getAttributeResourceValue(namespace, "drawableTop", 0);
        if (drawableTop != 0) mIconNormalTop = context.getResources().getDrawable(drawableTop);
        int drawableRight = attrs.getAttributeResourceValue(namespace, "drawableRight", 0);
        if (drawableRight != 0)
            mIconNormalRight = context.getResources().getDrawable(drawableRight);
        int drawableBottom = attrs.getAttributeResourceValue(namespace, "drawableBottom", 0);
        if (drawableBottom != 0)
            mIconNormalBottom = context.getResources().getDrawable(drawableBottom);
        int drawableStart = attrs.getAttributeResourceValue(namespace, "drawableStart", 0);
        if (drawableStart != 0) {
            if (TextViewUtils.isRight2Left()) {
                mIconNormalRight = context.getResources().getDrawable(drawableStart);
            } else {
                mIconNormalLeft = context.getResources().getDrawable(drawableStart);
            }
        }
        int drawableEnd = attrs.getAttributeResourceValue(namespace, "drawableEnd", 0);
        if (drawableEnd != 0) {
            if (TextViewUtils.isRight2Left()) {
                mIconNormalLeft = context.getResources().getDrawable(drawableEnd);
            } else {
                mIconNormalRight = context.getResources().getDrawable(drawableEnd);
            }
        }


        //text
        mTextColorNormal = a.getColor(R.styleable.RTextView_text_color_normal, mView.getCurrentTextColor());
        mTextColorPressed = a.getColor(R.styleable.RTextView_text_color_pressed, _C);
        mTextColorUnable = a.getColor(R.styleable.RTextView_text_color_unable, _C);
        mTextColorSelected = a.getColor(R.styleable.RTextView_text_color_selected, _C);
        mTextColorChecked = a.getColor(R.styleable.RTextView_text_color_checked, _C);
        //typeface
        mTypefacePath = a.getString(R.styleable.RTextView_text_typeface);
        //drawableWithText
        mDrawableWithText = a.getBoolean(R.styleable.RTextView_icon_with_text, false);

        a.recycle();

        //setup
        setup();

    }

    private void setupDefaultValue(boolean init) {
        if (init) {
            mHasPressedTextColor = mTextColorPressed != _C;
            mHasUnableTextColor = mTextColorUnable != _C;
            mHasSelectedTextColor = mTextColorSelected != _C;
            mHasCheckedTextColor = mTextColorChecked != _C;
        }

        if (!mHasPressedTextColor) mTextColorPressed = mTextColorNormal;
        if (!mHasUnableTextColor) mTextColorUnable = mTextColorNormal;
        if (!mHasSelectedTextColor) mTextColorSelected = mTextColorNormal;
        if (!mHasCheckedTextColor) mTextColorChecked = mTextColorNormal;

    }

    /**
     * 设置
     */
    private void setup() {

        /**
         * icon
         */
        if (!mView.isEnabled()) {
            mIcon = mIconUnable;
            mIconLeft = mIconUnableLeft;
            mIconRight = mIconUnableRight;
            mIconTop = mIconUnableTop;
            mIconBottom = mIconUnableBottom;
        } else if (mView.isSelected()) {
            mIcon = mIconSelected;
            mIconLeft = mIconSelectedLeft;
            mIconRight = mIconSelectedRight;
            mIconTop = mIconSelectedTop;
            mIconBottom = mIconSelectedBottom;
        } else if (isCompoundButtonChecked()) {
            mIcon = mIconChecked;
            mIconLeft = mIconCheckedLeft;
            mIconRight = mIconCheckedRight;
            mIconTop = mIconCheckedTop;
            mIconBottom = mIconCheckedBottom;
        } else {
            mIcon = mIconNormal;
            mIconLeft = mIconNormalLeft;
            mIconRight = mIconNormalRight;
            mIconTop = mIconNormalTop;
            mIconBottom = mIconNormalBottom;
        }

        //unable,focused,pressed,checked,selected,normal
        states[0] = new int[]{-android.R.attr.state_enabled};//unable
        states[1] = new int[]{android.R.attr.state_focused};//focused
        states[2] = new int[]{android.R.attr.state_pressed};//pressed
        states[3] = new int[]{android.R.attr.state_checked};//checked
        states[4] = new int[]{android.R.attr.state_selected};//selected
        states[5] = new int[]{android.R.attr.state_enabled};//normal

        //设置默认值
        setupDefaultValue(true);

        //设置文本颜色
        setTextColor();

        //设置ICON
        setIcon();

        //设置文本字体样式
        setTypeface();

    }


    /************************
     * Typeface
     ************************/

    public RTextViewHelper setTypeface(String typefacePath) {
        this.mTypefacePath = typefacePath;
        setTypeface();
        return this;
    }

    public String getTypefacePath() {
        return mTypefacePath;
    }

    private void setTypeface() {
        if (!TextUtils.isEmpty(mTypefacePath)) {
            AssetManager assetManager = mContext.getAssets();
            Typeface typeface = Typeface.createFromAsset(assetManager, mTypefacePath);
            mView.setTypeface(typeface);
        }
    }

    /************************
     * Icon
     ************************/
    /**
     * **********版本兼容代码***********
     */
    @Deprecated
    public RTextViewHelper setIconNormal(Drawable icon) {
        this.mIconNormal = icon;
        this.mIcon = icon;
        setIcon();
        return this;
    }

    @Deprecated
    public Drawable getIconNormal() {
        return mIconNormal;
    }

    @Deprecated
    public RTextViewHelper setIconPressed(Drawable icon) {
        this.mIconPressed = icon;
        this.mIcon = icon;
        setIcon();
        return this;
    }

    @Deprecated
    public Drawable getIconPressed() {
        return mIconPressed;
    }

    @Deprecated
    public RTextViewHelper setIconUnable(Drawable icon) {
        this.mIconUnable = icon;
        this.mIcon = icon;
        setIcon();
        return this;
    }

    @Deprecated
    public Drawable getIconUnable() {
        return mIconUnable;
    }

    @Deprecated
    public RTextViewHelper setIconSelected(Drawable icon) {
        this.mIconSelected = icon;
        this.mIcon = icon;
        setIcon();
        return this;
    }

    @Deprecated
    public Drawable getIconSelected() {
        return mIconSelected;
    }

    @Deprecated
    public RTextViewHelper setIconChecked(Drawable icon) {
        this.mIconChecked = icon;
        this.mIcon = icon;
        setIcon(icon);
        return this;
    }

    @Deprecated
    public Drawable getIconChecked() {
        return mIconChecked;
    }

    @Deprecated
    public RTextViewHelper setIconSize(int iconWidth, int iconHeight) {
        this.mIconWidth = iconWidth;
        this.mIconHeight = iconHeight;
        setIcon();
        return this;
    }

    @Deprecated
    public RTextViewHelper setIconWidth(int iconWidth) {
        this.mIconWidth = iconWidth;
        setIcon();
        return this;
    }

    @Deprecated
    public int getIconWidth() {
        return mIconWidth;
    }

    @Deprecated
    public RTextViewHelper setIconHeight(int iconHeight) {
        this.mIconHeight = iconHeight;
        setIcon();
        return this;
    }

    @Deprecated
    public int getIconHeight() {
        return mIconHeight;
    }

    @Deprecated
    public RTextViewHelper setIconDirection(int iconDirection) {
        this.mIconDirection = iconDirection;
        setIcon();
        return this;
    }

    @Deprecated
    public int getIconDirection() {
        return mIconDirection;
    }


    /**
     * *******新版本逻辑********
     */
    public RTextViewHelper setIconNormalLeft(Drawable icon) {
        this.mIconNormalLeft = icon;
        this.mIconLeft = icon;
        setIcon();
        return this;
    }

    public RTextViewHelper setIconNormalRight(Drawable icon) {
        this.mIconNormalRight = icon;
        this.mIconRight = icon;
        setIcon();
        return this;
    }

    public RTextViewHelper setIconNormalTop(Drawable icon) {
        this.mIconNormalTop = icon;
        this.mIconTop = icon;
        setIcon();
        return this;
    }

    public RTextViewHelper setIconNormalBottom(Drawable icon) {
        this.mIconNormalBottom = icon;
        this.mIconBottom = icon;
        setIcon();
        return this;
    }

    public Drawable getIconNormalLeft() {
        return mIconNormalLeft;
    }

    public Drawable getIconNormalRight() {
        return mIconNormalRight;
    }

    public Drawable getIconNormalTop() {
        return mIconNormalTop;
    }

    public Drawable getIconNormalBottom() {
        return mIconNormalBottom;
    }

    public RTextViewHelper setIconPressedLeft(Drawable icon) {
        this.mIconPressedLeft = icon;
        this.mIconLeft = icon;
        setIcon();
        return this;
    }

    public RTextViewHelper setIconPressedRight(Drawable icon) {
        this.mIconPressedRight = icon;
        this.mIconRight = icon;
        setIcon();
        return this;
    }

    public RTextViewHelper setIconPressedTop(Drawable icon) {
        this.mIconPressedTop = icon;
        this.mIconTop = icon;
        setIcon();
        return this;
    }

    public RTextViewHelper setIconPressedBottom(Drawable icon) {
        this.mIconPressedBottom = icon;
        this.mIconBottom = icon;
        setIcon();
        return this;
    }

    public Drawable getIconPressedLeft() {
        return mIconPressedLeft;
    }

    public Drawable getIconPressedRight() {
        return mIconPressedRight;
    }

    public Drawable getIconPressedTop() {
        return mIconPressedTop;
    }

    public Drawable getIconPressedBottom() {
        return mIconPressedBottom;
    }

    public RTextViewHelper setIconUnableLeft(Drawable icon) {
        this.mIconUnableLeft = icon;
        this.mIconLeft = icon;
        setIcon();
        return this;
    }

    public RTextViewHelper setIconUnableRight(Drawable icon) {
        this.mIconUnableRight = icon;
        this.mIconRight = icon;
        setIcon();
        return this;
    }

    public RTextViewHelper setIconUnableTop(Drawable icon) {
        this.mIconUnableTop = icon;
        this.mIconTop = icon;
        setIcon();
        return this;
    }

    public RTextViewHelper setIconUnableBottom(Drawable icon) {
        this.mIconUnableBottom = icon;
        this.mIconBottom = icon;
        setIcon();
        return this;
    }


    public Drawable getIconUnableLeft() {
        return mIconUnableLeft;
    }

    public Drawable getIconUnableRight() {
        return mIconUnableRight;
    }

    public Drawable getIconUnableTop() {
        return mIconUnableTop;
    }

    public Drawable getIconUnableBottom() {
        return mIconUnableBottom;
    }

    public RTextViewHelper setIconSelectedLeft(Drawable icon) {
        this.mIconSelectedLeft = icon;
        this.mIconLeft = icon;
        setIcon();
        return this;
    }

    public RTextViewHelper setIconSelectedRight(Drawable icon) {
        this.mIconSelectedRight = icon;
        this.mIconRight = icon;
        setIcon();
        return this;
    }

    public RTextViewHelper setIconSelectedTop(Drawable icon) {
        this.mIconSelectedTop = icon;
        this.mIconTop = icon;
        setIcon();
        return this;
    }

    public RTextViewHelper setIconSelectedBottom(Drawable icon) {
        this.mIconSelectedBottom = icon;
        this.mIconBottom = icon;
        setIcon();
        return this;
    }

    public Drawable getIconSelectedLeft() {
        return mIconSelectedLeft;
    }

    public Drawable getIconSelectedRight() {
        return mIconSelectedRight;
    }

    public Drawable getIconSelectedTop() {
        return mIconSelectedTop;
    }

    public Drawable getIconSelectedBottom() {
        return mIconSelectedBottom;
    }

    public RTextViewHelper setIconCheckedLeft(Drawable icon) {
        this.mIconCheckedLeft = icon;
        setIconLeft(icon);
        return this;
    }

    public RTextViewHelper setIconCheckedRight(Drawable icon) {
        this.mIconCheckedRight = icon;
        setIconRight(icon);
        return this;
    }

    public RTextViewHelper setIconCheckedTop(Drawable icon) {
        this.mIconCheckedTop = icon;
        setIconTop(icon);
        return this;
    }

    public RTextViewHelper setIconCheckedBottom(Drawable icon) {
        this.mIconCheckedBottom = icon;
        setIconBottom(icon);
        return this;
    }

    public Drawable getIconCheckedLeft() {
        return mIconCheckedLeft;
    }

    public Drawable getIconCheckedRight() {
        return mIconCheckedRight;
    }

    public Drawable getIconCheckedTop() {
        return mIconCheckedTop;
    }

    public Drawable getIconCheckedBottom() {
        return mIconCheckedBottom;
    }

    public RTextViewHelper setIconSizeLeft(int iconWidth, int iconHeight) {
        this.mIconWidthLeft = iconWidth;
        this.mIconHeightLeft = iconHeight;
        setIcon();
        return this;
    }

    public RTextViewHelper setIconSizeRight(int iconWidth, int iconHeight) {
        this.mIconWidthRight = iconWidth;
        this.mIconHeightRight = iconHeight;
        setIcon();
        return this;
    }

    public RTextViewHelper setIconSizeTop(int iconWidth, int iconHeight) {
        this.mIconWidthTop = iconWidth;
        this.mIconHeightTop = iconHeight;
        setIcon();
        return this;
    }

    public RTextViewHelper setIconSizeBottom(int iconWidth, int iconHeight) {
        this.mIconWidthBottom = iconWidth;
        this.mIconHeightBottom = iconHeight;
        setIcon();
        return this;
    }

    public int getIconWidthLeft() {
        return mIconWidthLeft;
    }

    public int getIconHeightLeft() {
        return mIconHeightLeft;
    }

    public int getIconWidthRight() {
        return mIconWidthRight;
    }

    public int getIconHeightRight() {
        return mIconHeightRight;
    }

    public int getIconWidthTop() {
        return mIconWidthTop;
    }

    public int getIconHeightTop() {
        return mIconHeightTop;
    }

    public int getIconWidthBottom() {
        return mIconWidthBottom;
    }

    public int getIconHeightBottom() {
        return mIconHeightBottom;
    }

    /**
     * 主要用于子类调用
     * 备注:用于库内确定逻辑的调用，不建议开发者直接调用
     */
    @Deprecated
    @SuppressWarnings("unchecked")
    protected void setIcon(Drawable icon) {
        this.mIcon = icon;
        setIcon();
    }

    /**
     * 主要用于子类调用
     * 备注:用于库内确定逻辑的调用，不建议开发者直接调用
     * 备注：添加上下左右之后此处功能默认 drawableLeft
     */
    @SuppressWarnings("unchecked")
    protected void setIconLeft(Drawable icon) {
        this.mIconLeft = icon;
        setIcon();
    }

    @SuppressWarnings("unchecked")
    protected void setIconRight(Drawable icon) {
        this.mIconRight = icon;
        setIcon();
    }

    @SuppressWarnings("unchecked")
    protected void setIconTop(Drawable icon) {
        this.mIconTop = icon;
        setIcon();
    }

    @SuppressWarnings("unchecked")
    protected void setIconBottom(Drawable icon) {
        this.mIconBottom = icon;
        setIcon();
    }

    private void setIcon() {
        //未设置图片大小
        if (mIconHeightLeft == 0 && mIconWidthLeft == 0) {
            if (mIconLeft != null) {
                mIconWidthLeft = mIconLeft.getIntrinsicWidth();
                mIconHeightLeft = mIconLeft.getIntrinsicHeight();
            }
        }
        if (mIconHeightRight == 0 && mIconWidthRight == 0) {
            if (mIconRight != null) {
                mIconWidthRight = mIconRight.getIntrinsicWidth();
                mIconHeightRight = mIconRight.getIntrinsicHeight();
            }
        }
        if (mIconHeightTop == 0 && mIconWidthTop == 0) {
            if (mIconTop != null) {
                mIconWidthTop = mIconTop.getIntrinsicWidth();
                mIconHeightTop = mIconTop.getIntrinsicHeight();
            }
        }
        if (mIconHeightBottom == 0 && mIconWidthBottom == 0) {
            if (mIconBottom != null) {
                mIconWidthBottom = mIconBottom.getIntrinsicWidth();
                mIconHeightBottom = mIconBottom.getIntrinsicHeight();
            }
        }

        //版本兼容代码
        if (mIconHeight == 0 && mIconWidth == 0) {
            if (mIcon != null) {
                mIconWidth = mIcon.getIntrinsicWidth();
                mIconHeight = mIcon.getIntrinsicHeight();
            }
        }

        if (isSingleDirection()) {//老版本逻辑
            setSingleCompoundDrawable(mIcon, mIconWidth, mIconHeight, mIconDirection);
        } else {
            setCompoundDrawables(mIconLeft, mIconRight, mIconTop, mIconBottom);
        }
    }

    /**
     * 新版本设置icon逻辑代码
     *
     * @param drawableLeft
     * @param drawableRight
     * @param drawableTop
     * @param drawableBottom
     */
    private void setCompoundDrawables(Drawable drawableLeft, Drawable drawableRight, Drawable drawableTop, Drawable drawableBottom) {
        if (drawableLeft != null)
            drawableLeft.setBounds(0, 0, mIconWidthLeft, mIconHeightLeft);
        if (drawableRight != null)
            drawableRight.setBounds(0, 0, mIconWidthRight, mIconHeightRight);
        if (drawableTop != null)
            drawableTop.setBounds(0, 0, mIconWidthTop, mIconHeightTop);
        if (drawableBottom != null)
            drawableBottom.setBounds(0, 0, mIconWidthBottom, mIconHeightBottom);
        //setDrawable
        mView.setCompoundDrawables(drawableLeft, drawableTop, drawableRight, drawableBottom);
    }

    /**
     * 老版本设置icon逻辑代码
     *
     * @param drawable
     * @param drawableWidth
     * @param drawableHeight
     * @param direction
     */
    @Deprecated
    private void setSingleCompoundDrawable(Drawable drawable, int drawableWidth, int drawableHeight, int direction) {
        if (drawable != null)
            drawable.setBounds(0, 0, drawableWidth, drawableHeight);
        mView.setCompoundDrawables(
                direction == ICON_DIR_LEFT ? drawable : null,
                direction == ICON_DIR_TOP ? drawable : null,
                direction == ICON_DIR_RIGHT ? drawable : null,
                direction == ICON_DIR_BOTTOM ? drawable : null);

    }


    /**
     * 设置 单一icon 和 文本 间距
     */
    private void setSingleIconWithText() {

        //drawable间距
        if (!mDrawableWithText || mView == null || mView.getWidth() == 0) return;

        int drawablePadding = mView.getCompoundDrawablePadding();
        int drawableWidth = mIconWidth;
        int drawableHeight = mIconHeight;
        int drawablePaddingHorizontal = drawablePadding;//水平方向上drawable间距
        int drawablePaddingVertical = drawablePadding;//垂直方向上drawable间距

        if (mIconDirection == ICON_DIR_LEFT || mIconDirection == ICON_DIR_RIGHT) {
            drawableHeight = 0;
            drawablePaddingVertical = 0;
        }
        if (mIconDirection == ICON_DIR_TOP || mIconDirection == ICON_DIR_BOTTOM) {
            drawableWidth = 0;
            drawablePaddingHorizontal = 0;
        }

        //水平方向计算
        float textWidth = TextViewUtils.get().getTextWidth(mView, drawableWidth, mPaddingLeft, mPaddingRight, drawablePaddingHorizontal);
        float bodyWidth = textWidth + drawableWidth + drawablePaddingHorizontal;//内容宽度
        float actualWidth = mView.getWidth() - (mPaddingLeft + mPaddingRight);//实际可用宽度
        int translateX = (int) (actualWidth - bodyWidth) / 2;//两边使用
        if (translateX < 0) translateX = 0;
        //垂直方向计算
        float textHeight = TextViewUtils.get().getTextHeight(mView, drawableHeight, mPaddingTop, mPaddingBottom, drawablePaddingVertical);
        float bodyHeight = textHeight + drawableHeight + drawablePaddingVertical;//内容高度
        float actualHeight = mView.getHeight() - (mPaddingTop + mPaddingBottom);//实际可用高度
        int translateY = (int) (actualHeight - bodyHeight) / 2;
        if (translateY < 0) translateY = 0;

        String paddingStr = new StringBuilder()
                .append(mView.getWidth())
                .append(mView.getHeight())
                .append(translateX).append(mPaddingLeft)
                .append(translateY).append(mPaddingTop)
                .append(translateX).append(mPaddingRight)
                .append(translateY).append(mPaddingBottom).toString();

        if (!paddingStr.equals(mCacheSingleIconPaddingVale)) {
            mCacheSingleIconPaddingVale = paddingStr;
            //关键技术点(padding 会调用 invalidate())
            mView.setPadding(translateX + mPaddingLeft, translateY + mPaddingTop, translateX + mPaddingRight, translateY + mPaddingBottom);
        }

    }

    /**
     * 设置 多个icon 和 文本 间距
     */
    private void setMultipleIconWithText() {
        //drawable间距
        if (!mDrawableWithText || mView == null || mView.getWidth() == 0) return;

        int drawablePadding = mView.getCompoundDrawablePadding();
        int drawablePaddingHorizontal = 0, drawablePaddingVertical = 0;
        if (mIconLeft != null) drawablePaddingHorizontal += drawablePadding;
        if (mIconRight != null) drawablePaddingHorizontal += drawablePadding;
        if (mIconTop != null) drawablePaddingVertical += drawablePadding;
        if (mIconBottom != null) drawablePaddingVertical += drawablePadding;

        final int drawableWidthFinal = mIconWidthLeft + mIconWidthRight;
        final int drawableHeightFinal = mIconHeightTop + mIconHeightBottom;
        final int drawablePaddingVerticalFinal = drawablePaddingVertical;//垂直方向上drawable间距
        final int drawablePaddingHorizontalFinal = drawablePaddingHorizontal;//水平方向上drawable间距

        //水平方向计算
        float textWidth = TextViewUtils.get().getTextWidth(mView, drawableWidthFinal, mPaddingLeft, mPaddingRight, drawablePaddingHorizontalFinal);
        float bodyWidth = textWidth + drawableWidthFinal + drawablePaddingHorizontalFinal;//内容宽度
        float actualWidth = mView.getWidth() - (mPaddingLeft + mPaddingRight);//实际可用宽度
        int translateX = (int) (actualWidth - bodyWidth) / 2;//两边使用
        if (translateX < 0) translateX = 0;
        //垂直方向计算
        float textHeight = TextViewUtils.get().getTextHeight(mView, drawableHeightFinal, mPaddingTop, mPaddingBottom, drawablePaddingVerticalFinal);
        float bodyHeight = textHeight + drawableHeightFinal + drawablePaddingVerticalFinal;//内容高度
        float actualHeight = mView.getHeight() - (mPaddingTop + mPaddingBottom);//实际可用高度
        int translateY = (int) (actualHeight - bodyHeight) / 2;
        if (translateY < 0) translateY = 0;

        String paddingStr = new StringBuilder()
                .append(mView.getWidth())
                .append(mView.getHeight())
                .append(translateX).append(mPaddingLeft)
                .append(translateY).append(mPaddingTop)
                .append(translateX).append(mPaddingRight)
                .append(translateY).append(mPaddingBottom).toString();

        if (!paddingStr.equals(mCacheMultipleIconPaddingVale)) {
            mCacheMultipleIconPaddingVale = paddingStr;
            //关键技术点(padding 会调用 invalidate())
            mView.setPadding(translateX + mPaddingLeft, translateY + mPaddingTop, translateX + mPaddingRight, translateY + mPaddingBottom);
        }
    }

    /************************
     * text color
     ************************/

    public RTextViewHelper setTextColorNormal(@ColorInt int textColor) {
        this.mTextColorNormal = textColor;
        updateTextColor();
        return this;
    }

    public RTextViewHelper setTextColorPressed(@ColorInt int textColor) {
        this.mTextColorPressed = textColor;
        mHasPressedTextColor = true;
        updateTextColor();
        return this;
    }

    public RTextViewHelper setTextColorUnable(@ColorInt int textColor) {
        this.mTextColorUnable = textColor;
        mHasUnableTextColor = true;
        updateTextColor();
        return this;
    }

    public RTextViewHelper setTextColorSelected(@ColorInt int textColor) {
        this.mTextColorSelected = textColor;
        mHasSelectedTextColor = true;
        updateTextColor();
        return this;
    }

    public RTextViewHelper setTextColor(@ColorInt int normal, @ColorInt int pressed, @ColorInt int unable, @ColorInt int selected, @ColorInt int checked) {
        this.mTextColorNormal = normal;
        this.mTextColorPressed = pressed;
        this.mTextColorUnable = unable;
        this.mTextColorSelected = selected;
        this.mTextColorChecked = checked;
        mHasPressedTextColor = true;
        mHasUnableTextColor = true;
        mHasSelectedTextColor = true;
        mHasCheckedTextColor = true;
        updateTextColor();
        return this;
    }

    public int getTextColorNormal() {
        return mTextColorNormal;
    }

    public int getTextColorPressed() {
        return mTextColorPressed;
    }

    public int getTextColorUnable() {
        return mTextColorUnable;
    }

    public int getTextColorSelected() {
        return mTextColorSelected;
    }

    public RTextViewHelper setTextColorChecked(@ColorInt int textColor) {
        this.mTextColorChecked = textColor;
        mHasCheckedTextColor = true;
        updateTextColor();
        return this;
    }

    public int getTextColorChecked() {
        return mTextColorChecked;
    }

    protected void setTextColor() {
        //unable,focused,pressed,checked,selected,normal
        int[] colors = new int[]{mTextColorUnable, mTextColorPressed, mTextColorPressed, mTextColorChecked, mTextColorSelected, mTextColorNormal};
        mTextColorStateList = new ColorStateList(states, colors);
        mView.setTextColor(mTextColorStateList);
    }

    /**
     * 更新文本颜色
     */
    private void updateTextColor() {
        setupDefaultValue(false);
        setTextColor();
    }

    /**
     * 是否仅支持单一方向ICON
     * 老版本仅支持单一方向icon，版本兼容判断逻辑
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    protected boolean isSingleDirection() {
        //老版本属性任意一个不为空，默认用户使用老版本逻辑，新版本只能使用带方向后缀的属性字段
        if (mIconNormal != null || mIconPressed != null || mIconUnable != null || mIconSelected != null || mIconChecked != null) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public void onGlobalLayout() {
        super.onGlobalLayout();
        //由于iconWithText采用setPadding方式实现，此处保存原始用户设置的padding
        mPaddingLeft = mView.getPaddingLeft();
        mPaddingRight = mView.getPaddingRight();
        mPaddingTop = mView.getPaddingTop();
        mPaddingBottom = mView.getPaddingBottom();
    }

    /**
     * 设置是否启用
     * 备注:用于库内确定逻辑的调用，不建议开发者直接调用
     */
    @SuppressWarnings("unchecked")
    @Override
    public void setEnabled(boolean enabled) {
        mIconLeft = !enabled && mIconUnableLeft != null ? mIconUnableLeft : mIconNormalLeft;
        mIconRight = !enabled && mIconUnableRight != null ? mIconUnableRight : mIconNormalRight;
        mIconTop = !enabled && mIconUnableTop != null ? mIconUnableTop : mIconNormalTop;
        mIconBottom = !enabled && mIconUnableBottom != null ? mIconUnableBottom : mIconNormalBottom;
        mIcon = !enabled && mIconUnable != null ? mIconUnable : mIconNormal;
        setIcon();
    }

    /**
     * 设置是否选中
     * 备注:用于库内确定逻辑的调用，不建议开发者直接调用
     */
    @SuppressWarnings("unchecked")
    @Override
    public void setSelected(boolean selected) {
        if (!mView.isEnabled()) return;
        mIconLeft = selected && mIconSelectedLeft != null ? mIconSelectedLeft : mIconNormalLeft;
        mIconRight = selected && mIconSelectedRight != null ? mIconSelectedRight : mIconNormalRight;
        mIconTop = selected && mIconSelectedTop != null ? mIconSelectedTop : mIconNormalTop;
        mIconBottom = selected && mIconSelectedBottom != null ? mIconSelectedBottom : mIconNormalBottom;
        mIcon = selected && mIconSelected != null ? mIconSelected : mIconNormal;
        setIcon();
    }

    /**
     * 触摸事件逻辑
     * 备注:用于库内确定逻辑的调用，不建议开发者直接调用
     */
    @SuppressWarnings("unchecked")
    @Override
    public void onTouchEvent(MotionEvent event) {
        if (!mView.isEnabled()) return;
        if (isCompoundButtonChecked()) return;
        if (mView.isSelected()) return;
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_UP://抬起
            case MotionEvent.ACTION_CANCEL://父级控件获取控制权
                mIconLeft = mIconNormalLeft;
                mIconRight = mIconNormalRight;
                mIconTop = mIconNormalTop;
                mIconBottom = mIconNormalBottom;
                mIcon = mIconNormal;
                setIcon();
                break;
            case MotionEvent.ACTION_DOWN://按下
                //对应的状态有没有值，没有则不改变（使用默认状态）
                if (mIconPressedLeft != null) mIconLeft = mIconPressedLeft;
                if (mIconPressedRight != null) mIconRight = mIconPressedRight;
                if (mIconPressedTop != null) mIconTop = mIconPressedTop;
                if (mIconPressedBottom != null) mIconBottom = mIconPressedBottom;
                if (mIconPressed != null) mIcon = mIconPressed;
                setIcon();
                break;
            case MotionEvent.ACTION_MOVE://移动
                int x = (int) event.getX();
                int y = (int) event.getY();
                if (isOutsideView(x, y)) {
                    mIconLeft = mIconNormalLeft;
                    mIconRight = mIconNormalRight;
                    mIconTop = mIconNormalTop;
                    mIconBottom = mIconNormalBottom;
                    mIcon = mIconNormal;
                    setIcon();
                }
                break;
        }
    }

    /**
     * ICON 和 文本 一起居中
     * 备注:用于库内确定逻辑的调用，不建议开发者直接调用
     */
    @SuppressWarnings("unchecked")
    @Override
    public void drawIconWithText() {
        if (isSingleDirection()) {
            setSingleIconWithText();
        } else {
            setMultipleIconWithText();
        }
    }

    /**
     * 选中监听，用于更新icon状态
     * 备注:用于库内确定逻辑的调用，不建议开发者直接调用
     *
     * @param checked
     */
    @SuppressWarnings("unchecked")
    public void setChecked(boolean checked) {
        setIconLeft(checked ? mIconCheckedLeft : getIconNormalLeft());
        setIconRight(checked ? mIconCheckedRight : getIconNormalRight());
        setIconTop(checked ? mIconCheckedTop : getIconNormalTop());
        setIconBottom(checked ? mIconCheckedBottom : getIconNormalBottom());
        setIcon(checked ? mIconChecked : getIconNormal());
    }


    /**
     * 按钮是否选中状态,子类重写
     *
     * @return
     */
    protected boolean isCompoundButtonChecked() {
        return false;
    }
}
