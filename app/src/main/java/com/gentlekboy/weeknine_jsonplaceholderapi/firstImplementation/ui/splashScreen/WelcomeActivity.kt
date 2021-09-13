package com.gentlekboy.weeknine_jsonplaceholderapi.firstImplementation.ui.splashScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.gentlekboy.weeknine_jsonplaceholderapi.databinding.ActivityWelcomeBinding
import com.gentlekboy.weeknine_jsonplaceholderapi.firstImplementation.ui.posts.PostActivity

private lateinit var binding: ActivityWelcomeBinding

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navigateToNextActivity()
    }

    private fun navigateToNextActivity(){
        val handler = Handler()
        handler.postDelayed({
            startActivity(Intent(this, PostActivity::class.java))
            finish()
        }, 1500)
    }
}