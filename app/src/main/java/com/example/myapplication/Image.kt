package com.example.myapplication

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_image.*

class Image : AppCompatActivity() {

    private val uri by lazy {
        intent?.extras?.get("uri") as Uri
    }

    private val json: String? = intent?.extras?.get("json") as String?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        cameraImage.setImageURI(uri)

        val str = if (json != null) {
            json?.toDataClass().let {
                "${it.foodname}\n${it.calorie}"
            }
        } else {"Nothing"}

        Toast.makeText(this, str, Toast.LENGTH_LONG).show()
    }
}