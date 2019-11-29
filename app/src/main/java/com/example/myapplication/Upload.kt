package com.example.myapplication

import android.util.Log
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.io.File

interface FileUploadService{
    @Multipart
    @POST("upload")
    fun upload(@Part file: MultipartBody.Part): Call<ResponseBody>
}

fun uploadFile(file: File){
    val service = ServiceGenerator().createService(FileUploadService::class.java)
    val requestFile = RequestBody.create(MediaType.parse("multipart/from-data"),file)
    val body = MultipartBody.Part.createFormData("POST先のフィールド名",file.name,requestFile)
    val call = service.upload(body)
    call.enqueue(object : Callback<ResponseBody> {
        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            Log.d("ミスった:",t.message)
        }

        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
            Log.d("test","完了しました")
        }

    } )
}