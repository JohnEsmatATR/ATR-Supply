package com.akhnaton.atrSupply.ui.auth.onBoarding

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import com.akhnaton.atrSupply.databinding.ActivityWelcomeBinding
import com.akhnaton.atrSupply.shared.BaseActivity

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
            val intent = Intent(baseContext, OnBoardingActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}