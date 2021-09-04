package com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.gentlekboy.weeknine_jsonplaceholderapi.R
import com.gentlekboy.weeknine_jsonplaceholderapi.databinding.ActivityCommentBinding
import com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.model.adapter.CommentAdapter
import com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.model.data.comments.CommentItems
import com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.repository.Repository
import com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.viewmodel.MainViewModel
import com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.viewmodel.MainViewModelFactory
import kotlin.properties.Delegates

private lateinit var binding: ActivityCommentBinding
private lateinit var viewModel: MainViewModel

class CommentActivity : AppCompatActivity() {
    //Declare variables to be initialized
    private lateinit var commentAdapter: CommentAdapter
    private val linearLayoutManager = LinearLayoutManager(this)
    private lateinit var inputMethodManager: InputMethodManager
    private lateinit var listOfComments: ArrayList<CommentItems>
    private lateinit var postId: String
    private lateinit var postBody: String
    private var numberOfComments by Delegates.notNull<Int>()
    private var numberOfLikes by Delegates.notNull<Int>()

    //Initialize repository and main view model factory
    private val repository = Repository()
    private val viewModelFactory = MainViewModelFactory(repository)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Get data from post activity
        postId = intent.getIntExtra("postId", 1).toString()
        postBody = intent.getStringExtra("postBody").toString()

        //Initialize required variables
        inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        listOfComments = arrayListOf()
        numberOfComments = 5
        numberOfLikes = 25
        commentAdapter = CommentAdapter(listOfComments, this)

        //Set up recyclerview
        binding.commentsRecyclerview.adapter = commentAdapter
        binding.commentsRecyclerview.setHasFixedSize(true)
        binding.commentsRecyclerview.layoutManager = linearLayoutManager

        //Request focus and show keyboard when comment button is clicked
        binding.commentButton.setOnClickListener {
            binding.addComment.requestFocus()
            inputMethodManager.showSoftInput(binding.addComment, InputMethodManager.SHOW_IMPLICIT)
        }

        //Set on click listener on the share button to share post
        binding.shareButton.setOnClickListener {
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, postBody)
                type = "text/plain"
            }

            startActivity(Intent.createChooser(intent, "Share via"))
        }

        //Set on click listener on the like button to toggle like action
        binding.likeButton.setOnCheckedChangeListener { compoundButton, _ ->
            if (compoundButton.isChecked){
                numberOfLikes++
                binding.numberOfLikes.text = numberOfLikes.toString()
                binding.likeIcon.setColorFilter(resources.getColor(R.color.teal_200))
                binding.likeButton.setTextColor(resources.getColor(R.color.teal_200))
            }else{
                numberOfLikes--
                binding.numberOfLikes.text = numberOfLikes.toString()
                binding.likeIcon.setColorFilter(resources.getColor(R.color.black))
                binding.likeButton.setTextColor(resources.getColor(R.color.black))
            }
        }

        //Set on click listener on the back button to go back to previous activity
        binding.button.setOnClickListener {
            onBackPressed()
        }

        //add a comment when the comment button is clicked
        binding.postCommentButton.setOnClickListener {
            addNewComment()
        }

        populatePostDetails()
        displayCommentsOnUi()
    }

    //This function fetches and displays comments on the UI
    private fun displayCommentsOnUi(){
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.fetchComments(postId)
        viewModel.allComments.observe(this, {

            if (it.isSuccessful){
                val response = it.body()

                if (response != null){
                    commentAdapter.addComments(response)
                }
            }else{
                Log.d("GKB", "onCreate: ${it.errorBody()}")
            }
        })
    }

    //This function populates details of post from post activity
    private fun populatePostDetails(){
        binding.postBody.text = postBody

        if (postId.toInt() % 2 == 0 || postId.toInt() % 7 == 0){
            binding.profileImage.setImageResource(R.drawable.chinenye)
            binding.profileName.text = getString(R.string.chinenye)
            binding.profileBio.text = getString(R.string.chinenye_bio)
        }else{
            binding.profileImage.setImageResource(R.drawable.my_image)
            binding.profileName.text = getString(R.string.kufre_udoh)
            binding.profileBio.text = getString(R.string.software_engineer_at_google)
        }
    }

    //This function adds new comment to the UI
    private fun addNewComment(){
        val newInputtedComment = binding.addComment.text.toString().trim()

        if (newInputtedComment.isNotEmpty()){
            numberOfComments++

            val commentItems = CommentItems(newInputtedComment, "kufreabasi.udoh@decagon.dev", 11, "Kufre Udoh", numberOfComments)

            listOfComments.add(commentItems)
            commentAdapter.notifyItemInserted(listOfComments.size-1)

            binding.numberOfComments.text = numberOfComments.toString()
            binding.addComment.text = null
            binding.addComment.clearFocus()

            //Hide keyboard after clicking the comment button
            inputMethodManager.hideSoftInputFromWindow(binding.addComment.windowToken, 0)
        }
    }
}