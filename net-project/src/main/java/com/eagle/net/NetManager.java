package com.eagle.net;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.eagle.net.cache.CacheEntity;
import com.eagle.net.cache.CacheMode;
import com.eagle.net.cookie.CookieJarImpl;
import com.eagle.net.https.HttpsUtils;
import com.eagle.net.interceptor.HttpLoggingInterceptor;
import com.eagle.net.model.HttpHeaders;
import com.eagle.net.model.HttpParams;
import com.eagle.net.request.DeleteRequest;
import com.eagle.net.request.GetRequest;
import com.eagle.net.request.HeadRequest;
import com.eagle.net.request.OptionsRequest;
import com.eagle.net.request.PatchRequest;
import com.eagle.net.request.PostRequest;
import com.eagle.net.request.PutRequest;
import com.eagle.net.request.TraceRequest;
import com.eagle.net.utils.HttpUtils;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.Call;
import okhttp3.OkHttpClient;

/**
 * 网络请求的入口类
 */
public class NetManager {
    public static final long DEFAULT_MILLISECONDS = 10000;      //默认的超时时间
    public static long REFRESH_TIME = 300;                      //回调刷新时间（单位ms）

    private Application context;            //全局上下文
    private Handler mDelivery;              //用于在主线程执行的调度器
    private OkHttpClient okHttpClient;      //ok请求的客户端
    private HttpParams mCommonParams;       //全局公共请求参数
    private HttpHeaders mCommonHeaders;     //全局公共请求头
    private int mRetryCount;                //全局超时重试次数
    private CacheMode mCacheMode;           //全局缓存模式
    private long mCacheTime;                //全局缓存过期时间,默认永不过期

    private NetManager() {
        mDelivery = new Handler(Looper.getMainLooper());
        mRetryCount = 3;
        mCacheTime = CacheEntity.CACHE_NEVER_EXPIRE;
        mCacheMode = CacheMode.NO_CACHE;

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("NetManager");
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
        loggingInterceptor.setColorLevel(Level.INFO);
        builder.addInterceptor(loggingInterceptor);

        builder.readTimeout(NetManager.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        builder.writeTimeout(NetManager.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        builder.connectTimeout(NetManager.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);

        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory();
        builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        builder.hostnameVerifier(HttpsUtils.UnSafeHostnameVerifier);
        okHttpClient = builder.build();
    }

    public static NetManager getInstance() {
        return OkGoHolder.holder;
    }

    private static class OkGoHolder {
        private static NetManager holder = new NetManager();
    }

    /** get请求 */
    public static <T> GetRequest<T> get(String url) {
        return new GetRequest<>(url);
    }

    /** post请求 */
    public static <T> PostRequest<T> post(String url) {
        return new PostRequest<>(url);
    }

    /** put请求 */
    public static <T> PutRequest<T> put(String url) {
        return new PutRequest<>(url);
    }

    /** head请求 */
    public static <T> HeadRequest<T> head(String url) {
        return new HeadRequest<>(url);
    }

    /** delete请求 */
    public static <T> DeleteRequest<T> delete(String url) {
        return new DeleteRequest<>(url);
    }

    /** options请求 */
    public static <T> OptionsRequest<T> options(String url) {
        return new OptionsRequest<>(url);
    }

    /** patch请求 */
    public static <T> PatchRequest<T> patch(String url) {
        return new PatchRequest<>(url);
    }

    /** trace请求 */
    public static <T> TraceRequest<T> trace(String url) {
        return new TraceRequest<>(url);
    }

    /** 必须在全局Application先调用，获取context上下文，否则缓存无法使用 */
    public NetManager init(Application app) {
        context = app;
        return this;
    }

    /** 获取全局上下文 */
    public Context getContext() {
        HttpUtils.checkNotNull(context, "please call NetManager.getInstance().init() first in application!");
        return context;
    }

    public Handler getDelivery() {
        return mDelivery;
    }

    public OkHttpClient getOkHttpClient() {
        HttpUtils.checkNotNull(okHttpClient, "please call NetManager.getInstance().setOkHttpClient() first in application!");
        return okHttpClient;
    }

    /** 必须设置 */
    public NetManager setOkHttpClient(OkHttpClient okHttpClient) {
        HttpUtils.checkNotNull(okHttpClient, "okHttpClient == null");
        this.okHttpClient = okHttpClient;
        return this;
    }

    /** 获取全局的cookie实例 */
    public CookieJarImpl getCookieJar() {
        return (CookieJarImpl) okHttpClient.cookieJar();
    }

    /** 超时重试次数 */
    public NetManager setRetryCount(int retryCount) {
        if (retryCount < 0) throw new IllegalArgumentException("retryCount must > 0");
        mRetryCount = retryCount;
        return this;
    }

    /** 超时重试次数 */
    public int getRetryCount() {
        return mRetryCount;
    }

    /** 全局的缓存模式 */
    public NetManager setCacheMode(CacheMode cacheMode) {
        mCacheMode = cacheMode;
        return this;
    }

    /** 获取全局的缓存模式 */
    public CacheMode getCacheMode() {
        return mCacheMode;
    }

    /** 全局的缓存过期时间 */
    public NetManager setCacheTime(long cacheTime) {
        if (cacheTime <= -1) cacheTime = CacheEntity.CACHE_NEVER_EXPIRE;
        mCacheTime = cacheTime;
        return this;
    }

    /** 获取全局的缓存过期时间 */
    public long getCacheTime() {
        return mCacheTime;
    }

    /** 获取全局公共请求参数 */
    public HttpParams getCommonParams() {
        return mCommonParams;
    }

    /** 添加全局公共请求参数 */
    public NetManager addCommonParams(HttpParams commonParams) {
        if (mCommonParams == null) mCommonParams = new HttpParams();
        mCommonParams.put(commonParams);
        return this;
    }

    /** 获取全局公共请求头 */
    public HttpHeaders getCommonHeaders() {
        return mCommonHeaders;
    }

    /** 添加全局公共请求参数 */
    public NetManager addCommonHeaders(HttpHeaders commonHeaders) {
        if (mCommonHeaders == null) mCommonHeaders = new HttpHeaders();
        mCommonHeaders.put(commonHeaders);
        return this;
    }

    /** 根据Tag取消请求 */
    public void cancelTag(Object tag) {
        if (tag == null) return;
        for (Call call : getOkHttpClient().dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : getOkHttpClient().dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    /** 根据Tag取消请求 */
    public static void cancelTag(OkHttpClient client, Object tag) {
        if (client == null || tag == null) return;
        for (Call call : client.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : client.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    /** 取消所有请求请求 */
    public void cancelAll() {
        for (Call call : getOkHttpClient().dispatcher().queuedCalls()) {
            call.cancel();
        }
        for (Call call : getOkHttpClient().dispatcher().runningCalls()) {
            call.cancel();
        }
    }

    /** 取消所有请求请求 */
    public static void cancelAll(OkHttpClient client) {
        if (client == null) return;
        for (Call call : client.dispatcher().queuedCalls()) {
            call.cancel();
        }
        for (Call call : client.dispatcher().runningCalls()) {
            call.cancel();
        }
    }
}
