package com.example.darkflix.API

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class RequestInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url()
        val body = request.body()

        // Log the URL and body
        Log.d("API", "URL: $url")
        Log.d("API", "Body: $body")

        return chain.proceed(request)
    }
}