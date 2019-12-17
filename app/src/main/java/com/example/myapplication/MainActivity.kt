package com.example.myapplication

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.CoroutineContext

const val CAMERA_REQUEST_CODE = 1
const val CAMERA_PERMISSION_REQUEST_CODE = 2
const val IMAGE_REQUEST_CODE = 3

class MainActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var file: File
    private lateinit var uri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Camerabutton.setOnClickListener { checkPermission() }
    }

    //ぱーにっしょんをリクエストするやつ
    private fun requestPermission() {
        val str: Array<String> = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) {
            ActivityCompat.requestPermissions(this, str, CAMERA_PERMISSION_REQUEST_CODE)
        }
    }

    //ぱーにっしょん確認するやつ
    private fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            == PackageManager.PERMISSION_GRANTED
        ) cameraIntent() else requestPermission()
    }

    //    カメラ起動させるやつ
    private fun cameraIntent() {
        val folder = getExternalFilesDir(Environment.DIRECTORY_DCIM)
        val name = SimpleDateFormat("ddHHmmss", Locale.US).format(Date()).let {
            String.format("CameraIntent_%s.jpg", it)
        }
        file = File(folder, name)
        uri = FileProvider.getUriForFile(
            applicationContext, "$packageName.fileprovider",
            file
        )

        Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            .putExtra(MediaStore.EXTRA_OUTPUT, uri).run {
                startActivityForResult(this, CAMERA_REQUEST_CODE)
            }
    }

    //写真をDB内に追加するやつ
    private fun registerDatabase(file: File) {
        val contentValues = ContentValues().also {
            it.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
            it.put("_data", file.absolutePath)
        }
        this.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

    }

    //ぱーにっしょん
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                cameraIntent() else Toast.makeText(
                this,
                "これ以上なにもないよ",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    //写真撮った後のやつ
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA_REQUEST_CODE) {

                frame.visibility = FrameLayout.VISIBLE
                supportFragmentManager.beginTransaction().replace(frame.id, BlankFragment())
                    .commit()


                registerDatabase(file)

                launch {
                    val intent = Intent(this@MainActivity,Image::class.java)
                        .putExtra("uri",uri)
                    withContext(Dispatchers.Default) {
                        Repository(this@MainActivity, file,intent).uploadToServer()
                    }
                }
                Toast.makeText(this, "aaaa", Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == IMAGE_REQUEST_CODE) {
            frame.visibility = FrameLayout.INVISIBLE
            supportFragmentManager.beginTransaction().remove(BlankFragment()).commit()
            Toast.makeText(this, "uu", Toast.LENGTH_SHORT).show()
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Job()
}