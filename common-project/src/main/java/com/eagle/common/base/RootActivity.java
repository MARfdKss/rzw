package com.eagle.common.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;


public abstract class RootActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setWindowAnimations(getAnimationId());
    }

    @Override
    protected void onStart() {
        super.onStart();
        RootApplication.getInstance().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        RootApplication.getInstance().removeActivity(this);
        super.onDestroy();

    }

    protected int getAnimationId() {
        return R.style.ActivityAnimation;
    }

    protected abstract void findById();

    protected abstract void setListener();

    protected abstract void initView();

    protected abstract int getContentView();
}
