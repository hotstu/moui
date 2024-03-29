package io.github.hotstu.moui.dialog;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.TransformationMethod;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import java.util.ArrayList;

import androidx.annotation.LayoutRes;
import androidx.appcompat.widget.AppCompatTextView;

import io.github.hotstu.moui.R;
import io.github.hotstu.moui.util.MODisplayHelper;
import io.github.hotstu.moui.util.MOResHelper;
import io.github.hotstu.moui.util.MOViewHelper;
import io.github.hotstu.moui.widget.MOWrapContentScrollView;

/**
 * MODialog 对话框一般由 {@link MODialogBuilder} 及其子类创建, 不同的 Builder 可以创建不同类型的对话框,
 * 例如消息类型的对话框、菜单项对话框等等。
 *
 * @author cginechen
 * @date 2015-10-20
 * @see MODialogBuilder
 */
public class MODialog extends Dialog {

    public MODialog(Context context) {
        this(context, R.style.MO_Dialog);
    }

    public MODialog(Context context, int styleRes) {
        super(context, styleRes);
        init();
    }

    private void init() {
        setCancelable(true);
        setCanceledOnTouchOutside(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDialogWidth();
    }

    private void initDialogWidth() {
        Window window = getWindow();
        if (window == null) {
            return;
        }
        WindowManager.LayoutParams wmlp = window.getAttributes();
        wmlp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        window.setAttributes(wmlp);
    }

    /**
     * 消息类型的对话框 Builder。通过它可以生成一个带标题、文本消息、按钮的对话框。
     */
    public static class MessageDialogBuilder extends MODialogBuilder<MessageDialogBuilder> {
        protected CharSequence mMessage;
        private final MOWrapContentScrollView mScrollContainer;
        private AppCompatTextView mTextView;

        public MessageDialogBuilder(Context context) {
            super(context);
            mTextView = new AppCompatTextView(mContext);
            mTextView.setTextColor(MOResHelper.getAttrColor(mContext, R.attr.mo_color_gray_4));
            mTextView.setLineSpacing(MODisplayHelper.dpToPx(2), 1.0f);
            mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, MOResHelper.getAttrDimen(mContext, R.attr.mo_dialog_content_message_text_size));

            mScrollContainer = new MOWrapContentScrollView(mContext);
            mScrollContainer.addView(mTextView);
        }

        /**
         * 设置对话框的消息文本
         */
        public MessageDialogBuilder setMessage(CharSequence message) {
            this.mMessage = message;
            return this;
        }

        /**
         * 设置对话框的消息文本
         */
        public MessageDialogBuilder setMessage(int resId) {
            return setMessage(mContext.getResources().getString(resId));
        }

        @Override
        protected void onCreateContent(MODialog dialog, ViewGroup parent) {
            if (mMessage != null && mMessage.length() != 0) {
                mScrollContainer.setMaxHeight(getContentAreaMaxHeight());
                mTextView.setText(mMessage);
                mTextView.setPadding(
                        MOResHelper.getAttrDimen(mContext, R.attr.mo_dialog_padding_horizontal),
                        MOResHelper.getAttrDimen(mContext, hasTitle() ? R.attr.mo_dialog_content_padding_top : R.attr.mo_dialog_content_padding_top_when_no_title),
                        MOResHelper.getAttrDimen(mContext, R.attr.mo_dialog_padding_horizontal),
                        MOResHelper.getAttrDimen(mContext, R.attr.mo_dialog_content_padding_bottom)
                );
                parent.addView(mScrollContainer);
            }
        }

        public AppCompatTextView getTextView() {
            return mTextView;
        }
    }

    /**
     * 带 CheckBox 的消息确认框 Builder
     */
    public static class CheckBoxMessageDialogBuilder extends MODialogBuilder<CheckBoxMessageDialogBuilder> {

        private final MOWrapContentScrollView mScrollContainer;
        protected String mMessage;
        private boolean mIsChecked = false;
        private Drawable mCheckMarkDrawable;
        private AppCompatTextView mTextView;

