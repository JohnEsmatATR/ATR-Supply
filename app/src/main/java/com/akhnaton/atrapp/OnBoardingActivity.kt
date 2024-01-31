package com.akhnaton.atrapp

import android.content.Intent
import android.os.Bundle
import com.akhnaton.atrapp.databinding.ActivityOnBoardingBinding
import com.akhnaton.atrapp.shared.BaseActivity

class OnBoardingActivity : BaseActivity() {
    lateinit var binding: ActivityOnBoardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        onClick()
    }

    private fun init() {

    }

    private fun onClick() {
        binding.btnSignUp.setOnClickListener {
            val intent = Intent(this@OnBoardingActivity, SignUpInfoActivity::class.java)
            startActivity(intent)
        }
        binding.btnLogin.setOnClickListener {
            val intent = Intent(this@OnBoardingActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}