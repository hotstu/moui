package io.github.hotstu.moui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * height is wrapContent but limited by maxHeight
 * <p>
 * Created by cgspine on 2017/12/21.
 */

public class MOWrapContentScrollView extends MOObservableScrollView {
    private int mMaxHeight = Integer.MAX_VALUE >> 2;

    public MOWrapContentScrollView(Context context) {
        super(context);
    }

    public MOWrapContentScrollView(Context context, int maxHeight) {
        super(context);
        mMaxHeight = maxHeight;
    }

    public MOWrapContentScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MOWrapContentScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setMaxHeight(int maxHeight) {
        if (mMaxHeight != maxHeight) {
            mMaxHeight = maxHeight;
            requestLayout();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = View.MeasureSpec.makeMeasureSpec(mMaxHeight,
                View.MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
