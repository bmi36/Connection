package com.example.myapplication

import android.util.Log
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
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
    fun upload(@Part("description") description: RequestBody,
               @Part file: MultipartBody.Part): Call<RequestBody>
}

fun uploadFile(file: File){
    val service =
        SeviceGenerator().createService(FileUploadService::class.java)

    val requestFile: RequestBody =
        RequestBody.create(MediaType.parse("multipart/form-data"),file)

    val body = MultipartBody.Part.createFormData("picture",file.name,requestFile)

    val descriptionString = "hello this is description speaking"

    val description: RequestBody =
        RequestBody.create(MultipartBody.FORM,descriptionString)

    val call: Call<RequestBody> = service.upload(description,body)
    call.enqueue(object : Callback<RequestBody> {
        override fun onFailure(call: Call<RequestBody>, t: Throwable) {
            Log.d("ミスった:",t.message)
        }

        override fun onResponse(call: Call<RequestBody>, response: Response<RequestBody>) {
            Log.d("test","完了しました")
        }

    })
}