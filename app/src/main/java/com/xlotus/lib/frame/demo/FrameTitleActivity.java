package com.xlotus.lib.frame.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.xlotus.lib.core.utils.ui.SafeToast;
import com.xlotus.lib.frame.demo.databinding.ActivityMainBinding;
import com.xlotus.lib.frame.demo.databinding.ActivityTitleDemoBinding;
import com.xlotus.lib.frame.utils.StatusBarUtil;

public class FrameTitleActivity extends BindingTitleActivity<ActivityTitleDemoBinding> implements View.OnClickListener {

    public ActivityTitleDemoBinding getBinding() {
        return ActivityTitleDemoBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleText(R.string.app_name);
        binding.btnTest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == binding.btnTest.getId()) {
            SafeToast.showToast("Test", Toast.LENGTH_SHORT);
        }
    }
}
