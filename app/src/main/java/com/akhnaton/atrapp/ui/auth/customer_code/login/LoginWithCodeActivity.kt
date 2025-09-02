package com.akhnaton.atrapp.ui.auth.customer_code.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.akhnaton.atrapp.databinding.ActivityLoginWithCodeBinding

class LoginWithCodeActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginWithCodeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginWithCodeBinding.inflate(layoutInflater)

        setContentView(binding.root)

    }
}