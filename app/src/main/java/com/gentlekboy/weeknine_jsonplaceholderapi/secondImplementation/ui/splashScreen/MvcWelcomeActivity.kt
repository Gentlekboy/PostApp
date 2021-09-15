package com.gentlekboy.weeknine_jsonplaceholderapi.secondImplementation.ui.splashScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.gentlekboy.weeknine_jsonplaceholderapi.databinding.ActivityMvcWelcomeBinding
import com.gentlekboy.weeknine_jsonplaceholderapi.secondImplementation.ui.posts.view.MvcPostActivity

private lateinit var binding: ActivityMvcWelcomeBinding

class MvcWelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMvcWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navigateToNextActivity()
    }

    private fun navigateToNextActivity(){
        val handler = Handler()
        handler.postDelayed({
            startActivity(Intent(this, MvcPostActivity::class.java))
            finish()
        }, 1500)
    }
}