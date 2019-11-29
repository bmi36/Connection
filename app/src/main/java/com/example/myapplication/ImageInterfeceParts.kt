package com.example.myapplication

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


const val URL = "http://192.168.3.7:8080"
const val PROBASE = "/json/server_android/result_return.json"
const val BASE = "/json/android_server/json_create.json"

class SeviceGenerator{
    val retrofit= Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val httpClient = OkHttpClient.Builder()

    fun <S>createService(serviceClass:Class<S> ): S {
        return retrofit.create(serviceClass)
    }
}