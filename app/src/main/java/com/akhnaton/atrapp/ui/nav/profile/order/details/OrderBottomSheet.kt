package com.akhnaton.atrapp.ui.nav.profile.order.details

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.akhnaton.atrapp.databinding.ActivityOrderBottomSheetBinding

class OrderBottomSheet : AppCompatActivity() {

    private lateinit var binding: ActivityOrderBottomSheetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityOrderBottomSheetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val subtotal = intent.getDoubleExtra("SUBTOTAL", 0.0)
        val tax = intent.getDoubleExtra("TAX", 0.0)
        val grandTotal = intent.getDoubleExtra("GRAND_TOTAL", 0.0)

        binding.txtSubtotal.text = "$subtotal L.E"
        binding.txtTax.text = "$tax L.E"
        binding.txtGrandTotal.text = "$grandTotal L.E"
    }
}