package com.example.myapplication

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.io.ByteArrayOutputStream
import java.io.File

interface RetrofitInterface {
    @POST("/post")
    fun sendImage(@Body image: String?): Call<TestCallback>
}


//写真をBASE64にエンコードするやつ
private fun toBase(bitmap: Bitmap): String? {
    val bao = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao)
    val ba = bao.toByteArray()
    return Base64.encodeToString(ba, Base64.DEFAULT)
}

fun retrofitBuild(): RetrofitInterface {
    return Retrofit.Builder()
        .baseUrl(URL).client(OkHttpClient().newBuilder().build())
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(RetrofitInterface::class.java)
}

suspend fun uploadToServer(file: File): TestCallback? {
    val bitmap = BitmapFactory.decodeFile(file.absolutePath)
    val retrofit = retrofitBuild()
    val image = toBase(bitmap)
    var str : TestCallback? = null
    retrofit.sendImage(image).enqueue(object : Callback<TestCallback> {

        override fun onFailure(call: Call<TestCallback>, t: Throwable) {
        }

        override fun onResponse(call: Call<TestCallback>, response: Response<TestCallback>) {
            Log.d("result", "成功した")
            Log.d("result", response.message())

            str =response.body()
        }
    })
    return str
}

