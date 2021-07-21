package io.github.hotstu.moui.widget.recycler;

import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

public interface MOAdapterDelegate<T extends MOTypedRecyclerAdapter> {
    RecyclerView.ViewHolder onCreateViewHolder(T adapter, ViewGroup parent);

    void onBindViewHolder(T adapter, RecyclerView.ViewHolder holder, Object data);

    boolean isDelegateOf(Class<?> clazz, Object item, int position);
}