package com.ruffian.library.widget.helper;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.content.res.AppCompatResources;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.ruffian.library.widget.R;

/**
 * Check-Helper
 *
 * @author ZhongDaFeng
 */
public class RCheckHelper extends RBaseHelper<CompoundButton> {

    int[][] states = new int[4][];

    // Text
    private int mTextColorNormal;
    private int mTextColorPressed;
    private int mTextColorUnable;
    private int mTextColorChecked;
    private ColorStateList mTextColorStateList;
    private int currentTextColor;//默认原生字体颜色

    //typeface
    private String mTypefacePath;

    /**
     * 是否设置对应的属性
     */
    private boolean mHasPressedTextColor = false;
    private boolean mHasUnableTextColor = false;
    private boolean mHasCheckedTextColor = false;


    public RCheckHelper(Context context, CompoundButton view, AttributeSet attrs) {
        super(context, view, attrs);
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
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RRadioButton);
        //text
        currentTextColor = mView.getCurrentTextColor();
        mTextColorNormal = a.getColor(R.styleable.RRadioButton_text_color_normal, currentTextColor);
        mTextColorPressed = a.getColor(R.styleable.RRadioButton_text_color_pressed, 0);
        mTextColorUnable = a.getColor(R.styleable.RRadioButton_text_color_unable, 0);
        mTextColorChecked = a.getColor(R.styleable.RRadioButton_text_color_checked, 0);
        //typeface
        mTypefacePath = a.getString(R.styleable.RRadioButton_text_typeface);

        a.recycle();

        mHasPressedTextColor = mTextColorPressed < 0;
        mHasUnableTextColor = mTextColorUnable < 0;
        mHasCheckedTextColor = mTextColorChecked < 0;

        //setup
        setup();

    }

    /**
     * 设置
     */
    private void setup() {

        /**
         * 设置文字颜色默认值
         */
        if (!mHasPressedTextColor) {
            mTextColorPressed = mTextColorNormal;
        }
        if (!mHasUnableTextColor) {
            mTextColorUnable = mTextColorNormal;
        }
        if (!mHasCheckedTextColor) {
            mTextColorChecked = mTextColorNormal;
        }
        //state_pressed,state_checked,Unable,Normal
        states[0] = new int[]{android.R.attr.state_enabled, android.R.attr.state_pressed};
        states[1] = new int[]{android.R.attr.state_checked};
        states[2] = new int[]{-android.R.attr.state_enabled};
        states[3] = new int[]{android.R.attr.state_enabled, -android.R.attr.state_checked};

        //设置文本颜色
        setTextColor();

        //设置文本字体样式
        setTypeface();

    }

    /************************
     * Typeface
     ************************/

    public RCheckHelper setTypeface(String typefacePath) {
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
     * text color
     ************************/

    public RCheckHelper setTextColorNormal(int textColor) {
        this.mTextColorNormal = textColor;
        if (!mHasPressedTextColor) {
            mTextColorPressed = mTextColorNormal;
        }
        if (!mHasUnableTextColor) {
            mTextColorUnable = mTextColorNormal;
        }
        if (!mHasCheckedTextColor) {
            mTextColorChecked = mTextColorNormal;
        }
        setTextColor();
        return this;
    }

    public int getTextColorNormal() {
        return mTextColorNormal;
    }

    public RCheckHelper setPressedTextColor(int textColor) {
        this.mTextColorPressed = textColor;
        this.mHasPressedTextColor = true;
        setTextColor();
        return this;
    }

    public int getPressedTextColor() {
        return mTextColorPressed;
    }

    public RCheckHelper setTextColorUnable(int textColor) {
        this.mTextColorUnable = textColor;
        this.mHasUnableTextColor = true;
        setTextColor();
        return this;
    }

    public int getTextColorUnable() {
        return mTextColorUnable;
    }

    public RCheckHelper setTextColorChecked(int textColor) {
        this.mTextColorChecked = textColor;
        this.mHasCheckedTextColor = true;
        setTextColor();
        return this;
    }

    public int getTextColorChecked() {
        return mTextColorChecked;
    }

    public void setTextColor(int normal, int pressed, int unable, int checked) {
        this.mTextColorNormal = normal;
        this.mTextColorPressed = pressed;
        this.mTextColorUnable = unable;
        this.mTextColorChecked = checked;
        this.mHasPressedTextColor = true;
        this.mHasUnableTextColor = true;
        this.mHasCheckedTextColor = true;
        setTextColor();
    }

    private void setTextColor() {
        //state_pressed,state_checked,Unable,Normal
        int[] colors = new int[]{mTextColorPressed, mTextColorChecked, mTextColorUnable, mTextColorNormal};
        mTextColorStateList = new ColorStateList(states, colors);
        mView.setTextColor(mTextColorStateList);
    }

}
