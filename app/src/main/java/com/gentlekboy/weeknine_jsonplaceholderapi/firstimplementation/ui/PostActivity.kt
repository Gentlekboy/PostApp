package com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.ui

/*
* Using the endpoints found here https://jsonplaceholder.typicode.com
* build a simple application that shows a list of post
* and then a single post page with comments,
* you should be able to add new comment.
* Build a nice UI and the home page should have a search/filter field.
* You should also have a page to create a new post
* */

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.gentlekboy.weeknine_jsonplaceholderapi.databinding.ActivityPostBinding
import com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.model.adapter.OnclickPostItem
import com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.model.adapter.PostAdapter
import com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.model.data.posts.PostItems
import com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.model.data.posts.Posts
import com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.repository.Repository
import com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.viewmodel.MainViewModel
import com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.viewmodel.MainViewModelFactory

class PostActivity : AppCompatActivity(), OnclickPostItem {
    //Declare required variables
    private lateinit var binding: ActivityPostBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var postAdapter: PostAdapter
    private lateinit var inputMethodManager: InputMethodManager
    private val linearLayoutManager = LinearLayoutManager(this)
    private lateinit var copyOfListOfPosts: ArrayList<PostItems>
    private lateinit var listOfPosts: ArrayList<PostItems>
    private lateinit var response: Posts

    //Instantiate variables
    private val repository = Repository()
    private val viewModelFactory = MainViewModelFactory(repository)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        /*
        listOfPosts holds items in the adapter
        copyOfListOfPosts contains all items in the adapter and is used to filter posts
         */
        copyOfListOfPosts = arrayListOf()
        listOfPosts = arrayListOf()

        //Set up adapter
        postAdapter = PostAdapter(listOfPosts, this, this)

        //Set up recyclerview
        binding.postRecyclerview.adapter = postAdapter
        binding.postRecyclerview.setHasFixedSize(true)
        binding.postRecyclerview.layoutManager = linearLayoutManager

        binding.addPostButton.setOnClickListener {
            addNewPost()
        }

        displayPostsOnUi()
        filterPosts()
    }

    //This function filters posts based on user's query in the search view
    private fun filterPosts(){
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            //Filters posts as user types in the search view
            override fun onQueryTextChange(newText: String?): Boolean {
                listOfPosts.clear()
                val searchText = newText?.lowercase()?.trim()

                if (searchText != null) {
                    if (searchText.isNotEmpty()){
                        copyOfListOfPosts.forEach {
                            if (it.body.lowercase().contains(searchText)){
                                listOfPosts.add(it)
                            }
                        }

                        postAdapter.notifyDataSetChanged()
                    }else{
                        listOfPosts.clear()
                        listOfPosts.addAll(copyOfListOfPosts)
                        postAdapter.notifyDataSetChanged()
                    }
                }
                return true
            }
        })
    }

    private fun addNewPost(){
        val postBody = binding.addPostEditText.text.toString().trim()

        if (postBody.isNotEmpty()){
            var id = 101
            val postItems = PostItems(postBody, id, "Kufre's Post", 11)

            id++

            listOfPosts.add(postItems)
            postAdapter.notifyItemInserted(listOfPosts.size-1)

            binding.addPostEditText.text = null
            binding.addPostEditText.clearFocus()

            //Hide keyboard after clicking the comment button
            inputMethodManager.hideSoftInputFromWindow(binding.addPostEditText.windowToken, 0)

            sendPostToServer(id, postBody)
        }
    }

    private fun sendPostToServer(id: Int,  body: String){
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.makeAPostToApi(11, id, "Kufre's Post", body)
        viewModel.pushPost2.observe(this, {
            if (it.isSuccessful){
                Log.d("GKB", "sendPostToServer: ${it.body()}")
                Log.d("GKB", "sendPostToServer: ${it.code()}")
            }else{
                Log.d("GKB", "sendPostToServer: ${it.code()}")
                Log.d("GKB", "sendPostToServer: ${it.errorBody()}")
            }
        })
    }

    //This function fetches posts and displays them on the UI
    private fun displayPostsOnUi(){
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.fetchPosts()
        viewModel.allPosts.observe(this, {

            if (it.isSuccessful){
                response = it.body()!!

                postAdapter.addPosts(response)

                copyOfListOfPosts.addAll(listOfPosts)
            }else{
                Log.d("GKB", "onCreate: ${it.errorBody()}")
            }
        })
    }

    //This function handles clicking items on the recyclerview and passing data to the next activity
    override fun clickPostItem(position: Int, id: Int) {
        val postBody = response[position].body
        val intent = Intent(this, CommentActivity::class.java)
        intent.putExtra("postId", id)
        intent.putExtra("postBody", postBody)
        startActivity(intent)
    }
}