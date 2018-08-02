package github.hotstu.naiue.widget.recycler;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


public class MOCommonViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mViews;

    public MOCommonViewHolder(View itemView) {
        super(itemView);
        mViews = new SparseArray<>();
    }

    private <T extends View> T findViewById(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getView(int viewId) {
        return findViewById(viewId);
    }

    public TextView getTextView(int viewId) {
        return (TextView) getView(viewId);
    }

    public Button getButton(int viewId) {
        return (Button) getView(viewId);
    }

    public ImageView getImageView(int viewId) {
        return (ImageView) getView(viewId);
    }

    public ImageButton getImageButton(int viewId) {
        return (ImageButton) getView(viewId);
    }

    public EditText getEditText(int viewId) {
        return (EditText) getView(viewId);
    }

    public MOCommonViewHolder setText(int viewId, String value) {
        TextView view = findViewById(viewId);
        view.setText(value);
        return this;
    }

    public void setTag(Object  tag) {
        this.itemView.setTag(tag);
    }

    public Object  getTag() {
        return this.itemView.getTag();
    }

    public MOCommonViewHolder setBackground(int viewId, int resId) {
        View view = findViewById(viewId);
        view.setBackgroundResource(resId);
        return this;
    }

    public MOCommonViewHolder setClickListener(int viewId, View.OnClickListener listener) {
        if (viewId == 0) {
            this.itemView.setOnClickListener(listener);
        } else {
            View view = findViewById(viewId);
            view.setOnClickListener(listener);
        }
        return this;
    }

    public MOCommonViewHolder setClickListener( View.OnClickListener listener) {
        return this.setClickListener(0, listener);
    }
}
