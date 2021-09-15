package com.gentlekboy.weeknine_jsonplaceholderapi.firstImplementation.ui.posts

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
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

    //Instantiate variables
    private val repository = Repository()
    private val viewModelFactory = MainViewModelFactory(repository)
    private var newPostBody: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Initialize input method manager
        connectivityLiveData = ConnectivityLiveData(application)
        inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        newPostBody = intent.getStringExtra("newPostBody")

        setUpRecyclerViewAdapter()

        //Navigate to add post activity
        binding.view.setOnClickListener {
            startActivity(Intent(this, AddActivity::class.java))
        }

        //Navigate to add post activity
        binding.fab.setOnClickListener {
            startActivity(Intent(this, AddActivity::class.java))
        }

        makeAPostRequest()
        fetchPosts()
        floatingActionButtonVisibility(binding.nestedScrollview, binding.fab)
        observeNetworkChanges(
            this,
            application,
            this,
            binding.reloadMessage,
            binding.connectionLostText,
            binding.connectionLostImage,
            binding.progressBar,
            binding.loadingPosts,
            reversedListOfPosts,
            binding.appName,
            binding.searchView,
            binding.nestedScrollview
        )
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
        binding.postRecyclerview.layoutManager = LinearLayoutManager(this)
    }

    //This function makes a post request and adds new post to the recycler view
    private fun makeAPostRequest(){
        val id = listOfPosts.size + 1
        val postItems = newPostBody?.let { PostItems(it, id, "Kufre's Post", 11) }

        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        if (newPostBody?.isNotEmpty() == true){
            viewModel.makeAPostToApi(11, id, "Kufre's Post", newPostBody!!)
            viewModel.pushPost2.observe(this, {
                if (it.isSuccessful){
                    if (postItems != null) {
                        addNewPostToRecyclerView(postItems)
                        Toast.makeText(this, "Post sent!.", Toast.LENGTH_LONG).show()
                    }
                }else{
                    Log.d("GKB", "sendPostToServer: ${it.code()}")
                    Log.d("GKB", "sendPostToServer: ${it.errorBody()}")
                    Toast.makeText(this, "Check your internet and try again.", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    //This function adds new post to the recycler view
    private fun addNewPostToRecyclerView(postItems: PostItems){
        listOfPosts.add(postItems)
        copyOfListOfPosts.add(postItems)
        postAdapter.notifyItemInserted(listOfPosts.indexOf(listOfPosts[0]))
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

    //This function hides starting views and displays main layouts
    private fun displayAppLayouts(){
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            if (reversedListOfPosts.isNotEmpty()){
                binding.progressBar.visibility = View.GONE
                binding.loadingPosts.visibility = View.GONE

                binding.appName.visibility = View.VISIBLE
                binding.searchView.visibility = View.VISIBLE
                binding.nestedScrollview.visibility = View.VISIBLE
            }
        }, 1500)
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

    override fun sharePost(position: Int, bodyOfPost: String) {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, bodyOfPost)
            type = "text/plain"
        }

        ContextCompat.startActivity(this, Intent.createChooser(intent, "Share via"), bundleOf())
    }

    override fun checkLikeButton(position: Int, id: Int, compoundButton: CompoundButton, likeCounter: TextView, likeIcon: ImageView, likeButton: ToggleButton) {
        clickLikeButtonActions(id, compoundButton, likeCounter, likeIcon, likeButton, this)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
    }
}