package com.gentlekboy.weeknine_jsonplaceholderapi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gentlekboy.weeknine_jsonplaceholderapi.databinding.ActivityMainBinding
import com.gentlekboy.weeknine_jsonplaceholderapi.firstimplementation.ui.posts.PostActivity
import com.gentlekboy.weeknine_jsonplaceholderapi.secondimplementation.ui.posts.MvcPostActivity

private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Navigate to the first implementation
        binding.firstImplementationButton.setOnClickListener {
            startActivity(Intent(this, PostActivity::class.java))
        }

        //Navigate to the second implementation
        binding.secondImplementationButton.setOnClickListener {
            startActivity(Intent(this, MvcPostActivity::class.java))
        }
    }
}