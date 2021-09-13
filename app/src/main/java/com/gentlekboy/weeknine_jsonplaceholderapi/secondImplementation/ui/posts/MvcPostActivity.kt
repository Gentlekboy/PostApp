package com.gentlekboy.weeknine_jsonplaceholderapi.secondImplementation.ui.posts

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
import com.gentlekboy.weeknine_jsonplaceholderapi.MainActivity
import com.gentlekboy.weeknine_jsonplaceholderapi.databinding.ActivityMvcPostBinding
import com.gentlekboy.weeknine_jsonplaceholderapi.firstImplementation.utils.ConnectivityLiveData
import com.gentlekboy.weeknine_jsonplaceholderapi.secondImplementation.model.adapter.MvcOnclickPostItem
import com.gentlekboy.weeknine_jsonplaceholderapi.secondImplementation.model.adapter.MvcPostAdapter
import com.gentlekboy.weeknine_jsonplaceholderapi.secondImplementation.model.api.MvcRetrofit
import com.gentlekboy.weeknine_jsonplaceholderapi.secondImplementation.model.data.posts.MvcPostItems
import com.gentlekboy.weeknine_jsonplaceholderapi.secondImplementation.model.data.posts.MvcPosts
import com.gentlekboy.weeknine_jsonplaceholderapi.secondImplementation.ui.comments.MvcCommentActivity
import com.gentlekboy.weeknine_jsonplaceholderapi.secondImplementation.ui.posts.MvcSearchViewInPost.mvcFilterPostsWithSearchView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MvcPostActivity : AppCompatActivity(), MvcOnclickPostItem {
    private lateinit var connectivityLiveData: ConnectivityLiveData
    private lateinit var binding: ActivityMvcPostBinding
    private lateinit var postAdapter: MvcPostAdapter
    private lateinit var inputMethodManager: InputMethodManager
    private val linearLayoutManager = LinearLayoutManager(this)
    private lateinit var copyOfListOfPosts: MutableList<MvcPostItems>
    private lateinit var reversedListOfPosts: MutableList<MvcPostItems>
    private lateinit var listOfPosts: MutableList<MvcPostItems>
    private lateinit var commentResponse: MvcPosts
    private var newPostBody: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMvcPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Initialize input method manager
        connectivityLiveData = ConnectivityLiveData(application)
        inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        newPostBody = intent.getStringExtra("newPostBody")

        setUpRecyclerViewAdapter()

        //Navigate to add post activity
        binding.view.setOnClickListener {
            startActivity(Intent(this, MvcAddActivity::class.java))
        }

        //Navigate to add post activity
        binding.fab.setOnClickListener {
            startActivity(Intent(this, MvcAddActivity::class.java))
        }

        makeAPostRequest()
        observeNetworkChanges()
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
        val id = listOfPosts.size + 1
        val postItems = newPostBody?.let { MvcPostItems(it, id, "Kufre's Post", 11) }

        if (newPostBody?.isNotEmpty() == true) {
            postItems?.let { MvcRetrofit.api.makeAPost(it) }
                ?.enqueue(object : Callback<MvcPostItems?> {
                    override fun onResponse(call: Call<MvcPostItems?>, response: Response<MvcPostItems?>) {
                        if (response.isSuccessful) {
                            newPostBody?.let { addNewPostToRecyclerView(postItems) }
                            Toast.makeText(this@MvcPostActivity, "Post sent!.", Toast.LENGTH_LONG).show()
                        } else {
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
        val connectedRetrofit = MvcRetrofit.api.getPost()
        connectedRetrofit.enqueue(object : Callback<MvcPosts?> {
            override fun onResponse(call: Call<MvcPosts?>, response: Response<MvcPosts?>) {
                commentResponse = response.body()!!

                if (response.isSuccessful){
                    postAdapter.addPosts(commentResponse)
                    copyOfListOfPosts.addAll(listOfPosts)
                    displayAppLayouts()
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
                binding.progressBar.visibility = View.GONE
                binding.loadingPosts.visibility = View.GONE
                binding.appName.visibility = View.VISIBLE
                binding.searchView.visibility = View.VISIBLE
                binding.nestedScrollview.visibility = View.VISIBLE
            }
        }, 100)
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

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}