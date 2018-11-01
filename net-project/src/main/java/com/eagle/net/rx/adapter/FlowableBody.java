package com.eagle.net.rx.adapter;


import com.eagle.net.adapter.AdapterParam;
import com.eagle.net.adapter.Call;
import com.eagle.net.adapter.CallAdapter;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

public class FlowableBody<T> implements CallAdapter<T, Flowable<T>> {
    @Override
    public Flowable<T> adapt(Call<T> call, AdapterParam param) {
        ObservableBody<T> observable = new ObservableBody<>();
        return observable.adapt(call, param).toFlowable(BackpressureStrategy.LATEST);
    }
}
