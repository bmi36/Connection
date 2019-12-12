package com.example.myapplication

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import com.google.gson.Gson
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
    suspend fun sendImage(@Body image: String?): Call<TestCallback>
}


class Repository {
    companion object {
        const val URL = "http://192.168.3.7:8080"

        val instance: Repository
        @Synchronized get(){
            return Repository()
        }
    }

    //写真をBASE64にエンコードするやつ
    private fun toBase(bitmap: Bitmap): String? {
        val bao = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao)
        val ba = bao.toByteArray()
        return Base64.encodeToString(ba, Base64.DEFAULT)
    }

    private fun retrofitBuild(): RetrofitInterface {
        return Retrofit.Builder()
            .baseUrl(URL).client(OkHttpClient().newBuilder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(RetrofitInterface::class.java)
    }

    private val retrofit = retrofitBuild()

    private fun Image(file: File) = toBase(BitmapFactory.decodeFile(file.absolutePath))

    suspend fun uploadToServer(file: File): TestCallback? {
        val image = Image(file)
        var res: TestCallback? = null
        retrofit.sendImage(image).enqueue(object : Callback<TestCallback> {

            override fun onFailure(call: Call<TestCallback>, t: Throwable) {
                Log.d("test", t.message)

            }

            override fun onResponse(call: Call<TestCallback>, response: Response<TestCallback>) {
                Log.d("result", "成功した")
                Log.d("result", response.body()?.foodname)
                Log.d("result", response.body()?.calorie.toString())

                res = response.body()

            }
        })
        return res
    }
}
