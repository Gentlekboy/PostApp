package com.gentlekboy.weeknine_jsonplaceholderapi.firstImplementation.ui.posts

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
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.gentlekboy.weeknine_jsonplaceholderapi.MainActivity
import com.gentlekboy.weeknine_jsonplaceholderapi.databinding.ActivityPostBinding
import com.gentlekboy.weeknine_jsonplaceholderapi.firstImplementation.model.adapter.OnclickPostItem
import com.gentlekboy.weeknine_jsonplaceholderapi.firstImplementation.model.adapter.PostAdapter
import com.gentlekboy.weeknine_jsonplaceholderapi.firstImplementation.model.data.posts.PostItems
import com.gentlekboy.weeknine_jsonplaceholderapi.firstImplementation.model.data.posts.Posts
import com.gentlekboy.weeknine_jsonplaceholderapi.firstImplementation.repository.Repository
import com.gentlekboy.weeknine_jsonplaceholderapi.firstImplementation.ui.comments.CommentActivity
import com.gentlekboy.weeknine_jsonplaceholderapi.firstImplementation.ui.posts.SearchViewInPost.filterPostsWithSearchView
import com.gentlekboy.weeknine_jsonplaceholderapi.firstImplementation.utils.ConnectivityLiveData
import com.gentlekboy.weeknine_jsonplaceholderapi.firstImplementation.viewModel.MainViewModel
import com.gentlekboy.weeknine_jsonplaceholderapi.firstImplementation.viewModel.MainViewModelFactory

class PostActivity : AppCompatActivity(), OnclickPostItem {
    private lateinit var connectivityLiveData: ConnectivityLiveData
    private lateinit var binding: ActivityPostBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var postAdapter: PostAdapter
    private lateinit var inputMethodManager: InputMethodManager
    private lateinit var copyOfListOfPosts: MutableList<PostItems>
    private lateinit var reversedListOfPosts: MutableList<PostItems>
    private lateinit var listOfPosts: MutableList<PostItems>
    private lateinit var response: Posts
    private lateinit var newPostBody: String
    private val linearLayoutManager = LinearLayoutManager(this)

    //Instantiate variables
    private val repository = Repository()
    private val viewModelFactory = MainViewModelFactory(repository)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Initialize input method manager
        connectivityLiveData = ConnectivityLiveData(application)
        inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        setUpRecyclerViewAdapter()

        //Add a new post when the add post button is clicked
        binding.addPostButton.setOnClickListener {
            makeAPost()
        }

        //Set focus on edit text and open keyboard when edit text container is clicked
        binding.editTextContainer.setOnClickListener {
            binding.addPostEditText.requestFocus()
            inputMethodManager.showSoftInput(binding.addPostEditText, InputMethodManager.SHOW_IMPLICIT)
        }

        observeNetworkChanges()
        floatingActionButtonVisibility()
        filterPostsWithSearchView(binding.searchView, inputMethodManager, listOfPosts, copyOfListOfPosts, postAdapter)
    }

    //This function sets up the recycler view and adapter
    private fun setUpRecyclerViewAdapter(){
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
    }

    //This function makes a post request and adds new post to the recycler view
    private fun makeAPost(){
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        newPostBody = binding.addPostEditText.text.toString().trim()
        val id = listOfPosts.size + 1

        if (newPostBody.isNotEmpty()){
            viewModel.makeAPostToApi(11, id, "Kufre's Post", newPostBody)
            viewModel.pushPost2.observe(this, {

                if (it.isSuccessful){
                    addNewPostToRecyclerView(newPostBody, id)
                    Toast.makeText(this, "Post sent!.", Toast.LENGTH_LONG).show()
                }else{
                    Log.d("GKB", "sendPostToServer: ${it.code()}")
                    Log.d("GKB", "sendPostToServer: ${it.errorBody()}")
                    Toast.makeText(this, "Check your internet and try again.", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    //This function adds new post to the recycler view
    private fun addNewPostToRecyclerView(newPostBody: String, id: Int){
        val postItems = PostItems(newPostBody, id, "Kufre's Post", 11)
        listOfPosts.add(postItems)
        copyOfListOfPosts.add(postItems)
        postAdapter.notifyItemInserted(listOfPosts.indexOf(listOfPosts[0]))

        binding.addPostEditText.text = null
        binding.addPostEditText.clearFocus()

        //Hide keyboard after clicking the comment button
        inputMethodManager.hideSoftInputFromWindow(binding.addPostEditText.windowToken, 0)
    }

    //This function fetches posts and displays them on the UI
    private fun observeNetworkChanges(){
        connectivityLiveData.observe(this, { isAvailable ->
            when(isAvailable){
                true -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.loadingPosts.visibility = View.VISIBLE
                    fetchPosts()
                }
                false -> {
                    binding.appName.visibility = View.GONE
                    binding.searchView.visibility = View.GONE
                    binding.nestedScrollview.visibility = View.GONE
                    Log.d("GKB", "observeNetworkState: Network Unavailable")
                    Toast.makeText(this, "Network Unavailable", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun fetchPosts(){
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.fetchPosts()
        viewModel.allPosts.observe(this, {

            if (it.isSuccessful){
                response = it.body()!!
                postAdapter.addPosts(response)
                copyOfListOfPosts.addAll(listOfPosts)

                displayAppLayouts()

            }else{
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                Log.d("GKB", "onCreate: ${it.errorBody()}")
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
                binding.progressBar.visibility = View.GONE
                binding.loadingPosts.visibility = View.GONE
                binding.appName.visibility = View.VISIBLE
                binding.searchView.visibility = View.VISIBLE
                binding.nestedScrollview.visibility = View.VISIBLE
            }
        }, 500)
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

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}