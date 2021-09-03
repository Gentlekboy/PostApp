package com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.gentlekboy.weeknine_jsonplaceholderapi.databinding.ActivityAddPostBinding
import com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.model.data.posts.PostItems
import com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.model.data.posts.Posts
import com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.repository.Repository
import com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.viewmodel.MainViewModel
import com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.viewmodel.MainViewModelFactory

private lateinit var binding: ActivityAddPostBinding

class AddPostActivity : AppCompatActivity() {
    private val repository = Repository()
    private val viewModelFactory = MainViewModelFactory(repository)
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val myPost = PostItems("This is my sentence", 101, "Test sentence", 11)

        binding.postButton.setOnClickListener {
            sendPost(myPost)
        }
    }

    private fun sendPost(myPost: PostItems){
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.pushPost(myPost)
        viewModel.pushPost.observe(this, {

            if (it.isSuccessful){
                val response = it.body()

                if (response != null){

                    binding.test.text = response.body

                    Log.d("GKB", "displayCommentsOnUi: ${it.code()}")
                }
            }else{
                Log.d("GKB", "onCreate: ${it.errorBody()}")
            }
        })
    }

    fun addToRecyclerView(){

    }
}