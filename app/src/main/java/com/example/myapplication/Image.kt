package com.example.myapplication

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_image.*
import java.io.File

class Image : AppCompatActivity() {

    private val uri by lazy {
        intent?.extras?.get("uri") as Uri
    }

    private val file by lazy {
        intent?.extras?.get("file") as File
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)


       show()

        cameraImage.setImageURI(uri)
        Log.d("test",uri.path)

    }

    override fun onResume() {
        super.onResume()
        textView.text = intent?.extras?.getString("str","何もないよ")
    }

    private fun show(){
        textView.text = intent?.extras?.getString("str","何もないよ")
    }
}