        public CheckBoxMessageDialogBuilder(Context context) {
            super(context);
            mCheckMarkDrawable = MOResHelper.getAttrDrawable(context, R.attr.mo_s_checkbox);
            mScrollContainer = new MOWrapContentScrollView(mContext);
            mTextView = new AppCompatTextView(mContext);
            mTextView.setTextColor(MOResHelper.getAttrColor(mContext, R.attr.mo_color_gray_4));
            mTextView.setLineSpacing(MODisplayHelper.dpToPx(2), 1.0f);
            mTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, MOResHelper.getAttrDimen(mContext, R.attr.mo_dialog_content_message_text_size));
            mScrollContainer.addView(mTextView);
        }

        /**
         * 设置对话框的消息文本
         */
        public CheckBoxMessageDialogBuilder setMessage(String message) {
            this.mMessage = message;
            return this;
        }

        /**
         * 设置对话框的消息文本
         */
        public CheckBoxMessageDialogBuilder setMessage(int resid) {
            return setMessage(mContext.getResources().getString(resid));
        }

        /**
         * CheckBox 是否处于勾选状态
         */
        public boolean isChecked() {
            return mIsChecked;
        }

        /**
         * 设置 CheckBox 的勾选状态
         */
        public CheckBoxMessageDialogBuilder setChecked(boolean checked) {
            if (mIsChecked != checked) {
                mIsChecked = checked;
                if (mTextView != null) {
                    mTextView.setSelected(checked);
                }
            }

            return this;
        }

