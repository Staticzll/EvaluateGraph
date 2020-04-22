package com.example.evaluategraph;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

public class MultiLineText extends View {

    private Paint paint;

    public MultiLineText(Context context) {
        this(context, null);
    }

    public MultiLineText(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiLineText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // 画笔
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.GRAY);
        paint.setTextSize(sp2px(50));
        paint.setStyle(Paint.Style.STROKE);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 将坐标原点移到控件中心
        canvas.translate(getWidth() / 2, getHeight() / 2);
        // x轴
        canvas.drawLine(-getWidth() / 2, 0, getWidth() / 2, 0, paint);
        // y轴
        canvas.drawLine(0, -getHeight() / 2, 0, getHeight() / 2, paint);

        // 绘制坐标
        //               drawCoordinate(canvas);
        drawCenterMultiText3(canvas);
    }

    /**
     * 绘制坐标
     *
     * @param canvas 画布
     */
    private void drawCoordinate(Canvas canvas) {
        // 绘制文本
        canvas.drawText("YangLe", 0, 0, paint);

        // top-红
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float top = fontMetrics.top;
        paint.setColor(Color.RED);
        canvas.drawLine(-getWidth() / 2, top, getWidth() / 2, top, paint);

        // ascent-黄
        float ascent = fontMetrics.ascent;
        paint.setColor(Color.parseColor("#ffc90e"));
        canvas.drawLine(-getWidth() / 2, ascent, getWidth() / 2, ascent, paint);

        // descent-绿
        float descent = fontMetrics.descent;
        paint.setColor(Color.GREEN);
        canvas.drawLine(-getWidth() / 2, descent, getWidth() / 2, descent, paint);

        // bottom-蓝
        float bottom = fontMetrics.bottom;
        paint.setColor(Color.BLUE);
        canvas.drawLine(-getWidth() / 2, bottom, getWidth() / 2, bottom, paint);
    }

    private void drawCenterMultiText(Canvas canvas) {
        String text = "ABC";

        // 画笔
        TextPaint textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.GRAY);
        textPaint.setTextSize(sp2px(25));

        //StaticLayout(CharSequence source, TextPaint paint,
        //                        int width,
        //                        Alignment align, float spacingmult, float spacingadd,
        //                        boolean includepad)
        // 设置宽度超过50dp时换行
        StaticLayout staticLayout = new StaticLayout(text, textPaint, dip2px(20),
                Layout.Alignment.ALIGN_CENTER, 1, 0, false);
        canvas.save();
        // StaticLayout默认从（0，0）点开始绘制
        // 如果需要调整位置，只能在绘制之前移动Canvas的起始坐标
        canvas.translate(-staticLayout.getWidth() / 2, -staticLayout.getHeight() / 2);
        staticLayout.draw(canvas);
        canvas.restore();
    }

    /**
     * 绘制多行居中文本（方式2）
     *
     * @param canvas 画布
     */
    private void drawCenterMultiText2(Canvas canvas) {
        String[] text = {"A", "B", "C"};
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float top = Math.abs(fontMetrics.top);
        float bottom = Math.abs(fontMetrics.bottom);
        float descent = Math.abs(fontMetrics.descent);
        float ascent = Math.abs(fontMetrics.ascent);
        int textLines = text.length;
        float textHeight = top + bottom;
        float textTotalHeight = textHeight * textLines;

        float basePosition = (textLines - 1) / 2f;

        for (int i = 0; i < textLines; i++) {
            float textWidth = paint.measureText(text[i]);
            float baseLine;

            if (i < basePosition) {
                baseLine = -(textTotalHeight / 2 - textHeight * i - top);
            } else if (i > basePosition) {
                baseLine = textTotalHeight / 2 - textHeight * (textLines - i - 1) - bottom;
            } else {
                baseLine = (ascent - descent) / 2;
            }
            canvas.drawText(text[i], -textWidth / 2, baseLine, paint);
        }
    }

    private void drawCenterMultiText3(Canvas canvas) {
        String[] text = {"A", "B", "C"};
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float textLines = text.length;

        float textHeight = fontMetrics.bottom - fontMetrics.top;

        float centerBaseLine = Math.abs(fontMetrics.ascent + fontMetrics.descent) / 2;

        for (int i = 0; i < textLines; i++) {
            float textWidth = paint.measureText(text[i]);
            float baseY = centerBaseLine + (i - (textLines - 1) / 2.0f) * textHeight;
            canvas.drawText(text[i], -textWidth / 2, baseY, paint);
        }

    }

    private int dip2px(float dipValue) {
        //applyDimension(int unit, float value,DisplayMetrics metrics)
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, getContext().getResources().getDisplayMetrics());
    }

    private int sp2px(float spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, getContext().getResources().getDisplayMetrics())
                ;
    }
}
