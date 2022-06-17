package com.xlotus.lib.frame.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

public abstract class BaseBindingActivity<T extends ViewBinding> extends BaseActivity {

    protected T binding;

    protected abstract T getBinding();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getBinding();
        setContentView(binding.getRoot());
    }
}
