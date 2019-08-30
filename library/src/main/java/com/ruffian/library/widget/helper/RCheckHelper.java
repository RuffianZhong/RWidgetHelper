package com.ruffian.library.widget.helper;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.v7.content.res.AppCompatResources;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.CompoundButton;

import com.ruffian.library.widget.R;

/**
 * Check-Helper
 *
 * @author ZhongDaFeng
 */
public class RCheckHelper extends RTextViewHelper {

    // Text
    private int mTextColorChecked;
    private boolean mHasCheckedTextColor = false;
    //Icon
    private Drawable mIconChecked;

    {
        //先于构造函数重置
        states = new int[4][];
    }


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
        mTextColorChecked = a.getColor(R.styleable.RRadioButton_text_color_checked, 0);

        //icon
        //Vector兼容处理
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mIconChecked = a.getDrawable(R.styleable.RRadioButton_icon_src_checked);
        } else {
            int checkedId = a.getResourceId(R.styleable.RRadioButton_icon_src_checked, -1);
            if (checkedId != -1)
                mIconChecked = AppCompatResources.getDrawable(context, checkedId);
        }

        a.recycle();

        mHasCheckedTextColor = mTextColorChecked != 0;

        //setup
        setup();

    }

    /**
     * 设置
     */
    private void setup() {

        /**
         * icon
         */
        if (isChecked()) {
            setIcon(mIconChecked);
        }

        /**
         * 设置文字颜色默认值
         */
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

        ((CompoundButton) mView).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mView == buttonView) {
                    setIcon(isChecked ? mIconChecked : getIconNormal());
                }
            }
        });
    }


    /************************
     * text color
     ************************/

    @Override
    public RCheckHelper setTextColorNormal(@ColorInt int textColor) {
        if (!mHasCheckedTextColor) {
            mTextColorChecked = textColor;
        }
        super.setTextColorNormal(textColor);
        return this;
    }

    public RCheckHelper setTextColorChecked(@ColorInt int textColor) {
        this.mTextColorChecked = textColor;
        this.mHasCheckedTextColor = true;
        setTextColor();
        return this;
    }

    public int getTextColorChecked() {
        return mTextColorChecked;
    }

    public RCheckHelper setTextColor(@ColorInt int normal, @ColorInt int pressed, @ColorInt int unable, @ColorInt int checked) {
        this.mTextColorChecked = checked;
        this.mHasCheckedTextColor = true;
        super.setTextColor(normal, pressed, unable);
        return this;
    }

    @Override
    protected void setTextColor() {
        //state_pressed,state_checked,Unable,Normal
        int[] colors = new int[]{mTextColorPressed, mTextColorChecked, mTextColorUnable, mTextColorNormal};
        mTextColorStateList = new ColorStateList(states, colors);
        mView.setTextColor(mTextColorStateList);
    }

    /************************
     * icon
     ************************/

    public RCheckHelper setIconChecked(Drawable icon) {
        this.mIconChecked = icon;
        setIcon(icon);
        return this;
    }

    public Drawable getIconChecked() {
        return mIconChecked;
    }

    @Override
    public void onTouchEvent(MotionEvent event) {
        if (!isChecked()) {
            super.onTouchEvent(event);
        }
    }

    public boolean isChecked() {
        if (mView != null) {
            return ((CompoundButton) mView).isChecked();
        }
        return false;
    }
}
