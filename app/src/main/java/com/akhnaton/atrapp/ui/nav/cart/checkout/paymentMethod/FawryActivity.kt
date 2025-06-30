package com.akhnaton.atrapp.ui.nav.cart.checkout.paymentMethod

import android.os.Bundle
import android.util.Log
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
        val orderNumbers = intent.getStringArrayListExtra("order_numbers") ?: arrayListOf()

        // اطبعهم في اللوج للتأكد
        Log.d("ORDER_PATH", "Received Order Numbers: $orderNumbers")

        // مثال لو عايز تعرض أول رقم بس في TextView
        if (orderNumbers.isNotEmpty()) {
            binding.txtOrderNumber.text = orderNumbers.joinToString("\n")
        } else {
            binding.txtOrderNumber.text = "No order numbers received."
        }
    }


    private fun onClick() {
        binding.btnConfirm.setOnClickListener {
            finish()
        }
    }
}