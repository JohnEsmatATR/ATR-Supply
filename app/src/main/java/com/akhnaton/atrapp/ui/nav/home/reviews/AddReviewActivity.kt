package com.akhnaton.atrapp.ui.nav.home.reviews

import android.os.Bundle
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.databinding.ActivityAddReviewBinding
import com.akhnaton.atrapp.shared.BaseActivity

class AddReviewActivity : BaseActivity() {
    lateinit var binding: ActivityAddReviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        onClick()
    }

    private fun init() {

    }

    private fun onClick() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}