package com.example.myapplication

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import java.io.File

class LoadFragment(
    val file: File
) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bitmap = BitmapFactory.decodeFile(file.absolutePath)
//        uploadToServer(bitmap)

        return super.onCreateView(inflater, container, savedInstanceState)
    }


}