package com.example.darkflix.API;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request()
                .newBuilder()
                .addHeader("api_key", "a6372208be8115e77f05da30622b4465")
                .build();
        Response response = chain.proceed(request);
        return response;
    }
}