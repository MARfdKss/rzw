package com.eagle.net.rx.adapter;


import com.eagle.net.adapter.AdapterParam;
import com.eagle.net.adapter.Call;
import com.eagle.net.adapter.CallAdapter;
import com.eagle.net.model.Response;
import com.eagle.net.model.Result;
import com.eagle.net.rx.observable.ResultObservable;

import io.reactivex.Observable;

public class ObservableResult<T> implements CallAdapter<T, Observable<Result<T>>> {
    @Override
    public Observable<Result<T>> adapt(Call<T> call, AdapterParam param) {
        Observable<Response<T>> observable = AnalysisParams.analysis(call, param);
        return new ResultObservable<>(observable);
    }
}
