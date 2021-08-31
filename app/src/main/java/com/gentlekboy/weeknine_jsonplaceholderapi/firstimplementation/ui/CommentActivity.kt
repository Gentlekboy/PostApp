package com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.gentlekboy.weeknine_jsonplaceholderapi.R
import com.gentlekboy.weeknine_jsonplaceholderapi.databinding.ActivityCommentBinding
import com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.model.adapter.CommentAdapter
import com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.model.adapter.PostAdapter
import com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.repository.Repository
import com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.viewmodel.MainViewModel
import com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.viewmodel.MainViewModelFactory
import kotlin.properties.Delegates

private lateinit var binding: ActivityCommentBinding
private lateinit var viewModel: MainViewModel

class CommentActivity : AppCompatActivity() {
    private lateinit var commentAdapter: CommentAdapter
    private val linearLayoutManager = LinearLayoutManager(this)
    private lateinit var postId: String
    private var userId by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        postId = intent.getIntExtra("postId", 1).toString()
        userId = intent.getIntExtra("userId", 1)

        //Set up adapter
        commentAdapter = CommentAdapter(this)

        //Set up recyclerview
        binding.commentsRecyclerview.adapter = commentAdapter
        binding.commentsRecyclerview.setHasFixedSize(true)
        binding.commentsRecyclerview.layoutManager = linearLayoutManager

        displayCommentsOnUi()
    }

    private fun displayCommentsOnUi(){
        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)

        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.fetchComments(postId)
        viewModel.allComments.observe(this, {

            if (it.isSuccessful){
                val response = it.body()

                if (response != null){

                    Log.d("GKB", "displayCommentsOnUi: $response")
                    commentAdapter.addComments(response)
                }
            }else{
                Log.d("GKB", "onCreate: ${it.errorBody()}")
            }
        })
    }
}