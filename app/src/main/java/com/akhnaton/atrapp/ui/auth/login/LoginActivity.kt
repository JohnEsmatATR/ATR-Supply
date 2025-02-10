package com.akhnaton.atrapp.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.akhnaton.atrapp.data.statuesValue.auth.login.LoginIntent
import com.akhnaton.atrapp.data.statuesValue.auth.login.LoginStatus
import com.akhnaton.atrapp.databinding.ActivityLoginBinding
import com.akhnaton.atrapp.shared.BaseActivity
import com.akhnaton.atrapp.shared.Common
import com.akhnaton.atrapp.shared.SharedPreferenceHelper
import com.akhnaton.atrapp.ui.auth.forgetPassword.sendOtp.ForgetPasswordActivity
import com.akhnaton.atrapp.ui.auth.signUp.info.SignUpInfoActivity
import com.akhnaton.atrapp.ui.nav.HomeActivity
import kotlinx.coroutines.launch

class LoginActivity : BaseActivity() {
    lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        onClick()
    }

    private fun init() {
        observeLogin()

    }

    private fun onClick() {
        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.txtForgetPassword.setOnClickListener {
            val intent = Intent(baseContext, ForgetPasswordActivity::class.java)
            startActivity(intent)
        }
        binding.txtSignUp.setOnClickListener {
            val intent = Intent(baseContext, SignUpInfoActivity::class.java)
            startActivity(intent)
        }
        binding.btnLogin.setOnClickListener {
            postLogin()
        }
    }


    private fun observeLogin() {
        lifecycleScope.launch {
            viewModel.state.collect {
                when (it) {
                    is LoginStatus.Idle -> Log.d(Common.KeroDebug, "observeLogin: it")
                    is LoginStatus.Loading -> {
                        Log.d(Common.KeroDebug, "observeLogin: it")
                        showProgressDialog(binding.progressLoading)
                    }

                    is LoginStatus.Login -> {
                        if (it.data.status == 200) {
                            hideProgressDialog(binding.progressLoading)
                            showToastSnack(it.data.message, false)

                            SharedPreferenceHelper.let { sharedPref ->
                                sharedPref.isLogged = true
                                sharedPref.userObj = it.data.data!!
                                sharedPref.userToken = it.data.data!!.token
                            }

                            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                            finishAffinity()

                        } else {
                            hideProgressDialog(binding.progressLoading)
                            showToastSnack(it.data.message, true)
                        }
                    }

                    is LoginStatus.Error -> {
                        Log.d(Common.KeroDebug, "observeLogin Error: ${it.error.toString()}")
                        hideProgressDialog(binding.progressLoading)
                        showToastSnack(it.error.toString(), true)
                    }
                }
            }
        }
    }

    private fun postLogin() {
        lifecycleScope.launch {
            viewModel.loginIntent.send(
                LoginIntent.Login(
                    binding.layoutEmail.editText!!.text.toString().lowercase().trim(),
                    binding.layoutPassword.editText!!.text.toString().trim(),
                )
            )
        }

    }

}