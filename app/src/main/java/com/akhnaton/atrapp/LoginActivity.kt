package com.akhnaton.atrapp

import android.os.Bundle
import com.akhnaton.atrapp.databinding.ActivityLoginBinding
import com.akhnaton.atrapp.shared.BaseActivity

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
    }
}