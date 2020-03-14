package com.alirnp.tempview;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class TempView extends View {

    private static final String DEFAULT_TEXT_TIME = "03:00";
    private static final String DEFAULT_TEXT_STATUS = "temperature";
    private final static int DEFAULT_BACKGROUND_PROGRESS_COLOR = Color.parseColor("#F5F5F5");
    private final static float DEFAULT_BACKGROUND_PROGRESS_RADIUS = dpToPx(120);
    private final static float DEFAULT_BACKGROUND_PROGRESS_STROKE_WIDTH = dpToPx(20);
    private final static float DEFAULT_CIRCLE_STROKE_WIDTH = dpToPx(3);
    private final static int DEFAULT_PROGRESS_COLOR = Color.parseColor("#1a8dff");
    private final static int DEFAULT_START_TIME_STROKE_COLOR = Color.parseColor("#00E676");
    private final static int DEFAULT_CLOCK_COLOR = Color.parseColor("#CFD8DC");
    private final static int DEFAULT_TEXT_TIME_COLOR = Color.parseColor("#2196F3");
    private static float DEFAULT_SPACE_TEXT = dpToPx(45);
    private final static float DEFAULT_MIN_VALUE = -10;
    private final static float DEFAULT_MAX_VALUE = 14;


    private final static float DEFAULT_START_DEGREE = 315;
    private final static float DEFAULT_END_DEGREE = 270;

    /*
    private final static float DEFAULT_START_DEGREE = -90 ;
    private final static float DEFAULT_END_DEGREE = 270 ;
*/
    private Context context;
    private float mDegreeValue;
    private float mRadiusBackgroundProgress;
    private float mStrokeWithBackgroundProgress;
    private float mStrokeWithCircle;
    private int mColorProgress;
    private int mColorBackgroundProgress;
    private int mColorValue;
    private int mColorTextTime;
    private float mFloatValue;
    private String mStringTextCenter;
    private String mStringTextStatus;
    private Paint mPaintBackgroundProgress;
    private Paint mPaintValue;
    private Paint mPaintTimeProgress;
    private Paint mPaintHandClock;
    private Paint mPaintHandClockColored;
    private Paint mPaintCenterText;
    private Paint mPaintTopText;
    private RectF mRectBackground;
    private RectF mRectProgress;
    private RectF mRectClock;
    private float mFloatLengthOfClockLines;
    private float mFloatBeginOfClockLines;
    private int mWidthBackgroundProgress;
    private int mHeightBackgroundProgress;
    private CircleArea mCircleArea = new CircleArea();

    private OnSeekChangeListener onSeekCirclesListener;

    private boolean accessMoving;
    private boolean isIndicator;
    private static final String TAG = "TempViewLog";
    private float mIntegerMinValue;
    private float mIntegerMaxValue;

    public TempView(Context context) {
        super(context);
        this.context = context;
    }

    public TempView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        init(attrs);
    }

    public TempView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);

    }

    public void setOnSeekCirclesListener(OnSeekChangeListener onSeekCirclesListener) {
        this.onSeekCirclesListener = onSeekCirclesListener;
    }

    public void setCurrentValue(float value) {

        value = validateValue(value);
        value = rotateValue(value);

        this.mFloatValue = value;

        mDegreeValue = (value - mIntegerMinValue) * getDegreePerHand();

        invalidate();
    }

    public void setMinValue(float value) {
        mIntegerMinValue = value;
        invalidate();
    }

    public void setMaxValue(float value) {
        mIntegerMaxValue = value;
        invalidate();
    }

    private static void setTextSizeForWidth(Paint paint, float desiredWidth, String text) {

        final float testTextSize = dpToPx(48f);

        paint.setTextSize(testTextSize);
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);

        DEFAULT_SPACE_TEXT = (testTextSize * desiredWidth / bounds.width()) /1.2f;

        paint.setTextSize(DEFAULT_SPACE_TEXT);
    }

    private void setTextSizeForWidthSingleText(Paint paint, float desiredWidth, String text) {

        final float testTextSize = dpToPx(48f);

        paint.setTextSize(testTextSize);
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);

        DEFAULT_SPACE_TEXT = testTextSize * desiredWidth / bounds.width();

        paint.setTextSize(DEFAULT_SPACE_TEXT);
    }

    private static int dpToPx(float dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public void setIsIndicator(boolean isIndicator) {
        this.isIndicator = isIndicator;
    }

    public void setTemp(float value) {


        if (value > 0) {

            if (Math.round(value) > 9) {
                mStringTextCenter = String.format("%s C°", Math.round(value));
            } else {
                mStringTextCenter = String.format("0%s C°", Math.round(value));
            }

        } else {

            if (Math.round(value) > -9) {
                mStringTextCenter = String.format("%s C°", String.valueOf(Math.round(value)).replace("-", "-0"));
            } else {
                mStringTextCenter = String.format("%s C°", Math.round(value));
            }
        }

        setCurrentValue(value);

        invalidate();

    }

    public void setTextStatus(String status) {
        this.mStringTextStatus = status;
    }

    private void init(AttributeSet attrs) {

        mStringTextCenter = DEFAULT_TEXT_TIME;
        mStringTextStatus = DEFAULT_TEXT_STATUS;

        if (attrs != null) {

            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TempView, 0, 0);

            try {

                isIndicator = a.getBoolean(R.styleable.TempView_tv_is_indicator, true);

                mColorBackgroundProgress = a.getColor(R.styleable.TempView_tv_color_background_progress, DEFAULT_BACKGROUND_PROGRESS_COLOR);
                mColorValue = a.getColor(R.styleable.TempView_tv_color_value, DEFAULT_START_TIME_STROKE_COLOR);
                mColorProgress = a.getColor(R.styleable.TempView_tv_color_progress, DEFAULT_PROGRESS_COLOR);
                mColorTextTime = a.getColor(R.styleable.TempView_tv_color_text_time, DEFAULT_TEXT_TIME_COLOR);

                mStrokeWithCircle = a.getDimension(R.styleable.TempView_tv_stroke_width_circle, DEFAULT_CIRCLE_STROKE_WIDTH);
                mStrokeWithBackgroundProgress = a.getDimension(R.styleable.TempView_tv_stroke_width_background_progress, DEFAULT_BACKGROUND_PROGRESS_STROKE_WIDTH);

                mStringTextCenter = a.getString(R.styleable.TempView_tv_text_center);
                if (mStringTextCenter == null)
                    mStringTextCenter = DEFAULT_TEXT_TIME;


                mStringTextStatus = a.getString(R.styleable.TempView_tv_text_status);
                if (mStringTextStatus == null)
                    mStringTextStatus = DEFAULT_TEXT_STATUS;


                mIntegerMinValue = a.getFloat(R.styleable.TempView_tv_min_value, DEFAULT_MIN_VALUE);
                mIntegerMaxValue = a.getFloat(R.styleable.TempView_tv_max_value, DEFAULT_MAX_VALUE);

                setCurrentValue(a.getFloat(R.styleable.TempView_tv_current_value, 0));

                setTemp(rollbackValue(mFloatValue));


            } finally {

                a.recycle();
            }

            mPaintBackgroundProgress = new Paint();
            mPaintBackgroundProgress.setAntiAlias(true);
            mPaintBackgroundProgress.setColor(mColorBackgroundProgress);
            mPaintBackgroundProgress.setStrokeWidth(mStrokeWithBackgroundProgress);
            mPaintBackgroundProgress.setStyle(Paint.Style.STROKE);
            mPaintBackgroundProgress.setStrokeCap(Paint.Cap.ROUND);


            mPaintValue = new Paint();
            mPaintValue.setAntiAlias(true);
            mPaintValue.setStrokeWidth(mStrokeWithCircle);
            mPaintValue.setColor(mColorValue);
            mPaintValue.setStyle(Paint.Style.FILL_AND_STROKE);

            mPaintTimeProgress = new Paint();
            mPaintTimeProgress.setAntiAlias(true);
            mPaintTimeProgress.setStrokeWidth(mPaintBackgroundProgress.getStrokeWidth());
            mPaintTimeProgress.setColor(mColorProgress);
            mPaintTimeProgress.setStrokeCap(Paint.Cap.ROUND);
            mPaintTimeProgress.setStyle(Paint.Style.STROKE);


            mPaintHandClock = new Paint();
            mPaintHandClock.setAntiAlias(true);
            mPaintHandClock.setStrokeWidth(mPaintBackgroundProgress.getStrokeWidth() / 3.2f);
            mPaintHandClock.setColor(DEFAULT_CLOCK_COLOR);
            mPaintHandClock.setStyle(Paint.Style.STROKE);
            mPaintHandClock.setStrokeCap(Paint.Cap.ROUND);

            mPaintHandClockColored = new Paint();
            mPaintHandClockColored.setAntiAlias(true);
            mPaintHandClockColored.setStrokeWidth(mPaintBackgroundProgress.getStrokeWidth() / 3.2f);
            mPaintHandClockColored.setColor(DEFAULT_PROGRESS_COLOR);
            mPaintHandClockColored.setStyle(Paint.Style.STROKE);
            mPaintHandClockColored.setStrokeCap(Paint.Cap.ROUND);

            mPaintCenterText = new Paint();
            mPaintCenterText.setAntiAlias(true);
            mPaintCenterText.setColor(mColorTextTime);
            mPaintCenterText.setTextAlign(Paint.Align.CENTER);
            mPaintCenterText.setStyle(Paint.Style.FILL_AND_STROKE);

            mPaintTopText = new Paint();
            mPaintTopText.setAntiAlias(true);
            mPaintTopText.setColor(mColorTextTime);
            mPaintTopText.setTextAlign(Paint.Align.CENTER);
            mPaintTopText.setStyle(Paint.Style.FILL_AND_STROKE);
            mPaintTopText.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));

            mRectProgress = new RectF();
            mRectBackground = new RectF();
            mRectClock = new RectF();

        }

    }

    public static void fillCircleStrokeBorder(
            Canvas c, float cx, float cy, float radius,
            int circleColor, float borderWidth, int borderColor, Paint p) {

        int saveColor = p.getColor();
        p.setColor(circleColor);
        Paint.Style saveStyle = p.getStyle();
        p.setStyle(Paint.Style.FILL);
        c.drawCircle(cx, cy, radius, p);
        if (borderWidth > 0) {
            p.setColor(borderColor);
            p.setStyle(Paint.Style.STROKE);
            float saveStrokeWidth = p.getStrokeWidth();
            p.setStrokeWidth(borderWidth);
            c.drawCircle(cx, cy, radius - (borderWidth / 2), p);
            p.setStrokeWidth(saveStrokeWidth);
        }
        p.setColor(saveColor);
        p.setStyle(saveStyle);
    }

    private float getDegreePerHand() {
        return (360 - 90) / (float) getHandCount();
    }

    private float getHandCount() {
        float left = (mIntegerMaxValue - mIntegerMinValue);
        return left % 2 == 0 ? left : left + 1;
    }

    private float getLeftValue() {
        return (mIntegerMaxValue - mIntegerMinValue);
    }

    private float getSweepProgressArc() {

        float sweep = (DEFAULT_START_DEGREE < mDegreeValue) ? mDegreeValue - DEFAULT_START_DEGREE : 360 - (DEFAULT_START_DEGREE - mDegreeValue);

        if (mDegreeValue == DEFAULT_START_DEGREE)
            sweep = 1;


        return sweep;
    }

    private float getDrawXOnBackgroundProgress(float degree, float backgroundRadius, float backgroundWidth) {
        float drawX = (float) Math.cos(Math.toRadians(degree));
        drawX *= backgroundRadius;
        drawX += backgroundWidth / 2;
        return drawX;
    }

    private float getDrawYOnBackgroundProgress(float degree, float backgroundRadius, float backgroundHeight) {
        float drawY = (float) Math.sin(Math.toRadians(degree));
        drawY *= backgroundRadius;
        drawY += backgroundHeight / 2;
        return drawY;
    }

    private float rotateValue(float value) {
        float _25 = getLeftValue() / 4;
        float _12_5 = getLeftValue() / 6;

        if (value <= _25)
            value = value - _12_5;

        else
            value = value - _12_5;

        return value;
    }

    private float rollbackValue(float value) {
        float _25 = getLeftValue() / 4;
        float _12_5 = getLeftValue() / 6;

        if (value <= _25)
            value = value + _12_5;

        else
            value = value + _12_5;

        return value;
    }

    private float get25Percentage(float value) {
        return value / 4;
    }

    private float get75Percentage(float value) {
        return get25Percentage(value) * 3;
    }

    private float validateValue(float value) {
        if (value < mIntegerMinValue)
            value = mIntegerMinValue;

        if (value > mIntegerMaxValue)
            value = mIntegerMaxValue;

        return value;
    }

    private float getValueFromAngel(double angel) {
        return (float) ((angel + 45) / getDegreePerHand()) + mIntegerMinValue;
    }

    private float rollbackValueFromAngel(double angel) {
        return (float) ((angel - 90) / getDegreePerHand()) + mIntegerMinValue;
    }

    private int getDesireHeight() {
        return (int) ((mRadiusBackgroundProgress * 2) + mPaintBackgroundProgress.getStrokeWidth() + getVerticalPadding());
    }

    private int getVerticalPadding() {
        return getPaddingTop() + getPaddingBottom();
    }

    private int getDesireWidth() {
        return (int) ((mRadiusBackgroundProgress * 2) + mPaintBackgroundProgress.getStrokeWidth() + getHorizontalPadding());
    }

    private int getHorizontalPadding() {
        return getPaddingLeft() + getPaddingRight();
    }

    private double getAngleFromPoint(double firstPointX, double firstPointY, double secondPointX, double secondPointY) {

        if ((secondPointX > firstPointX)) {

            return (Math.atan2((secondPointX - firstPointX), (firstPointY - secondPointY)) * 180 / Math.PI);

        } else if ((secondPointX < firstPointX)) {

            return 360 - (Math.atan2((firstPointX - secondPointX), (firstPointY - secondPointY)) * 180 / Math.PI);

        }

        return Math.atan2(0, 0);

    }

    private CircleArea getCircleArea(float centerX, float centerY, float radius) {

        radius = radius + (radius / 2);
        mCircleArea.setXStart(centerX - radius);
        mCircleArea.setXEnd(centerX + radius);
        mCircleArea.setYStart(centerY - radius);
        mCircleArea.setYEnd(centerY + radius);
        return mCircleArea;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mFloatBeginOfClockLines = mRadiusBackgroundProgress - (mStrokeWithBackgroundProgress * 1.6f);
        mFloatLengthOfClockLines = mRadiusBackgroundProgress - (mStrokeWithBackgroundProgress) - (mStrokeWithCircle * 8);

        mRectBackground.set(
                (float) mWidthBackgroundProgress / 2 - mRadiusBackgroundProgress + (mPaintBackgroundProgress.getStrokeWidth() / 1.2f),
                (float) mHeightBackgroundProgress / 2 - mRadiusBackgroundProgress + (mPaintBackgroundProgress.getStrokeWidth() / 1.2f),
                (float) mWidthBackgroundProgress / 2 + mRadiusBackgroundProgress - (mPaintBackgroundProgress.getStrokeWidth() / 1.2f),
                (float) mHeightBackgroundProgress / 2 + mRadiusBackgroundProgress - (mPaintBackgroundProgress.getStrokeWidth() / 1.2f));

        mRectProgress.set(
                (float) mWidthBackgroundProgress / 2 - mRadiusBackgroundProgress + (mPaintBackgroundProgress.getStrokeWidth() / 1.2f),
                (float) mHeightBackgroundProgress / 2 - mRadiusBackgroundProgress + (mPaintBackgroundProgress.getStrokeWidth() / 1.2f),
                (float) mWidthBackgroundProgress / 2 + mRadiusBackgroundProgress - (mPaintBackgroundProgress.getStrokeWidth() / 1.2f),
                (float) mHeightBackgroundProgress / 2 + mRadiusBackgroundProgress - (mPaintBackgroundProgress.getStrokeWidth() / 1.2f));


        mRectClock.set(
                ((float) (mWidthBackgroundProgress / 2) - mRadiusBackgroundProgress),
                ((float) (mHeightBackgroundProgress / 2) - mRadiusBackgroundProgress),
                ((float) (mWidthBackgroundProgress / 2) + mRadiusBackgroundProgress),
                ((float) (mHeightBackgroundProgress / 2) + mRadiusBackgroundProgress));


        if (mStringTextStatus.equals("")) {
            setTextSizeForWidthSingleText(mPaintCenterText, mRadiusBackgroundProgress, mStringTextCenter);
        } else {

            setTextSizeForWidth(mPaintTopText, mRadiusBackgroundProgress  / 1.2f, mStringTextStatus);
            setTextSizeForWidth(mPaintCenterText, mRadiusBackgroundProgress / 1.4f, mStringTextCenter);
        }


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMeasureMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthMeasureSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMeasureMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightMeasureSize = MeasureSpec.getSize(heightMeasureSpec);


        switch (widthMeasureMode) {
            case MeasureSpec.UNSPECIFIED:
                mWidthBackgroundProgress = getDesireWidth();
                break;

            case MeasureSpec.EXACTLY:
                mWidthBackgroundProgress = widthMeasureSize + getHorizontalPadding();
                break;

            case MeasureSpec.AT_MOST:
                mWidthBackgroundProgress = Math.min(widthMeasureSize, getDesireWidth() + getHorizontalPadding());

                break;
        }


        switch (heightMeasureMode) {
            case MeasureSpec.UNSPECIFIED:
                mHeightBackgroundProgress = getDesireHeight();
                break;

            case MeasureSpec.EXACTLY:
                mHeightBackgroundProgress = heightMeasureSize + getVerticalPadding();


                break;

            case MeasureSpec.AT_MOST:
                mHeightBackgroundProgress = Math.min(heightMeasureSize, getDesireHeight() + getHorizontalPadding());

                break;
        }

        if (widthMeasureMode == MeasureSpec.EXACTLY || heightMeasureMode == MeasureSpec.EXACTLY) {
            int size = Math.min(widthMeasureSize - getHorizontalPadding(), heightMeasureSize - getVerticalPadding());
            mRadiusBackgroundProgress = (size - mPaintBackgroundProgress.getStrokeWidth()) / 2;
        } else {
            mRadiusBackgroundProgress = DEFAULT_BACKGROUND_PROGRESS_RADIUS;
        }

        int length = Math.min(mWidthBackgroundProgress, mHeightBackgroundProgress) + (int) mStrokeWithCircle;

        setMeasuredDimension(length, length);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float mFloatCenterXCircle = getDrawXOnBackgroundProgress(mDegreeValue, mRadiusBackgroundProgress - (mPaintBackgroundProgress.getStrokeWidth() / 1.2f), mWidthBackgroundProgress);
        float mFloatCenterYCircle = getDrawYOnBackgroundProgress(mDegreeValue, mRadiusBackgroundProgress - (mPaintBackgroundProgress.getStrokeWidth() / 1.2f), mHeightBackgroundProgress);
        //ADD CIRCLE AREA FOR DETECT TOUCH
        mCircleArea = getCircleArea(mFloatCenterXCircle, mFloatCenterYCircle, mPaintBackgroundProgress.getStrokeWidth());


        //BACKGROUNDS
        canvas.drawArc(mRectBackground, DEFAULT_START_DEGREE, DEFAULT_END_DEGREE, false, mPaintBackgroundProgress);


        float sweep = getSweepProgressArc();
        //PROGRESS TIME
        canvas.drawArc(mRectProgress, DEFAULT_START_DEGREE, sweep, false, mPaintTimeProgress);


        //TEXT
        canvas.drawText(mStringTextCenter, mWidthBackgroundProgress / 2, (float) (mHeightBackgroundProgress / 2) + DEFAULT_SPACE_TEXT, mPaintCenterText);
        canvas.drawText(mStringTextStatus, mWidthBackgroundProgress / 2, (float) (mHeightBackgroundProgress / 2) - DEFAULT_SPACE_TEXT, mPaintTopText);


        //CIRCLE VALUE
        if (!isIndicator)
            fillCircleStrokeBorder(canvas, mFloatCenterXCircle, mFloatCenterYCircle, mPaintBackgroundProgress.getStrokeWidth() / 2f, Color.WHITE, mStrokeWithCircle, mColorValue, mPaintValue);


        {
            //LINES
            float angel = 270;
            float x1, y1, x2, y2;

            float degreePerHand = getDegreePerHand();

            for (int i = 0; i <= getLeftValue(); i++) {

                angel += degreePerHand;

                if (i % 2 != 0) {
                    x1 = (float) (Math.cos(Math.toRadians(angel))) * (mFloatBeginOfClockLines - 0) + (float) (mWidthBackgroundProgress / 2);
                    y1 = (float) (Math.sin(Math.toRadians(angel))) * (mFloatBeginOfClockLines - 0) + (float) (mHeightBackgroundProgress / 2);
                    x2 = (float) (Math.cos(Math.toRadians(angel))) * (mFloatLengthOfClockLines - 10) + (float) (mWidthBackgroundProgress / 2);
                    y2 = (float) (Math.sin(Math.toRadians(angel))) * (mFloatLengthOfClockLines - 10) + (float) (mHeightBackgroundProgress / 2);

                } else {
                    x1 = (float) (Math.cos(Math.toRadians(angel))) * (mFloatBeginOfClockLines - 10) + (float) (mWidthBackgroundProgress / 2);
                    y1 = (float) (Math.sin(Math.toRadians(angel))) * (mFloatBeginOfClockLines - 10) + (float) (mHeightBackgroundProgress / 2);
                    x2 = (float) (Math.cos(Math.toRadians(angel))) * (mFloatLengthOfClockLines - 0) + (float) (mWidthBackgroundProgress / 2);
                    y2 = (float) (Math.sin(Math.toRadians(angel))) * (mFloatLengthOfClockLines - 0) + (float) (mHeightBackgroundProgress / 2);
                }

                float current = rollbackValue(mFloatValue);

                if (i < current)
                    canvas.drawLine(x1, y1, x2, y2, mPaintHandClockColored);
                else
                    canvas.drawLine(x1, y1, x2, y2, mPaintHandClock);

            }

        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (isIndicator)
            return false;

        int x = (int) event.getX();
        int y = (int) event.getY();

        double angel = 0;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                boolean found = (x >= mCircleArea.getXStart()
                        && x <= mCircleArea.getXEnd()
                        && y >= mCircleArea.getYStart()
                        && y <= mCircleArea.getYEnd());

                if (found) {
                    accessMoving = true;
                    break;
                } else {
                    accessMoving = false;
                }


                break;
            case MotionEvent.ACTION_MOVE:

                if (accessMoving) {

                    angel = getAngleFromPoint((double) mWidthBackgroundProgress / 2, (double) mHeightBackgroundProgress / 2, (double) x, (double) y) - 90;

                    if (angel < DEFAULT_END_DEGREE - 45 && angel + 45 > 0) {

                        mDegreeValue = (float) angel;
                        int val = Math.round(getValueFromAngel(mDegreeValue));
                        setTemp(val);

                        if (onSeekCirclesListener != null)
                            onSeekCirclesListener.onSeekChange(val);


                        invalidate();
                    }

                }
                break;
            case MotionEvent.ACTION_UP:

                if (onSeekCirclesListener != null)
                    onSeekCirclesListener.onSeekComplete(Math.round(getValueFromAngel(mDegreeValue)));


                accessMoving = false;
                break;
        }

        return true;
    }

    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }

    private static class CircleArea {

        private float xStart;
        private float xEnd;

        private float yStart;
        private float yEnd;

        public float getXStart() {
            return xStart;
        }

        public void setXStart(float xStart) {
            this.xStart = xStart;
        }

        public float getXEnd() {
            return xEnd;
        }

        public void setXEnd(float xEnd) {
            this.xEnd = xEnd;
        }

        public float getYStart() {
            return yStart;
        }

        public void setYStart(float yStart) {
            this.yStart = yStart;
        }

        public float getYEnd() {
            return yEnd;
        }

        public void setYEnd(float yEnd) {
            this.yEnd = yEnd;
        }
    }

    public interface OnSeekChangeListener {
        void onSeekChange(int value);

        void onSeekComplete(int value);
    }

}
