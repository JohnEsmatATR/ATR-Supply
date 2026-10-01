package com.akhnaton.atrapp.ui.auth.forgetPassword.changePassword

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.data.statuesValue.auth.forgetPassword.changePassword.ChangePasswordIntent
import com.akhnaton.atrapp.data.statuesValue.auth.forgetPassword.changePassword.ChangePasswordStatus
import com.akhnaton.atrapp.databinding.ActivityNewPasswordBinding
import com.akhnaton.atrapp.shared.BaseActivity
import com.akhnaton.atrapp.shared.Common
import com.akhnaton.atrapp.shared.SharedPreferenceHelper
import com.akhnaton.atrapp.ui.auth.PasswordResetSuccessfullyActivity
import kotlinx.coroutines.launch

class NewPasswordActivity : BaseActivity() {

    lateinit var binding: ActivityNewPasswordBinding
    private val viewModel: ChangePasswordViewModel by viewModels()

    private var email = ""
    private var otp = ""

    companion object {
        private const val LANGUAGE_AR = "ar"
        private const val HTTP_STATUS_SUCCESS = 200

        // Intent Extras
        const val EXTRA_EMAIL = "email"
        const val EXTRA_OTP = "otp"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewPasswordBinding.inflate(layoutInflater)
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

        email = intent?.getStringExtra(EXTRA_EMAIL).orEmpty()
        otp = intent?.getStringExtra(EXTRA_OTP).orEmpty()

        observeChangePassword()
    }

    private fun onClick() {
        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.btnNext.setOnClickListener {
            if (isVerify()) {
                fetchChangePassword()
            }
        }
    }

    private fun observeChangePassword() {
        lifecycleScope.launch {
            viewModel.state.collect { status ->
                when (status) {
                    is ChangePasswordStatus.Idle -> {
                        Log.d(Common.KeroDebug, "ChangePasswordStatus: Idle")
                    }
                    is ChangePasswordStatus.Loading -> {
                        Log.d(Common.KeroDebug, "ChangePasswordStatus: Loading")
                        showProgressDialog(binding.progressLoading)
                    }
                    is ChangePasswordStatus.ChangePassword -> {
                        hideProgressDialog(binding.progressLoading)
                        if (status.data.status == HTTP_STATUS_SUCCESS) {
                            showToastSnack(status.data.message, false)

                            val intent = Intent(baseContext, PasswordResetSuccessfullyActivity::class.java)
                            startActivity(intent)
                            finishAffinity()
                        } else {
                            showToastSnack(status.data.message, true)
                        }
                    }
                    is ChangePasswordStatus.Error -> {
                        Log.d(Common.KeroDebug, "ChangePasswordStatus Error: ${status.error}")
                        hideProgressDialog(binding.progressLoading)
                        showToastSnack(status.error.toString(), true)
                    }
                }
            }
        }
    }

    private fun fetchChangePassword() {
        val password = binding.layoutPassword.editText?.text?.toString().orEmpty()
        lifecycleScope.launch {
            viewModel.sendOtpIntent.send(
                ChangePasswordIntent.SendOtp(
                    email,
                    otp,
                    password
                )
            )
        }
    }

    private fun isVerify(): Boolean {
        val password = binding.layoutPassword.editText?.text?.toString().orEmpty()
        val confirmPassword = binding.layoutConfirmPassword.editText?.text?.toString().orEmpty()
        return password == confirmPassword
    }
}