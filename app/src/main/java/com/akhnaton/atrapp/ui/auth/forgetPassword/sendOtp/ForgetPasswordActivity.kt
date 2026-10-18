package com.akhnaton.atrapp.ui.auth.forgetPassword.sendOtp

import android.content.Intent
import android.os.Bundle
import android.util.Log
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

    companion object {
        private const val LANGUAGE_AR = "ar"
        private const val HTTP_STATUS_SUCCESS = 200
        private const val AT_SYMBOL = "@"

        private const val EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
        private const val MIN_PHONE_LENGTH = 10
        private const val MAX_PHONE_LENGTH = 15

        private const val ERROR_EMPTY_INPUT = "Please enter your email or phone number"
        private const val ERROR_INVALID_EMAIL = "Please enter a valid email address"
        private const val ERROR_INVALID_PHONE = "Please enter a valid phone number"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        onClick()
    }

    private fun init() {
        val isArabic = SharedPreferenceHelper.language == LANGUAGE_AR
        if (isArabic) {
            binding.btnBack.setImageResource(R.drawable.ic_back_ar)
        } else {
            binding.btnBack.setImageResource(R.drawable.ic_back)
        }

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
            viewModel.state.collect { status ->
                when (status) {
                    is SendOtpStatus.Idle -> {
                        Log.d(Common.KeroDebug, "SendOtpStatus: Idle")
                    }
                    is SendOtpStatus.Loading -> {
                        Log.d(Common.KeroDebug, "SendOtpStatus: Loading")
                        showProgressDialog(binding.progressLoading)
                    }
                    is SendOtpStatus.SendOtp -> {
                        if (status.data.status == HTTP_STATUS_SUCCESS) {
                            hideProgressDialog(binding.progressLoading)
                            showToastSnack(status.data.message, false)

                            val email = binding.txtEmail.text.toString()

                            val intent = Intent(this@ForgetPasswordActivity, OTPActivity::class.java)
                            intent.putExtra(OTPActivity.EXTRA_EMAIL, email)
                            startActivity(intent)
                        } else {
                            hideProgressDialog(binding.progressLoading)
                            showToastSnack(status.data.message, true)
                        }
                    }
                    is SendOtpStatus.Error -> {
                        Log.d(Common.KeroDebug, "SendOtpStatus Error: ${status.error}")
                        hideProgressDialog(binding.progressLoading)
                        showToastSnack(status.error.toString(), true)
                    }
                }
            }
        }
    }

    private fun fetchSendOtp() {
        val email = binding.txtEmail.text.toString().trim()
        val loginIdentifier = if (email.contains(AT_SYMBOL)) email.lowercase() else email

        lifecycleScope.launch {
            viewModel.sendOtpIntent.send(
                SendOtpIntent.SendOtp(loginIdentifier)
            )
        }
    }

    private fun isVerify(): Boolean {
        val email = binding.txtEmail.text.toString().trim()

        if (email.isEmpty()) {
            binding.layoutEmail.error = ERROR_EMPTY_INPUT
            return false
        }

        if (email.contains(AT_SYMBOL)) {
            if (!isValidEmail(email.lowercase())) {
                binding.layoutEmail.error = ERROR_INVALID_EMAIL
                return false
            }
        } else {
            if (!isValidPhoneNumber(email)) {
                binding.layoutEmail.error = ERROR_INVALID_PHONE
                return false
            }
        }

        return true
    }

    private fun isValidEmail(email: String): Boolean {
        return email.matches(Regex(EMAIL_PATTERN, RegexOption.IGNORE_CASE))
    }

    private fun isValidPhoneNumber(phone: String): Boolean {
        return phone.all { it.isDigit() } && phone.length in MIN_PHONE_LENGTH..MAX_PHONE_LENGTH
    }
}