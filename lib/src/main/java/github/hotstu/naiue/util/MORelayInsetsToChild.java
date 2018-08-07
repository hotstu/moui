package github.hotstu.naiue.util;

import android.graphics.Rect;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.view.View;
import android.view.ViewGroup;

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
    int preInsetLeft = 0;
    int preInsetTop = 0;
    int preInsetRight = 0;
    int preInsetBottom = 0;


    @Override
    public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
        ViewGroup viewGroup = ((ViewGroup) v);
        if (!insets.hasSystemWindowInsets()) {
            return insets;
        }
        Rect childInsets = new Rect();
        childInsets.set(insets.getSystemWindowInsetLeft(),
                insets.getSystemWindowInsetTop(),
                insets.getSystemWindowInsetRight(),
                insets.getSystemWindowInsetBottom());
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
        v.setPadding(v.getPaddingLeft() -preInsetLeft+ childInsets.left, v.getPaddingTop()-preInsetTop + childInsets.top,
                v.getPaddingRight()-preInsetRight + childInsets.right, v.getPaddingBottom()-preInsetBottom + childInsets.bottom);
        preInsetLeft = childInsets.left;
        preInsetRight = childInsets.right;
        preInsetTop = childInsets.top;
        preInsetBottom = childInsets.bottom;
        return insets.consumeSystemWindowInsets();

    }

    public static boolean isInsetsAwareContainer(View child) {
        return child instanceof InsetsAwareView || child instanceof CoordinatorLayout;
    }
}
