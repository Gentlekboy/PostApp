package com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.ui

/*
* Using the endpoints found here https://jsonplaceholder.typicode.com
* build a simple application that shows a list of post
* and then a single post page with comments,
* you should be able to add new comment.
* Build a nice UI and the home page should have a search/filter field.
* You should also have a page to create a new post
* */

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.gentlekboy.weeknine_jsonplaceholderapi.databinding.ActivityPostBinding
import com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.model.adapter.OnclickPostItem
import com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.model.adapter.PostAdapter
import com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.repository.Repository
import com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.viewmodel.MainViewModel
import com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.viewmodel.MainViewModelFactory

private lateinit var binding: ActivityPostBinding
private lateinit var viewModel: MainViewModel

class PostActivity : AppCompatActivity(), OnclickPostItem {
    private lateinit var postAdapter: PostAdapter
    private val linearLayoutManager = LinearLayoutManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Set up adapter
        postAdapter = PostAdapter(this, this)

        //Set up recyclerview
        binding.postRecyclerview.adapter = postAdapter
        binding.postRecyclerview.setHasFixedSize(true)
        binding.postRecyclerview.layoutManager = linearLayoutManager

        displayPostsOnUi()
    }

    private fun displayPostsOnUi(){
        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)

        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.fetchPosts()
        viewModel.allPosts.observe(this, {

            if (it.isSuccessful){
                val response = it.body()

                if (response != null){

                    postAdapter.addPosts(response)
                }
            }else{
                Log.d("GKB", "onCreate: ${it.errorBody()}")
            }
        })
    }

    override fun clickPostItem(position: Int, id: Int, userId: Int) {
        val intent = Intent(this, CommentActivity::class.java)
        intent.putExtra("postId", id)
        intent.putExtra("userId", userId)
        startActivity(intent)
    }
}