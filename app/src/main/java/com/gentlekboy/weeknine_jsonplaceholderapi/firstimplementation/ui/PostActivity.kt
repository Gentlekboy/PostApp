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
import android.widget.Toast
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
    private lateinit var copyOfListOfPosts: MutableList<PostItems>
    private lateinit var reversedListOfPosts: MutableList<PostItems>
    private lateinit var listOfPosts: MutableList<PostItems>
    private lateinit var response: Posts
    private lateinit var newPostBody: String

    //Instantiate variables
    private val repository = Repository()
    private val viewModelFactory = MainViewModelFactory(repository)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        //Initialize lists
        copyOfListOfPosts = mutableListOf() //copyOfListOfPosts contains all items in the adapter and is used to filter posts
        listOfPosts = mutableListOf() //listOfPosts holds items in the adapter
        reversedListOfPosts = listOfPosts.asReversed() //reversedListOfPosts reverses the list such that a new post appears on top

        //Set up adapter
        postAdapter = PostAdapter(reversedListOfPosts, this, this)

        //Set up recyclerview
        binding.postRecyclerview.adapter = postAdapter
        binding.postRecyclerview.setHasFixedSize(true)
        binding.postRecyclerview.layoutManager = linearLayoutManager

        binding.addPostButton.setOnClickListener {
            sendPostToServer()
        }

        binding.editTextContainer.setOnClickListener {
            binding.addPostEditText.requestFocus()
            inputMethodManager.showSoftInput(binding.addPostEditText, InputMethodManager.SHOW_IMPLICIT)
        }

        fetchAndDisplayPostsOnUi()
        filterPosts()
    }

    //This function fetches posts and displays them on the UI
    private fun fetchAndDisplayPostsOnUi(){
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

    //Makes a post request and adds new post to the recycler view
    private fun sendPostToServer(){
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        newPostBody = binding.addPostEditText.text.toString().trim()
        val id = listOfPosts.size + 1

        if (newPostBody.isNotEmpty()){
            viewModel.makeAPostToApi(11, id, "Kufre's Post", newPostBody)
            viewModel.pushPost2.observe(this, {

                if (it.isSuccessful){
                    val postItems = PostItems(newPostBody, id, "Kufre's Post", 11)
                    listOfPosts.add(postItems)
                    copyOfListOfPosts.add(postItems)
                    postAdapter.notifyItemInserted(listOfPosts.indexOf(listOfPosts[0]))

                    binding.addPostEditText.text = null
                    binding.addPostEditText.clearFocus()

                    //Hide keyboard after clicking the comment button
                    inputMethodManager.hideSoftInputFromWindow(binding.addPostEditText.windowToken, 0)

                    Log.d("GKB", "sendPostToServer: ${it.code()}")
                }else{
                    Log.d("GKB", "sendPostToServer: ${it.code()}")
                    Log.d("GKB", "sendPostToServer: ${it.errorBody()}")
                }
            })
        }
    }

    //This function filters posts based on user's query in the search view
    private fun filterPosts(){
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                inputMethodManager.hideSoftInputFromWindow(binding.searchView.windowToken, 0)
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

    //This function handles clicking items on the recyclerview and passing data to the next activity
    override fun navigateToCommentsActivity(position: Int, id: Int, userId: Int) {
        val intent = Intent(this, CommentActivity::class.java)

        if (listOfPosts.size > 100){
            intent.putExtra("postBody", newPostBody)
        }else{
            val postBody = response[position].body
            intent.putExtra("postBody", postBody)
        }

        intent.putExtra("postId", id)
        intent.putExtra("userId", userId)
        startActivity(intent)
        Toast.makeText(this, "$id", Toast.LENGTH_SHORT).show()
    }
}
