package com.akhnaton.atrapp.ui.auth.forgetPassword.sendOtp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.data.statuesValue.auth.forgetPassword.sendOtp.SendOtpIntent
import com.akhnaton.atrapp.data.statuesValue.auth.forgetPassword.sendOtp.SendOtpStatus
import com.akhnaton.atrapp.databinding.ActivityForgetPasswordBinding
import com.akhnaton.atrapp.shared.BaseActivity
import com.akhnaton.atrapp.shared.Common
import com.akhnaton.atrapp.shared.SharedPreferenceHelper
import com.akhnaton.atrapp.ui.auth.forgetPassword.checkOtp.OTPActivity
import kotlinx.coroutines.launch

class ForgetPasswordActivity : BaseActivity() {
    lateinit var binding: ActivityForgetPasswordBinding
    private val viewModel: SendOTPViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        onClick()
    }

    private fun init() {
        var isArabic = SharedPreferenceHelper.language == "ar"
        if (isArabic) binding.btnBack.setImageResource(R.drawable.ic_back_ar)
        else binding.btnBack.setImageResource(R.drawable.ic_back)

        observeSendOtp()
    }

    private fun onClick() {
        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.btnNext.setOnClickListener {
            clearErrors()
            if (isVerify()) {
                fetchSendOtp()
            }
        }
        
        // Clear error when user starts typing
        binding.txtEmail.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.layoutEmail.error = null
            }
        }
        
        binding.txtEmail.setOnClickListener {
            binding.layoutEmail.error = null
        }
    }
    
    private fun clearErrors() {
        binding.layoutEmail.error = null
    }

    private fun observeSendOtp() {
        lifecycleScope.launch {
            viewModel.state.collect {
                when (it) {
                    is SendOtpStatus.Idle -> Log.d(Common.KeroDebug, "observeLogin: it")
                    is SendOtpStatus.Loading -> {
                        Log.d(Common.KeroDebug, "observeLogin: it")
                        showProgressDialog(binding.progressLoading)
                    }

                    is SendOtpStatus.SendOtp -> {
                        if (it.data.status == 200) {
                            hideProgressDialog(binding.progressLoading)
                            showToastSnack(it.data.message, false)

                            val email = binding.txtEmail.text.toString()

                            val intent = Intent(this@ForgetPasswordActivity, OTPActivity::class.java)
                            intent.putExtra("email", email)
                            startActivity(intent)

                        } else {
                            hideProgressDialog(binding.progressLoading)
                            showToastSnack(it.data.message, true)
                        }
                    }

                    is SendOtpStatus.Error -> {
                        Log.d(Common.KeroDebug, "observeLogin Error: ${it.error.toString()}")
                        hideProgressDialog(binding.progressLoading)
                        showToastSnack(it.error.toString(), true)
                    }
                }
            }
        }
    }

    private fun fetchSendOtp() {
        val email = binding.txtEmail.text.toString().trim()
        
        // Lowercase email only if it's an email (contains @), otherwise keep as is (phone number)
        val loginIdentifier = if (email.contains("@")) email.lowercase() else email
        
        lifecycleScope.launch {
            viewModel.sendOtpIntent.send(
                SendOtpIntent.SendOtp(
                    loginIdentifier
                )
            )
        }
    }

    private fun isVerify(): Boolean {
        val email = binding.txtEmail.text.toString().trim()
        
        if (email.isEmpty()) {
            binding.layoutEmail.error = "Please enter your email or phone number"
            return false
        }
        
        // Validate email format if input contains @ (looks like email)
        if (email.contains("@")) {
            if (!isValidEmail(email.lowercase())) {
                binding.layoutEmail.error = "Please enter a valid email address"
                return false
            }
        } else {
            // Validate phone number if it doesn't contain @ (looks like phone number)
            if (!isValidPhoneNumber(email)) {
                binding.layoutEmail.error = "Please enter a valid phone number"
                return false
            }
        }
        
        return true
    }
    
    private fun isValidEmail(email: String): Boolean {
        // More comprehensive email validation pattern
        val emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
        return email.matches(Regex(emailPattern, RegexOption.IGNORE_CASE))
    }
    
    private fun isValidPhoneNumber(phone: String): Boolean {
        // Phone number should be digits only and typically 10-15 digits
        return phone.all { it.isDigit() } && phone.length >= 10 && phone.length <= 15
    }
}