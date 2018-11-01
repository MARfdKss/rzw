package com.eagle.net.rx.adapter;


import com.eagle.net.adapter.AdapterParam;
import com.eagle.net.adapter.Call;
import com.eagle.net.model.Response;
import com.eagle.net.rx.observable.CallEnqueueObservable;
import com.eagle.net.rx.observable.CallExecuteObservable;

import io.reactivex.Observable;

class AnalysisParams {

    static <T> Observable<Response<T>> analysis(Call<T> call, AdapterParam param) {
        Observable<Response<T>> observable;
        if (param == null) param = new AdapterParam();
        if (param.isAsync) {
            observable = new CallEnqueueObservable<>(call);
        } else {
            observable = new CallExecuteObservable<>(call);
        }
        return observable;
    }
}
