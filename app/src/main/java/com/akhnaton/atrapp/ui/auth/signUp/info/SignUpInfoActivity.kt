package com.akhnaton.atrapp.ui.auth.signUp.info

import android.content.Intent
import android.os.Bundle
import com.akhnaton.atrapp.ui.auth.signUp.pdf.SignUpPdfActivity
import com.akhnaton.atrapp.databinding.ActivitySignUpInfoBinding
import com.akhnaton.atrapp.shared.BaseActivity

class SignUpInfoActivity : BaseActivity() {
    lateinit var binding: ActivitySignUpInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        onClick()
    }

    private fun init() {

    }

    private fun onClick() {
        binding.btnNext.setOnClickListener {
            val intent = Intent(this@SignUpInfoActivity, SignUpPdfActivity::class.java)
            startActivity(intent)
        }
        binding.btnBack.setOnClickListener {
            finish()
        }

    }
}