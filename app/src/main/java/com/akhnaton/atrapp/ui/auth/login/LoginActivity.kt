package com.akhnaton.atrapp.ui.auth.login

import android.content.Intent
import android.os.Bundle
import com.akhnaton.atrapp.databinding.ActivityLoginBinding
import com.akhnaton.atrapp.shared.BaseActivity
import com.akhnaton.atrapp.ui.auth.forgetPassword.ForgetPasswordActivity
import com.akhnaton.atrapp.ui.nav.HomeActivity

class LoginActivity : BaseActivity() {
    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        onClick()
    }

    private fun init() {

    }

    private fun onClick() {
        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.txtForgetPassword.setOnClickListener {
            val intent = Intent(baseContext, ForgetPasswordActivity::class.java)
            startActivity(intent)
        }
        binding.btnLogin.setOnClickListener {
            val intent = Intent(baseContext, HomeActivity::class.java)
            startActivity(intent)
        }
    }
}