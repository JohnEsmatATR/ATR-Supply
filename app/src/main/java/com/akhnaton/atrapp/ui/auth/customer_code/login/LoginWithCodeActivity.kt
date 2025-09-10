package com.akhnaton.atrapp.ui.auth.customer_code.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.data.statuesValue.auth.loginWithCustomerCode.RegisterFromLineIntent
import com.akhnaton.atrapp.data.statuesValue.auth.loginWithCustomerCode.RegisterFromLineState
import com.akhnaton.atrapp.databinding.ActivityLoginWithCodeBinding
import com.akhnaton.atrapp.shared.BaseActivity
import com.akhnaton.atrapp.ui.auth.login.LoginActivity
import com.akhnaton.atrapp.ui.nav.HomeActivity
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginWithCodeActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginWithCodeBinding
    private lateinit var invoiceCode: String
    private lateinit var phoneNumber: String
    private lateinit var otp : String

    private val viewModel: RegisterFromLineViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginWithCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        invoiceCode = intent.getStringExtra("invoice_code").toString()
        phoneNumber = intent.getStringExtra("phone_number").toString()


        lifecycleScope.launch {
            viewModel.state.collectLatest { state ->
                when (state) {
                    is RegisterFromLineState.Error -> {
                        hideProgressDialog(binding.progressLoading)
                        showToastSnack(state.error, true)
                    }
                    RegisterFromLineState.Idle -> Unit
                    RegisterFromLineState.Loading -> {
                        showProgressDialog(binding.progressLoading)
                    }
                    is RegisterFromLineState.Success -> {
                        hideProgressDialog(binding.progressLoading)
                        if (state.state == 200){
                            showToastSnack(state.message, false)
                            delay(500)
                            startActivity(Intent(this@LoginWithCodeActivity , LoginActivity::class.java))
                            finishAffinity()
                        }
                    }
                }
            }
        }

        binding.btnLogin.setOnClickListener {
            val firstName = binding.txtFName.text.toString().trim()
            val lastName = binding.txtLName.text.toString().trim()
            val email = binding.txtEmail.text.toString().trim()
            val password = binding.txtPassword.text.toString().trim()
            val confirmPassword = binding.txtConfirmPassword.text.toString().trim()


            when {
                firstName.isEmpty() -> {
                    Toast.makeText(this, getString(R.string.error_first_name), Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                lastName.isEmpty() -> {
                    Toast.makeText(this, getString(R.string.error_last_name), Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                email.isEmpty() -> {
                    Toast.makeText(this, getString(R.string.error_email), Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                password.isEmpty() || confirmPassword.isEmpty() -> {
                    Toast.makeText(this, getString(R.string.error_password_empty), Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            if (password != confirmPassword) {
                Toast.makeText(this, getString(R.string.error_password_mismatch), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            val passwordPattern =
                Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&+=!])(?=\\S+\$).{8,}\$")

            if (!password.matches(passwordPattern)) {
                Toast.makeText(this, getString(R.string.error_password_weak), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }


            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                    return@addOnCompleteListener
                }

                val fbToken = task.result ?: ""
                Log.d("FCM", "Token: $fbToken")

                lifecycleScope.launch {
                    viewModel.registerIntent.send(
                        RegisterFromLineIntent.Register(
                            fbToken = fbToken,
                            code = invoiceCode,
                            email = email,
                            phone = phoneNumber,
                            lastName = lastName,
                            password = password,
                            firstName = firstName
                        )
                    )
                }
            }
        }


    }
}