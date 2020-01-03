package com.ruffian.library.widget.helper;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.annotation.ColorInt;
import androidx.appcompat.content.res.AppCompatResources;
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
        states = new int[6][];
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
        if (isChecked()) setIcon(mIconChecked);

        /**
         * 设置文字颜色默认值
         */
        if (!mHasCheckedTextColor) {
            mTextColorChecked = mTextColorNormal;
        }

        //unable,focused,pressed,checked,selected,normal
        states[0] = new int[]{-android.R.attr.state_enabled};//unable
        states[1] = new int[]{android.R.attr.state_focused};//focused
        states[2] = new int[]{android.R.attr.state_pressed};//pressed
        states[3] = new int[]{android.R.attr.state_checked};//checked
        states[4] = new int[]{android.R.attr.state_selected};//selected
        states[5] = new int[]{android.R.attr.state_enabled};//normal

        //设置文本颜色
        setTextColor();
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

    public RCheckHelper setTextColor(@ColorInt int normal, @ColorInt int pressed, @ColorInt int unable, @ColorInt int checked, @ColorInt int selected) {
        this.mTextColorChecked = checked;
        this.mHasCheckedTextColor = true;
        super.setTextColor(normal, pressed, unable, selected);
        return this;
    }

    @Override
    protected void setTextColor() {
        //unable,focused,pressed,checked,selected,normal
        int[] colors = new int[]{mTextColorUnable, mTextColorPressed, mTextColorPressed, mTextColorChecked, mTextColorSelected, mTextColorNormal};
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

    private boolean isChecked() {
        if (mView != null) {
            return ((CompoundButton) mView).isChecked();
        }
        return false;
    }

    /**
     * 选中监听，用于更新icon状态
     * 备注:用于库内确定逻辑的调用，不建议开发者直接调用
     *
     * @param checked
     */
    @SuppressWarnings("unchecked")
    public void setChecked(boolean checked) {
        setIcon(checked ? mIconChecked : getIconNormal());
    }

}
