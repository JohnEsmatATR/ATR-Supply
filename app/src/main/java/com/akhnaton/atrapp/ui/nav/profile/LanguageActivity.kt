package com.akhnaton.atrapp.ui.nav.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.databinding.ActivityLanguageBinding

class LanguageActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityLanguageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
    }

    private fun setupBinding() {
        binding = DataBindingUtil.setContentView(this@LanguageActivity, R.layout.activity_language)
        binding.btnBack.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        if (v.id == binding.btnBack.id) {
            finish()
        }
    }
}