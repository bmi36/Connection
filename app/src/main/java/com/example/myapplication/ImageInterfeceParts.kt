package com.example.myapplication

import android.util.Log
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.io.File


const val URL = "http://192.168.3.7:8080"
const val PROBASE = "/json/server_android/result_return.json"
const val BASE = "/json/android_server/json_create.json"

interface ImageInterface{
    @Multipart
    @POST(BASE)
    fun upload(@Part file: MultipartBody.Part): Call<RequestBody>
}
class ServiceGenerator{
    private val httpClient = OkHttpClient.Builder()
    private val builder = Retrofit.Builder().baseUrl(URL)

    private fun createService(): ImageInterface? {
        val retrofit = builder.addConverterFactory(GsonConverterFactory.create()).build()
        return retrofit.create(ImageInterface::class.java)
    }

    fun uploadImage(file: File){
        val service = createService()
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"),file)
        val body = MultipartBody.Part.createFormData("POST先のフィールド名",file.name,requestFile)
        val call = service?.upload(body)
        call?.enqueue(object : Callback<RequestBody>{
            override fun onFailure(call: Call<RequestBody>, t: Throwable) {
                Log.d("test","ミスってる",t)
            }

            override fun onResponse(
                call: Call<RequestBody>,
                response: Response<RequestBody>
            ) {
                val demo = response.body()
                Log.d("test",demo.toString())
            }

        })
    }
}