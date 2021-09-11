package com.gentlekboy.weeknine_jsonplaceholderapi.secondimplementation.ui.posts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gentlekboy.weeknine_jsonplaceholderapi.databinding.ActivityMvcAddBinding

private lateinit var binding: ActivityMvcAddBinding

class MvcAddActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMvcAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}