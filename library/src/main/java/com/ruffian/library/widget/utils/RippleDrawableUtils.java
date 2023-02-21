package com.ruffian.library.widget.utils;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

/**
 * RippleDrawable包装类
 * 备注：此类没有实质意义，纯粹是绕一圈支持新版Android studio预览自定义View
 */
public class RippleDrawableUtils {

    private Drawable contentDrawable, maskDrawable;

    /**
     * 此处的参数必须为Object类型，否则导致 Android studio 无法预览
     *
     * @param content
     * @param mask
     */
    public RippleDrawableUtils(Object content, Object mask) {
        this.contentDrawable = content == null ? null : (Drawable) content;
        this.maskDrawable = mask == null ? null : (Drawable) mask;
    }

    /**
     *
     * 新版Android studio 奇葩报错
     *
     * Exception Details java.lang.VerifyError: Bad type on operand stack Exception
     * Details: Location: com/ruffian/library/widget/helper/RBaseHelper.getRippleDrawableWithTag(ZI)[Ljava/lang/Object; @233: invokespecial
     * Reason: Type 'java/lang/Object' (current frame, stack[2]) is not assignable to 'android/graphics/drawable/Drawable'
     * Current Frame:
     * bci: @233
     * flags: { }
     * locals: { 'com/ruffian/library/widget/helper/RBaseHelper', integer, integer, 'java/lang/Object', 'java/lang/Object', '[[I', '[I', 'android/content/res/ColorStateList' }
     * stack: { uninitialized 226, uninitialized 226, 'java/lang/Object', 'java/lang/Object' }
     *
     */

    /**
     * 获取 RippleDrawable
     * 新版 Android studio 有个BUG，目标接收 Drawable 的参数，如果开发者传递 null 会报错：'java/lang/Object'  is not assignable to 'android/graphics/drawable/Drawable
     * 这里曲线救国，改为 Object 接收 content 和 mask
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RippleDrawable getRippleDrawable(@NonNull ColorStateList color) {
        //color – The ripple color
        //content – The content drawable, may be null
        //mask – The mask drawable, may be null
        return new RippleDrawable(color, contentDrawable, maskDrawable);
    }

}
