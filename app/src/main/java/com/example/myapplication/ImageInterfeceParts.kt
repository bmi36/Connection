package com.example.myapplication

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


const val URL = "http://192.168.3.7:8080"
const val PROBASE = "/json/server_android/result_return.json"
const val BASE = "/json/android_server/"
///php1.php

class ServiceGenerator{
    private val builder = Retrofit.Builder()
        .baseUrl("http://192.168.3.7:8080/json/android_server/php1.php")
        .addConverterFactory(GsonConverterFactory.create())

    private var retrofit = builder.build()
    private val httpClient = OkHttpClient.Builder()
    private val logging =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    fun <S>createService(serviceClass:Class<S> ): S {
        if (!httpClient.interceptors().contains(logging)){
            httpClient.addInterceptor(logging)
            builder.client(httpClient.build())
            retrofit = builder.build()
        }
        return retrofit.create(serviceClass)
    }
}