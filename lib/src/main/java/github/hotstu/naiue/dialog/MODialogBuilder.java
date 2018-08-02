package github.hotstu.naiue.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import github.hotstu.naiue.R;
import github.hotstu.naiue.util.MODisplayHelper;
import github.hotstu.naiue.util.MOResHelper;

/**
 * 创建 {@link MODialog} 的 Builder 基类, 不同的 Builder 子类拥有创建不同类型对话框的能力, 具体见子类。
 * <p>该类产生的 Dialog 分为上中下三个部分:</p>
 * <ul>
 * <li>上部分是 title 区域, 支持显示纯文本标题, 通过 {@link #setTitle(int)} 系列方法设置。
 * 子类也可以通过 override {@link #onCreateTitle(MODialog, ViewGroup)} 方法自定义</li>
 * <li>中间部分的内容由各个子类决定, 子类通过 override {@link #onCreateContent(MODialog, ViewGroup)} 方法自定义。</li>
 * <li>下部分是操作区域, 支持添加操作按钮, 通过 {@link #addAction(int, int, MODialogAction.ActionListener)} 系列方法添加。
 * 子类也可以通过 override {@link #onCreateHandlerBar(MODialog, ViewGroup)} 方法自定义。
 * 其中操作按钮有内联和块级之分, 也有普通、正向、反向之分, 具体见 {@link MODialogAction}
 * </li>
 * </ul>
 *
 * @author cginechen
 * @date 2015-10-20
 */
public abstract class MODialogBuilder<T extends MODialogBuilder> {
    protected Context mContext;
    protected MODialog mDialog;
    protected LayoutInflater mInflater;
    protected String mTitle;
    private boolean mCancelable = true;

    protected LinearLayout mRootView;
    protected LinearLayout mDialogWrapper;
    protected View mAnchorTopView;
    protected View mAnchorBottomView;
    protected List<MODialogAction> mActions = new ArrayList<>();
    protected MODialogAction mLeftAction;

    protected TextView mTitleView;
    protected LinearLayout mActionContainer;
    private int mContentAreaMaxHeight;

    public MODialogBuilder(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        mContentAreaMaxHeight = (int) (MODisplayHelper.getScreenHeight(mContext) * 0.75);
    }

    protected int getContentAreaMaxHeight() {
        return mContentAreaMaxHeight;
    }

    /**
     * 设置内容区域最高的高度
     *
     * @param contentAreaMaxHeight
     */
    public T setContentAreaMaxHeight(int contentAreaMaxHeight) {
        mContentAreaMaxHeight = contentAreaMaxHeight;
        return (T) this;
    }

    /**
     * 设置对话框顶部的标题文字
     */
    @SuppressWarnings("unchecked")
    public T setTitle(String title) {
        if (title != null && title.length() > 0) {
            this.mTitle = title + mContext.getString(R.string.mo_tool_fixellipsize);
        }
        return (T) this;
    }

    /**
     * 设置对话框顶部的标题文字
     */
    public T setTitle(int resId) {
        return setTitle(mContext.getResources().getString(resId));
    }

    @SuppressWarnings("unchecked")
    public T setCancelable(boolean cancelable) {
        mCancelable = cancelable;
        return (T) this;
    }


    //region 添加action

    /**
     * 添加对话框底部的操作按钮
     */
    @SuppressWarnings("unchecked")
    public T addAction(@Nullable MODialogAction action) {
        if (action != null) {
            mActions.add(action);
        }

        return (T) this;
    }

    /**
     * 添加无图标正常类型的操作按钮
     *
     * @param strResId 文案
     * @param listener 点击回调事件
     */
    public T addAction(int strResId, MODialogAction.ActionListener listener) {
        return addAction(0, strResId, listener);
    }

    /**
     * 添加无图标正常类型的操作按钮
     *
     * @param str      文案
     * @param listener 点击回调事件
     */
    public T addAction(String str, MODialogAction.ActionListener listener) {
        return addAction(0, str, listener);
    }

    /**
     * 添加普通类型的操作按钮
     *
     * @param iconResId 图标
     * @param strResId  文案
     * @param listener  点击回调事件
     */
    public T addAction(int iconResId, int strResId, MODialogAction.ActionListener listener) {
        return addAction(iconResId, strResId, MODialogAction.ACTION_PROP_NEUTRAL, listener);
    }

