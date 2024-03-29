package io.github.hotstu.moui.arch;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import io.github.hotstu.moui.R;
import io.github.hotstu.moui.util.MOViewHelper;
import io.github.hotstu.moui.widget.swipback.MOSwipeBackLayout;

/**
 * 基础 Fragment 类，提供各种基础功能。
 * Created by cgspine on 15/9/14.
 */
public abstract class MOFragment extends Fragment {
    private static final String SWIPE_BACK_VIEW = "swipe_back_view";
    private static final String TAG = MOFragment.class.getSimpleName();

    /**
     * Edge flag indicating that the left edge should be affected.
     */
    public static final int EDGE_LEFT = MOSwipeBackLayout.EDGE_LEFT;

    /**
     * Edge flag indicating that the right edge should be affected.
     */
    public static final int EDGE_RIGHT = MOSwipeBackLayout.EDGE_RIGHT;

    /**
     * Edge flag indicating that the bottom edge should be affected.
     */
    public static final int EDGE_BOTTOM = MOSwipeBackLayout.EDGE_BOTTOM;

    // === 提供两种默认的进入退出动画 ===
    protected static final TransitionConfig SLIDE_TRANSITION_CONFIG = new TransitionConfig(
            R.anim.slide_in_right, R.anim.slide_out_left,
            R.anim.slide_in_left, R.anim.slide_out_right);

    protected static final TransitionConfig SCALE_TRANSITION_CONFIG = new TransitionConfig(
            R.anim.scale_enter, R.anim.slide_still,
            R.anim.slide_still, R.anim.scale_exit);

    private View mBaseView;
    private MOSwipeBackLayout mCacheView;
    private boolean isCreateForSwipeBack = false;
    private int mBackStackIndex = 0;

    public MOFragment() {
        super();
    }

    public final MOFragmentActivity getBaseFragmentActivity() {
        return (MOFragmentActivity) getActivity();
    }

    public boolean isAttachedToActivity() {
        return !isRemoving() && mBaseView != null;
    }

    /**
     * 重写这个方法如果需要自己指定返回时的临时view/override this if your want show custom view when swiping back
     * @param inflater
     * @param container
     * @return
     */
    public View getBackingView(LayoutInflater inflater, ViewGroup container) {
        return  onCreateView(inflater, container, null);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mBaseView = null;
    }

    protected void startFragment(MOFragment fragment) {
        startFragment(fragment, null);
    }

    protected void startFragment(MOFragment fragment,@Nullable TransitionConfig transitionConfig) {
        MOFragmentActivity baseFragmentActivity = this.getBaseFragmentActivity();
        if (baseFragmentActivity != null) {
            if (this.isAttachedToActivity()) {
                baseFragmentActivity.startFragment(fragment, transitionConfig);
            } else {
                Log.e("QMUIFragment", "fragment not attached:" + this);
            }
        } else {
            Log.e("QMUIFragment", "startFragment null:" + this);
        }
    }


