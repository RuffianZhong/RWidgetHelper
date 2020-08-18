package com.ruffian.library.widget.clip;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;

/**
 * ClipHelper
 *
 * @author ZhongDaFeng
 */
public class ClipHelper implements IClip {

    private final Paint clipPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected PorterDuffXfermode DST_OUT_MODE = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
    protected PorterDuffXfermode DST_IN_MODE = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
    private final Path clipPath = new Path();
    private final Path rectView = new Path();

    private ClipPathManager clipPathManager = new ClipPathManager();

    private boolean requestShapeUpdate = true;

    private View mView;
    private boolean mClipLayout;

    public ClipHelper() {
        clipPaint.setAntiAlias(true);
        clipPaint.setColor(Color.BLUE);
        clipPaint.setStyle(Paint.Style.FILL);
        clipPaint.setStrokeWidth(1);
    }


    /**
     * 初始化Clip
     *
     * @param view
     * @param clipLayout
     * @param clipPathCreator
     */
    public void initClip(View view, boolean clipLayout, ClipPathManager.ClipPathCreator clipPathCreator) {
        this.mView = view;
        this.mClipLayout = clipLayout;
        if (!canClip()) return;

        getView().setDrawingCacheEnabled(true);
        getView().setWillNotDraw(false);

        if (Build.VERSION.SDK_INT <= 27) {
            clipPaint.setXfermode(DST_IN_MODE);
            getView().setLayerType(View.LAYER_TYPE_SOFTWARE, clipPaint); //Only works for software layers
        } else {
            clipPaint.setXfermode(DST_OUT_MODE);
            getView().setLayerType(View.LAYER_TYPE_SOFTWARE, null); //Only works for software layers
        }

        //设置clip
        clipPathManager.setClipPathCreator(clipPathCreator);

        requestShapeUpdate();
    }


    @Override
    public void dispatchDraw(Canvas canvas) {
        if (!canClip()) return;

        if (requestShapeUpdate) {
            calculateLayout(canvas.getWidth(), canvas.getHeight());
            requestShapeUpdate = false;
        }
        if (Build.VERSION.SDK_INT <= 27) {
            canvas.drawPath(clipPath, clipPaint);
        } else {
            canvas.drawPath(rectView, clipPaint);
        }

        if (Build.VERSION.SDK_INT <= 27) {
            getView().setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
    }

    @Override
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (!canClip()) return;
        if (changed) requestShapeUpdate();
    }


    private void calculateLayout(int width, int height) {
        rectView.reset();
        rectView.addRect(0f, 0f, 1f * getView().getWidth(), 1f * getView().getHeight(), Path.Direction.CW);

        if (width > 0 && height > 0) {
            clipPathManager.setupClipLayout(width, height);
            clipPath.reset();
            clipPath.set(clipPathManager.getClipPath());

            //invert the path for android P
            if (Build.VERSION.SDK_INT > 27) {
                final boolean success = rectView.op(clipPath, Path.Op.DIFFERENCE);
            }

            //this needs to be fixed for 25.4.0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && ViewCompat.getElevation(getView()) > 0f) {
                try {
                    getView().setOutlineProvider(getView().getOutlineProvider());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        getView().postInvalidate();
    }

    /**
     * 请求更新
     */
    public void requestShapeUpdate() {
        this.requestShapeUpdate = true;
        getView().postInvalidate();
    }

    public View getView() {
        return mView;
    }

    /**
     * 是否满足裁剪条件
     *
     * @return
     */
    public boolean canClip() {
        return getView() != null && getView() instanceof ViewGroup && mClipLayout;
    }


}
