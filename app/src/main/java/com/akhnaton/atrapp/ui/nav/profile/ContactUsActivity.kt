package com.akhnaton.atrapp.ui.nav.profile

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.databinding.ActivityContactUsBinding


class ContactUsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContactUsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        setupListeners()
    }

    private fun setupBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contact_us)
    }

    private fun setupListeners() {

        binding.whatsAppCard.setOnClickListener {
            val phone = "+20 217125"
            val url = "https://wa.me/$phone"
            openLink(url)
        }


        binding.facebookCard.setOnClickListener {
            openLink("https://www.facebook.com/share/1FGQyT7r9k/")
        }


        binding.linkedinCard.setOnClickListener {
            openLink("https://www.linkedin.com/company/akhnaton-trading-and-distribution/posts/?feedView=all")
        }


        binding.linkedasdinCard.setOnClickListener {
            openLink("https://youtube.com/@atr-akhnatontradinganddist9112")
        }


        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun openLink(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        startActivity(intent)
    }
}
