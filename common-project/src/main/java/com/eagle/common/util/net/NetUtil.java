package com.eagle.common.util.net;


import android.support.annotation.NonNull;

import com.google.gson.reflect.TypeToken;
import com.eagle.net.NetManager;
import com.eagle.net.callback.FileCallback;
import com.eagle.net.callback.StringCallback;
import com.eagle.net.convert.JsonConvert;
import com.eagle.net.model.HttpHeaders;
import com.eagle.net.model.NetResponse;
import com.eagle.net.model.Progress;
import com.eagle.net.model.Response;
import com.eagle.net.rx.adapter.ObservableBody;
import com.eagle.net.utils.Convert;
import com.eagle.net.utils.EncryptUtil;

import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class NetUtil {

    /**
     * get 请求
     */
    public static <T> void getRequest(String url, Map<String, String> params, Observer<? super T> observer) {
        getRequest(url, null, params, observer);
    }

    /**
     * get 请求
     */
    public static <T> void getRequest(String url, HttpHeaders headers, Map<String, String> params,
                                      Observer<? super T> observer) {

        NetManager.<NetResponse<Object>>get(url)
                .headers(getHeaders(headers))
                .params(getParams(params))
                .converter(new JsonConvert<NetResponse<Object>>() {
                })
                .adapt(new ObservableBody<NetResponse<Object>>())
                .subscribeOn(Schedulers.io())
                .map(new Function<NetResponse<Object>, T>() {
                    @Override
                    public T apply(@NonNull NetResponse<Object> response) throws Exception {
                        String json = Convert.toJson(response.data);
                        Type type = ((ParameterizedType) new TypeToken<T>() {
                        }.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
                        return Convert.fromJson(json, type);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * post请求
     */
    public static <T> void postRequest(String url, Map<String, String> params, Observer<? super T> observer) {
        postRequest(url, null, params, observer);
    }

    /**
     * post请求
     */
    public static <T> void postRequest(String url, HttpHeaders headers, Map<String, String> params,
                                       Observer<? super T> observer) {
        NetManager.<NetResponse<Object>>post(url)
                .headers(getHeaders(headers))
                .params(getParams(params))
                .converter(new JsonConvert<NetResponse<Object>>() {
                })
                .adapt(new ObservableBody<NetResponse<Object>>())
                .subscribeOn(Schedulers.io())
                .map(new Function<NetResponse<Object>, T>() {
                    @Override
                    public T apply(@NonNull NetResponse<Object> response) throws Exception {
                        String json = Convert.toJson(response.data);
                        Type type = ((ParameterizedType) new TypeToken<T>() {
                        }.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
                        return Convert.fromJson(json, type);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * post json请求
     */
    public static <T> void postJsonRequest(String url, Map<String, String> params, Observer<? super T> observer) {
        postJsonRequest(url, null, params, observer);
    }

    /**
     * post json请求
     */
    public static <T> void postJsonRequest(String url, HttpHeaders headers, Map<String, String> params,
                                           Observer<? super T> observer) {
        NetManager.<NetResponse<Object>>post(url)
                .headers(getHeaders(headers))
                .upJson(Convert.toJson(getParams(params)))
                .converter(new JsonConvert<NetResponse<Object>>() {
                })
                .adapt(new ObservableBody<NetResponse<Object>>())
                .subscribeOn(Schedulers.io())
                .map(new Function<NetResponse<Object>, T>() {
                    @Override
                    public T apply(@NonNull NetResponse<Object> response) throws Exception {
                        String json = Convert.toJson(response.data);
                        Type type = ((ParameterizedType) new TypeToken<T>() {
                        }.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
                        return Convert.fromJson(json, type);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 下载
     */
    public static void download(final String url, String dirPath, String fileName, final HttpHeaders headers, final Map<String, String> params, Observer<Progress> observer) {
        Observable.create(new ObservableOnSubscribe<Progress>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<Progress> e) throws Exception {
                NetManager.<File>get(url)
                        .headers(headers)
                        .params(params)
                        .execute(new FileCallback() {
                            @Override
                            public void onSuccess(Response<File> response) {
                                e.onComplete();
                            }

                            @Override
                            public void onError(Response<File> response) {
                                e.onError(response.getException());
                            }

                            @Override
                            public void downloadProgress(Progress progress) {
                                e.onNext(progress);
                            }
                        });
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 上传
     */
    public static void upload(final String url, final HttpHeaders headers, final Map<String, String> params, List<String> fileList, Observer<? super Progress> observer) {
        final ArrayList<File> files = new ArrayList<>();
        if (fileList != null && fileList.size() > 0) {
            for (int i = 0; i < fileList.size(); i++) {
                files.add(new File(fileList.get(i)));
            }
        }

        Observable.create(new ObservableOnSubscribe<Progress>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<Progress> e) throws Exception {
                NetManager.<String>post(url)
                        .tag(this)
                        .headers(headers)
                        .params(params)
                        .addFileParams("file", files)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                e.onComplete();
                            }

                            @Override
                            public void onError(Response<String> response) {
                                e.onError(response.getException());
                            }

                            @Override
                            public void uploadProgress(Progress progress) {
                                e.onNext(progress);
                            }
                        });
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    private String getUrl(String domain, String pathUrl) {
        return domain + pathUrl;
    }

    private static HttpHeaders getHeaders(HttpHeaders headers){
        if(headers == null){
            headers = new HttpHeaders();
        }
        //公共header
        headers.put("a", "aaaaa");
        headers.put("b", "bbbbb");
        return headers;
    }

    private static Map<String, String> getParams(Map<String, String> params){
        if(params == null){
            params = new HashMap<>();
        }
        return EncryptUtil.sign(params);
    }

}
