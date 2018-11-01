package com.eagle.net.rx.adapter;


import com.eagle.net.adapter.AdapterParam;
import com.eagle.net.adapter.Call;
import com.eagle.net.adapter.CallAdapter;

import io.reactivex.Maybe;

public class MaybeBody<T> implements CallAdapter<T, Maybe<T>> {
    @Override
    public Maybe<T> adapt(Call<T> call, AdapterParam param) {
        ObservableBody<T> observable = new ObservableBody<>();
        return observable.adapt(call, param).singleElement();
    }
}
