package com.akhnaton.atrapp.ui.nav.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.databinding.ActivityAboutUsBinding

class AboutUsActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityAboutUsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_about_us)

        binding.btnBack.setOnClickListener(this)

    }

    override fun onClick(v: View) {
        if(v.id == binding.btnBack.id) {
            finish()
        }
    }
}