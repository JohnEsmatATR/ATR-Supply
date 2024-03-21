package com.akhnaton.atrapp.ui.nav.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.RawContacts.Data
import android.view.View
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.databinding.ActivityAccountDetailsBinding
import com.akhnaton.atrapp.shared.BaseActivity

class AccountDetailsActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityAccountDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_account_details)


        binding.updateBtn.setOnClickListener(this)
        binding.btnBack.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        if (v.id == binding.updateBtn.id) {
            finish()
        }

        if(v.id == binding.btnBack.id) {
            finish()
        }
    }
}