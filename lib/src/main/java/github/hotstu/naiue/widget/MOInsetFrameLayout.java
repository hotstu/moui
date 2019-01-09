package github.hotstu.naiue.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import github.hotstu.naiue.util.MORelayInsetsToChild;

/**
 * @author hglf
 * @since 2018/7/19
 */
public class MOInsetFrameLayout extends FrameLayout implements InsetsAwareView {
    public MOInsetFrameLayout(@NonNull Context context) {
        super(context);
        init();
    }



    public MOInsetFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MOInsetFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MOInsetFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        ViewCompat.setOnApplyWindowInsetsListener(this, new MORelayInsetsToChild());
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }
}
