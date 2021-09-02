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
import android.view.Menu
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.gentlekboy.weeknine_jsonplaceholderapi.R
import com.gentlekboy.weeknine_jsonplaceholderapi.databinding.ActivityPostBinding
import com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.model.adapter.OnclickPostItem
import com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.model.adapter.PostAdapter
import com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.model.data.posts.PostItems
import com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.repository.Repository
import com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.viewmodel.MainViewModel
import com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.viewmodel.MainViewModelFactory

class PostActivity : AppCompatActivity(), OnclickPostItem {
    private lateinit var binding: ActivityPostBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var postAdapter: PostAdapter
    private val linearLayoutManager = LinearLayoutManager(this)
    private val repository = Repository()
    private val viewModelFactory = MainViewModelFactory(repository)
    private lateinit var temporaryListOfPosts: ArrayList<PostItems>
    private lateinit var mainListOfPosts: ArrayList<PostItems>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        temporaryListOfPosts = arrayListOf()
        mainListOfPosts = arrayListOf()

        //Set up adapter
        postAdapter = PostAdapter(mainListOfPosts, this, this)

        //Set up recyclerview
        binding.postRecyclerview.adapter = postAdapter
        binding.postRecyclerview.setHasFixedSize(true)
        binding.postRecyclerview.layoutManager = linearLayoutManager

        binding.makePost.setOnClickListener {
            startActivity(Intent(this, AddPostActivity::class.java))
        }

        displayPostsOnUi()
    }

    private fun displayPostsOnUi(){
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.fetchPosts()
        viewModel.allPosts.observe(this, {

            if (it.isSuccessful){
                val response = it.body()

                if (response != null){
                    postAdapter.addPosts(response)

                    temporaryListOfPosts.addAll(mainListOfPosts)
                }
            }else{
                Log.d("GKB", "onCreate: ${it.errorBody()}")
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_items, menu)
        val item = menu?.findItem(R.id.search_action)
        val searchview = item?.actionView as SearchView

        searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                mainListOfPosts.clear()
                Log.d("GKB", "SHOULD BE EMPTY: $mainListOfPosts")

                val searchText = newText?.lowercase()?.trim()

                if (searchText != null) {
                    if (searchText.isNotEmpty()){
                        temporaryListOfPosts.forEach {
                            if (it.body.lowercase().contains(searchText)){
                                mainListOfPosts.add(it)
                            }
                        }

                        Log.d("GKB", "SHOULD CONTAIN SOME DATA: $mainListOfPosts")

                        postAdapter.notifyDataSetChanged()
                    }else{
                        mainListOfPosts.clear()

                        Log.d("GKB", "ELSE -> SHOULD BE EMPTY: $mainListOfPosts")
                        mainListOfPosts.addAll(temporaryListOfPosts)

                        Log.d("GKB", "SHOULD CONTAIN ALL DATA: $mainListOfPosts")
                        postAdapter.notifyDataSetChanged()
                    }
                }
                return true
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun clickPostItem(position: Int, id: Int, userId: Int) {
        val intent = Intent(this, CommentActivity::class.java)
        intent.putExtra("postId", id)
        intent.putExtra("userId", userId)
        startActivity(intent)
    }
}