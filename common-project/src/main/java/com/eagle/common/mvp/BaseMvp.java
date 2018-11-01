package com.eagle.common.mvp;


public interface BaseMvp<M extends IModel, V extends IView, P extends BasePresenter> {
    M createModel();

    V createView();

    P createPresenter();
}
