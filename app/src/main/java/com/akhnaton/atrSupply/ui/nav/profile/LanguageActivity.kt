package com.akhnaton.atrSupply.ui.nav.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.akhnaton.atrSupply.R
import com.akhnaton.atrSupply.databinding.ActivityLanguageBinding
import com.akhnaton.atrSupply.shared.SharedPreferenceHelper

class LanguageActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLanguageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        SharedPreferenceHelper.setLocale(this, SharedPreferenceHelper.language ?: "ar")

        setupBinding()
        setupListeners()
        updateSelectedLanguageUI()
    }

    private fun setupBinding() {
        binding = DataBindingUtil.setContentView(this@LanguageActivity, R.layout.activity_language)
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

        val intent = baseContext.packageManager
            .getLaunchIntentForPackage(baseContext.packageName)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finishAffinity()
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

