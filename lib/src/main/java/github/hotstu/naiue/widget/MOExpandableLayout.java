package github.hotstu.naiue.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import github.hotstu.naiue.R;


public class MOExpandableLayout extends FrameLayout {

    private int mHeight;
    private int mAnimHeight;
    private int mLastAnimHeight;
    private boolean mExpanded;
    private boolean mAnimating;
    private ValueAnimator mValueAnimator;

    private OnHeightUpdatedListener mOnHeightUpdatedListener;

    public interface OnHeightUpdatedListener {
        void OnHeightUpdate(MOExpandableLayout v, int height, int changed);
    }

    public void setOnHeightUpdatedListener(OnHeightUpdatedListener onHeightUpdatedListener) {
        mOnHeightUpdatedListener = onHeightUpdatedListener;
    }

    public MOExpandableLayout(Context context) {
        this(context, null);
    }

    public MOExpandableLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MOExpandableLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MOExpandableLayout, defStyleAttr, 0);
        mExpanded = a.getBoolean(R.styleable.MOExpandableLayout_mo_isExpanded, false);
        a.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (isInEditMode()) {
            return;
        }

        mHeight = getMeasuredHeight();

        if (mAnimating) {
            setMeasuredDimension(getMeasuredWidth(), mAnimHeight);
        } else if (!mExpanded) {
            setMeasuredDimension(getMeasuredWidth(), 0);
        }
    }

    public void toggle() {
        setExpanded(!mExpanded);
    }

    public boolean isExpanded() {
        return mExpanded;
    }

    public void setExpanded(boolean expanded) {
        setExpanded(expanded, true);
    }

    public void setExpanded(boolean expanded, boolean anim) {
        if (mExpanded == expanded) {
            return;
        }

        mExpanded = expanded;

        if (mAnimating) {
            mValueAnimator.cancel();
        }

        if (!anim) {
            mAnimating = false;
            requestLayout();
            return;
        }

        final int from, to;
        if (mExpanded) {
            from = 0;
            to = mHeight;
        } else {
            from = mHeight;
            to = 0;
        }

        mLastAnimHeight = from;
        mValueAnimator = ValueAnimator.ofInt(from, to);
        mValueAnimator.setDuration(getContext().getResources().getInteger(android.R.integer.config_shortAnimTime));
        mValueAnimator.addUpdateListener(animation -> {
            mAnimHeight = (int) animation.getAnimatedValue();
            requestLayout();

            if (mOnHeightUpdatedListener != null) {
                mOnHeightUpdatedListener.OnHeightUpdate(MOExpandableLayout.this, mAnimHeight, mAnimHeight - mLastAnimHeight);
            }

            mLastAnimHeight = mAnimHeight;
        });
        mValueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mOnHeightUpdatedListener != null) {
                    mOnHeightUpdatedListener.OnHeightUpdate(MOExpandableLayout.this, mHeight, mHeight - mLastAnimHeight);
                }

                mAnimating = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mValueAnimator.start();
    }
}
