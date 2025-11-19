package com.akhnaton.atrapp.ui.auth.waiting

import android.content.Intent
import android.os.Bundle
import com.akhnaton.atrapp.databinding.ActivityWaitingBinding
import com.akhnaton.atrapp.shared.BaseActivity
import com.akhnaton.atrapp.ui.nav.HomeActivity

class WaitingActivity : BaseActivity() {
    lateinit var binding: ActivityWaitingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWaitingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        onClick()
    }

    private fun init() {}

    private fun onClick() {
        binding.btnContinueShopping.setOnClickListener {
            startActivity(Intent(this@WaitingActivity,HomeActivity::class.java))
        }
    }

}