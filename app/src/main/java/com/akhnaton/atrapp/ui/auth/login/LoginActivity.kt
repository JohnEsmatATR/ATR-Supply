package com.akhnaton.atrapp.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.data.statuesValue.auth.login.LoginIntent
import com.akhnaton.atrapp.data.statuesValue.auth.login.LoginStatus
import com.akhnaton.atrapp.databinding.ActivityLoginBinding
import com.akhnaton.atrapp.shared.BaseActivity
import com.akhnaton.atrapp.shared.Common
import com.akhnaton.atrapp.shared.SharedPreferenceHelper
import com.akhnaton.atrapp.ui.auth.forgetPassword.sendOtp.ForgetPasswordActivity
import com.akhnaton.atrapp.ui.auth.signUp.info.SignUpInfoActivity
import com.akhnaton.atrapp.ui.nav.HomeActivity
import com.google.firebase.messaging.FirebaseMessaging
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
        var isArabic = SharedPreferenceHelper.language == "ar"
        if (isArabic) binding.btnBack.setImageResource(R.drawable.ic_back_ar)
        else binding.btnBack.setImageResource(R.drawable.ic_back)

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
        val email = binding.layoutEmail.editText?.text?.toString()?.trim() ?: ""
        val password = binding.layoutPassword.editText?.text?.toString()?.trim() ?: ""


        if (email.isEmpty()) {
            Toast.makeText(this, "Please enter your email or phone number", Toast.LENGTH_SHORT).show()
            return
        }

        // Validate email format if input contains @ (looks like email)
        if (email.contains("@") && !isValidEmail(email.lowercase())) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_password_empty), Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }

            val fbToken = task.result ?: ""
            Log.d("FCM", "Token: $fbToken")

            // Lowercase email only if it's an email (contains @), otherwise keep as is (phone number)
            val loginIdentifier = if (email.contains("@")) email.lowercase() else email

            lifecycleScope.launch {
                viewModel.loginIntent.send(
                    LoginIntent.Login(
                        loginIdentifier,
                        password,
                        fbToken
                    )
                )
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}"
        return email.matches(Regex(emailPattern))
    }

}