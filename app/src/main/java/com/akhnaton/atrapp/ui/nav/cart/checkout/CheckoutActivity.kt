package com.akhnaton.atrapp.ui.nav.cart.checkout

import android.content.Intent
import android.os.Bundle
import com.akhnaton.atrapp.databinding.ActivityChackoutBinding
import com.akhnaton.atrapp.shared.BaseActivity
import com.akhnaton.atrapp.shared.CustomDialog
import com.akhnaton.atrapp.ui.nav.cart.addresses.AddressesActivity
import com.akhnaton.atrapp.ui.nav.cart.addresses.AddressesAdapter


class CheckoutActivity : BaseActivity() {
    lateinit var binding: ActivityChackoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChackoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mDialog = CustomDialog(this)
        binding.btnCheckout.setOnClickListener {
            mDialog.showDialog()
        }

        init()
        onClick()
    }

    private fun init() {

    }

    private fun onClick() {

        binding.imAddressChange.setOnClickListener {
            val intent = Intent(this@CheckoutActivity, AddressesActivity::class.java)
            startActivity(intent)
        }

        binding.imBack.setOnClickListener {
            finish()
        }
    }
}