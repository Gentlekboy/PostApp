package com.gentlekboy.weeknine_jsonplaceholderapi.secondImplementation.ui.comments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.gentlekboy.weeknine_jsonplaceholderapi.R
import com.gentlekboy.weeknine_jsonplaceholderapi.databinding.ActivityMvcCommentBinding
import com.gentlekboy.weeknine_jsonplaceholderapi.firstImplementation.ui.comments.PopulatePostDetails.populatePostDetailsInCommentActivity
import com.gentlekboy.weeknine_jsonplaceholderapi.firstImplementation.utils.ConnectivityLiveData
import com.gentlekboy.weeknine_jsonplaceholderapi.secondImplementation.model.adapter.MvcCommentAdapter
import com.gentlekboy.weeknine_jsonplaceholderapi.secondImplementation.model.api.MvcRetrofit
import com.gentlekboy.weeknine_jsonplaceholderapi.secondImplementation.model.data.comments.MvcComments
import com.gentlekboy.weeknine_jsonplaceholderapi.secondImplementation.model.data.comments.MvcCommentsItem
import com.gentlekboy.weeknine_jsonplaceholderapi.secondImplementation.ui.posts.MvcPostActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates

class MvcCommentActivity : AppCompatActivity() {
    private lateinit var connectivityLiveData: ConnectivityLiveData
    private lateinit var binding: ActivityMvcCommentBinding
    private lateinit var commentAdapter: MvcCommentAdapter
    private lateinit var inputMethodManager: InputMethodManager
    private lateinit var listOfComments: ArrayList<MvcCommentsItem>
    private lateinit var postId: String
    private lateinit var postBody: String
    private val linearLayoutManager = LinearLayoutManager(this)
    private var numberOfComments by Delegates.notNull<Int>()
    private var numberOfCommentsForNewPost by Delegates.notNull<Int>()
    private var numberOfLikes by Delegates.notNull<Int>()
    private var userId by Delegates.notNull<Int>()
    private var postIdToInteger by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMvcCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Initialize required variables
        connectivityLiveData = ConnectivityLiveData(application)
        inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        numberOfComments = 5

        getDataFromPreviousActivity()
        setUpRecyclerViewAdapter()
        setNumberOfLikes()
        hideLikesAndCommentsForNewPostsInTheCommentActivity()

        //Set number of likes on UI
        binding.numberOfLikes.text = numberOfLikes.toString()

        //Request focus and show keyboard when comment button is clicked
        binding.commentButton.setOnClickListener {
            binding.addComment.requestFocus()
            inputMethodManager.showSoftInput(binding.addComment, InputMethodManager.SHOW_IMPLICIT)
        }

        //Set on click listener on the share button to share post
        binding.shareButton.setOnClickListener {
            sharePost()
        }

        //Set on click listener on the like button to toggle like action
        binding.likeButton.setOnCheckedChangeListener { compoundButton, _ ->
            toggleTheLikeButton(compoundButton)
        }

        //Set on click listener on the back button to go back to previous activity
        binding.button.setOnClickListener {
            startActivity(Intent(this, MvcPostActivity::class.java))
        }

        //Add a comment when the comment button is clicked
        binding.postCommentButton.setOnClickListener {
            addNewComment()
        }

