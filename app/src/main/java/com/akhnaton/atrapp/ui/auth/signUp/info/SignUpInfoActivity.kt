package com.akhnaton.atrapp.ui.auth.signUp.info

import android.content.Intent
import android.os.Bundle
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.databinding.ActivitySignUpInfoBinding
import com.akhnaton.atrapp.shared.BaseActivity
import com.akhnaton.atrapp.shared.SharedPreferenceHelper
import com.akhnaton.atrapp.ui.auth.signUp.map.SignUpMapsActivity

class SignUpInfoActivity : BaseActivity() {
    lateinit var binding: ActivitySignUpInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleTopBottomKeyboard()

        init()
        onClick()
    }

    private fun handleTopBottomKeyboard() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())

            val bottomPadding = maxOf(imeInsets.bottom, systemBars.bottom)

            binding.root.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                bottomPadding
            )

            insets
        }
    }

    private fun init() {
        var isArabic = SharedPreferenceHelper.language == "ar"
        if (isArabic) binding.btnBack.setImageResource(R.drawable.ic_back_ar)
        else binding.btnBack.setImageResource(R.drawable.ic_back)
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
                showToastSnack("Please complete all fields.", true)
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                showToastSnack("Passwords do not match.", true)
                return@setOnClickListener
            }

            if (!isValidEmail(email)) {
                showToastSnack("Please enter a valid email address.", true)
                return@setOnClickListener
            }


            if (!isValidPhone(phone)) {
                showToastSnack("Phone number must be 11 digits.", true)
                return@setOnClickListener
            }


            val passwordError = getPasswordError(password)
            if (passwordError != null) {
                showToastSnack(passwordError, true)
                return@setOnClickListener
            }


            val intent = Intent(this@SignUpInfoActivity, SignUpMapsActivity::class.java)
            intent.putExtra("firstName", firstName)
            intent.putExtra("lastName", lastName)
            intent.putExtra("email", email)
            intent.putExtra("password", password)
            intent.putExtra("phone", phone)
            startActivity(intent)
        }

        binding.btnBack.setOnClickListener {
            finish()
        }
    }


    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}"
        return email.matches(Regex(emailPattern))
    }


    private fun isValidPhone(phone: String): Boolean {
        return phone.length == 11 && phone.all { it.isDigit() }
    }

    private fun getPasswordError(password: String): String? {
        if (!password.matches(Regex(".*[A-Z].*"))) {
            return "Password must contain at least one uppercase letter."
        }
        if (!password.matches(Regex(".*[a-z].*"))) {
            return "Password must contain at least one lowercase letter."
        }
        if (!password.matches(Regex(".*\\d.*"))) {
            return "Password must contain at least one digit."
        }
        if (!password.matches(Regex(".*[@$!%*?&].*"))) {
            return "Password must contain at least one special character."
        }
        if (password.length < 8) {
            return "Password must be at least 8 characters long."
        }
        return null
    }
}