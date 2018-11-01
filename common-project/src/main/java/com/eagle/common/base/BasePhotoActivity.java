package com.eagle.common.base;

import android.content.Intent;
import android.os.Bundle;
import com.eagle.common.mvp.BasePresenter;
import com.eagle.common.mvp.IModel;
import com.eagle.common.mvp.IView;
import com.eagle.photo.app.TakePhoto;
import com.eagle.photo.app.TakePhotoImpl;
import com.eagle.photo.model.InvokeParam;
import com.eagle.photo.model.TContextWrap;
import com.eagle.photo.model.TResult;
import com.eagle.photo.permission.InvokeListener;
import com.eagle.photo.permission.PermissionManager;
import com.eagle.photo.permission.TakePhotoInvocationHandler;


/**
 * 继承这个类来让Activity获取拍照的能力<br>
 */
public abstract class BasePhotoActivity<M extends IModel, V extends IView, P extends BasePresenter> extends BaseActivity<M, V, P>implements TakePhoto.TakeResultListener, InvokeListener {
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);
    }

    /**
     * 获取TakePhoto实例
     */
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }

    @Override
    public void takeSuccess(TResult result) {

    }

    @Override
    public void takeFail(TResult result, String msg) {

    }

    @Override
    public void takeCancel() {

    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

}
