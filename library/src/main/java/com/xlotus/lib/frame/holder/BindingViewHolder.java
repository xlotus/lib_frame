package com.xlotus.lib.frame.holder;

import android.view.ViewGroup;

import androidx.viewbinding.ViewBinding;

import com.bumptech.glide.RequestManager;
import com.xlotus.lib.frame.viewtracker.ImpressionInterface;
import com.xlotus.lib.imageloader.GlideUtils;

/**
 *
 * @param <VB> 布局的ViewBinding
 */
public class BindingViewHolder<T, VB extends ViewBinding> extends BaseRecyclerViewHolder<T> implements ImpressionInterface {
    private VB binding;

    public BindingViewHolder(ViewGroup parent, VB t) {
        this(parent, t, GlideUtils.getRequestManager(parent.getContext()));
    }

    public BindingViewHolder(ViewGroup parent, VB t, RequestManager requestManager) {
        super(t.getRoot(), requestManager);
        binding = t;
    }

    public VB getBinding() {
        return binding;
    }
}
