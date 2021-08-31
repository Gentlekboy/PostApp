package com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.model.Posts
import com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(private val repository: Repository): ViewModel() {
    val myResponse: MutableLiveData<Response<Posts>> = MutableLiveData()

    fun fetchPosts(){
        viewModelScope.launch {

            val response = repository.getPost()

            myResponse.value = response
        }
    }
}