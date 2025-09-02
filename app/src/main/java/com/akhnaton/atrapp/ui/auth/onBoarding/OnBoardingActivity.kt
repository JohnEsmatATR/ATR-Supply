package com.akhnaton.atrapp.ui.auth.onBoarding

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import com.akhnaton.atrapp.databinding.ActivityOnBoardingBinding
import com.akhnaton.atrapp.shared.BaseActivity
import com.akhnaton.atrapp.ui.auth.customer_code.code.CustomerInvoiceCodeActivity
import com.akhnaton.atrapp.ui.auth.customer_code.otp.OTPCustomerCodeActivity
import com.akhnaton.atrapp.ui.auth.login.LoginActivity
import com.akhnaton.atrapp.ui.auth.signUp.info.SignUpInfoActivity
import com.akhnaton.atrapp.ui.nav.HomeActivity

class OnBoardingActivity : BaseActivity() {
    lateinit var binding: ActivityOnBoardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        init()
        onClick()
    }

    private fun init() {

    }

    private fun onClick() {
        binding.btnSignUp.setOnClickListener {
            val intent = Intent(this@OnBoardingActivity, SignUpInfoActivity::class.java)
            startActivity(intent)
        }
        binding.btnLogin.setOnClickListener {
            val intent = Intent(this@OnBoardingActivity, LoginActivity::class.java)
            startActivity(intent)

        }
        binding.guest.setOnClickListener {
            startActivity(Intent(this@OnBoardingActivity,HomeActivity::class.java))
            finish()
        }
        binding.goToCustomerCodeLogin.setOnClickListener {
            startActivity(Intent(this@OnBoardingActivity, CustomerInvoiceCodeActivity::class.java))
        }
    }
}