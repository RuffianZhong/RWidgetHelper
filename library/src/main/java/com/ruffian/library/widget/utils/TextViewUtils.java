package com.ruffian.library.widget.utils;

import android.graphics.Paint;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

/**
 * TextView工具类
 *
 * @author ZhongDaFeng
 */
public class TextViewUtils {

    private static TextViewUtils instance = null;

    private TextViewUtils() {
    }

    public static TextViewUtils get() {
        if (instance == null) {
            synchronized (TextViewUtils.class) {
                if (instance == null) {
                    instance = new TextViewUtils();
                }
            }
        }
        return instance;
    }


    /**
     * 获取Text实际宽度
     * 备注:单行最大宽度
     *
     * @param view                      view
     * @param drawableWidth             drawable宽度
     * @param paddingLeft               内边距-左
     * @param paddingRight              内边距-右
     * @param drawablePaddingHorizontal 水平方向上drawable间距
     * @return
     */
    public float getTextWidth(TextView view, int drawableWidth, int paddingLeft, int paddingRight, int drawablePaddingHorizontal) {
        if (view == null) return 0;

        float textWidth;
        /**
         * 1.是否存在\n换行（获取宽度最大的值）
         * 2.不存在\n获取单行宽度值（判断是否自动换行临界值）
         */
        String originalText = view.getText().toString();
        if (originalText.contains("\n")) {
            String[] originalTextArray;
            ArrayList<Float> widthList;
            originalTextArray = originalText.split("\n");
            int arrayLen = originalTextArray.length;
            widthList = new ArrayList<>(arrayLen);
            for (int i = 0; i < arrayLen; i++) {
                widthList.add(view.getPaint().measureText(originalTextArray[i]));
            }
            textWidth = Collections.max(widthList);
        } else {
            textWidth = view.getPaint().measureText(originalText);
        }

        //计算自动换行临界值，不允许超过换行临界值
        int maxWidth = view.getWidth() - drawableWidth - paddingLeft - paddingRight - drawablePaddingHorizontal;
        if (textWidth > maxWidth) {
            textWidth = maxWidth;
        }
        return textWidth;
    }

    /**
     * 获取Text实际高度
     * 备注:多行最大高度
     *
     * @param view                    view
     * @param drawableHeight          drawable高度
     * @param paddingTop              内边距-左
     * @param paddingBottom           内边距-右
     * @param drawablePaddingVertical 垂直方向上drawable间距
     * @return
     */
    public float getTextHeight(TextView view, int drawableHeight, int paddingTop, int paddingBottom, int drawablePaddingVertical) {
        if (view == null) return 0;

        /**
         * 1.单行高度*行数
         * 2.最大高度临界值
         */
        Paint.FontMetrics fontMetrics = view.getPaint().getFontMetrics();
        float singleLineHeight = Math.abs((fontMetrics.bottom - fontMetrics.top));//单行高度
        float textHeight = singleLineHeight * view.getLineCount();

        //最大高度临界值，不允许超过最大高度临界值
        int maxHeight = view.getHeight() - drawableHeight - paddingTop - paddingBottom - drawablePaddingVertical;//最大允许的宽度
        if (textHeight > maxHeight) {
            textHeight = maxHeight;
        }
        return textHeight;
    }

}
