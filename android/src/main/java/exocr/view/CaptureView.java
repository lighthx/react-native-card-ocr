package exocr.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.wintone.view.ViewfinderView;

/**
 * description: 扫描框
 * create by kalu on 2018/11/20 13:28
 */
public final class CaptureView extends View {
    private ViewfinderView myView;
    private boolean isFront = true;

    public CaptureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setFront(boolean front) {
        isFront = front;
    }

    /**********************************************************************************************/

//    private final DashPathEffect mDashPathEffect = new DashPathEffect(new float[]{20f, 10f}, 0);
    private final int[] laserAlpha = {0, 64, 128, 192, 255, 192, 128, 64};
    private int laserAlphaIndex = 0;
    private   int animatedIndex=0;

    @Override
    protected void onDraw(Canvas canvas) {

        final int canvasHeight = canvas.getHeight();
        final int canvasWidth = canvas.getWidth();
        Log.e("kalu1", "canvasHeight = " + canvasHeight + ", canvasWidth = " + canvasWidth);

        final Paint mPaint = new Paint();
        mPaint.clearShadowLayer();
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setFakeBoldText(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.parseColor("#ff7f00"));
        final DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        final float stroke = 2 * metrics.density;
        mPaint.setStrokeWidth(stroke);

//        mPaint.setPathEffect(mDashPathEffect);

        if (canvasWidth < canvasHeight) {

            final float layerWidth = canvasWidth * 0.65f;
            final float layerHeight = layerWidth / 1.7f;
            final float layerLeft = (canvasWidth - layerWidth) / 2;
            final float layerTop = (canvasHeight - layerHeight) / 2;
            if (isFront) {
                drawFace(canvas, mPaint, layerWidth, layerHeight, layerLeft, layerTop);
            } else {
                drawEmblem(canvas, mPaint, layerWidth, layerHeight, layerLeft, layerTop);
            }

        } else {

            final float layerHeight = canvasHeight * 0.75f;
            final float layerWidth = layerHeight * 1.8f;
            final float layerLeft = (canvasWidth - layerWidth) / 2;
            final float layerTop = (canvasHeight - layerHeight) / 2;

            if (isFront) {
                drawFace(canvas, mPaint, layerWidth, layerHeight, layerLeft, layerTop);
            } else {
                drawEmblem(canvas, mPaint, layerWidth, layerHeight, layerLeft, layerTop);
            }
        }
    }

    private final void drawFace(final Canvas canvas, final Paint paint, final float layerWidth, final float layerHeight, final float layerLeft, final float layerTop) {
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(50);
        paint.setColor(Color.WHITE);
        final String text="扫描身份证人像面";
        float stringWidth = paint.measureText(text);
        float  width=(getWidth()-stringWidth)/2;
        canvas.drawText(text,width,getHeight()/2-40,paint);
    }

    private final void drawEmblem(final Canvas canvas, final Paint paint, final float layerWidth, final float layerHeight, final float layerLeft, final float layerTop) {
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(50);
        paint.setColor(Color.WHITE);
        final String text="扫描身份证国徽面";
        float stringWidth = paint.measureText(text);
        float  width=(getWidth()-stringWidth)/2;
        canvas.drawText(text,width,getHeight()/2-40,paint);
    }

    /**********************************************************************************************/

    @Override
    public void setBackground(Drawable background) {
    }

    @Override
    public void setBackgroundColor(int color) {
    }

    @Override
    public void setBackgroundResource(int resid) {
    }

    @Override
    public void setBackgroundDrawable(Drawable background) {
    }

    @Override
    public void setBackgroundTintList(@Nullable ColorStateList tint) {
    }

    @Override
    public void setBackgroundTintMode(@Nullable PorterDuff.Mode tintMode) {
    }

    /**********************************************************************************************/
}
    
