package com.akhnaton.atrapp.ui.nav.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.databinding.ActivityPrivacyBinding
import com.akhnaton.atrapp.shared.BaseActivity
import com.akhnaton.atrapp.shared.SharedPreferenceHelper

class PrivacyActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityPrivacyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_privacy)

        var isArabic = SharedPreferenceHelper.language == "ar"
        if (isArabic) binding.btnBack.setImageResource(R.drawable.ic_back_ar)
        else binding.btnBack.setImageResource(R.drawable.ic_back)

        Log.d("WHAT", "LANG: ${SharedPreferenceHelper.language}")
        binding.btnBack.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        if (v.id == binding.btnBack.id) {
            finish()
        }
    }
}