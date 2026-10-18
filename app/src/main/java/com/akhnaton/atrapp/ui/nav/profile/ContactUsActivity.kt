package com.akhnaton.atrapp.ui.nav.profile

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.databinding.ActivityContactUsBinding
import com.akhnaton.atrapp.shared.BaseActivity
import com.akhnaton.atrapp.shared.SharedPreferenceHelper


class ContactUsActivity : BaseActivity() {
    private lateinit var binding: ActivityContactUsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        setupListeners()
    }

    private fun setupBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contact_us)

        val isArabic = SharedPreferenceHelper.language == LANG_AR
        if (isArabic) binding.btnBack.setImageResource(R.drawable.ic_back_ar)
        else binding.btnBack.setImageResource(R.drawable.ic_back)

    }

    private fun setupListeners() {

        binding.whatsAppCard.setOnClickListener {
            val phone = "+20 217125"


            val url = WHATSAPP_BASE_URL + MY_PHONE_NUMBER
            openLink(url)
        }


        binding.facebookCard.setOnClickListener {
            openLink(FACEBOOK_URL)
        }


        binding.linkedinCard.setOnClickListener {
            openLink(LINKEDIN_URL)
        }


        binding.linkedasdinCard.setOnClickListener {
            openLink(YOUTUBE_URL)
        }


        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun openLink(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        startActivity(intent)
    }


    companion object {
        private const val LANG_AR = "ar"
        private const val MY_PHONE_NUMBER = "+20 217125"
        private const val WHATSAPP_BASE_URL = "https://wa.me/"
        private const val FACEBOOK_URL = "https://www.facebook.com/share/1FGQyT7r9k/"
        private const val LINKEDIN_URL = "https://www.linkedin.com/company/akhnaton-trading-and-distribution/posts/?feedView=all"
        private const val YOUTUBE_URL = "https://youtube.com/@atr-akhnatontradinganddist9112"
    }
}
