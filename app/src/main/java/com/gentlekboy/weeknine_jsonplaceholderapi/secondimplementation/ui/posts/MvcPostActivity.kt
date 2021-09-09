package com.gentlekboy.weeknine_jsonplaceholderapi.secondimplementation.ui.posts

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.gentlekboy.weeknine_jsonplaceholderapi.databinding.ActivityMvcPostBinding
import com.gentlekboy.weeknine_jsonplaceholderapi.secondimplementation.model.adapter.MvcOnclickPostItem
import com.gentlekboy.weeknine_jsonplaceholderapi.secondimplementation.model.adapter.MvcPostAdapter
import com.gentlekboy.weeknine_jsonplaceholderapi.secondimplementation.model.api.MvcRetrofit
import com.gentlekboy.weeknine_jsonplaceholderapi.secondimplementation.model.data.posts.MvcPostItems
import com.gentlekboy.weeknine_jsonplaceholderapi.secondimplementation.model.data.posts.MvcPosts
import com.gentlekboy.weeknine_jsonplaceholderapi.secondimplementation.ui.comments.MvcCommentActivity
import com.gentlekboy.weeknine_jsonplaceholderapi.secondimplementation.ui.posts.MvcSearchViewInPost.mvcFilterPostsWithSearchView
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
    private lateinit var commentResponse: MvcPosts
    private lateinit var newPostBody: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMvcPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        setUpRecyclerViewAdapter()

        binding.editTextContainer.setOnClickListener {
            binding.addPostEditText.requestFocus()
            inputMethodManager.showSoftInput(binding.addPostEditText, InputMethodManager.SHOW_IMPLICIT)
        }

        fetchPosts()
        mvcFilterPostsWithSearchView(binding.searchView, inputMethodManager, listOfPosts, copyOfListOfPosts, postAdapter)
    }

    private fun setUpRecyclerViewAdapter(){
        copyOfListOfPosts = mutableListOf() //copyOfListOfPosts contains all items in the adapter and is used to filter posts
        listOfPosts = mutableListOf() //listOfPosts holds items in the adapter
        reversedListOfPosts = listOfPosts.asReversed() //reversedListOfPosts reverses the list such that a new post appears on top

        postAdapter = MvcPostAdapter(reversedListOfPosts, this, this)

        binding.postRecyclerview.adapter = postAdapter
        binding.postRecyclerview.setHasFixedSize(true)
        binding.postRecyclerview.layoutManager = linearLayoutManager
    }

    private fun fetchPosts(){
        val connectedRetrofit = MvcRetrofit.api.getPost()
        connectedRetrofit.enqueue(object : Callback<MvcPosts?> {
            override fun onResponse(call: Call<MvcPosts?>, response: Response<MvcPosts?>) {
                commentResponse = response.body()!!

                postAdapter.addPosts(commentResponse)
                copyOfListOfPosts.addAll(listOfPosts)
                Log.d("GKB", "onResponse: $commentResponse")
            }

            override fun onFailure(call: Call<MvcPosts?>, t: Throwable) {
                Log.d("GKB", "onFailure: ${t.message}")
            }
        })
    }

    override fun navigateToCommentsActivity(position: Int, id: Int, userId: Int) {
        val intent = Intent(this, MvcCommentActivity::class.java)

        if (listOfPosts.size > 100){
            intent.putExtra("postBody", newPostBody)
        }else{
            val postBody = commentResponse[position].body
            intent.putExtra("postBody", postBody)
        }

        intent.putExtra("postId", id)
        intent.putExtra("userId", userId)
        startActivity(intent)
        Toast.makeText(this, "$id", Toast.LENGTH_SHORT).show()
    }
}