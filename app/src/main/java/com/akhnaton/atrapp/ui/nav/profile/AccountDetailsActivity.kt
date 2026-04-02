package com.akhnaton.atrapp.ui.nav.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.databinding.ActivityAccountDetailsBinding
import com.akhnaton.atrapp.shared.BaseActivity
import com.akhnaton.atrapp.shared.SharedPreferenceHelper

class AccountDetailsActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityAccountDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_account_details)

        init()
        binding.updateBtn.setOnClickListener(this)
        binding.btnBack.setOnClickListener(this)
    }

    @SuppressLint("SuspiciousIndentation")
    private fun init() {

        var isArabic = SharedPreferenceHelper.language == "ar"
        if (isArabic) binding.btnBack.setImageResource(R.drawable.ic_back_ar)
        else binding.btnBack.setImageResource(R.drawable.ic_back)

        binding.layoutPdf.visibility = View.GONE
        binding.passwordLayout.visibility = View.GONE
      //  binding.password.visibility = View.GONE

        binding.nameED.isEnabled = false
        binding.phoneED.isEnabled = false
        binding.emailED.isEnabled = false
        binding.addressED.isEnabled = false

        // Ensure phone layout is visible
        binding.phoneLayout.visibility = View.VISIBLE

        val user = SharedPreferenceHelper.userObj
        if (user != null) {
            val fullName = "${user.first_name} ${user.last_name}".trim()
            val phone = user.phone?.takeIf { it.isNotEmpty() } ?: ""
            val email = user.email?.takeIf { it.isNotEmpty() } ?: ""
            val address = user.address?.ADDRESS?.takeIf { it.isNotEmpty() } ?: ""
            
            binding.nameED.setText(fullName.ifEmpty { "-" })
            binding.phoneED.setText(phone.ifEmpty { "-" })
            binding.emailED.setText(email.ifEmpty { "-" })
            binding.addressED.setText(address.ifEmpty { "-" })
        }
    }

    override fun onClick(v: View) {
        if (v.id == binding.updateBtn.id) {
            finish()
        }

        if (v.id == binding.btnBack.id) {
            finish()
        }
    }
}