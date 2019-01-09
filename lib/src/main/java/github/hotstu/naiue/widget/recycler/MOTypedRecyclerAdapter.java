package github.hotstu.naiue.widget.recycler;


import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class MOTypedRecyclerAdapter extends RecyclerView.Adapter {

    public interface AdapterDelegate {
        RecyclerView.ViewHolder onCreateViewHolder(MOTypedRecyclerAdapter adapter, ViewGroup parent);
        void onBindViewHolder(MOTypedRecyclerAdapter adapter, RecyclerView.ViewHolder holder, Object data);
        boolean isDelegateOf(Class<?> clazz, Object item, int position);
    }
    private static final String TAG = "MOTypedRecyclerAdapter";
    final List mList;
    final List<AdapterDelegate> delegates;

    AdapterDelegate defaultDelegate = new AdapterDelegate() {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull MOTypedRecyclerAdapter adapter, @NonNull ViewGroup parent) {
            TextView v = new TextView(parent.getContext());
            v.setText("不支持的数据类型，请检查adapter配置");
            return new MOCommonViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull MOTypedRecyclerAdapter adapter, @NonNull RecyclerView.ViewHolder holder, @NonNull Object data) {

        }

        @Override
        public boolean isDelegateOf(@NonNull Class clazz, @NonNull Object item, int position) {
            return false;
        }
    };


    public MOTypedRecyclerAdapter() {
        mList = new ArrayList();
        delegates = new ArrayList<>();
    }

    public void addDelegate(AdapterDelegate delegate) {
        delegates.add(delegate);
    }

    public void addDelegate(int index, AdapterDelegate delegate) {
        delegates.add(index, delegate);
    }

    public AdapterDelegate getDelegate(int index) {
        return delegates.get(index);
    }

    public int getDelegateSize() {
        return delegates.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == -1) {
            return defaultDelegate.onCreateViewHolder(this, parent);
        }
        return delegates.get(viewType).onCreateViewHolder(this, parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = holder.getItemViewType();
        if(viewType == -1) {
            defaultDelegate.onBindViewHolder(this, holder, getItem(position));
            return;
        }
        delegates.get(viewType).onBindViewHolder(this, holder, getItem(position));
    }

    @Override
    public int getItemViewType(int position) {
        Object item = getItem(position);
        Class<?> type = item.getClass();
        //Log.d(TAG, "item.class=" + type.getCanonicalName());
        for (int i = 0; i < delegates.size(); i++) {
            AdapterDelegate yhTypedAdapterDelegate = delegates.get(i);
            if (yhTypedAdapterDelegate.isDelegateOf(type, item, position)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public final void setDataSet(List list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public final void clearDataSet() {
        notifyItemRangeRemoved(0, mList.size());
        mList.clear();
    }

    public final Object getItem(int position) {
        return mList.get(position);
    }

    public final void addItem(Object object) {
        mList.add(object);
        notifyItemInserted(mList.size() - 1);
    }

    public final void addItem(Object object, int position) {
        mList.add(position, object);
        notifyItemInserted(position);
    }

    public final void addItems(List list) {
        int start = mList.size();
        mList.addAll(list);
        notifyItemRangeInserted(start, list.size());
    }

    public final void removeItem(int position) {
        if (position == RecyclerView.NO_POSITION) {
            Log.w(TAG, "removeItem with index = NO_POSITION ");
            return;
        }
        mList.remove(position);
        notifyItemRemoved(position);
    }

    public final int findItem(Object item) {
        int position = RecyclerView.NO_POSITION;
        for (int i = 0; i < mList.size(); i++) {
            if (item.equals(mList.get(i))) {
                position = i;
                break;
            }
        }
        return position;
    }

    public final void removeItem(Object item) {
        int position = findItem(item);
        if (position != RecyclerView.NO_POSITION) {
            mList.remove(position);
            notifyItemRemoved(position);
        }
    }



    public final void moveItem(int fromPostion, int toPositon) {
        //Log.d(TAG, "moveItem() called with: " + "fromPostion = [" + fromPostion + "], toPositon = [" + toPositon + "]");
        Collections.swap(mList, fromPostion, toPositon);
        notifyItemMoved(fromPostion, toPositon);
    }
}