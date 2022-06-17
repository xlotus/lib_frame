package com.xlotus.lib.frame.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

import com.xlotus.lib.frame.activity.BaseTitleActivity;
import com.xlotus.lib.frame.demo.databinding.ActivityMainBinding;
import com.xlotus.lib.core.utils.ui.SafeToast;

public abstract class BindingTitleActivity<T extends ViewBinding> extends BaseTitleActivity implements View.OnClickListener {

    protected T binding;

    public abstract T getBinding();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getBinding();
        setContentView(binding.getRoot());
    }

    @Override
    protected void onLeftButtonClick() {
        finish();
    }

    @Override
    protected void onRightButtonClick() {

    }
}
