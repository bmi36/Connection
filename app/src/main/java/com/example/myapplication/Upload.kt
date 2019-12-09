package com.example.myapplication

import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import okhttp3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.io.ByteArrayOutputStream
import java.io.File


interface Upload {
    @Multipart
    @POST(BASE)
    fun uploadImage(
        @Part("description") description: RequestBody,
        @Part image: MultipartBody.Part
    ): Call<TestCallback>
}

class NetworkClient {

    fun retrofitBuild(): Upload {

        return Retrofit.Builder()
            .baseUrl(URL).client(OkHttpClient().newBuilder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Upload::class.java)
    }
}

fun uploadToServer(file: File) {
    val retrofit = NetworkClient().retrofitBuild()

    val description = RequestBody.create(MultipartBody.FORM,"TODO test")
    val type = file.path.substring(file.path.lastIndexOf(".") + 1)
    val requestFile = RequestBody.create(MediaType.parse(type),file)

    val body = MultipartBody.Part.createFormData("picture", file.name,requestFile)

    retrofit.uploadImage(description,body).enqueue(object : Callback<TestCallback> {

        override fun onFailure(call: Call<TestCallback>, t: Throwable) {
            Log.d("test", "えらー")
            Log.d("test",t.toString())
            Log.d("unko",call.request().toString())
        }

        override fun onResponse(call: Call<TestCallback>, response: Response<TestCallback>) {
            Log.d("test", "dekita")
            Log.d("unko",response.body()?.ret)

        }

    })
}

//写真をBASE64にエンコードするやつ
private fun toBase(bitmap: Bitmap): String? {
    val bao = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao)
    val ba = bao.toByteArray()
    return Base64.encodeToString(ba, Base64.DEFAULT)
}