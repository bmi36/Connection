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

class Repository(
    private val activity: MainActivity,
    file: File,
    private val intent: Intent
) {

    private val retrofit = retrofitBuild()

    private val baseImage: String = toBase(BitmapFactory.decodeFile(file.absolutePath))

    fun uploadToServer() {

        retrofit.sendImage(baseImage).enqueue(object : Callback<TestCallback> {
            override fun onFailure(call: Call<TestCallback>, t: Throwable) {
                Log.d("test", t.message)
                intent.removeExtra("json")
                activity.startActivityForResult(intent, IMAGE_REQUEST_CODE)



            }

            override fun onResponse(
                call: Call<TestCallback>,
                response: Response<TestCallback>
            ) {
                Log.d("result", "成功した")
                Log.d("result", response.body()?.foodname)
                Log.d("result", response.body()?.calorie.toString())

                intent.putExtra("json",response.body()?.toJson())
                activity.startActivityForResult(intent, IMAGE_REQUEST_CODE)
            }
        })
    }
}