    //============================= 生命周期 ================================


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            int backStackEntryCount = fragmentManager.getBackStackEntryCount();
            for (int i = backStackEntryCount - 1; i >= 0; i--) {
                FragmentManager.BackStackEntry entry = fragmentManager.getBackStackEntryAt(i);
                if (getTagNmae().equals(entry.getName())) {
                    mBackStackIndex = i;
                    break;
                }
            }
        }
    }

    protected String getTagNmae() {
        return getClass().getSimpleName();
    }

    private MOSwipeBackLayout newSwipeBackLayout() {
        View rootView = onCreateView();
        if (translucentFull()) {
            rootView.setFitsSystemWindows(false);
        } else {
            rootView.setFitsSystemWindows(true);
        }
        MOSwipeBackLayout swipeBackLayout = MOSwipeBackLayout.wrap(rootView, dragBackEdge());
        swipeBackLayout.setEnableGesture(canDragBack());
        swipeBackLayout.addSwipeListener(new MOSwipeBackLayout.SwipeListener() {
            @Override
            public void onScrollStateChange(int state, float scrollPercent) {
                Log.i(TAG, "SwipeListener:onScrollStateChange: state = " + state + " ;scrollPercent = " + scrollPercent);
                ViewGroup container = getBaseFragmentActivity().getFragmentContainer();
                int childCount = container.getChildCount();
                if (state == MOSwipeBackLayout.STATE_IDLE) {
                    if (scrollPercent <= 0.0F) {
                        for (int i = childCount - 1; i >= 0; i--) {
                            View view = container.getChildAt(i);
                            Object tag = view.getTag(R.id.mo_arch_swipe_layout_in_back);
                            if (tag != null && SWIPE_BACK_VIEW.equals(tag)) {
                                container.removeView(view);
                            }
                        }
                    } else if (scrollPercent >= 1.0F) {
                        for (int i = childCount - 1; i >= 0; i--) {
                            View view = container.getChildAt(i);
                            Object tag = view.getTag(R.id.mo_arch_swipe_layout_in_back);
                            if (tag != null && SWIPE_BACK_VIEW.equals(tag)) {
                                container.removeView(view);
                            }
                        }
                        FragmentManager fragmentManager = getFragmentManager();
                        if (fragmentManager == null) {
                            return;
                        }
                        Utils.findAndModifyOpInBackStackRecord(fragmentManager, -1, new Utils.OpHandler() {
                            @Override
                            public boolean handle(Object op) {
                                Field cmdField = Utils.getOpCmdField(op);
                                if (cmdField == null) {
                                    return false;
                                }
                                try {
                                    cmdField.setAccessible(true);
                                    int cmd = (int) cmdField.get(op);
                                    if (cmd == 1) {
                                        Field popEnterAnimField = Utils.getOpPopExitAnimField(op);
                                        if (popEnterAnimField != null) {
                                            popEnterAnimField.setAccessible(true);
                                            popEnterAnimField.set(op, 0);
                                        }
                                    } else if (cmd == 3) {
                                        Field popExitAnimField = Utils.getOpPopEnterAnimField(op);
                                        if (popExitAnimField != null) {
                                            popExitAnimField.setAccessible(true);
                                            popExitAnimField.set(op, 0);
                                        }
                                    }
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                                return false;
                            }

                            @Override
                            public boolean needReNameTag() {
                                return false;
                            }

                            @Override
                            public String newTagName() {
                                return null;
                            }
                        });
                        popBackStack();
                    }
                }
            }

            @Override
            public void onScroll(int edgeFlag, float scrollPercent) {
                int targetOffset = (int) (Math.abs(backViewInitOffset()) * (1 - scrollPercent));
                ViewGroup container = getBaseFragmentActivity().getFragmentContainer();
                int childCount = container.getChildCount();
                for (int i = childCount - 1; i >= 0; i--) {
                    View view = container.getChildAt(i);
                    Object tag = view.getTag(R.id.mo_arch_swipe_layout_in_back);
                    if (tag != null && SWIPE_BACK_VIEW.equals(tag)) {
                        if (edgeFlag == EDGE_BOTTOM) {
                            ViewCompat.offsetTopAndBottom(view, targetOffset - view.getTop());
                        } else if (edgeFlag == EDGE_RIGHT) {
                            ViewCompat.offsetLeftAndRight(view, targetOffset - view.getLeft());
                        } else {
                            Log.i(TAG, "targetOffset = " + targetOffset + " ; view.getLeft() = " + view.getLeft());
                            ViewCompat.offsetLeftAndRight(view, -targetOffset - view.getLeft());
                        }
                    }
                }
            }

            @Override
            public void onEdgeTouch(int edgeFlag) {
                Log.i(TAG, "SwipeListener:onEdgeTouch: edgeFlag = " + edgeFlag);
                FragmentManager fragmentManager = getFragmentManager();
                if (fragmentManager == null) {
                    return;
                }
                int backstackCount = fragmentManager.getBackStackEntryCount();
                if (backstackCount > 1) {
                    Utils.findAndModifyOpInBackStackRecord(fragmentManager, -1, new Utils.OpHandler() {
                        @Override
                        public boolean handle(Object op) {
                            Field cmdField = Utils.getOpCmdField(op);
                            if (cmdField == null) {
                                return false;
                            }
                            try {
                                cmdField.setAccessible(true);
                                int cmd = (int) cmdField.get(op);
                                if (cmd == 3) {
                                    Field popEnterAnimField = Utils.getOpPopEnterAnimField(op);
                                    if (popEnterAnimField != null) {
                                        popEnterAnimField.setAccessible(true);
                                        popEnterAnimField.set(op, 0);
                                    }


                                    Field fragmentField = Utils.getOpFragmentField(op);
                                    if (fragmentField != null) {
                                        fragmentField.setAccessible(true);
                                        Object fragmentObject = fragmentField.get(op);
                                        if (fragmentObject instanceof MOFragment) {
                                            MOFragment fragment = (MOFragment) fragmentObject;
                                            ViewGroup container = getBaseFragmentActivity().getFragmentContainer();
                                            fragment.isCreateForSwipeBack = true;
                                            View baseView = null;
                                            if (fragment.getContext() != null) {
                                                //填充了底层的fragment 到当前层
                                                baseView = fragment.getBackingView(LayoutInflater.from(getContext()), container);
                                            }
                                            fragment.isCreateForSwipeBack = false;
                                            if (baseView != null) {
                                                baseView.setTag(R.id.mo_arch_swipe_layout_in_back, SWIPE_BACK_VIEW);
                                                container.addView(baseView, 0);
                                                int offset = Math.abs(backViewInitOffset());
                                                if (edgeFlag == EDGE_BOTTOM) {
                                                    ViewCompat.offsetTopAndBottom(baseView, offset);
                                                } else if (edgeFlag == EDGE_RIGHT) {
                                                    ViewCompat.offsetLeftAndRight(baseView, offset);
                                                } else {
                                                    ViewCompat.offsetLeftAndRight(baseView, -1 * offset);
                                                }
                                            }
                                        }
                                    }
                                }
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                            return false;
                        }

                        @Override
                        public boolean needReNameTag() {
                            return false;
                        }

                        @Override
                        public String newTagName() {
                            return null;
                        }
                    });
                } else {
                    if (getActivity() != null) {
                        getActivity().getWindow().getDecorView().setBackgroundColor(0);
                        Utils.convertActivityToTranslucent(getActivity());
                    }
                }

            }

            @Override
            public void onScrollOverThreshold() {
                Log.i(TAG, "SwipeListener:onEdgeTouch:onScrollOverThreshold");
            }
        });
        return swipeBackLayout;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MOSwipeBackLayout swipeBackLayout;
        if (mCacheView == null) {
            swipeBackLayout = newSwipeBackLayout();
            mCacheView = swipeBackLayout;
        } else if (isCreateForSwipeBack) {
            // in swipe back, must not in animation
            swipeBackLayout = mCacheView;
        } else {
            boolean isInRemoving = false;
            try {
                Method method = Fragment.class.getDeclaredMethod("getAnimatingAway");
                method.setAccessible(true);
                Object object = method.invoke(this);
                if (object != null) {
                    isInRemoving = true;
                }
            } catch (NoSuchMethodException e) {
                isInRemoving = true;
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                isInRemoving = true;
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                isInRemoving = true;
                e.printStackTrace();
            }
            if (isInRemoving) {
                swipeBackLayout = newSwipeBackLayout();
                mCacheView = swipeBackLayout;
            } else {
                swipeBackLayout = mCacheView;
            }
        }


        if (!isCreateForSwipeBack) {
            mBaseView = swipeBackLayout.getContentView();
            swipeBackLayout.setTag(R.id.mo_arch_swipe_layout_in_back, null);
        }

        ViewCompat.setTranslationZ(swipeBackLayout, mBackStackIndex);

        swipeBackLayout.setFitsSystemWindows(false);

        if (getActivity() != null) {
            MOViewHelper.requestApplyInsets(getActivity().getWindow());
        }

        if (swipeBackLayout.getParent() != null) {
            ViewGroup viewGroup = (ViewGroup) swipeBackLayout.getParent();
            if (viewGroup.indexOfChild(swipeBackLayout) > -1) {
                viewGroup.removeView(swipeBackLayout);
            } else {
                // see https://issuetracker.google.com/issues/71879409
                try {
                    Field parentField = View.class.getDeclaredField("mParent");
                    parentField.setAccessible(true);
                    parentField.set(swipeBackLayout, null);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return swipeBackLayout;
    }

    protected void popBackStack() {
        getBaseFragmentActivity().popBackStack();
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (!enter && getParentFragment() != null && getParentFragment().isRemoving()) {
            // This is a workaround for the bug where child fragments disappear when
            // the parent is removed (as all children are first removed from the parent)
            // See https://code.google.com/p/android/issues/detail?id=55228
            Animation doNothingAnim = new AlphaAnimation(1, 1);
            int duration = getResources().getInteger(R.integer.mo_anim_duration);
            doNothingAnim.setDuration(duration);
            return doNothingAnim;
        }
        return null;
    }

    /**
     * onCreateView
     */
    protected abstract View onCreateView();

    /**
     * disable or enable drag back
     *
     * @return
     */
    protected boolean canDragBack() {
        return true;
    }

    /**
     * if enable drag back,
     *
     * @return
     */
    protected int backViewInitOffset() {
        return 0;
    }

    protected int dragBackEdge() {
        return EDGE_LEFT;
    }

    //============================= 新流程 ================================

    /**
     * 沉浸式处理，返回 false，则状态栏下为内容区域，返回 true, 则状态栏下为 padding 区域
     */
    protected boolean translucentFull() {
        return false;
    }

    /**
     * 如果是最后一个Fragment，finish后执行的方法
     */
    @SuppressWarnings("SameReturnValue")
    public Object onLastFragmentFinish() {
        return null;
    }

    /**
     * 转场动画控制
     */
    public TransitionConfig onFetchTransitionConfig() {
        return SLIDE_TRANSITION_CONFIG;
    }

    ////////界面跳转动画
    public static final class TransitionConfig {
        public final int enter;
        public final int exit;
        public final int popenter;
        public final int popout;

        public TransitionConfig(int enter, int popout) {
            this(enter, 0, 0, popout);
        }

        public TransitionConfig(int enter, int exit, int popenter, int popout) {
            this.enter = enter;
            this.exit = exit;
            this.popenter = popenter;
            this.popout = popout;
        }
    }
}

