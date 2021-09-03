package com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.model.data.comments.Comments
import com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.model.data.posts.PostItems
import com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.model.data.posts.Posts
import com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(private val repository: Repository): ViewModel() {
    val allPosts: MutableLiveData<Response<Posts>> = MutableLiveData()
    val allComments: MutableLiveData<Response<Comments>> = MutableLiveData()
    val pushPost: MutableLiveData<Response<PostItems>> = MutableLiveData()
    val pushPost2: MutableLiveData<Response<PostItems>> = MutableLiveData()

    fun fetchPosts(){
        viewModelScope.launch {
            val response = repository.getPost()
            allPosts.value = response
        }
    }

    fun fetchComments(id: String){
        viewModelScope.launch {
            val response = repository.getComment(id)
            allComments.value = response
        }
    }

    fun pushPost(post: PostItems){
        viewModelScope.launch {
            val response = repository.pushPost(post)
            pushPost.value = response
        }
    }

    fun pushPost2(userId: Int, id: Int, title: String, body: String){
        viewModelScope.launch {
            val response = repository.pushPost2(userId, id, title, body)
            pushPost2.value = response
        }
    }
}