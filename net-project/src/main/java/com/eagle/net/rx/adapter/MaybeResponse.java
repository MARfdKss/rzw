package com.eagle.net.rx.adapter;


import com.eagle.net.adapter.AdapterParam;
import com.eagle.net.adapter.Call;
import com.eagle.net.adapter.CallAdapter;
import com.eagle.net.model.Response;

import io.reactivex.Maybe;

public class MaybeResponse<T> implements CallAdapter<T, Maybe<Response<T>>> {
    @Override
    public Maybe<Response<T>> adapt(Call<T> call, AdapterParam param) {
        ObservableResponse<T> observable = new ObservableResponse<>();
        return observable.adapt(call, param).singleElement();
    }
}
