package com.example.myapplication

import com.google.gson.Gson

data class TestCallback(
    val foodname: String,
    val calorie: Int
)
    fun TestCallback.toJson(): String = Gson().toJson(this)
    fun String.toDataClass(): TestCallback = Gson().fromJson(this,TestCallback::class.java)