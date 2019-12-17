package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_image.*

class Image : AppCompatActivity() {

    private val uri by lazy {
        intent?.extras?.get("uri") as Uri?
    }

    private val json: String? = intent?.extras?.get("json") as String?

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        val str = json?.toDataClass()
        if (json == null && uri == null) startActivity(
            Intent(this, ImageFalse::class.java))

        cameraImage.setImageURI(uri)

        nameText.text = "${getString(R.string.foodname)} ${str?.foodname}"
        caloryText.text = "${getString(R.string.calory)} ${str?.calorie}"
    }
}