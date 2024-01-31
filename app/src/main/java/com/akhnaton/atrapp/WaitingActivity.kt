package com.akhnaton.atrapp

import android.os.Bundle
import com.akhnaton.atrapp.databinding.ActivityWaitingBinding
import com.akhnaton.atrapp.shared.BaseActivity

class WaitingActivity : BaseActivity() {
    lateinit var binding: ActivityWaitingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWaitingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        onClick()
    }

    private fun init() {}

    private fun onClick() {}

}