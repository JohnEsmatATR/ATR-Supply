package com.akhnaton.atrSupply.ui.auth.forgetPassword.changePassword

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.akhnaton.atrSupply.data.statuesValue.auth.forgetPassword.changePassword.ChangePasswordIntent
import com.akhnaton.atrSupply.data.statuesValue.auth.forgetPassword.changePassword.ChangePasswordStatus
import com.akhnaton.atrSupply.databinding.ActivityNewPasswordBinding
import com.akhnaton.atrSupply.shared.BaseActivity
import com.akhnaton.atrSupply.shared.Common
import com.akhnaton.atrSupply.ui.auth.PasswordResetSuccessfullyActivity
import kotlinx.coroutines.launch

class NewPasswordActivity : BaseActivity() {
    lateinit var binding: ActivityNewPasswordBinding
    private val viewModel: ChangePasswordViewModel by viewModels()
    var email = ""
    var otp = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        onClick()
    }

    private fun init() {
        email = intent!!.getStringExtra("email") ?: ""
        otp = intent!!.getStringExtra("otp") ?: ""
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
            viewModel.state.collect {
                when (it) {
                    is ChangePasswordStatus.Idle -> Log.d(Common.KeroDebug, "observeLogin: it")
                    is ChangePasswordStatus.Loading -> {
                        Log.d(Common.KeroDebug, "observeLogin: it")
                        showProgressDialog(binding.progressLoading)
                    }

                    is ChangePasswordStatus.ChangePassword -> {
                        if (it.data.status == 200) {
                            hideProgressDialog(binding.progressLoading)
                            showToastSnack(it.data.message, false)

                            val intent = Intent(baseContext, PasswordResetSuccessfullyActivity::class.java)
                            startActivity(intent)
                            finishAffinity()

                        } else {
                            hideProgressDialog(binding.progressLoading)
                            showToastSnack(it.data.message, true)
                        }
                    }

                    is ChangePasswordStatus.Error -> {
                        Log.d(Common.KeroDebug, "observeLogin Error: ${it.error.toString()}")
                        hideProgressDialog(binding.progressLoading)
                        showToastSnack(it.error.toString(), true)
                    }
                }
            }
        }
    }

    private fun fetchChangePassword() {
        lifecycleScope.launch {
            viewModel.sendOtpIntent.send(
                ChangePasswordIntent.SendOtp(
                    email,
                    otp,
                    binding.layoutPassword.editText!!.text.toString(),
                )
            )
        }

    }

    private fun isVerify(): Boolean {
        val password = binding.layoutPassword.editText!!.text.toString()
        val confirmPassword = binding.layoutConfirmPassword.editText!!.text.toString()
        return password.equals(confirmPassword)
    }

}