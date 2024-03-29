package com.example.myapplication

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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


                registerDatabase(file)

                //retrofit使うやつ
                sendJob(file).start()

            }
            Toast.makeText(this, "aaaa", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendJob(file: File) = launch(Dispatchers.Main) {
        val progress = ProgressDialog(this@MainActivity).also {
            it.isIndeterminate = true
            it.setMessage("Now Loading...")
        }
        progress.show()

        val retrofit = retrofitBuild()
        val baseImage: String = toBase(BitmapFactory.decodeFile(file.absolutePath))

        withContext(Dispatchers.Default) {

            supportFragmentManager.beginTransaction().remove(BlankFragment()).commit()
            retrofit.sendImage(baseImage).enqueue(object : Callback<TestCallback> {
                override fun onFailure(call: Call<TestCallback>, t: Throwable) {
                    frame.visibility = FrameLayout.INVISIBLE
                    progress.dismiss()
                    startActivityForResult(
                        Intent(this@MainActivity, ImageFalse::class.java).apply {
                            this.removeExtra("json")
                        }, IMAGE_REQUEST_CODE
                    )
                }

                override fun onResponse(
                    call: Call<TestCallback>,
                    response: Response<TestCallback>
                ) {
                    progress.dismiss()
                    Log.d("result", "成功した")
                    Log.d("result", response.body()?.foodname)
                    Log.d("result", response.body()?.calorie.toString())


                    if (response.isSuccessful) {
                        val task = response.body() as TestCallback
                        startActivityForResult(

                            Intent(this@MainActivity, Image::class.java).apply {
                                this.putExtra("uri", uri)
                                this.putExtra("json", task.toJson()).also {
                                    Log.d("test", task.toJson())
                                }
                            }, IMAGE_REQUEST_CODE
                        )
                    } else {
                        startActivity(Intent(this@MainActivity, ImageFalse::class.java))
                    }

                }
            })
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Job()
}