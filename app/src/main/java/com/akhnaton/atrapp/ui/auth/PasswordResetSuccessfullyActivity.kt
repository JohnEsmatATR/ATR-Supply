package com.akhnaton.atrapp.ui.auth

import android.content.Intent
import android.os.Bundle
import com.akhnaton.atrapp.ui.auth.login.LoginActivity
import com.akhnaton.atrapp.databinding.ActivityPasswordResetSuccessfullyBinding
import com.akhnaton.atrapp.shared.BaseActivity

class PasswordResetSuccessfullyActivity : BaseActivity() {
    lateinit var binding: ActivityPasswordResetSuccessfullyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPasswordResetSuccessfullyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        onClick()
    }

    private fun init() {}

    private fun onClick() {
        binding.btnSubmit.setOnClickListener{
            val intent = Intent(baseContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}