package com.eagle.net.rx.adapter;


import com.eagle.net.adapter.AdapterParam;
import com.eagle.net.adapter.Call;
import com.eagle.net.adapter.CallAdapter;
import com.eagle.net.model.Response;

import io.reactivex.Single;

public class SingleResponse<T> implements CallAdapter<T, Single<Response<T>>> {
    @Override
    public Single<Response<T>> adapt(Call<T> call, AdapterParam param) {
        ObservableResponse<T> observable = new ObservableResponse<>();
        return observable.adapt(call, param).singleOrError();
    }
}
