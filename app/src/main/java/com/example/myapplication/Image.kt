package com.example.myapplication

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_image.*
import java.io.File

class Image : AppCompatActivity() {

    private val bitmap by lazy {
        intent?.extras?.get("bitmap") as Bitmap
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        cameraImage.setImageBitmap(bitmap)

    }
}