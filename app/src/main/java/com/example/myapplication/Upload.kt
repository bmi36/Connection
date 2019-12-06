package com.example.myapplication

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
import java.io.File


interface Upload {
    @Multipart
    @POST(BASE)
    fun uploadImage(
        @Part image: MultipartBody.Part,
        @Part("name") result: RequestBody
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

    val part = RequestBody.create(MediaType.parse("image/jpeg"), file).let {
        MultipartBody.Part.createFormData("upload", file.name, it)
    }
    val description = RequestBody.create(MediaType.parse("text/plan"), "image-type")

    retrofit.uploadImage(part,description).enqueue(object : Callback<TestCallback> {

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