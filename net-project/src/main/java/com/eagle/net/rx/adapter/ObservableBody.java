package com.eagle.net.rx.adapter;


import com.eagle.net.adapter.AdapterParam;
import com.eagle.net.adapter.Call;
import com.eagle.net.adapter.CallAdapter;
import com.eagle.net.model.Response;
import com.eagle.net.rx.observable.BodyObservable;

import io.reactivex.Observable;

public class ObservableBody<T> implements CallAdapter<T, Observable<T>> {
    @Override
    public Observable<T> adapt(Call<T> call, AdapterParam param) {
        Observable<Response<T>> observable = AnalysisParams.analysis(call, param);
        return new BodyObservable<>(observable);
    }
}
