package com.gentlekboy.weeknine_jsonplaceholderapi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.gentlekboy.weeknine_jsonplaceholderapi.databinding.ActivityMainBinding
import com.gentlekboy.weeknine_jsonplaceholderapi.firstImplementation.ui.splashScreen.WelcomeActivity
import com.gentlekboy.weeknine_jsonplaceholderapi.secondImplementation.ui.splashScreen.MvcWelcomeActivity

private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Navigate to the first implementation
        binding.firstImplementationButton.setOnClickListener {
            startActivity(Intent(this, WelcomeActivity::class.java))
        }

        //Navigate to the second implementation
        binding.secondImplementationButton.setOnClickListener {
            startActivity(Intent(this, MvcWelcomeActivity::class.java))
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}