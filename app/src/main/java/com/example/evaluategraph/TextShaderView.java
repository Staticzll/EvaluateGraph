package com.example.evaluategraph;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

public class TextShaderView extends View {

    private final Paint paint;
    private float[] positions = {0.2f, 0.5f, 0.8f};
    private int[] colors = {Color.parseColor("#4C4D4B"), Color.parseColor("#D9D6DA"), Color.parseColor("#4C4D4B")};
    private int offset;
    private LinearGradient linearGradient;

    public TextShaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setTextSize(180);

        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setIntValues(-1000, 1000);
        valueAnimator.setDuration(2000);
        valueAnimator.setRepeatCount(-1);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {


            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                offset = (int) animation.getAnimatedValue();
                Log.d("Tag","offset:"+offset);
                linearGradient = new LinearGradient(offset, 300, offset + 1000, 600, colors, positions, Shader.TileMode.CLAMP);
                paint.setShader(linearGradient);
                invalidate();
            }
        });
        valueAnimator.start();


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.parseColor("#232423"));
        paint.setAlpha(255);
        canvas.drawText("演示文字", 100, 200, paint);
    }
}
