package com.akhnaton.atrapp.ui.nav.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.databinding.ActivityLanguageBinding
import com.akhnaton.atrapp.shared.BaseActivity
import com.akhnaton.atrapp.shared.SharedPreferenceHelper

class LanguageActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLanguageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


//        SharedPreferenceHelper.setLocale(this, SharedPreferenceHelper.language ?: "ar")

        setupBinding()
        setupListeners()
        updateSelectedLanguageUI()
    }

    private fun setupBinding() {
        binding = DataBindingUtil.setContentView(this@LanguageActivity, R.layout.activity_language)

        var isArabic = SharedPreferenceHelper.language == "ar"
        if (isArabic) binding.btnBack.setImageResource(R.drawable.ic_back_ar)
        else binding.btnBack.setImageResource(R.drawable.ic_back)

    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener(this)
        binding.btnArabic.setOnClickListener(this)
        binding.btnEnglish.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            binding.btnBack.id -> finish()
            binding.btnArabic.id -> changeLanguage("ar")
            binding.btnEnglish.id -> changeLanguage("en")
        }
    }

    private fun changeLanguage(langCode: String) {
        SharedPreferenceHelper.language = langCode

        val intent = Intent(this, com.akhnaton.atrapp.ui.splash.SplashActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }

    private fun updateSelectedLanguageUI() {
        val selectedLang = SharedPreferenceHelper.language ?: "ar"


        binding.layoutEnglish.findViewById<ImageView>(R.id.ic_checkmark).visibility = View.GONE
        binding.layoutArabic.findViewById<ImageView>(R.id.ic_checkmark2).visibility = View.GONE


        if (selectedLang == "en") {

            binding.layoutEnglish.strokeColor = ContextCompat.getColor(this, R.color.orange)
            binding.layoutEnglish.strokeWidth = 3
            binding.layoutArabic.strokeWidth = 3
        } else {

            binding.layoutArabic.strokeColor = ContextCompat.getColor(this, R.color.orange)
            binding.layoutArabic.strokeWidth = 3
            binding.layoutEnglish.strokeWidth = 3
        }

    }
}

