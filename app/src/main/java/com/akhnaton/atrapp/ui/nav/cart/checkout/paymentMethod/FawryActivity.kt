package com.akhnaton.atrapp.ui.nav.cart.checkout.paymentMethod

import android.os.Bundle
import com.akhnaton.atrapp.databinding.ActivityFawryBinding
import com.akhnaton.atrapp.shared.BaseActivity

class FawryActivity : BaseActivity() {
    lateinit var binding: ActivityFawryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFawryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        onClick()
    }

    private fun init() {

    }

    private fun onClick() {
        binding.btnConfirm.setOnClickListener {
            finish()
        }
    }
}