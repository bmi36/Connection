package com.example.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.io.File

class ViewModelSample(
    private val file: File
)  : ViewModel() {
    private val repository = Repository.instance

    private val mdata = MutableLiveData<TestCallback>()

    val data = mdata

    init {
        loadLiveData()
    }

    private fun loadLiveData(){
        viewModelScope.launch { repository.uploadToServer(file) }
    }

}