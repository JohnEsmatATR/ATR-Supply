package com.akhnaton.atrapp

import android.os.Bundle
import com.akhnaton.atrapp.databinding.ActivityForgetPasswordBinding
import com.akhnaton.atrapp.shared.BaseActivity

class ForgetPasswordActivity : BaseActivity() {
    lateinit var binding: ActivityForgetPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        onClick()
    }

    private fun init() {

    }

    private fun onClick() {

    }
}