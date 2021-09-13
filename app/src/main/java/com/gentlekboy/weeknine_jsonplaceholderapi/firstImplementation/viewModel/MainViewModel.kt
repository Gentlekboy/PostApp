package com.gentlekboy.weeknine_jsonplaceholderapi.firstImplementation.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gentlekboy.weeknine_jsonplaceholderapi.firstImplementation.model.data.comments.Comments
import com.gentlekboy.weeknine_jsonplaceholderapi.firstImplementation.model.data.posts.PostItems
import com.gentlekboy.weeknine_jsonplaceholderapi.firstImplementation.model.data.posts.Posts
import com.gentlekboy.weeknine_jsonplaceholderapi.firstImplementation.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(private val repository: Repository): ViewModel() {
    val allPosts: MutableLiveData<Response<Posts>> = MutableLiveData()
    val allComments: MutableLiveData<Response<Comments>> = MutableLiveData()
    val pushPost2: MutableLiveData<Response<PostItems>> = MutableLiveData()

    //Save fetched posts to livedata
    fun fetchPosts(){
        viewModelScope.launch (Dispatchers.IO){
            try {
                val response = repository.getPost()
                allPosts.postValue(response)
            }catch (e: Exception){
                Log.d("GKB", "fetchPosts: ${e.message}")
            }
        }
    }

    //Save fetched comments to livedata
    fun fetchComments(id: String){
        viewModelScope.launch (Dispatchers.IO) {
            try {
                val response = repository.getComment(id)
                allComments.postValue(response)
            }catch (e: Exception){
                Log.d("GKB", "fetchComments: ${e.message}")
            }
        }
    }

    //Save posted response to livedata
    fun makeAPostToApi(userId: Int, id: Int, title: String, body: String){
        viewModelScope.launch (Dispatchers.IO) {
            try{
                val response = repository.pushPost2(userId, id, title, body)
                pushPost2.postValue(response)
            }catch (e: Exception){
                Log.d("GKB", "fetchPosts: ${e.message}")
            }
        }
    }
}