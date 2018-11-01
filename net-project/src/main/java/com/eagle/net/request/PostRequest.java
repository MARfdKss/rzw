package com.eagle.net.request;


import com.eagle.net.model.HttpMethod;
import com.eagle.net.request.base.BodyRequest;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Post请求的实现类，注意需要传入本类的泛型
 */
public class PostRequest<T> extends BodyRequest<T, PostRequest<T>> {

    public PostRequest(String url) {
        super(url);
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.POST;
    }

    @Override
    public Request generateRequest(RequestBody requestBody) {
        Request.Builder requestBuilder = generateRequestBuilder(requestBody);
        return requestBuilder.post(requestBody).url(url).tag(tag).build();
    }
}