        observeNetworkChanges()
        populatePostDetailsInCommentActivity(binding.postBody, postBody, userId, binding.profileImage, binding.profileName, binding.profileBio, this)
    }

    override fun onBackPressed() {
        super.onBackPressed()

        startActivity(Intent(this, MvcPostActivity::class.java))
    }

    //This function gets data from the previous activity using intents
    private fun getDataFromPreviousActivity() {
        postId = intent.getIntExtra("postId", 1).toString()
        userId = intent.getIntExtra("userId", 1)
        postBody = intent.getStringExtra("postBody").toString()
    }

    //This function sets up the recycler view and adapter
    private fun setUpRecyclerViewAdapter() {
        listOfComments = arrayListOf()
        commentAdapter = MvcCommentAdapter(listOfComments, this)
        binding.commentsRecyclerview.adapter = commentAdapter
        binding.commentsRecyclerview.setHasFixedSize(true)
        binding.commentsRecyclerview.layoutManager = linearLayoutManager
    }

    //This function sets number of likes
    private fun setNumberOfLikes(){
        postIdToInteger = postId.toInt()

        if (postIdToInteger < 101 && postIdToInteger % 2 == 0){
            numberOfLikes = 6
        } else if (postIdToInteger < 101 && postIdToInteger % 3 == 0){
            numberOfLikes = 12
        } else if (postIdToInteger < 101 && postIdToInteger % 5 == 0){
            numberOfLikes = 8
        } else if (postIdToInteger < 101 && postIdToInteger % 7 == 0){
            numberOfLikes = 14
        } else if (postIdToInteger < 101 && postIdToInteger % 11 == 0){
            numberOfLikes = 2
        } else if (postIdToInteger < 101 && postIdToInteger % 13 == 0){
            numberOfLikes = 13
        } else if (postIdToInteger < 101 && postIdToInteger % 17 == 0){
            numberOfLikes = 3
        } else if (postIdToInteger < 101 && postIdToInteger % 19 == 0){
            numberOfLikes = 1
        } else if (postIdToInteger > 100){
            numberOfLikes = 0
        } else{
            numberOfLikes = 36
        }
    }

    //This function controls view visibility in the comment section of a newly added post
    private fun hideLikesAndCommentsForNewPostsInTheCommentActivity() {
        if (postIdToInteger > 100){
            numberOfCommentsForNewPost = 0
            binding.comments.visibility = View.INVISIBLE
            binding.numberOfComments.visibility = View.INVISIBLE
            binding.likeIcon.visibility = View.INVISIBLE
            binding.numberOfLikes.visibility = View.INVISIBLE
        }
    }

    //This function enables sharing of posts
    private fun sharePost() {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, postBody)
            type = "text/plain"
        }

        startActivity(Intent.createChooser(intent, "Share via"))
    }

    //This function controls toggling the like button
    private fun toggleTheLikeButton(compoundButton: CompoundButton) {
        if (compoundButton.isChecked){
            numberOfLikes++

            if (postIdToInteger > 100){
                binding.likeIcon.visibility = View.VISIBLE
                binding.numberOfLikes.visibility = View.VISIBLE
            }

            binding.numberOfLikes.text = numberOfLikes.toString()
            binding.likeIcon.setColorFilter(resources.getColor(R.color.blue))
            binding.likeButton.setTextColor(resources.getColor(R.color.blue))
        }else{
            numberOfLikes--

            if (postIdToInteger >100){
                binding.numberOfLikes.visibility = View.INVISIBLE
                binding.likeIcon.visibility = View.INVISIBLE
            }

            binding.numberOfLikes.text = numberOfLikes.toString()
            binding.likeIcon.setColorFilter(resources.getColor(R.color.black))
            binding.likeButton.setTextColor(resources.getColor(R.color.black))
        }
    }

    //This function adds new comment to the recyclerview
    private fun addNewComment(){
        val newInputtedComment = binding.addComment.text.toString().trim()

        if (newInputtedComment.isNotEmpty()){
            val commentItems: MvcCommentsItem

            if (postIdToInteger > 100){
                numberOfCommentsForNewPost++

                if (numberOfCommentsForNewPost == 1){
                    binding.comments.text = getString(R.string._comment)
                } else {
                    binding.comments.text = getString(R.string.comments)
                }
                binding.numberOfComments.text = numberOfCommentsForNewPost.toString()
                commentItems = MvcCommentsItem(newInputtedComment, "kufreabasi.udoh@decagon.dev", 11, "Kufre Udoh", numberOfCommentsForNewPost)
            }else{
                numberOfComments++
                binding.numberOfComments.text = numberOfComments.toString()
                commentItems = MvcCommentsItem(newInputtedComment, "kufreabasi.udoh@decagon.dev", 11, "Kufre Udoh", numberOfComments)
            }

            listOfComments.add(commentItems)
            commentAdapter.notifyItemInserted(listOfComments.size-1)

            binding.comments.visibility = View.VISIBLE
            binding.numberOfComments.visibility = View.VISIBLE
            binding.addComment.text = null
            binding.addComment.clearFocus()

            //Hide keyboard after clicking the comment button
            inputMethodManager.hideSoftInputFromWindow(binding.addComment.windowToken, 0)
        }
    }

    private fun observeNetworkChanges(){
        connectivityLiveData.observe(this, { isAvailable ->
            when(isAvailable){
                true -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.loadingComments.visibility = View.VISIBLE
                    fetchComments()
                }
                false -> {
                    binding.nestedScrollview.visibility = View.GONE
                    binding.cardViewComment.visibility = View.GONE
                    binding.addComment.visibility = View.GONE
                    binding.postCommentButton.visibility = View.GONE
                    binding.button.visibility = View.GONE

                    Log.d("GKB", "observeNetworkState: Network Unavailable")
                    Toast.makeText(this, "Network Unavailable", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    //This function fetches comments based on the post id selected
    private fun fetchComments(){
        val connectedRetrofit = MvcRetrofit.api.getComments(postId)
        connectedRetrofit.enqueue(object : Callback<MvcComments?> {
            override fun onResponse(call: Call<MvcComments?>, response: Response<MvcComments?>) {
                val fetchedComments = response.body()

                commentAdapter.addComments(fetchedComments)
                displayAppLayouts()
                Log.d("GKB", "onFailure: $fetchedComments")
            }

            override fun onFailure(call: Call<MvcComments?>, t: Throwable) {
                Log.d("GKB", "onFailure: ${t.message}")
            }
        })
    }

    //This function hides starting views and displays main layouts
    private fun displayAppLayouts(){
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            if (listOfComments.isNotEmpty()){
                binding.progressBar.visibility = View.GONE
                binding.loadingComments.visibility = View.GONE

                binding.nestedScrollview.visibility = View.VISIBLE
                binding.cardViewComment.visibility = View.VISIBLE
                binding.addComment.visibility = View.VISIBLE
                binding.postCommentButton.visibility = View.VISIBLE
                binding.button.visibility = View.VISIBLE
            }
        }, 100)
    }
}