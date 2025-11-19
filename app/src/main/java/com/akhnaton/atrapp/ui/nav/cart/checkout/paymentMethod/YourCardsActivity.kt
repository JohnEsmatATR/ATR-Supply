package com.akhnaton.atrapp.ui.nav.cart.checkout.paymentMethod

import android.content.Intent
import android.os.Bundle
import com.akhnaton.atrapp.databinding.ActivityYourCardsBinding
import com.akhnaton.atrapp.shared.BaseActivity

class YourCardsActivity : BaseActivity() {
    lateinit var binding: ActivityYourCardsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityYourCardsBinding.inflate(layoutInflater)
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
        binding.btnAddCard.setOnClickListener {
            val intent = Intent(this@YourCardsActivity, AddCardActivity::class.java)
            startActivity(intent)
        }
    }
}