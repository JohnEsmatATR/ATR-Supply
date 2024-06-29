package com.akhnaton.atrapp.ui.auth.signUp.info

import android.content.Intent
import android.os.Bundle
import com.akhnaton.atrapp.ui.auth.signUp.pdf.SignUpPdfActivity
import com.akhnaton.atrapp.databinding.ActivitySignUpInfoBinding
import com.akhnaton.atrapp.shared.BaseActivity
import com.akhnaton.atrapp.ui.auth.signUp.map.SignUpMapsActivity

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
            val firstName = binding.txtFirstName.text.toString()
            val lastName = binding.txtLastName.text.toString()
            val email = binding.txtEmail.text.toString()
            val password = binding.txtPassword.text.toString()
            val confirmPassword = binding.txtConfirmPassword.text.toString()
            val phone = binding.txtPhone.text.toString()
            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() ||
                password.isEmpty() || confirmPassword.isEmpty() || phone.isEmpty()
            ) {
                showToastSnack("please complete all fields.", true)
            } else if (password != confirmPassword) {
                showToastSnack("Passwords not matched.", true)
            } else {
                val intent = Intent(this@SignUpInfoActivity, SignUpMapsActivity::class.java)
                intent.putExtra("firstName", firstName)
                intent.putExtra("lastName", lastName)
                intent.putExtra("email", email)
                intent.putExtra("password", password)
                intent.putExtra("phone", phone)
                startActivity(intent)
            }
        }
        binding.btnBack.setOnClickListener {
            finish()
        }

    }
}