package com.example.myapplication

import android.graphics.Bitmap
import android.os.AsyncTask
import android.util.Base64
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.OutputStreamWriter
import java.net.HttpURLConnection

class HttpRespondsAsync(bim: Bitmap): AsyncTask<Bitmap, Unit, String>() {

    private val image by lazy {
        //Bitmap -> バイナリ
        val babs = ByteArrayOutputStream()
        bim.compress(Bitmap.CompressFormat.JPEG,100,babs)
        val v = babs.toByteArray()
        Base64.encodeToString(v,Base64.NO_WRAP)
    }

    private val mbim by lazy { bim }

    override fun doInBackground(vararg params: Bitmap?): String? {

        val word = "word$params"
        val url = java.net.URL(URL+ BASE)
        val con = url.openConnection() as HttpURLConnection
        con.requestMethod = "POST"
        con.instanceFollowRedirects = false
        con.doInput = false
        con.doOutput = true
        con.readTimeout = 10000
        con.connectTimeout = 20000
        con.setChunkedStreamingMode(0)
        con.connect()

        val outputStream = OutputStreamWriter(con.outputStream)
        //違ったらUTF－８に換える

        outputStream.write(word)
        outputStream.flush()

        val status = con.responseCode

        Log.d("result", mbim.toString())
        return if (status == HttpURLConnection.HTTP_OK){
            Log.d("test","出来たかもー")
            "HTTP_OK"
        }else{
            Log.d("test","出来てないかも―")
            Log.d("test",status.toString())
            "status ->$status"
        }
    }
}