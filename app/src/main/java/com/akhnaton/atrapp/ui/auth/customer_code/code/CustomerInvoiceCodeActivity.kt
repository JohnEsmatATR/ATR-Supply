package com.akhnaton.atrapp.ui.auth.customer_code.code

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.akhnaton.atrapp.databinding.ActivityCustomerInvoiceCodeBinding
import com.akhnaton.atrapp.ui.auth.customer_code.otp.OTPCustomerCodeActivity

class CustomerInvoiceCodeActivity : AppCompatActivity() {
    private lateinit var binding : ActivityCustomerInvoiceCodeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding = ActivityCustomerInvoiceCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnEnterInvoice.setOnClickListener {
            startActivity(
                Intent(
                    this@CustomerInvoiceCodeActivity,
                    OTPCustomerCodeActivity::class.java
                )
            )
        }


    }
}