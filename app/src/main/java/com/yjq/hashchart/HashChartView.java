package com.yjq.hashchart;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

/**
 * 算力曲线图
 * Created By YJQ
 */
public class HashChartView extends View {
    private static final float SMOOTHNESS = 0.7f;

    private static final int TOP_PADDING = 4;
    private static final int BOT_PADDING = 16;
    private static final int LEFT_PADDING = 30;
    private static final int RIGHT_PADDING = 30;

    private int pl, pt, pb, pr;

    private TextPaint textPaint;
    private Paint paint;
    private Paint linePaint;
    private Paint ratePaint;
    private Paint crossPaint;

    private int mWidth;
    private int mHeight;

    private ValueAnimator mAnimator;

    private List<LinePointManager.LinePoint> mDatas = new ArrayList<>();

    private List<PointF> mValuePoints = new ArrayList<>();
    private List<PointF> mRatePoints = new ArrayList<>();

    private int tMax;
    private float wspace;

    private String mDimension = "1h";
    private String mCoinType;

    protected boolean isShowCrossLine = false;

    public HashChartView(Context context) {
        super(context);
        init();
    }

    public HashChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HashChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        textPaint = new TextPaint();
        textPaint.setColor(UIUtils.getColor(R.color.grey_5));
        textPaint.setTextSize(UIUtils.sp2px(10));

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(UIUtils.getColor(R.color.grey_5));
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.STROKE);

        crossPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        crossPaint.setColor(UIUtils.getColor(R.color.colorPrimary));
        crossPaint.setStrokeWidth(2);
        crossPaint.setStyle(Paint.Style.STROKE);
        crossPaint.setPathEffect(new DashPathEffect(new float[]{8, 8, 8, 8}, 1));

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(UIUtils.getColor(R.color.dark_blue));
        linePaint.setStrokeWidth(4);
        linePaint.setStyle(Paint.Style.STROKE);
        CornerPathEffect cornerPathEffect = new CornerPathEffect(10);
        linePaint.setPathEffect(cornerPathEffect);

        ratePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        ratePaint.setColor(UIUtils.getColor(R.color.red));
        ratePaint.setStrokeWidth(4);
        ratePaint.setStyle(Paint.Style.STROKE);
        ratePaint.setPathEffect(cornerPathEffect);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.i("yjq", "onSizeChange执行");
        mWidth = w;
        mHeight = h;

        pl = UIUtils.dip2Px(LEFT_PADDING);
        pt = UIUtils.dip2Px(TOP_PADDING);
        pb = mHeight - UIUtils.dip2Px(BOT_PADDING);
        pr = mWidth - UIUtils.dip2Px(RIGHT_PADDING);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        drawBorder(canvas);

        if (validate()) {
            drawNoData(canvas);
        } else {
            drawLine(canvas);

            drawText(canvas);

            drawDate(canvas);

            drawCrossLine(canvas);
        }
    }

    private void drawBorder(Canvas canvas) {
        canvas.drawRect(pl, pt, pr, pb, paint);

        if (validate()) {
            return;
        }

        canvas.drawLine(pl, pt + (pb - pt) / 2, pr, pt + (pb - pt) / 2, paint);
    }


    private void drawLine(Canvas canvas) {
        if (mDatas.size() == 0) return;
        //画算力线
        Path path = new Path();
        Path gradientPath = new Path();
        PointF firstPoint = mValuePoints.get(0);
        path.moveTo(firstPoint.x, firstPoint.y);
        gradientPath.moveTo(firstPoint.x, firstPoint.y);

        for (int i = 1; i < (int) mAnimator.getAnimatedValue(); i++) {
            PointF rightPoint = mValuePoints.get(i);
            path.lineTo(rightPoint.x, rightPoint.y);
            gradientPath.lineTo(rightPoint.x, rightPoint.y);
            if (i == (int) mAnimator.getAnimatedValue() - 1) {
                gradientPath.lineTo(rightPoint.x, pb);
                gradientPath.lineTo(pl, pb);
            }
        }
        canvas.drawPath(path, linePaint);

        Paint mGradientPaint = new Paint();
        mGradientPaint.setAntiAlias(true);
        Shader shader = new LinearGradient(pl, pt, pl, pb, new int[]{linePaint.getColor(), Color.TRANSPARENT}, null, Shader.TileMode.CLAMP);

        mGradientPaint.setShader(shader);
        CornerPathEffect cornerPathEffect = new CornerPathEffect(10);
        mGradientPaint.setPathEffect(cornerPathEffect);
        canvas.drawPath(gradientPath, mGradientPaint);

        //画拒绝率线
        gradientPath.reset();
        Path ratePath = new Path();
        PointF rate1F = mRatePoints.get(0);
        ratePath.moveTo(rate1F.x, rate1F.y);
        gradientPath.moveTo(rate1F.x, rate1F.y);
        for (int i = 1; i < (int) mAnimator.getAnimatedValue(); i++) {
            PointF ratePoint = mRatePoints.get(i);
            ratePath.lineTo(ratePoint.x, ratePoint.y);
            gradientPath.lineTo(ratePoint.x, ratePoint.y);
            if (i == (int) mAnimator.getAnimatedValue() - 1) {
                gradientPath.lineTo(ratePoint.x, pb);
                gradientPath.lineTo(pl, pb);
            }
        }
        canvas.drawPath(ratePath, ratePaint);

        mGradientPaint.reset();
        mGradientPaint.setColor(ratePaint.getColor());
        mGradientPaint.setAntiAlias(true);
        Shader shader1 = new LinearGradient(pl, pt, pl, pb, new int[]{ratePaint.getColor(), Color.TRANSPARENT}, null, Shader.TileMode.CLAMP);

        mGradientPaint.setShader(shader1);
        mGradientPaint.setPathEffect(cornerPathEffect);
        canvas.drawPath(gradientPath, mGradientPaint);
    }

    private void drawText(Canvas canvas) {
        //画左侧算力
        canvas.drawText(tMax + "", pl - textPaint.measureText(tMax + "") - UIUtils.dip2Px(6), pt + getTextHeight(tMax + ""), textPaint);

        canvas.drawText(tMax / 2 + "", pl - textPaint.measureText(tMax / 2 + "") - UIUtils.dip2Px(6), pt + (pb - pt) / 2, textPaint);

        canvas.drawText("0", pl - textPaint.measureText("0") - UIUtils.dip2Px(6), pb, textPaint);

        //画右侧拒绝率
        canvas.drawText("100", pr + UIUtils.dip2Px(6), pt + getTextHeight("100"), textPaint);

        canvas.drawText("50", pr + UIUtils.dip2Px(6), pt + (pb - pt) / 2, textPaint);

        canvas.drawText("0", pr + UIUtils.dip2Px(6), pb, textPaint);
    }

    private void drawDate(Canvas canvas) {
        int divider = (pr - pl) / 6;
        for (int i = 0; i < mDatas.size(); i += 4) {
            String date = getFormattedDateValue(mDatas.get(i).time);
            canvas.drawText(date, pl + divider * i / 4 - textPaint.measureText(date) / 2, pb + getTextHeight(date) + UIUtils.dip2Px(8), textPaint);
        }
    }

    private void drawCrossLine(Canvas canvas) {
        if (isShowCrossLine && mDatas.size() != 0) {
            PointF pointF = mValuePoints.get(mSelectedIndex);

            //画横线
            Path pathH = new Path();
            pathH.moveTo(pl, pointF.y);
            pathH.lineTo(pr, pointF.y);
            canvas.drawPath(pathH, crossPaint);

            //画竖线
            Path pathV = new Path();
            pathV.moveTo(pointF.x, pt);
            pathV.lineTo(pointF.x, pb);
            canvas.drawPath(pathV, crossPaint);

            //画框框
            Paint paint = new Paint();
            paint.setColor(UIUtils.getColor(R.color.A7));
            paint.setStyle(Paint.Style.FILL);

            TextPaint textPaint = new TextPaint();
            textPaint.setColor(UIUtils.getColor(R.color.white));
            textPaint.setTextSize(UIUtils.sp2px(12));

            String date = FormatterUtils.formatDateNO1(mDatas.get(mSelectedIndex).time);
            String share = "算力" + ":" + FormatterUtils.df2(mDatas.get(mSelectedIndex).pow) + " " + CoinUtils.rateUnit(mCoinType, mDatas.get(mSelectedIndex).unit);
            String rate = "拒绝率" + ":" + mDatas.get(mSelectedIndex).rate + "%";
            float datewidth = textPaint.measureText(date);
            int dateheight = getTextHeight(date);
            float sharewidth = textPaint.measureText(share);
            int shareHeight = getTextHeight(share);
            float ratewidth = textPaint.measureText(rate);
            int rateheight = getTextHeight(rate);

            int rectH = dateheight + shareHeight + rateheight + UIUtils.dip2Px(16) * 2 + UIUtils.dip2Px(16);
            float rectW = UIUtils.dip2Px(140);

            if (pointF.x < (pr - pl) / 2 + pl) {
                canvas.drawRect(pointF.x, pt, pointF.x + rectW, pt + rectH, paint);

                canvas.drawText(date, pointF.x + rectW / 2 - datewidth / 2, pt + UIUtils.dip2Px(16), textPaint);

                canvas.drawText(share, pointF.x + rectW / 2 - sharewidth / 2, pt + rectH / 2, textPaint);

                canvas.drawText(rate, pointF.x + rectW / 2 - sharewidth / 2, pt + rectH - UIUtils.dip2Px(16) + rateheight, textPaint);
            } else {
                canvas.drawRect(pointF.x - rectW, pt, pointF.x, pt + rectH, paint);

                canvas.drawText(date, pointF.x - rectW / 2 - datewidth / 2, pt + UIUtils.dip2Px(16), textPaint);

                canvas.drawText(share, pointF.x - rectW / 2 - sharewidth / 2, pt + rectH / 2, textPaint);

                canvas.drawText(rate, pointF.x - rectW / 2 - sharewidth / 2, pt + rectH - UIUtils.dip2Px(16) + rateheight, textPaint);
            }
        }
    }

    private void drawNoData(Canvas canvas) {
        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(UIUtils.sp2px(12));
        textPaint.setColor(UIUtils.getColor(R.color.colorPrimary));

        String nodata = "暂无数据";
        canvas.drawText(nodata, (pr - pl) / 2 + pl - textPaint.measureText(nodata) / 2, (pb - pt) / 2 + pt, textPaint);
    }


    private int mDownX = 0;
    private int mDownY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (validate()) {
            return false;
        }
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) event.getX();
                mDownY = (int) event.getY();
                isShowCrossLine = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() == 1) {
                    if (!isShowCrossLine) {
                        int deltaX = (int) event.getX() - mDownX;
                        int deltaY = (int) event.getY() - mDownY;
                        //如果是左右滑动
                        if (Math.abs(deltaX) > Math.abs(deltaY)) {
                            isShowCrossLine = true;
                        }
                    }
                    //计算选中位置，拦截手势
                    if (isShowCrossLine && !mAnimator.isRunning()) {
                        calculateSelectIndex(event.getX());
                        invalidate();
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                isShowCrossLine = false;
                invalidate();
                break;
            default:
                break;
        }
        return true;
    }

    public void setData(String dimension, String coinType, List<LinePointManager.LinePoint> data) {
        if (data != null && data.size() > 0) {
            mDatas = data;
            mDimension = dimension;
            mCoinType = coinType;
            calculateValuePoint();
            calculateRatePoint();
            initAnimator();
        }

    }

    private int mSelectedIndex = 0;

    private void calculateSelectIndex(float x) {
        for (int i = 0; i < mValuePoints.size(); i++) {
            if (Math.abs(x - mValuePoints.get(i).x) < wspace / 2) {
                mSelectedIndex = i;
                break;
            }
        }
    }

    private void calculateValuePoint() {
        if (mDatas.size() == 0) return;
        mValuePoints.clear();
        float max = mDatas.get(0).pow;
        for (LinePointManager.LinePoint linePoint : mDatas) {
            if (linePoint.pow > max) {
                max = linePoint.pow;
            }
        }
        settMax((int) max);
//        tMax = ((int) (max / 10) + 1) * 10;
        wspace = (float) (pr - pl) / (mDatas.size() - 1);
        for (int i = 0; i < mDatas.size(); i++) {
            float x = pl + wspace * i;
            float y = pb - (pb - pt) * mDatas.get(i).pow / tMax;
            mValuePoints.add(new PointF(x, y));
        }
    }

    private void calculateRatePoint() {
        if (mDatas.size() == 0) return;
        mRatePoints.clear();
        float rateMax = 100;
        for (int i = 0; i < mDatas.size(); i++) {
            float x = pl + wspace * i;
            float y = pb - (pb - pt) * mDatas.get(i).rate / rateMax;
            mRatePoints.add(new PointF(x, y));
        }
    }

    private void initAnimator() {
        mAnimator = ValueAnimator.ofInt(0, mValuePoints.size());
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.setDuration(500);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                invalidate();
            }
        });
        mAnimator.start();
    }

    private int getTextHeight(String text) {
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect.height();
    }


    private String getFormattedDateValue(long time) {
        try {
            if ("1d".equals(mDimension)) {
                return FormatterUtils.formatDateNO3(time);
            } else {
                return FormatterUtils.formatDateNO2(time);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";

    }

    private void settMax(int maxValue) {
        if (maxValue >= 10000) {
            tMax = 10000;
            return;
        }
        if (maxValue / 1000 % 10 > 0) {//大于1000   1222 9988
            int remainder = (maxValue / 1000 % 10 + 1);
            tMax = remainder * 1000;
        } else if (maxValue / 100 % 10 > 0) {
            int remainder = (maxValue / 100 % 10 + 1);
            tMax = remainder * 100;
        } else if (maxValue / 10 % 10 > 0) {
            int remainder = (maxValue / 10 % 10 + 1);
            tMax = remainder * 10;
        } else if (maxValue / 10 == 0) {
            if (maxValue % 2 != 0) {
                tMax = maxValue + 1;
            } else {
                tMax = maxValue + 2;
            }
        }
    }

    private boolean validate() {
        return mDatas == null || mDatas.size() == 0 || mValuePoints.size() == 0 || mRatePoints.size() == 0;
    }
}
