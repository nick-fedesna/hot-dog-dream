package com.vokal.hotdogdream.view;

import android.animation.TimeAnimator;
import android.animation.TimeAnimator.TimeListener;
import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class Bouncer extends FrameLayout implements TimeListener {

    private final TimeAnimator mAnimator;
    private float              mMaxSpeed;
    private int                mWidth, mHeight;

    public Bouncer(Context context) {
        this(context, null);
    }

    public Bouncer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Bouncer(Context context, AttributeSet attrs, int flags) {
        super(context, attrs, flags);
        mAnimator = new TimeAnimator();
        mAnimator.setTimeListener(this);
    }

    /**
     * Start the bouncing as soon as we�re on screen.
     */
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        mAnimator.start();
    }

    /**
     * Stop animations when the view hierarchy is torn down.
     */
    @Override
    public void onDetachedFromWindow() {
        mAnimator.cancel();
        super.onDetachedFromWindow();
    }

    /**
     * Whenever a view is added, place it randomly.
     */
    @Override
    public void addView(View v, ViewGroup.LayoutParams lp) {
        super.addView(v, lp);
        setupView(v);
    }

    /**
     * Reposition all children when the container size changes.
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        for (int i = 0; i < getChildCount(); i++) {
            setupView(getChildAt(i));
        }
    }

    /**
     * Bouncing view setup: random placement, random velocity.
     */
    private void setupView(View v) {
        final PointF p = new PointF();
        final float a = (float) (Math.random() * 360);
        p.x = mMaxSpeed * (float) (Math.cos(a));
        p.y = mMaxSpeed * (float) (Math.sin(a));
        v.setTag(p);
        v.setX((float) (Math.random() * (mWidth - v.getWidth())));
        v.setY((float) (Math.random() * (mHeight - v.getHeight())));
    }

//  @Override
//  public void onTimeUpdate(TimeAnimator animation, long totalTime, long deltaTime) {
    /**
     * Every TimeAnimator frame, nudge each bouncing view along.
     */
    @Override
    public void onTimeUpdate(TimeAnimator animation, long elapsed, long dt_ms) {
        final float dt = dt_ms / 1000f; // seconds
        for (int i = 0; i < getChildCount(); i++) {
            final View view = getChildAt(i);
            final PointF v = (PointF) view.getTag();

            // step view for velocity * time
            view.setX(view.getX() + v.x * dt);
            view.setY(view.getY() + v.y * dt);

            // handle reflections
            final float l = view.getX();
            final float t = view.getY();
            final float r = l + view.getWidth();
            final float b = t + view.getHeight();
            boolean flipX = false, flipY = false;
            if (r > mWidth) {
                view.setX(view.getX() - 2 * (r - mWidth));
                flipX = true;
            } else if (l < 0) {
                view.setX(-l);
                flipX = true;
            }
            if (b > mHeight) {
                view.setY(view.getY() - 2 * (b - mHeight));
                flipY = true;
            } else if (t < 0) {
                view.setY(-t);
                flipY = true;
            }
            if (flipX)
                v.x *= -1;
            if (flipY)
                v.y *= -1;
        }
    }

    public void setSpeed(float s) {
        mMaxSpeed = s;
    }

}
