package com.akhnaton.atrapp.ui.nav.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.databinding.ActivityContactUsBinding
import androidx.core.net.toUri


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
            val phone = "17125"
            val url = "https://wa.me/$phone"
            openLink(url)
        }


        binding.facebookCard.setOnClickListener {
            openLink("https://www.facebook.com/share/1FGQyT7r9k/")
        }


        binding.linkedinCard.setOnClickListener {
            openLink("https://www.linkedin.com/in/jonathan-ehab-1a818b223/")
        }


        binding.linkedasdinCard.setOnClickListener {
            openLink("https://youtube.com/@evapharma?si=ixHSkpWzAkf9cY7B")
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
