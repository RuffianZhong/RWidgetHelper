package com.ruffian.library.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.ruffian.library.widget.helper.RImageViewHelper;
import com.ruffian.library.widget.rounded.RoundDrawable;

/**
 * RImageView
 *
 * @author ZhongDaFeng
 */
public class RImageView extends ImageView {

    private RImageViewHelper mHelper;

    public RImageView(Context context) {
        this(context, null);
    }

    public RImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //  mHelper = new RImageViewHelper(context, this, attrs);
    }


    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        invalidate();
    }

    private Drawable mDrawable;
    private ScaleType mScaleType;
    private float mBorderWidth = 10f;
    private int mBorderColor = Color.RED;
    private boolean mIsCircle = true;


    @Override
    public ScaleType getScaleType() {
        return mScaleType;
    }


    @Override
    public void setScaleType(ScaleType scaleType) {
        assert scaleType != null;

       /* if (mScaleType != scaleType) {
            mScaleType = scaleType;

            switch (scaleType) {
                case CENTER:
                case CENTER_CROP:
                case CENTER_INSIDE:
                case FIT_CENTER:
                case FIT_START:
                case FIT_END:
                case FIT_XY:
                    super.setScaleType(ScaleType.FIT_XY);
                    break;
                default:
                    super.setScaleType(scaleType);
                    break;
            }

            updateDrawableAttrs();
            updateBackgroundDrawableAttrs(false);
            invalidate();
        }*/
    }

    private void updateBackgroundDrawableAttrs(boolean convert) {
       /* if (mMutateBackground) {
            if (convert) {
                mBackgroundDrawable = RoundedDrawable.fromDrawable(mBackgroundDrawable);
            }
            updateAttrs(mBackgroundDrawable, ScaleType.FIT_XY);
        }*/
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        mDrawable = RoundDrawable.fromBitmap(bm);
        updateDrawableAttrs();
        super.setImageDrawable(mDrawable);
    }


    @Override
    public void setImageDrawable(Drawable drawable) {
        //mResource = 0;
        mDrawable = RoundDrawable.fromDrawable(drawable);

        updateDrawableAttrs();
        super.setImageDrawable(mDrawable);
    }

    private void updateDrawableAttrs() {
        updateAttrs(mDrawable, mScaleType);
    }

    private void updateAttrs(Drawable drawable, ScaleType scaleType) {
        if (drawable == null) {
            return;
        }

        if (drawable instanceof RoundDrawable) {
          //  Log.e("tag", "========qqq==========w:" + getWidth() + "h:" + getHeight());
          /*  ((RoundDrawable) drawable)
                    .setScaleType(scaleType)
                    .setBorderWidth(mBorderWidth)
                    .setBorderColor(mBorderColor)
                    .setCircle(mIsCircle);*/

           /* if (mCornerRadii != null) {
                ((RoundedDrawable) drawable).setCornerRadius(
                        mCornerRadii[Corner.TOP_LEFT],
                        mCornerRadii[Corner.TOP_RIGHT],
                        mCornerRadii[Corner.BOTTOM_RIGHT],
                        mCornerRadii[Corner.BOTTOM_LEFT]);
            }*/

            //  applyColorMod();
        } else if (drawable instanceof LayerDrawable) {
            // loop through layers to and set drawable attrs
            LayerDrawable ld = ((LayerDrawable) drawable);
            for (int i = 0, layers = ld.getNumberOfLayers(); i < layers; i++) {
                updateAttrs(ld.getDrawable(i), scaleType);
            }
        }
    }

   /* @Override
    protected void onDraw(Canvas canvas) {
        if (!mHelper.isNormal() && getVisibility() == VISIBLE) {
            mHelper.onDraw(canvas);
        } else {
            super.onDraw(canvas);
        }
    }*/

 /*   public RImageViewHelper getHelper() {
        return mHelper;
    }*/

}
