package com.xlotus.lib.frame.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

public abstract class BaseBindingFragment<T extends ViewBinding> extends BaseFragment {

    protected T binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = getContentBinding(inflater, container);
        initView();
        return binding.getRoot();
    }

    public final void initView(View view) {

    }

    public final int getContentLayoutId() {
        return 0;
    }

    protected abstract T getContentBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container);
    protected abstract void initView();
}
