package io.github.hotstu.moui.widget.recycler;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * 让linearLayoutManager 支持predictive animation
 *
 * @author hglf
 * @since 2018/1/4
 */
public class PredictiveLinearLayoutManager extends LinearLayoutManager {
    public PredictiveLinearLayoutManager(Context context) {
        super(context);
    }

    public PredictiveLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public PredictiveLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean supportsPredictiveItemAnimations() {
        return true;
    }
}
