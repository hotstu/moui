package io.github.hotstu.moui.widget.recycler;


import android.util.SparseArray;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

public class BindingViewHolder<T extends ViewDataBinding> extends RecyclerView.ViewHolder {
    private T binding;
    private SparseArray<Object> items;

    public BindingViewHolder(T binding) {
        super(binding.getRoot());
        this.binding = binding;
        items = new SparseArray<>();
    }

    public T getBinding() {
        return  binding;
    }

    public void setItem(int variableId, Object value) {
        items.put(variableId, value);
        binding.setVariable(variableId, value);
    }

    public void executePendingBindings() {
        binding.executePendingBindings();
    }

    public Object getItem(int variableId) {
        return items.get(variableId);
    }
}
