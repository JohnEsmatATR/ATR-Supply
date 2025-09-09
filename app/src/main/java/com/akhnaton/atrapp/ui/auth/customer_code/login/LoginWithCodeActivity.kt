package com.akhnaton.atrapp.ui.auth.customer_code.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.akhnaton.atrapp.data.statuesValue.auth.loginWithCustomerCode.RegisterFromLineIntent
import com.akhnaton.atrapp.data.statuesValue.auth.loginWithCustomerCode.RegisterFromLineState
import com.akhnaton.atrapp.databinding.ActivityLoginWithCodeBinding
import com.akhnaton.atrapp.shared.BaseActivity
import com.akhnaton.atrapp.ui.nav.HomeActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginWithCodeActivity : BaseActivity() {
    private lateinit var binding: ActivityLoginWithCodeBinding
    private lateinit var invoiceCode: String
    private lateinit var phoneNumber: String

    private val viewModel: RegisterFromLineViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginWithCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        invoiceCode = intent.getStringExtra("invoice_code").toString()
        phoneNumber = intent.getStringExtra("phone_number").toString()
        Log.d("OTP", "Invoice Code: $invoiceCode, Phone: $phoneNumber")


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
                            startActivity(Intent(this@LoginWithCodeActivity , HomeActivity::class.java))
                            finishAffinity()
                        }
                    }
                }
            }
        }


        binding.btnLogin.setOnClickListener {
            val email = binding.txtEmail.text.toString().trim()
            val password = binding.txtPassword.text.toString().trim()
            val confirmPassword = binding.txtConfirmPassword.text.toString().trim()

            if (password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "ادخل كلمة المرور", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "كلمة المرور غير متطابقة", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ثابت دلوقتي
            val firstName = "Jonathan"
            val lastName = "Ehab"
            val fbToken = "dummy_fb_token"
            val otp = "123456"

            // Send intent to ViewModel
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