package test.com.myapplication.view;

import android.content.Context;

import com.eagle.common.mvp.IView;

/**
 * Created by wen on 2018/10/29.
 */

public interface MainView extends IView {
    Context getContext();
    void showLoadingDialog(String msg);
    void hideLoadingDialog();
}
