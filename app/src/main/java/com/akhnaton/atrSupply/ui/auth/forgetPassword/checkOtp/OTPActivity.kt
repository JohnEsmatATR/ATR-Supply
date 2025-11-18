package com.akhnaton.atrSupply.ui.auth.forgetPassword.checkOtp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.akhnaton.atrSupply.data.statuesValue.auth.forgetPassword.chackOtp.CheckOtpIntent
import com.akhnaton.atrSupply.data.statuesValue.auth.forgetPassword.chackOtp.CheckOtpStatus
import com.akhnaton.atrSupply.ui.auth.forgetPassword.changePassword.NewPasswordActivity
import com.akhnaton.atrSupply.databinding.ActivityOtpactivityBinding
import com.akhnaton.atrSupply.shared.BaseActivity
import com.akhnaton.atrSupply.shared.Common
import kotlinx.coroutines.launch

class OTPActivity : BaseActivity() {
    lateinit var binding: ActivityOtpactivityBinding
    private val viewModel: CheckOTPViewModel by viewModels()
    var email = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        onClick()
    }

    private fun init() {
        email = intent!!.getStringExtra("email")?:""
        observeCheckOtp()
    }

    private fun onClick() {
        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.btnNext.setOnClickListener {
            if (isVerify()) {
                fetchCheckOTP()
            }
        }

    }

    private fun observeCheckOtp() {
        lifecycleScope.launch {
            viewModel.state.collect {
                when (it) {
                    is CheckOtpStatus.Idle -> Log.d(Common.KeroDebug, "observeLogin: it")
                    is CheckOtpStatus.Loading -> {
                        Log.d(Common.KeroDebug, "observeLogin: it")
                        showProgressDialog(binding.progressLoading)
                    }

                    is CheckOtpStatus.CheckOtp -> {
                        if (it.data.status == 200) {
                            hideProgressDialog(binding.progressLoading)
                            showToastSnack(it.data.message, false)

                            val intent = Intent(baseContext, NewPasswordActivity::class.java)
                            val otp = binding.otpView.otp.toString()
                            intent.putExtra("email", email)
                            intent.putExtra("otp", otp)
                            startActivity(intent)

                        } else {
                            hideProgressDialog(binding.progressLoading)
                            showToastSnack(it.data.message, true)
                        }
                    }

                    is CheckOtpStatus.Error -> {
                        Log.d(Common.KeroDebug, "observeLogin Error: ${it.error.toString()}")
                        hideProgressDialog(binding.progressLoading)
                        showToastSnack(it.error.toString(), true)
                    }
                }
            }
        }
    }

    private fun fetchCheckOTP() {
        lifecycleScope.launch {
            viewModel.sendOtpIntent.send(
                CheckOtpIntent.SendOtp(
                    email,
                    binding.otpView.otp.toString().lowercase().trim(),
                )
            )
        }
    }

    private fun isVerify(): Boolean {
        val email = binding.otpView.otp.toString()
        return email.isNotEmpty()
    }

}