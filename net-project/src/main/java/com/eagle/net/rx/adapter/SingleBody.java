package com.eagle.net.rx.adapter;


import com.eagle.net.adapter.AdapterParam;
import com.eagle.net.adapter.Call;
import com.eagle.net.adapter.CallAdapter;

import io.reactivex.Single;

public class SingleBody<T> implements CallAdapter<T, Single<T>> {
    @Override
    public Single<T> adapt(Call<T> call, AdapterParam param) {
        ObservableBody<T> observable = new ObservableBody<>();
        return observable.adapt(call, param).singleOrError();
    }
}
