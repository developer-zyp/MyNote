package com.example.mynote.network

import com.example.mynote.common.AppConstant
import okhttp3.Interceptor
import okhttp3.Response

class RequestInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val httpUrl = originalRequest.url
        val url = httpUrl.newBuilder()
            .addQueryParameter("api_key", AppConstant.TMDB_APIKEY)
            .build()
        val request = originalRequest.newBuilder().url(url).build()
        return chain.proceed(request)
    }
}