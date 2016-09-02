package com.arvin.gifloader.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by arvin on 2016/9/2.
 */
public class GifView extends ImageView {
    private static final String TAG = "ImageView";
    private static final int DEFAULT_MOVIEW_DURATION = 2000;
    private int mMovieResourceId;
    private Movie mMovie;
    private long mMovieStart;
    private int mCurrentAnimationTime;
    /**
     * Position for drawing animation frames in the center of the view.
     */
    private float mLeft;
    private float mTop;

    private int mMeasureWidth;
    private int mMeasureHeight;

    /**
     * Scaling factor to fit the animation within view bounds.
     */
    private float mScale = 1;

    private float mScaleRate = 1;


    private volatile boolean mPaused;
    private boolean mVisible = true;

    public GifView(Context context) {
        this(context, null);
    }

    public GifView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GifView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setViewAttributes();
    }

    @SuppressLint("NewApi")
    public void setViewAttributes() {

        /**
         * Starting from HONEYCOMB(Api Level:11) have to turn off HW acceleration to draw
         * Movie on Canvas.
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e(TAG, "--ondraw");
        if (mMovie != null) {
            if (!mPaused) {
                updateAnimationTime();
                drawMovieFrame(canvas);
                invalidateView();
            } else {
                drawMovieFrame(canvas);
            }
        }
    }

    /**
     * Draw current GIF frame
     */
    private void drawMovieFrame(Canvas canvas) {

        mMovie.setTime(mCurrentAnimationTime);

        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        canvas.scale(mScale, mScale);
        mMovie.draw(canvas, mLeft / mScale, mTop / mScale);
        canvas.restore();
    }

    /**
     * Calculate current animation time
     */
    private void updateAnimationTime() {
        long now = android.os.SystemClock.uptimeMillis();

        if (mMovieStart == 0) {
            mMovieStart = now;
        }

        int dur = mMovie.duration();

        if (dur == 0) {
            dur = DEFAULT_MOVIEW_DURATION;
        }

        mCurrentAnimationTime = (int) ((now - mMovieStart) % dur);
    }

    /**
     * Invalidates view only if it is visible.
     * <br>
     * {@link #postInvalidateOnAnimation()} is used for Jelly Bean and higher.
     */
    @SuppressLint("NewApi")
    private void invalidateView() {
        if (mVisible) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                postInvalidateOnAnimation();
            } else {
                invalidate();
            }
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onScreenStateChanged(int screenState) {
        super.onScreenStateChanged(screenState);
        mVisible = screenState == SCREEN_STATE_ON;
        invalidateView();
    }

    @SuppressLint("NewApi")
    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        mVisible = visibility == View.VISIBLE;
        invalidateView();
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        mVisible = visibility == View.VISIBLE;
        invalidateView();
    }

    public void setMovieResource(int movieResId) {
        this.mMovieResourceId = movieResId;
        mMovie = Movie.decodeStream(getResources().openRawResource(mMovieResourceId));
        requestLayout();
    }

    public void setMovie(Movie movie) {
        this.mMovie = movie;
        setScale();
        invalidate();
    }

    private void setScale() {
        if (mMovie != null) {
            float scaleW = (float) getWidth() / mMovie.width();
            float scaleH = (float) getHeight() / mMovie.height();
            mScale = Math.min(scaleH, scaleW);
        }
    }

    public void setMovieTime(int time) {
        mCurrentAnimationTime = time;
        invalidate();
    }

    public void setPaused(boolean paused) {
        this.mPaused = paused;

        /**
         * Calculate new movie start time, so that it resumes from the same
         * frame.
         */
        if (!paused) {
            mMovieStart = android.os.SystemClock.uptimeMillis() - mCurrentAnimationTime;
        }

        invalidate();
    }

    public boolean isPaused() {
        return this.mPaused;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            if (mMovie != null) {
                float scaleW = (float) getWidth() / mMovie.width();
                float scaleH = (float) getHeight() / mMovie.height();
                mScale = Math.min(scaleH, scaleW);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mMovie != null) {
            Log.e(TAG, "---moviewidth   sss" + mMovie.width());
            int widthMode = MeasureSpec.getMode(widthMeasureSpec);
            int heightMode = MeasureSpec.getMode(heightMeasureSpec);
            int widthSize = MeasureSpec.getSize(widthMeasureSpec);
            int heightSize = MeasureSpec.getSize(heightMeasureSpec);
            if (widthMode == MeasureSpec.AT_MOST) {
                Log.e(TAG, "---moviewidth before" + mMovie.width());
                int movieWidth = mMovie.width();
                mMeasureWidth = Math.min(movieWidth, widthSize);
                Log.e(TAG, "---moviewidth after" + mMovie.width());
            } else {
                mMeasureWidth = widthSize;
            }
            if (heightMode == MeasureSpec.AT_MOST) {
                Log.e(TAG, "---height " + mMovie.height());
                int movieHieght = mMovie.height();
                mMeasureHeight = Math.min(movieHieght, heightSize);

            } else {
                mMeasureHeight = heightSize;
            }
            Log.e(TAG, "--wi : " + mMeasureWidth + "  h  : " + mMeasureHeight);
            setMeasuredDimension(mMeasureWidth, mMeasureHeight);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
