package com.example.evaluategraph;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import androidx.annotation.Nullable;

public class ShaderView extends View {

    private final Paint paint;
    private final Path path;

    private float offset = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    private final Bitmap bitmap;

    public ShaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(30);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.aaa);
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        paint.setShader(shader);
        path = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(path, paint);
    }

    private float mX;
    private float mY;
    private float mPreX;
    private float mPreY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.reset();
                float x = event.getX();
                float y = event.getY();
                mX = x;
                mY = y;
                path.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                float x1 = event.getX();
                float y1 = event.getY();
                mPreX = mX;
                mPreY = mY;
                float dx = Math.abs(x1 - mPreX);
                float dy = Math.abs(y1 - mPreY);

                if (dx >= offset || dy >= offset) {
                    float cX = (x1 + mPreX) / 2;
                    float cY = (y1 + mPreY) / 2;
                    path.quadTo(mPreX, mPreY, cX, cY);
                    mX = x1;
                    mY = y1;
                }
        }

        invalidate();
        return true;

    }
}
