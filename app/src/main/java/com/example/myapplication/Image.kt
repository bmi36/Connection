package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_image.*

class Image : AppCompatActivity() {

    private val uri by lazy {
        intent?.extras?.get("uri") as Uri?
    }

    private val json: TestCallback = intent.getStringExtra("json").toDataClass()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)

        Log.d("test",json.foodname)
        if (uri == null) startActivity(
            Intent(this, ImageFalse::class.java))

        cameraImage.setImageURI(uri)

        nameText.text = "${getString(R.string.foodname)} ${json.foodname}"
        caloryText.text = "${getString(R.string.calory)} ${json.calorie}"
    }
}