package com.gentlekboy.weeknine_jsonplaceholderapi.firstImplementation.ui.posts

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gentlekboy.weeknine_jsonplaceholderapi.databinding.ActivityAddBinding
import com.gentlekboy.weeknine_jsonplaceholderapi.secondImplementation.ui.posts.MvcPostActivity

private lateinit var binding: ActivityAddBinding

class AddActivity : AppCompatActivity() {
    private lateinit var newPostBody: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            getNewPost()
        }
    }

    private fun getNewPost(){
        newPostBody = binding.addPostEditText.text.toString()

        val intent = Intent(this, PostActivity::class.java)
        intent.putExtra("newPostBody", newPostBody)
        startActivity(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, PostActivity::class.java))
        finish()
    }
}