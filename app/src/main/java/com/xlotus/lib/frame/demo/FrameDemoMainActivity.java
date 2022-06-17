package com.xlotus.lib.frame.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.xlotus.lib.core.utils.ui.SafeToast;
import com.xlotus.lib.frame.activity.BaseBindingActivity;
import com.xlotus.lib.frame.demo.databinding.ActivityMainBinding;
import com.xlotus.lib.frame.utils.StatusBarUtil;

public class FrameDemoMainActivity extends BaseBindingActivity<ActivityMainBinding> implements View.OnClickListener {

    public ActivityMainBinding getBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.btnJump.setOnClickListener(this);
        binding.btnTest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == binding.btnJump.getId()) {
            startActivity(new Intent(this, FrameTitleActivity.class));
        }
        else if (id == binding.btnTest.getId()) {
            SafeToast.showToast("Test", Toast.LENGTH_SHORT);
        }
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageView(this, 0, binding.btnJump);
        StatusBarUtil.setLightMode(this);
    }
}
