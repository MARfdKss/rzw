package com.eagle.common.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import com.eagle.common.base.R;


public class LoadingDialog extends Dialog {
    private TextView mLoadingTv;
    private final ImageView loadingImg;

    public LoadingDialog(@NonNull Context context) {
        super(context, R.style.AlertDialogStyle);
        getWindow().setDimAmount(0);
        //点击屏幕或物理返回键,false不消失
        setCancelable(false);
        //点击屏幕
        setCanceledOnTouchOutside(false);

        setContentView(R.layout.dialog_loading);
        mLoadingTv = (TextView) findViewById(R.id.loading_tv);
        loadingImg = (ImageView) findViewById(R.id.progress_iv);
    }

    public void show(String msg) {
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.loading_rotate);
        loadingImg.startAnimation(animation);

        mLoadingTv.setText(msg);

        show();
    }
}