    /**
     * 添加普通类型的操作按钮
     *
     * @param iconResId 图标
     * @param str       文案
     * @param listener  点击回调事件
     */
    public T addAction(int iconResId, String str, MODialogAction.ActionListener listener) {
        return addAction(iconResId, str, MODialogAction.ACTION_PROP_NEUTRAL, listener);
    }

    /**
     * 添加普通类型的操作按钮
     *
     * @param iconRes  图标
     * @param strRes   文案
     * @param prop     属性
     * @param listener 点击回调事件
     */
    public T addAction(int iconRes, int strRes, @MODialogAction.Prop int prop, MODialogAction.ActionListener listener) {
        return addAction(iconRes, mContext.getResources().getString(strRes), prop, listener);
    }

    /**
     * 添加普通类型的操作按钮
     *
     * @param iconRes  图标
     * @param str      文案
     * @param prop     属性
     * @param listener 点击回调事件
     */
    public T addAction(int iconRes, String str, @MODialogAction.Prop int prop, MODialogAction.ActionListener listener) {
        return addAction(iconRes, str, prop, MODialogAction.ACTION_TYPE_NORMAL, listener);
    }

    /**
     * 添加普通类型的操作按钮
     *
     * @param iconRes  图标
     * @param strRes   文案
     * @param type     类型
     * @param prop     属性
     * @param listener 点击回调事件
     */
    protected T addAction(int iconRes, int strRes, @MODialogAction.Prop int prop, @MODialogAction.Type int type, MODialogAction.ActionListener listener) {
        return addAction(iconRes, mContext.getResources().getString(strRes), prop, type, listener);
    }

    /**
     * 添加普通类型的操作按钮
     *
     * @param iconRes  图标
     * @param str      文案
     * @param type     类型
     * @param prop     属性
     * @param listener 点击回调事件
     */
    @SuppressWarnings("unchecked")
    protected T addAction(int iconRes, String str, @MODialogAction.Prop int prop, @MODialogAction.Type int type, MODialogAction.ActionListener listener) {
        MODialogAction action = new MODialogAction(mContext, iconRes, str, type, prop, listener);
        mActions.add(action);
        return (T) this;
    }

    public MODialogAction setLeftAction(String str, MODialogAction.ActionListener listener) {
        return setLeftAction(0, str, listener);
    }

    public MODialogAction setLeftAction(int iconRes, String str, MODialogAction.ActionListener listener) {
        return setLeftAction(iconRes, str, MODialogAction.ACTION_PROP_NEUTRAL, listener);
    }


    public MODialogAction setLeftAction(int iconRes, String str, @MODialogAction.Prop int prop, MODialogAction.ActionListener listener) {
        mLeftAction = new MODialogAction(mContext, iconRes, str, MODialogAction.ACTION_TYPE_NORMAL, prop, listener);
        return mLeftAction;
    }

    //endregion

    /**
     * 判断对话框是否需要显示title
     *
     * @return 是否有title
     */
    protected boolean hasTitle() {
        return mTitle != null && mTitle.length() != 0;
    }

    /**
     * 产生一个 Dialog 并显示出来
     */
    public MODialog show() {
        final MODialog dialog = create();
        dialog.show();
        return dialog;
    }

    /**
     * 只产生一个 Dialog, 不显示出来
     *
     * @see #create(int)
     */
    public MODialog create() {
        return create(R.style.MO_Dialog);
    }

