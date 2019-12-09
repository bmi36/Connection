package com.example.myapplication

import android.graphics.Bitmap
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


data class Coock(
    var name: String,
    var colory: Int,
    var json: String
)

interface RetrofitInterface {
    @POST(BASE)
    fun sendImage(@Body image: String?): Call<Coock>
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
    Log.d("test", retrofit.toString())

    retrofit.sendImage(image).enqueue(object : Callback<Coock> {
        override fun onFailure(call: Call<Coock>, t: Throwable) {
            Log.d("result","失敗したやで")
            Log.d("unko",t.toString())
        }

        override fun onResponse(call: Call<Coock>, response: Response<Coock>) {
            Log.d("result","成功したやで")
        }

    })
}

private fun chengeFile(file: File){
}