        @Override
        protected void onCreateContent(MODialog dialog, ViewGroup parent) {
            if (mMessage != null && mMessage.length() != 0) {
                mScrollContainer.setMaxHeight(getContentAreaMaxHeight());
                mTextView.setText(mMessage);
                mTextView.setPadding(
                        MOResHelper.getAttrDimen(mContext, R.attr.mo_dialog_padding_horizontal),
                        MOResHelper.getAttrDimen(mContext, hasTitle() ? R.attr.mo_dialog_confirm_content_padding_top : R.attr.mo_dialog_content_padding_top_when_no_title),
                        MOResHelper.getAttrDimen(mContext, R.attr.mo_dialog_padding_horizontal),
                        MOResHelper.getAttrDimen(mContext, R.attr.mo_dialog_confirm_content_padding_bottom)
                );
                mCheckMarkDrawable.setBounds(0, 0, mCheckMarkDrawable.getIntrinsicWidth(), mCheckMarkDrawable.getIntrinsicHeight());
                mTextView.setCompoundDrawables(mCheckMarkDrawable, null, null, null);
                mTextView.setCompoundDrawablePadding(MODisplayHelper.dpToPx(12));
                mTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setChecked(!mIsChecked);
                    }
                });
                mTextView.setSelected(mIsChecked);
                parent.addView(mScrollContainer);
            }
        }

        public AppCompatTextView getTextView() {
            return mTextView;
        }

    }

    /**
     * 带输入框的对话框 Builder
     */
    public static class EditTextDialogBuilder extends MODialogBuilder<EditTextDialogBuilder> {
        protected String mPlaceholder;
        protected TransformationMethod mTransformationMethod;
        protected RelativeLayout mMainLayout;
        protected EditText mEditText;
        protected ImageView mRightImageView;
        private int mInputType = InputType.TYPE_CLASS_TEXT;

        public EditTextDialogBuilder(Context context) {
            super(context);
            mEditText = new EditText(mContext);
            mEditText.setHintTextColor(MOResHelper.getAttrColor(mContext, R.attr.mo_color_gray_3));
            mEditText.setTextColor(MOResHelper.getAttrColor(mContext, R.attr.mo_color_black));
            mEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, MOResHelper.getAttrDimen(mContext, R.attr.mo_dialog_content_message_text_size));
            mEditText.setFocusable(true);
            mEditText.setFocusableInTouchMode(true);
            mEditText.setImeOptions(EditorInfo.IME_ACTION_GO);
            mEditText.setGravity(Gravity.CENTER_VERTICAL);
            mEditText.setId(R.id.mo_dialog_edit_input);

            mRightImageView = new ImageView(mContext);
            mRightImageView.setId(R.id.mo_dialog_edit_right_icon);
            mRightImageView.setVisibility(View.GONE);
        }

        /**
         * 设置输入框的 placeholder
         */
        public EditTextDialogBuilder setPlaceholder(String placeholder) {
            this.mPlaceholder = placeholder;
            return this;
        }

        /**
         * 设置输入框的 placeholder
         */
        public EditTextDialogBuilder setPlaceholder(int resId) {
            return setPlaceholder(mContext.getResources().getString(resId));
        }

        /**
         * 设置 EditText 的 transformationMethod
         */
        public EditTextDialogBuilder setTransformationMethod(TransformationMethod transformationMethod) {
            mTransformationMethod = transformationMethod;
            return this;
        }

        /**
         * 设置 EditText 的 inputType
         */
        public EditTextDialogBuilder setInputType(int inputType) {
            mInputType = inputType;
            return this;
        }

        @Override
        protected void onCreateContent(MODialog dialog, ViewGroup parent) {
            mMainLayout = new RelativeLayout(mContext);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.topMargin = MOResHelper.getAttrDimen(mContext, hasTitle() ? R.attr.mo_dialog_edit_content_padding_top : R.attr.mo_dialog_content_padding_top_when_no_title);
            lp.leftMargin = MOResHelper.getAttrDimen(mContext, R.attr.mo_dialog_padding_horizontal);
            lp.rightMargin = MOResHelper.getAttrDimen(mContext, R.attr.mo_dialog_padding_horizontal);
            lp.bottomMargin = MOResHelper.getAttrDimen(mContext, R.attr.mo_dialog_edit_content_padding_bottom);
            mMainLayout.setBackgroundResource(R.drawable.mo_edittext_bg_border_bottom);
            mMainLayout.setLayoutParams(lp);

            if (mTransformationMethod != null) {
                mEditText.setTransformationMethod(mTransformationMethod);
            } else {
                mEditText.setInputType(mInputType);
            }

            mEditText.setBackgroundResource(0);
            mEditText.setPadding(0, 0, 0, MODisplayHelper.dpToPx(5));
            RelativeLayout.LayoutParams editLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            editLp.addRule(RelativeLayout.LEFT_OF, mRightImageView.getId());
            editLp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            if (mPlaceholder != null) {
                mEditText.setHint(mPlaceholder);
            }
            mMainLayout.addView(mEditText, createEditTextLayoutParams());
            mMainLayout.addView(mRightImageView, createRightIconLayoutParams());

            parent.addView(mMainLayout);
        }

        protected RelativeLayout.LayoutParams createEditTextLayoutParams() {
            RelativeLayout.LayoutParams editLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            editLp.addRule(RelativeLayout.LEFT_OF, mRightImageView.getId());
            editLp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            return editLp;
        }

        protected RelativeLayout.LayoutParams createRightIconLayoutParams() {
            RelativeLayout.LayoutParams rightIconLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            rightIconLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            rightIconLp.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            rightIconLp.leftMargin = MODisplayHelper.dpToPx(5);
            return rightIconLp;
        }

        @Override
        protected void onAfter(MODialog dialog, LinearLayout parent) {
            super.onAfter(dialog, parent);
            dialog.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    ((InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
                }
            });
            mEditText.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mEditText.requestFocus();
                    ((InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(mEditText, 0);
                }
            }, 300);
        }

        public EditText getEditText() {
            return mEditText;
        }

        public ImageView getRightImageView() {
            return mRightImageView;
        }
    }

    private static class MenuBaseDialogBuilder<T extends MODialogBuilder> extends MODialogBuilder<T> {
        protected ArrayList<MODialogMenuItemView> mMenuItemViews;
        protected LinearLayout mMenuItemContainer;
        protected LinearLayout.LayoutParams mMenuItemLp;

        public MenuBaseDialogBuilder(Context context) {
            super(context);
            mMenuItemViews = new ArrayList<>();
            mMenuItemLp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    MOResHelper.getAttrDimen(mContext, R.attr.mo_dialog_content_list_item_height)
            );
            mMenuItemLp.gravity = Gravity.CENTER_VERTICAL;
        }

        public void clear() {
            mMenuItemViews.clear();
        }

        @SuppressWarnings("unchecked")
        public T addItem(MODialogMenuItemView itemView, final OnClickListener listener) {
            itemView.setMenuIndex(mMenuItemViews.size());
            itemView.setListener(new MODialogMenuItemView.MenuItemViewListener() {
                @Override
                public void onClick(int index) {
                    onItemClick(index);
                    if (listener != null) {
                        listener.onClick(mDialog, index);
                    }
                }
            });
            mMenuItemViews.add(itemView);
            return (T) this;
        }

        protected void onItemClick(int index) {

        }

        @Override
        protected void onCreateContent(MODialog dialog, ViewGroup parent) {
            mMenuItemContainer = new LinearLayout(mContext);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            mMenuItemContainer.setPadding(
                    0, MOResHelper.getAttrDimen(mContext, R.attr.mo_dialog_content_padding_top_when_list),
                    0, MOResHelper.getAttrDimen(mContext, mActions.size() > 0 ? R.attr.mo_dialog_content_padding_bottom : R.attr.mo_dialog_content_padding_bottom_when_no_action)
            );
            mMenuItemContainer.setLayoutParams(layoutParams);
            mMenuItemContainer.setOrientation(LinearLayout.VERTICAL);
            if (mMenuItemViews.size() == 1) {
                mMenuItemContainer.setPadding(0, 0, 0, 0
                );
                if (hasTitle()) {
                    MOViewHelper.setPaddingTop(mMenuItemContainer, MOResHelper.getAttrDimen(mContext, R.attr.mo_dialog_content_padding_top_when_list));
                }
                if (mActions.size() > 0) {
                    MOViewHelper.setPaddingBottom(mMenuItemContainer, MOResHelper.getAttrDimen(mContext, R.attr.mo_dialog_content_padding_bottom));
                }
            }
            for (MODialogMenuItemView itemView : mMenuItemViews) {
                mMenuItemContainer.addView(itemView, mMenuItemLp);
            }
            ScrollView scrollView = new ScrollView(mContext) {
                @Override
                protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                    heightMeasureSpec = MeasureSpec.makeMeasureSpec(getContentAreaMaxHeight(),
                            MeasureSpec.AT_MOST);
                    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                }
            };
            scrollView.addView(mMenuItemContainer);
            parent.addView(scrollView);
        }
    }

    /**
     * 菜单类型的对话框 Builder
     */
    public static class MenuDialogBuilder extends MenuBaseDialogBuilder<MenuDialogBuilder> {

        public MenuDialogBuilder(Context context) {
            super(context);
        }

        /**
         * 添加多个菜单项
         *
         * @param items    所有菜单项的文字
         * @param listener 菜单项的点击事件
         */
        public MenuDialogBuilder addItems(CharSequence[] items, OnClickListener listener) {
            for (CharSequence item : items) {
                addItem(new MODialogMenuItemView.TextItemView(mContext, item), listener);
            }
            return this;
        }

        /**
         * 添加单个菜单项
         *
         * @param item     菜单项的文字
         * @param listener 菜单项的点击事件
         */
        public MenuDialogBuilder addItem(CharSequence item, OnClickListener listener) {
            addItem(new MODialogMenuItemView.TextItemView(mContext, item), listener);
            return this;
        }

    }

    /**
     * 单选类型的对话框 Builder
     */
    public static class CheckableDialogBuilder extends MenuBaseDialogBuilder<CheckableDialogBuilder> {

        /**
         * 当前被选中的菜单项的下标, 负数表示没选中任何项
         */
        private int mCheckedIndex = -1;

        public CheckableDialogBuilder(Context context) {
            super(context);
        }

        /**
         * 获取当前选中的菜单项的下标
         *
         * @return 负数表示没选中任何项
         */
        public int getCheckedIndex() {
            return mCheckedIndex;
        }

        /**
         * 设置选中的菜单项的下班
         */
        public CheckableDialogBuilder setCheckedIndex(int checkedIndex) {
            mCheckedIndex = checkedIndex;
            return this;
        }

        @Override
        protected void onCreateContent(MODialog dialog, ViewGroup parent) {
            super.onCreateContent(dialog, parent);
            if (mCheckedIndex > -1 && mCheckedIndex < mMenuItemViews.size()) {
                mMenuItemViews.get(mCheckedIndex).setChecked(true);
            }
        }

        @Override
        protected void onItemClick(int index) {
            for (int i = 0; i < mMenuItemViews.size(); i++) {
                MODialogMenuItemView itemView = mMenuItemViews.get(i);
                if (i == index) {
                    itemView.setChecked(true);
                    mCheckedIndex = index;
                } else {
                    itemView.setChecked(false);
                }
            }
        }

        /**
         * 添加菜单项
         *
         * @param items    所有菜单项的文字
         * @param listener 菜单项的点击事件,可以在点击事件里调用 {@link #setCheckedIndex(int)} 来设置选中某些菜单项
         */
        public CheckableDialogBuilder addItems(CharSequence[] items, OnClickListener listener) {
            for (CharSequence item : items) {
                addItem(new MODialogMenuItemView.MarkItemView(mContext, item), listener);
            }
            return this;
        }
    }

    /**
     * 多选类型的对话框 Builder
     */
    public static class MultiCheckableDialogBuilder extends MenuBaseDialogBuilder<MultiCheckableDialogBuilder> {

        /**
         * 该 int 的每一位标识菜单的每一项是否被选中 (1为选中,0位不选中)
         */
        private int mCheckedItems;

        public MultiCheckableDialogBuilder(Context context) {
            super(context);
        }

        /**
         * 设置被选中的菜单项的下标
         *
         * @param checkedItems <b>注意: 该 int 参数的每一位标识菜单项的每一项是否被选中</b>
         *                     <p>如 20 表示选中下标为 1、3 的菜单项, 因为 (2<<1) + (2<<3) = 20</p>
         */
        public MultiCheckableDialogBuilder setCheckedItems(int checkedItems) {
            mCheckedItems = checkedItems;
            return this;
        }

        /**
         * 设置被选中的菜单项的下标
         *
         * @param checkedIndexes 被选中的菜单项的下标组成的数组,如 [1,3] 表示选中下标为 1、3 的菜单项
         */
        public MultiCheckableDialogBuilder setCheckedItems(int[] checkedIndexes) {
            int checkedItemRecord = 0;
            for (int checkedIndexe : checkedIndexes) {
                checkedItemRecord += 2 << (checkedIndexe);
            }
            return setCheckedItems(checkedItemRecord);
        }

        /**
         * 添加菜单项
         *
         * @param items    所有菜单项的文字
         * @param listener 菜单项的点击事件,可以在点击事件里调用 {@link #setCheckedItems(int[])}} 来设置选中某些菜单项
         */
        public MultiCheckableDialogBuilder addItems(CharSequence[] items, OnClickListener listener) {
            for (CharSequence item : items) {
                addItem(new MODialogMenuItemView.CheckItemView(mContext, true, item), listener);
            }
            return this;
        }

        @Override
        protected void onCreateContent(MODialog dialog, ViewGroup parent) {
            super.onCreateContent(dialog, parent);
            for (int i = 0; i < mMenuItemViews.size(); i++) {
                MODialogMenuItemView itemView = mMenuItemViews.get(i);
                int v = 2 << i;
                itemView.setChecked((v & mCheckedItems) == v);
            }
        }

        @Override
        protected void onItemClick(int index) {
            MODialogMenuItemView itemView = mMenuItemViews.get(index);
            itemView.setChecked(!itemView.isChecked());
        }

        /**
         * @return 被选中的菜单项的下标 <b>注意: 如果选中的是1，3项(以0开始)，因为 (2<<1) + (2<<3) = 20</b>
         */
        public int getCheckedItemRecord() {
            int output = 0;
            int length = mMenuItemViews.size();

            for (int i = 0; i < length; i++) {
                MODialogMenuItemView itemView = mMenuItemViews.get(i);
                if (itemView.isChecked()) {
                    output += 2 << itemView.getMenuIndex();
                }
            }
            mCheckedItems = output;
            return output;
        }

        /**
         * @return 被选中的菜单项的下标数组。如果选中的是1，3项(以0开始)，则返回[1,3]
         */
        public int[] getCheckedItemIndexes() {
            ArrayList<Integer> array = new ArrayList<>();
            int length = mMenuItemViews.size();

            for (int i = 0; i < length; i++) {
                MODialogMenuItemView itemView = mMenuItemViews.get(i);
                if (itemView.isChecked()) {
                    array.add(itemView.getMenuIndex());
                }
            }
            int[] output = new int[array.size()];
            for (int i = 0; i < array.size(); i++) {
                output[i] = array.get(i);
            }
            return output;
        }

        protected boolean existCheckedItem() {
            return getCheckedItemRecord() <= 0;
        }
    }

    /**
     * 自定义对话框内容区域的 Builder
     */
    public static class CustomDialogBuilder extends MODialogBuilder {

        private int mLayoutId;

        public CustomDialogBuilder(Context context) {
            super(context);
        }

        /**
         * 设置内容区域的 layoutResId
         */
        public CustomDialogBuilder setLayout(@LayoutRes int layoutResId) {
            mLayoutId = layoutResId;
            return this;
        }

        @Override
        protected void onCreateContent(MODialog dialog, ViewGroup parent) {
            parent.addView(LayoutInflater.from(mContext).inflate(mLayoutId, parent, false));
        }
    }

    /**
     * 随键盘升降自动调整 Dialog 高度的 Builder
     */
    public static abstract class AutoResizeDialogBuilder extends MODialogBuilder {

        private ScrollView mScrollerView;

        private int mAnchorHeight = 0;
        private int mScreenHeight = 0;
        private int mScrollHeight = 0;

        public AutoResizeDialogBuilder(Context context) {
            super(context);
        }

        @Override
        protected void onCreateContent(MODialog dialog, ViewGroup parent) {
            mScrollerView = new ScrollView(mContext);
            mScrollerView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, onGetScrollHeight()));
            mScrollerView.addView(onBuildContent(dialog, mScrollerView));
            parent.addView(mScrollerView);
        }

        @Override
        protected void onAfter(MODialog dialog, LinearLayout parent) {
            super.onAfter(dialog, parent);
            bindEvent();
        }

        public abstract View onBuildContent(MODialog dialog, ScrollView parent);

        public int onGetScrollHeight() {
            return ScrollView.LayoutParams.WRAP_CONTENT;
        }

        private void bindEvent() {
            mAnchorTopView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                }
            });
            mAnchorBottomView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                }
            });
            mRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                    //noinspection ConstantConditions
                    View mDecor = mDialog.getWindow().getDecorView();
                    Rect r = new Rect();
                    mDecor.getWindowVisibleDisplayFrame(r);
                    mScreenHeight = MODisplayHelper.getScreenHeight(mContext);
                    int anchorShouldHeight = mScreenHeight - r.bottom;
                    if (anchorShouldHeight != mAnchorHeight) {
                        mAnchorHeight = anchorShouldHeight;
                        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mAnchorBottomView.getLayoutParams();
                        lp.height = mAnchorHeight;
                        mAnchorBottomView.setLayoutParams(lp);
                        LinearLayout.LayoutParams slp = (LinearLayout.LayoutParams) mScrollerView.getLayoutParams();
                        if (onGetScrollHeight() == ViewGroup.LayoutParams.WRAP_CONTENT) {
                            mScrollHeight = Math.max(mScrollHeight, mScrollerView.getMeasuredHeight());
                        } else {
                            mScrollHeight = onGetScrollHeight();
                        }
                        if (mAnchorHeight == 0) {
                            slp.height = mScrollHeight;
                        } else {
                            mScrollerView.getChildAt(0).requestFocus();
                            slp.height = mScrollHeight - mAnchorHeight;
                        }
                        mScrollerView.setLayoutParams(slp);
                    } else {
                        //如果内容过高,anchorShouldHeight=0,但实际下半部分会被截断,因此需要保护
                        //由于高度超过后,actionContainer并不会去测量和布局,所以这里拿不到action的高度,因此用比例估算一个值
                        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mDialogWrapper.getLayoutParams();
                        int dialogLayoutMaxHeight = mScreenHeight - lp.bottomMargin - lp.topMargin - r.top;
                        int scrollLayoutHeight = mScrollerView.getMeasuredHeight();
                        if (scrollLayoutHeight > dialogLayoutMaxHeight * 0.8) {
                            mScrollHeight = (int) (dialogLayoutMaxHeight * 0.8);
                            LinearLayout.LayoutParams slp = (LinearLayout.LayoutParams) mScrollerView.getLayoutParams();
                            slp.height = mScrollHeight;
                            mScrollerView.setLayoutParams(slp);
                        }
                    }
                }
            });
        }
    }
}
