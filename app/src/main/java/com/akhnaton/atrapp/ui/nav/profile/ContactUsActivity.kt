package com.akhnaton.atrapp.ui.nav.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.databinding.ActivityContactUsBinding


class ContactUsActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityContactUsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
    }

    private fun setupBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contact_us)
        binding.btnBack.setOnClickListener(this)
        binding.callServiceNumber.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        if (v.id == binding.btnBack.id) {
            finish()
        }

        if (v.id == binding.callServiceNumber.id) {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.setData(Uri.parse("tel:${binding.callServiceNumber.text}"))
            startActivity(intent)

        }

    }
}