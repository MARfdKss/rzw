package com.eagle.net.rx.adapter;

import com.eagle.net.adapter.AdapterParam;
import com.eagle.net.adapter.Call;
import com.eagle.net.adapter.CallAdapter;
import com.eagle.net.model.Response;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

public class FlowableResponse<T> implements CallAdapter<T, Flowable<Response<T>>> {
    @Override
    public Flowable<Response<T>> adapt(Call<T> call, AdapterParam param) {
        ObservableResponse<T> observable = new ObservableResponse<>();
        return observable.adapt(call, param).toFlowable(BackpressureStrategy.LATEST);
    }
}
