package com.gentlekboy.weeknine_jsonplaceholderapi.secondImplementation.ui.posts.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gentlekboy.weeknine_jsonplaceholderapi.databinding.ActivityMvcAddBinding
import com.gentlekboy.weeknine_jsonplaceholderapi.secondImplementation.ui.posts.view.MvcPostActivity

private lateinit var binding: ActivityMvcAddBinding

class MvcAddActivity : AppCompatActivity() {
    private lateinit var newPostBody: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMvcAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            getNewPost()
        }
    }

    private fun getNewPost(){
        newPostBody = binding.addPostEditText.text.toString()

        val intent = Intent(this, MvcPostActivity::class.java)
        intent.putExtra("newPostBody", newPostBody)
        startActivity(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MvcPostActivity::class.java))
        finish()
    }
}