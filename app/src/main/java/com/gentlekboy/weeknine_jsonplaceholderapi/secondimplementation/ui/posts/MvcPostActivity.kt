package com.gentlekboy.weeknine_jsonplaceholderapi.secondimplementation.ui.posts

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
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
import android.view.ViewTreeObserver
import android.view.ViewTreeObserver.OnScrollChangedListener


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

        //Initialize input method manager
        inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        setUpRecyclerViewAdapter()

        //Add a new post when the add post button is clicked
        binding.addPostButton.setOnClickListener {
            makeAPostRequest()
        }

        //Set focus on edit text and open keyboard when edit text container is clicked
        binding.editTextContainer.setOnClickListener {
            binding.addPostEditText.requestFocus()
            inputMethodManager.showSoftInput(binding.addPostEditText, InputMethodManager.SHOW_IMPLICIT)
        }

        //Set focus on edit text and open keyboard when edit text container is clicked
        binding.fab.setOnClickListener {
            binding.addPostEditText.requestFocus()
            inputMethodManager.showSoftInput(binding.addPostEditText, InputMethodManager.SHOW_IMPLICIT)
        }

        fetchPosts()
        displayAppLayouts()
        floatingActionButtonVisibility()
        mvcFilterPostsWithSearchView(binding.searchView, inputMethodManager, listOfPosts, copyOfListOfPosts, postAdapter)
    }

    //This function sets up the recycler view and adapter
    private fun setUpRecyclerViewAdapter(){
        copyOfListOfPosts = mutableListOf() //Contains all items in the adapter and is used to filter posts
        listOfPosts = mutableListOf() //Holds items in the adapter
        reversedListOfPosts = listOfPosts.asReversed() //Reverses the list such that a new post appears on top

        postAdapter = MvcPostAdapter(reversedListOfPosts, this, this)

        binding.postRecyclerview.adapter = postAdapter
        binding.postRecyclerview.setHasFixedSize(true)
        binding.postRecyclerview.layoutManager = linearLayoutManager
    }

    //This function makes a post request and adds new post to the recycler view
    private fun makeAPostRequest(){
        newPostBody = binding.addPostEditText.text.toString().trim()
        val id = listOfPosts.size + 1
        val postItems = MvcPostItems(newPostBody, id, "Kufre's Post", 11)

        if (newPostBody.isNotEmpty()){
            val connectedRetrofit = MvcRetrofit.api.makeAPost(postItems)
            connectedRetrofit.enqueue(object : Callback<MvcPostItems?> {
                override fun onResponse(call: Call<MvcPostItems?>, response: Response<MvcPostItems?>) {
                    if (response.isSuccessful){
                        addNewPostToRecyclerView(postItems)
                        Toast.makeText(this@MvcPostActivity, "Post sent!.", Toast.LENGTH_LONG).show()
                    }else{
                        Log.d("GKB", "sendPostToServer: ${response.code()}")
                        Toast.makeText(this@MvcPostActivity, "Check your internet and try again.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<MvcPostItems?>, t: Throwable) {
                    Log.d("GKB", "sendPostToServer: ${t.message}")
                    Toast.makeText(this@MvcPostActivity, "Check your internet and try again.", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    //This function adds new post to the recycler view
    private fun addNewPostToRecyclerView(postItems: MvcPostItems){
        listOfPosts.add(postItems)
        copyOfListOfPosts.add(postItems)
        postAdapter.notifyItemInserted(listOfPosts.indexOf(listOfPosts[0]))

        binding.addPostEditText.text = null
        binding.addPostEditText.clearFocus()

        //Hide keyboard after clicking the comment button
        inputMethodManager.hideSoftInputFromWindow(binding.addPostEditText.windowToken, 0)
    }

    //This function fetches posts and displays them on the UI
    private fun fetchPosts(){
        val connectedRetrofit = MvcRetrofit.api.getPost()
        connectedRetrofit.enqueue(object : Callback<MvcPosts?> {
            override fun onResponse(call: Call<MvcPosts?>, response: Response<MvcPosts?>) {
                commentResponse = response.body()!!

                if (response.isSuccessful){
                    postAdapter.addPosts(commentResponse)
                    copyOfListOfPosts.addAll(listOfPosts)
                }else{
                    Toast.makeText(this@MvcPostActivity, "Something went wrong. Try again!", Toast.LENGTH_SHORT).show()
                    Log.d("GKB", "onResponse: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<MvcPosts?>, t: Throwable) {
                Log.d("GKB", "onFailure: ${t.message}")
            }
        })
    }

    //Listen to scroll and display or hide floating action button
    private fun floatingActionButtonVisibility(){
        var previousScrollY = 0
        binding.nestedScrollview.viewTreeObserver.addOnScrollChangedListener {
            if (binding.nestedScrollview.scrollY > previousScrollY && binding.fab.visibility != View.VISIBLE) {
                binding.fab.show()
            } else if (binding.nestedScrollview.scrollY < previousScrollY && binding.fab.visibility == View.VISIBLE) {
                binding.fab.hide()
            }
            previousScrollY = binding.nestedScrollview.scrollY
        }
    }

    //This function hides starting views and displays main layouts
    private fun displayAppLayouts(){
        val handler = Handler()
        handler.postDelayed({
            if (reversedListOfPosts.isNotEmpty()){
                binding.nameOfApp.visibility = View.GONE
                binding.implementationType.visibility = View.GONE
                binding.appName.visibility = View.VISIBLE
                binding.searchView.visibility = View.VISIBLE
                binding.nestedScrollview.visibility = View.VISIBLE
            }
        }, 2000)
    }

    //This function handles clicking items on the recyclerview and passing data to the next activity
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