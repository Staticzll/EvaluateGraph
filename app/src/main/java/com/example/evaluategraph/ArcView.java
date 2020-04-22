package com.example.evaluategraph;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

public class ArcView extends View {

    private Paint bgPaint;
    private Paint scorePaint;
    private Paint evaluatePaint;
    private Paint titlePaint;
    private Paint bottomBgPaint;
    private float mScore;
    private String mTitle;
    private String mEvaluate;
    private LinearGradient linearGradient;
    private float centerX;
    private int paddingLeft;
    private int paddingRight;
    private float offset;
    private float lineSpacing;

    public ArcView(Context context) {
        this(context, null);
    }

    public ArcView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);

    }


    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ArcView);
        mTitle = ta.getString(R.styleable.ArcView_title);
        mEvaluate = ta.getString(R.styleable.ArcView_evaluate);
        mScore = ta.getFloat(R.styleable.ArcView_score, 0);
        float titleTextSize = ta.getDimensionPixelSize(R.styleable.ArcView_titleTextSize, sp2px(24));
        float scoreTextSize = ta.getDimensionPixelSize(R.styleable.ArcView_scoreTextSize, sp2px(24));
        float evaluateTextSize = ta.getDimensionPixelSize(R.styleable.ArcView_evaluateTextSize, sp2px(60));
        float strokeWidth = ta.getFloat(R.styleable.ArcView_strokeWidth, dip2px(30));
        lineSpacing = ta.getDimension(R.styleable.ArcView_android_lineSpacingExtra, dip2px(30));
        Log.d("tag", "mTitle" + mTitle + " mEvaluate:" + mEvaluate + " mScore:" + mScore + " titleTextSize:" + titleTextSize);
        ta.recycle();

        bgPaint = new Paint();
        bgPaint.setStrokeWidth(strokeWidth);
        bgPaint.setDither(true);
        bgPaint.setStyle(Paint.Style.STROKE);
        bgPaint.setAntiAlias(true);
        bgPaint.setStrokeCap(Paint.Cap.ROUND);


        bottomBgPaint = new Paint();
        bottomBgPaint.setStrokeWidth(strokeWidth);
        bottomBgPaint.setColor(getResources().getColor(R.color.colorCircleBottom));
        bottomBgPaint.setDither(true);
        bottomBgPaint.setStyle(Paint.Style.STROKE);
        bottomBgPaint.setAntiAlias(true);
        bottomBgPaint.setStrokeCap(Paint.Cap.ROUND);

        scorePaint = new Paint();
        scorePaint.setTextSize(scoreTextSize);
        scorePaint.setAntiAlias(true);
        scorePaint.setTypeface(Typeface.DEFAULT_BOLD);


        evaluatePaint = new Paint();
        evaluatePaint.setTextSize(evaluateTextSize);
        evaluatePaint.setAntiAlias(true);
        evaluatePaint.setColor(getResources().getColor(R.color.colorCircleText));

        titlePaint = new Paint();
        titlePaint.setTextSize(titleTextSize);
        titlePaint.setAntiAlias(true);
        titlePaint.setColor(getResources().getColor(R.color.colorCircleText));
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawLeftArc(canvas);
    }

    //    private void drawCenterArc(Canvas canvas) {
    //        int width = getWidth();
    //        int height = getHeight();
    //        float centerX = width / 2f;
    //        float centerY = height / 2f;
    //
    //        canvas.translate(centerX, centerY);
    //
    //        Log.d("TAG", "onDraw: centerX: " + centerX + " ,centerY:" + centerY);
    //
    //        float center = Math.min(centerY, centerX);
    //        Log.d("Tag", "center/2:" + center / 2);
    //
    //        // x轴
    //        canvas.drawLine(-centerX, 0, centerX, 0, bgPaint);
    //        // y轴
    //        canvas.drawLine(0, -centerY, 0, centerY, bgPaint);
    //        String text = "36.33";
    //        Paint.FontMetrics fontMetrics = scorePaint.getFontMetrics();
    //        float baseLineY = Math.abs(fontMetrics.descent + fontMetrics.ascent) / 2;
    //        float textWidth = scorePaint.measureText(text);
    //        float textHeight = fontMetrics.bottom - fontMetrics.top;
    //
    //        Log.d("tag", "" + center + " ,baseLine:" + baseLineY + " a:" + center / 2.0f + " textHeight:" + textHeight);
    //        canvas.drawText(text, -textWidth / 2, baseLineY, scorePaint);
    //
    //        canvas.translate(0, (center / 2 - textHeight) / 2);
    //        RectF rectF = new RectF(-center / 2, -center / 2, center / 2, center / 2);
    //        canvas.drawArc(rectF, 180, 180, false, bgPaint);
    //        bgPaint.setColor(Color.RED);
    //        canvas.drawArc(rectF, 180, 150, false, bgPaint);
    //    }


    public void setScore(float score) {
        mScore = score;
        long duration = (long) (3000 * mScore / 100);
        final float floatEndAngle = 180 * mScore / 100;
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(duration);
        valueAnimator.setFloatValues(0, floatEndAngle);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                offset = (float) animation.getAnimatedValue();
                Log.d("Tag", "offset:" + offset);
                int[] colors = {getResources().getColor(R.color.colorCircleStart), getResources().getColor(R.color.colorCircleCenter)};
                linearGradient = new LinearGradient(-centerX + dip2px(paddingLeft), 0, centerX - dip2px(paddingRight), 0, colors, null, Shader.TileMode.CLAMP);
                bgPaint.setShader(linearGradient);
                invalidate();
            }
        });
        valueAnimator.start();
    }

    public void setEvaluate(String evaluate) {
        mEvaluate = evaluate;
    }


    private void drawLeftArc(final Canvas canvas) {
        final float startAngle = 180;
        float defaultEndAngle = 180;
        int width = getWidth();
        centerX = width / 2f;
        canvas.translate(centerX, 0);
        int paddingBottom = getPaddingBottom();
        int paddingTop = getPaddingTop();
        paddingLeft = getPaddingLeft();
        paddingRight = getPaddingRight();
        final RectF rectF = new RectF(-centerX + dip2px(paddingLeft), dip2px(paddingTop), centerX - dip2px(paddingRight), centerX * 2 - dip2px(paddingBottom));
        canvas.drawArc(rectF, startAngle, defaultEndAngle, false, bottomBgPaint);
        canvas.drawArc(rectF, startAngle, offset, false, bgPaint);
        scorePaint.setShader(linearGradient);
        String score = String.valueOf(mScore);
        String title = TextUtils.isEmpty(mTitle) ? "字符串不能为空" : mTitle;
        String evaluate = TextUtils.isEmpty(mEvaluate) ? "字符串不能为空" : mEvaluate;

        Paint.FontMetrics fontMetrics = scorePaint.getFontMetrics();
        float textHeight = fontMetrics.bottom - fontMetrics.top;
        float y = (centerX + textHeight + dip2px(30)) / 2.0f + Math.abs(fontMetrics.descent + fontMetrics.ascent) / 2;
        float scoreWidth = scorePaint.measureText(score);
        canvas.drawText(score, -scoreWidth / 2, y, scorePaint);

        Paint.FontMetrics tfm = titlePaint.getFontMetrics();
        float tHeight = tfm.bottom - tfm.top;
        float y2 = (centerX + textHeight - tHeight - dip2px(30)) / 2.0f - lineSpacing + Math.abs(fontMetrics.descent + fontMetrics.ascent) / 2;
        float titleWidth = titlePaint.measureText(title);
        canvas.drawText(title, -titleWidth / 2, y2, titlePaint);

        Paint.FontMetrics efm = titlePaint.getFontMetrics();
        float eHeight = efm.bottom - efm.top;
        float y3 = (centerX + textHeight + eHeight + dip2px(30)) / 2.0f + lineSpacing + Math.abs(fontMetrics.descent + fontMetrics.ascent) / 2;
        float evaluateWidth = evaluatePaint.measureText(evaluate);
        canvas.drawText(evaluate, -evaluateWidth / 2, y3, evaluatePaint);

    }


    private int dip2px(float dipValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, getContext().getResources().getDisplayMetrics());
    }

    private int sp2px(float spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, getContext().getResources().getDisplayMetrics());
    }

}
