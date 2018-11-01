package com.eagle.common.mvp;


import java.lang.ref.WeakReference;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BasePresenter<M extends IModel, V extends IView> implements IPresenter<M, V> {
    /**
     * 使用弱引用来防止内存泄漏
     */
    private WeakReference<V> wrf;
    protected M model;
    private CompositeDisposable compositeDisposable;

    @Override
    public void registerModel(M model) {
        this.model = model;
    }

    @Override
    public void registerView(V view) {
        wrf = new WeakReference<>(view);
    }

    @Override
    public V getView() {
        return wrf == null ? null : wrf.get();
    }

    /**
     * 在Activity或Fragment卸载时调用View结束的方法
     */
    @Override
    public void destroy() {
        dispose();
        if (wrf != null) {
            wrf.clear();
            wrf = null;
        }
    }

    @Override
    public void addDisposable(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

    @Override
    public void dispose() {
        if (compositeDisposable != null){
            compositeDisposable.dispose();
        }
    }
}
