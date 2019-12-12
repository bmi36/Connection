package com.example.myapplication

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_image.*
import java.io.File

class Image : AppCompatActivity() {

    private val uri by lazy {
        intent?.extras?.get("uri") as Uri
    }

    private val file: File by lazy {
        intent?.extras?.get("file") as File
    }

    private val viewModel = ViewModelSample(file)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        viewModel.data.observe(this, Observer {
            Toast.makeText(this,"${it.foodname}\n${it.calorie}",Toast.LENGTH_LONG).show()
        })

        cameraImage.setImageURI(uri)
    }

}