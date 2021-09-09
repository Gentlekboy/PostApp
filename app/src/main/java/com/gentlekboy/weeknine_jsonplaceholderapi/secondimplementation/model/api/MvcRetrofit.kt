package com.gentlekboy.weeknine_jsonplaceholderapi.secondimplementation.model.api

import com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.utils.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MvcRetrofit {
    //Set up logging interceptor
    private val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    //Set up retrofit
    private val retrofit by lazy {
        Retrofit.Builder()
            .client(OkHttpClient.Builder().addInterceptor(loggingInterceptor).build())
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    //Connect retrofit to endpoint in interface
    val api: MvcApiInterface by lazy {
        retrofit.create(MvcApiInterface::class.java)
    }
}