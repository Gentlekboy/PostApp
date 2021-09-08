package com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
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

class CommentActivity : AppCompatActivity() {
    //Declare variables to be initialized
    private lateinit var commentAdapter: CommentAdapter
    private lateinit var binding: ActivityCommentBinding
    private lateinit var viewModel: MainViewModel
    private val linearLayoutManager = LinearLayoutManager(this)
    private lateinit var inputMethodManager: InputMethodManager
    private lateinit var listOfComments: ArrayList<CommentItems>
    private var numberOfComments by Delegates.notNull<Int>()
    private var numberOfCommentsForNewPost by Delegates.notNull<Int>()
    private var numberOfLikes by Delegates.notNull<Int>()
    private var userId by Delegates.notNull<Int>()
    private var postIdToInteger by Delegates.notNull<Int>()
    private lateinit var postId: String
    private lateinit var postBody: String

    //Initialize repository and main view model factory
    private val repository = Repository()
    private val viewModelFactory = MainViewModelFactory(repository)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Get data from post activity
        postId = intent.getIntExtra("postId", 1).toString()
        userId = intent.getIntExtra("userId", 1)
        postBody = intent.getStringExtra("postBody").toString()

        //Initialize required variables
        inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        listOfComments = arrayListOf()
        numberOfComments = 5
//        numberOfLikes = 25
        commentAdapter = CommentAdapter(listOfComments, this)

        //Set up recyclerview
        binding.commentsRecyclerview.adapter = commentAdapter
        binding.commentsRecyclerview.setHasFixedSize(true)
        binding.commentsRecyclerview.layoutManager = linearLayoutManager

        //Convert post id of string type to integer
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

        if (postIdToInteger > 100){
            numberOfCommentsForNewPost = 0
            binding.comments.visibility = View.INVISIBLE
            binding.numberOfComments.visibility = View.INVISIBLE
            binding.likeIcon.visibility = View.INVISIBLE
            binding.numberOfLikes.visibility = View.INVISIBLE
        }

        //Set number of likes on UI
        binding.numberOfLikes.text = numberOfLikes.toString()

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

        when(userId){
            1 -> {
                binding.profileImage.setImageResource(R.drawable.user_1)
                binding.profileName.text = getString(R.string.peter_akam)
                binding.profileBio.text = getString(R.string.peter_bio)
            }
            2 -> {
                binding.profileImage.setImageResource(R.drawable.user_2)
                binding.profileName.text = getString(R.string.benjamin_obetta)
                binding.profileBio.text = getString(R.string.benjamin_bio)
            }
            3 -> {
                binding.profileImage.setImageResource(R.drawable.user_3)
                binding.profileName.text = getString(R.string.anthony_idoko)
                binding.profileBio.text = getString(R.string.anthony_bio)
            }
            4 -> {
                binding.profileImage.setImageResource(R.drawable.user_4)
                binding.profileName.text = getString(R.string.johnson_oyesina)
                binding.profileBio.text = getString(R.string.johnson_bio)
            }
            5 -> {
                binding.profileImage.setImageResource(R.drawable.user_5)
                binding.profileName.text = getString(R.string.emmanuel_oruminighen)
                binding.profileBio.text = getString(R.string.emmanuel_bio)
            }
            6 -> {
                binding.profileImage.setImageResource(R.drawable.user_6)
                binding.profileName.text = getString(R.string.john_doe)
                binding.profileBio.text = getString(R.string.john_bio)
            }
            7 -> {
                binding.profileImage.setImageResource(R.drawable.user_7)
                binding.profileName.text = getString(R.string.chinenye)
                binding.profileBio.text = getString(R.string.chinenye_bio)
            }
            8 -> {
                binding.profileImage.setImageResource(R.drawable.user_8)
                binding.profileName.text = getString(R.string.jennifer_santos)
                binding.profileBio.text = getString(R.string.jennifer_bio)
            }
            9 -> {
                binding.profileImage.setImageResource(R.drawable.user_9)
                binding.profileName.text = getString(R.string.joe_davids)
                binding.profileBio.text = getString(R.string.john_davids_bio)
            }
            10 -> {
                binding.profileImage.setImageResource(R.drawable.user_10)
                binding.profileName.text = getString(R.string.cassidy_banks)
                binding.profileBio.text = getString(R.string.cassidey_bio)
            }
            else -> {
                binding.profileImage.setImageResource(R.drawable.my_image)
                binding.profileName.text = getString(R.string.kufre_udoh)
                binding.profileBio.text = getString(R.string.software_engineer_at_google)
            }
        }
    }

    //This function adds new comment to the UI
    private fun addNewComment(){
        val newInputtedComment = binding.addComment.text.toString().trim()

        if (newInputtedComment.isNotEmpty()){
            val commentItems: CommentItems

            if (postIdToInteger > 100){
                numberOfCommentsForNewPost++

                if (numberOfCommentsForNewPost == 1){
                    binding.comments.text = getString(R.string._comment)
                } else {
                    binding.comments.text = getString(R.string.comments)
                }

                binding.numberOfComments.text = numberOfCommentsForNewPost.toString()
                commentItems = CommentItems(newInputtedComment, "kufreabasi.udoh@decagon.dev", 11, "Kufre Udoh", numberOfCommentsForNewPost)
            }else{
                numberOfComments++
                binding.numberOfComments.text = numberOfComments.toString()
                commentItems = CommentItems(newInputtedComment, "kufreabasi.udoh@decagon.dev", 11, "Kufre Udoh", numberOfComments)
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
}