    /**
     * 产生一个Dialog，但不显示出来。
     *
     * @param style Dialog 的样式
     * @see #create()
     */
    @SuppressLint("InflateParams")
    public MODialog create(@StyleRes int style) {
        mDialog = new MODialog(mContext, style);

        mRootView = (LinearLayout) mInflater.inflate(R.layout.mo_dialog_layout, null);
        mDialogWrapper = (LinearLayout) mRootView.findViewById(R.id.dialog);
        mAnchorTopView = mRootView.findViewById(R.id.anchor_top);
        mAnchorBottomView = mRootView.findViewById(R.id.anchor_bottom);

        // title
        onCreateTitle(mDialog, mDialogWrapper);

        //content
        onCreateContent(mDialog, mDialogWrapper);

        // 操作
        onCreateHandlerBar(mDialog, mDialogWrapper);


        mDialog.addContentView(mRootView, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mDialog.setCancelable(mCancelable);
        onAfter(mDialog, mRootView);
        return mDialog;
    }

    /**
     * 创建顶部的标题区域
     */
    protected void onCreateTitle(MODialog dialog, ViewGroup parent) {
        if (hasTitle()) {
            mTitleView = new TextView(mContext);
            mTitleView.setSingleLine(true);
            mTitleView.setEllipsize(TextUtils.TruncateAt.END);
            mTitleView.setText(mTitle);
            mTitleView.setTextColor(MOResHelper.getAttrColor(mContext, R.attr.mo_dialog_title_text_color));
            mTitleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, MOResHelper.getAttrDimen(mContext, R.attr.mo_dialog_title_text_size));
            mTitleView.setPadding(
                    MOResHelper.getAttrDimen(mContext, R.attr.mo_dialog_padding_horizontal),
                    MOResHelper.getAttrDimen(mContext, R.attr.mo_dialog_title_margin_top),
                    MOResHelper.getAttrDimen(mContext, R.attr.mo_dialog_padding_horizontal),
                    0
            );
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mTitleView.setLayoutParams(lp);
            parent.addView(mTitleView);
        }
    }

    public TextView getTitleView() {
        return mTitleView;
    }

    /**
     * 创建中间的区域
     */
    protected abstract void onCreateContent(MODialog dialog, ViewGroup parent);

    /**
     * 创建底部的操作栏区域
     */
    protected void onCreateHandlerBar(final MODialog dialog, ViewGroup parent) {
        int size = mActions.size();
        if (size > 0 || mLeftAction != null) {
            mActionContainer = new LinearLayout(mContext);
            mActionContainer.setOrientation(LinearLayout.HORIZONTAL);
            mActionContainer.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mActionContainer.setPadding(
                    MOResHelper.getAttrDimen(mContext, R.attr.mo_dialog_action_container_margin_horizontal),
                    0,
                    MOResHelper.getAttrDimen(mContext, R.attr.mo_dialog_action_container_margin_horizontal),
                    MOResHelper.getAttrDimen(mContext, R.attr.mo_dialog_action_container_margin_bottom));
            if (mLeftAction != null) {
                mActionContainer.addView(mLeftAction.generateActionView(mContext, mDialog, 0, false));
            }
            View space = new View(mContext);
            LinearLayout.LayoutParams spaceLp = new LinearLayout.LayoutParams(0, 0);
            spaceLp.weight = 1;
            space.setLayoutParams(spaceLp);
            mActionContainer.addView(space);

            for (int i = 0; i < size; i++) {
                MODialogAction action = mActions.get(i);
                mActionContainer.addView(action.generateActionView(mContext, mDialog, i, true));
            }

            mActionContainer.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    int width = right - left;
                    int childCount = mActionContainer.getChildCount();
                    if (childCount > 0) {
                        View lastChild = mActionContainer.getChildAt(childCount - 1);
                        // 如果ActionButton的宽度过宽，则减小padding
                        if (lastChild.getRight() > width) {
                            int childPaddingHor = Math.max(0, lastChild.getPaddingLeft() - MODisplayHelper.dp2px(mContext, 3));
                            for (int i = 0; i < childCount; i++) {
                                mActionContainer.getChildAt(i).setPadding(childPaddingHor, 0, childPaddingHor, 0);
                            }
                        }
                    }

                }
            });
            parent.addView(mActionContainer);

        }
    }

    protected void onAfter(MODialog dialog, LinearLayout parent) {
        //默认情况下，点击anchorView使得dialog消失
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        };
        mAnchorBottomView.setOnClickListener(listener);
        mAnchorTopView.setOnClickListener(listener);
    }

    public View getAnchorTopView() {
        return mAnchorTopView;
    }

    public View getAnchorBottomView() {
        return mAnchorBottomView;
    }

    public List<MODialogAction> getPositiveAction() {
        List<MODialogAction> output = new ArrayList<>();
        for (MODialogAction action : mActions) {
            if (action.getActionProp() == MODialogAction.ACTION_PROP_POSITIVE) {
                output.add(action);
            }
        }
        return output;
    }
}
