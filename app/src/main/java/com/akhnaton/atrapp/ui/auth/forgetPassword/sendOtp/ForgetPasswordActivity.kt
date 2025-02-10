package com.akhnaton.atrapp.ui.auth.forgetPassword.sendOtp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.akhnaton.atrapp.data.statuesValue.auth.forgetPassword.sendOtp.SendOtpIntent
import com.akhnaton.atrapp.data.statuesValue.auth.forgetPassword.sendOtp.SendOtpStatus
import com.akhnaton.atrapp.databinding.ActivityForgetPasswordBinding
import com.akhnaton.atrapp.shared.BaseActivity
import com.akhnaton.atrapp.shared.Common
import com.akhnaton.atrapp.ui.auth.forgetPassword.checkOtp.OTPActivity
import com.akhnaton.atrapp.ui.nav.cart.checkout.CheckoutActivity
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
        observeSendOtp()
    }

    private fun onClick() {
        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.btnNext.setOnClickListener {
            if (isVerify()) {
                fetchSendOtp()
            }
        }
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
        lifecycleScope.launch {
            viewModel.sendOtpIntent.send(
                SendOtpIntent.SendOtp(
                    binding.layoutEmail.editText!!.text.toString().lowercase().trim(),
                )
            )
        }

    }

    private fun isVerify(): Boolean {
        val email = binding.txtEmail.text.toString()
        return email.isNotEmpty()
    }
}