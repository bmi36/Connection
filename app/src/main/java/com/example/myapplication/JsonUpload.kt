package com.example.myapplication

import android.graphics.Bitmap
import android.util.Base64
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit

interface RetrofitInterface {
    @POST("/post")
    fun sendImage(@Body image: String?): Call<TestCallback>
}

const val URL = "http://192.168.3.7:8080"

//写真をBASE64にエンコードするやつ
fun toBase(bitmap: Bitmap): String {
    val bao = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao)
    val ba = bao.toByteArray()
    return Base64.encodeToString(ba, Base64.DEFAULT)
}

fun retrofitBuild(): RetrofitInterface {
    return Retrofit.Builder()
        .baseUrl(URL).client(
            OkHttpClient().newBuilder()
                .connectTimeout(3, TimeUnit.SECONDS)
                .writeTimeout(3, TimeUnit.SECONDS)
                .readTimeout(3, TimeUnit.SECONDS)
                .build()
        )
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(RetrofitInterface::class.java)
}

