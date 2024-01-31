package com.akhnaton.atrapp

import android.content.Intent
import android.os.Bundle
import com.akhnaton.atrapp.databinding.ActivitySignUpPdfBinding
import com.akhnaton.atrapp.shared.BaseActivity

class SignUpPdfActivity : BaseActivity() {
    lateinit var binding: ActivitySignUpPdfBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpPdfBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        onClick()
    }

    private fun init() {

    }

    private fun onClick() {
        binding.btnNext.setOnClickListener {
            val intent = Intent(this@SignUpPdfActivity, SignUpMapsActivity::class.java)
            startActivity(intent)
        }
        binding.btnBack.setOnClickListener {
            finish()
        }

    }
}