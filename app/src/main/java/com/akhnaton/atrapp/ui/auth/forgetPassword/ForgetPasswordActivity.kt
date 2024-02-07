package com.akhnaton.atrapp.ui.auth.forgetPassword

import android.content.Intent
import android.os.Bundle
import com.akhnaton.atrapp.databinding.ActivityForgetPasswordBinding
import com.akhnaton.atrapp.shared.BaseActivity
import com.akhnaton.atrapp.ui.auth.otp.OTPActivity

class ForgetPasswordActivity : BaseActivity() {
    lateinit var binding: ActivityForgetPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        onClick()
    }

    private fun init() {

    }

    private fun onClick() {
        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.btnNext.setOnClickListener {
            val intent = Intent(baseContext, OTPActivity::class.java)
            startActivity(intent)
        }
    }
}