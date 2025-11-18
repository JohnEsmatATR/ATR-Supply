package com.akhnaton.atrSupply.ui.auth.onBoarding

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import com.akhnaton.atrSupply.databinding.ActivityOnBoardingBinding
import com.akhnaton.atrSupply.shared.BaseActivity
import com.akhnaton.atrSupply.ui.auth.customer_code.code.CustomerInvoiceCodeActivity
import com.akhnaton.atrSupply.ui.auth.login.LoginActivity
import com.akhnaton.atrSupply.ui.auth.signUp.info.SignUpInfoActivity
import com.akhnaton.atrSupply.ui.nav.HomeActivity

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