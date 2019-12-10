package com.example.myapplication

import android.app.ProgressDialog
import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import android.widget.ProgressBar
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.io.ByteArrayOutputStream

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

fun uploadToServer(bitmap: Bitmap){
    val retrofit = retrofitBuild()
    val image = toBase(bitmap)

    retrofit.sendImage(image).enqueue(object : Callback<TestCallback> {

        override fun onFailure(call: Call<TestCallback>, t: Throwable) {
            "失敗した\n${t.message}"


        }

        override fun onResponse(call: Call<TestCallback>, response: Response<TestCallback>) {
            Log.d("result", "成功した")
            Log.d("result", response.message())

            "成功した\n${response.message()}"
        }

    })
}

