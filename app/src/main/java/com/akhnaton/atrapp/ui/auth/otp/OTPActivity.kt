package com.akhnaton.atrapp.ui.auth.otp

import android.content.Intent
import android.os.Bundle
import com.akhnaton.atrapp.ui.auth.forgetPassword.NewPasswordActivity
import com.akhnaton.atrapp.databinding.ActivityOtpactivityBinding
import com.akhnaton.atrapp.shared.BaseActivity

class OTPActivity : BaseActivity() {
    lateinit var binding: ActivityOtpactivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpactivityBinding.inflate(layoutInflater)
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
            val intent = Intent(baseContext, NewPasswordActivity::class.java)
            startActivity(intent)
        }

    }
}