package com.eagle.common.util.net;

import com.eagle.common.mvp.IPresenter;
import com.eagle.common.mvp.IView;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class NetObserver<T> implements Observer<T> {

    private IPresenter presenter;

    public NetObserver(IPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onSubscribe(Disposable d) {
        if (presenter != null) {
            presenter.addDisposable(d);
        }
    }

    @Override
    public void onNext(T t) {
        if (isViewDestroy()) {
            return;
        }
        onSuccess(t);
    }

    @Override
    public void onError(Throwable e) {
        if (isViewDestroy()) {
            return;
        }
        onFailure(e);
    }

    @Override
    public void onComplete() {

    }

    private boolean isViewDestroy() {
        if (presenter == null) {
            return true;
        } else {
            IView IView = presenter.getView();
            return IView == null;
        }
    }

    public abstract void onSuccess(T t);

    public abstract void onFailure(Throwable e);

}
