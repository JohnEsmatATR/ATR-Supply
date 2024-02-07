package com.akhnaton.atrapp.ui.auth.forgetPassword

import android.content.Intent
import android.os.Bundle
import com.akhnaton.atrapp.databinding.ActivityNewPasswordBinding
import com.akhnaton.atrapp.shared.BaseActivity
import com.akhnaton.atrapp.ui.auth.PasswordResetSuccessfullyActivity

class NewPasswordActivity : BaseActivity() {
    lateinit var binding: ActivityNewPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        onClick()
    }

    private fun init() {}

    private fun onClick() {
        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.btnNext.setOnClickListener {
            val intent = Intent(baseContext, PasswordResetSuccessfullyActivity::class.java)
            startActivity(intent)
        }

    }
}