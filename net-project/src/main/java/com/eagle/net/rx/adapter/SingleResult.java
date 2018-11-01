package com.eagle.net.rx.adapter;


import com.eagle.net.adapter.AdapterParam;
import com.eagle.net.adapter.Call;
import com.eagle.net.adapter.CallAdapter;
import com.eagle.net.model.Result;

import io.reactivex.Single;

public class SingleResult<T> implements CallAdapter<T, Single<Result<T>>> {
    @Override
    public Single<Result<T>> adapt(Call<T> call, AdapterParam param) {
        ObservableResult<T> observable = new ObservableResult<>();
        return observable.adapt(call, param).singleOrError();
    }
}
