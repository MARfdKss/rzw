package com.eagle.net.request;


import com.eagle.net.model.HttpMethod;
import com.eagle.net.request.base.BodyRequest;

import okhttp3.Request;
import okhttp3.RequestBody;

public class OptionsRequest<T> extends BodyRequest<T, OptionsRequest<T>> {

    public OptionsRequest(String url) {
        super(url);
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.OPTIONS;
    }

    @Override
    public Request generateRequest(RequestBody requestBody) {
        Request.Builder requestBuilder = generateRequestBuilder(requestBody);
        return requestBuilder.method("OPTIONS", requestBody).url(url).tag(tag).build();
    }
}
