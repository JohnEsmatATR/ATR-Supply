package com.akhnaton.atrapp.ui.auth.onBoarding

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import com.akhnaton.atrapp.databinding.ActivityWelcomeBinding
import com.akhnaton.atrapp.shared.BaseActivity
import com.akhnaton.atrapp.ui.auth.login.LoginActivity

class WelcomeActivity : BaseActivity() {
    lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        init()
        onClick()
    }

    private fun init() {

    }

    private fun onClick() {
        binding.btnGetStarted.setOnClickListener {
            val intent = Intent(baseContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}