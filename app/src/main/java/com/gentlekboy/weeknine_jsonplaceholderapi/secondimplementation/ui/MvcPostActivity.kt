package com.gentlekboy.weeknine_jsonplaceholderapi.secondimplementation.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.gentlekboy.weeknine_jsonplaceholderapi.databinding.ActivityMvcPostBinding
import com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.model.data.posts.Posts
import com.gentlekboy.weeknine_jsonplaceholderapi.secondimplementation.model.adapter.MvcOnclickPostItem
import com.gentlekboy.weeknine_jsonplaceholderapi.secondimplementation.model.adapter.MvcPostAdapter
import com.gentlekboy.weeknine_jsonplaceholderapi.secondimplementation.model.api.MvcRetrofit
import com.gentlekboy.weeknine_jsonplaceholderapi.secondimplementation.model.data.posts.MvcPostItems
import com.gentlekboy.weeknine_jsonplaceholderapi.secondimplementation.model.data.posts.MvcPosts
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MvcPostActivity : AppCompatActivity(), MvcOnclickPostItem {
    private lateinit var binding: ActivityMvcPostBinding
    private lateinit var postAdapter: MvcPostAdapter
    private lateinit var inputMethodManager: InputMethodManager
    private val linearLayoutManager = LinearLayoutManager(this)
    private lateinit var copyOfListOfPosts: MutableList<MvcPostItems>
    private lateinit var reversedListOfPosts: MutableList<MvcPostItems>
    private lateinit var listOfPosts: MutableList<MvcPostItems>
    private lateinit var response: Posts
    private lateinit var newPostBody: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMvcPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        //Initialize lists
        copyOfListOfPosts = mutableListOf() //copyOfListOfPosts contains all items in the adapter and is used to filter posts
        listOfPosts = mutableListOf() //listOfPosts holds items in the adapter
        reversedListOfPosts = listOfPosts.asReversed() //reversedListOfPosts reverses the list such that a new post appears on top

        //Set up adapter
        postAdapter = MvcPostAdapter(reversedListOfPosts, this, this)

        //Set up recyclerview
        binding.postRecyclerview.adapter = postAdapter
        binding.postRecyclerview.setHasFixedSize(true)
        binding.postRecyclerview.layoutManager = linearLayoutManager

        binding.editTextContainer.setOnClickListener {
            binding.addPostEditText.requestFocus()
            inputMethodManager.showSoftInput(binding.addPostEditText, InputMethodManager.SHOW_IMPLICIT)
        }

        fetchPosts()
    }

    private fun fetchPosts(){
        val connectedRetrofit = MvcRetrofit.api.getPost()
        connectedRetrofit.enqueue(object : Callback<MvcPosts?> {
            override fun onResponse(call: Call<MvcPosts?>, response: Response<MvcPosts?>) {
                val fetchedPosts = response.body()
                postAdapter.addPosts(fetchedPosts)
                copyOfListOfPosts.addAll(listOfPosts)
                Log.d("GKB", "onResponse: $fetchedPosts")
            }

            override fun onFailure(call: Call<MvcPosts?>, t: Throwable) {
                Log.d("GKB", "onFailure: ${t.message}")
            }
        })
    }

    override fun navigateToCommentsActivity(position: Int, id: Int, userId: Int) {

    }
}