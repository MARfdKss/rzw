package com.eagle.net.request;


import com.eagle.net.model.HttpMethod;
import com.eagle.net.request.base.NoBodyRequest;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Trace请求的实现类，注意需要传入本类的泛型
 */
public class TraceRequest<T> extends NoBodyRequest<T, TraceRequest<T>> {

    public TraceRequest(String url) {
        super(url);
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.TRACE;
    }

    @Override
    public okhttp3.Request generateRequest(RequestBody requestBody) {
        Request.Builder requestBuilder = generateRequestBuilder(requestBody);
        return requestBuilder.method("TRACE", requestBody).url(url).tag(tag).build();
    }
}
