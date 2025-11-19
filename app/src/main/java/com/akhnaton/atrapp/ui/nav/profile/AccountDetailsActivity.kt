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
        binding.layoutPdf.visibility = View.GONE
        binding.passwordLayout.visibility = View.GONE
      //  binding.password.visibility = View.GONE

        binding.nameED.isEnabled = false
        binding.phoneED.isEnabled = false
        binding.emailED.isEnabled = false
        binding.addressED.isEnabled = false


        val fullName = "${SharedPreferenceHelper.userObj!!.first_name} ${SharedPreferenceHelper.userObj!!.last_name}"
        val phone = "${SharedPreferenceHelper.userObj!!.phone}"
        val email = "${SharedPreferenceHelper!!.userObj!!.email}"
        val address = "${SharedPreferenceHelper!!.userObj!!.address.ADDRESS}"
        binding.addressED.setText(address)
        binding.nameED.setText(fullName)
        binding.phoneED.setText(phone)
        binding.emailED.setText(email)
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