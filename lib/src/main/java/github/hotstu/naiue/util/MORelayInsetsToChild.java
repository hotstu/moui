package github.hotstu.naiue.util;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import github.hotstu.naiue.widget.InsetsAwareView;

/**
 *
 * fitsystemInsets 为ture时，android 默认行为为这个layout添加padding（coordinatlayout等除外）
 * 这个方法类将fitsystemInsets请求向下传递，如果子view不处理再由这个viewGroup处理
 * 使用场景：ViewGoup 向下传递给toolbar，让toolbar添加padding
 * @author hglf
 * @since 2018/7/13
 */
public class MORelayInsetsToChild implements OnApplyWindowInsetsListener {
    protected int preInsetLeft = 0;
    protected int preInsetTop = 0;
    protected int preInsetRight = 0;
    protected int preInsetBottom = 0;
    private final boolean consumeBottom;

    public MORelayInsetsToChild() {
        this.consumeBottom = false;
    }

    /**
     * 是否立即响应底部的inset，例如键盘弹起放下，不向下传递，默认为false
     * @param consumeBottom
     */
    public MORelayInsetsToChild(boolean consumeBottom) {
        this.consumeBottom = consumeBottom;
    }

    @Override
    public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
        ViewGroup viewGroup = ((ViewGroup) v);
        if (!insets.hasSystemWindowInsets()) {
            v.setPadding(v.getPaddingLeft() -preInsetLeft, v.getPaddingTop()-preInsetTop,
                    v.getPaddingRight()-preInsetRight, v.getPaddingBottom()-preInsetBottom);
            preInsetLeft = 0;
            preInsetRight = 0;
            preInsetTop = 0;
            preInsetBottom = 0;
            return insets;
        }
        Rect childInsets = new Rect();
        childInsets.set(insets.getSystemWindowInsetLeft(),
                insets.getSystemWindowInsetTop(),
                insets.getSystemWindowInsetRight(),
                consumeBottom?0:insets.getSystemWindowInsetBottom());
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            if (!ViewCompat.getFitsSystemWindows(child) && !isInsetsAwareContainer(child)) {
                continue;
            }
            WindowInsetsCompat windowInsetsCompat = ViewCompat.dispatchApplyWindowInsets(child, insets.replaceSystemWindowInsets(childInsets));
            childInsets.set(windowInsetsCompat.getSystemWindowInsetLeft(),
                    windowInsetsCompat.getSystemWindowInsetTop(),
                    windowInsetsCompat.getSystemWindowInsetRight(),
                    windowInsetsCompat.getSystemWindowInsetBottom());
            if (windowInsetsCompat.isConsumed()) {
                break;
            }
        }
        //已修复 bug 不能反映 ViewCompat.requestApplyInsets(view);的作用
        v.setPadding(v.getPaddingLeft() -preInsetLeft+ childInsets.left,
                v.getPaddingTop()-preInsetTop + childInsets.top,
                v.getPaddingRight()-preInsetRight + childInsets.right,
                v.getPaddingBottom()-preInsetBottom + (consumeBottom?insets.getSystemWindowInsetBottom():childInsets.bottom));
        preInsetLeft = childInsets.left;
        preInsetRight = childInsets.right;
        preInsetTop = childInsets.top;
        preInsetBottom = (consumeBottom?insets.getSystemWindowInsetBottom():childInsets.bottom);
        return insets.consumeSystemWindowInsets();
    }

    public static boolean isInsetsAwareContainer(View child) {
        return child instanceof InsetsAwareView || child instanceof CoordinatorLayout;
    }
}
