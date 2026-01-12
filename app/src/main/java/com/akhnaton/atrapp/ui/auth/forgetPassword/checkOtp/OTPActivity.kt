package com.akhnaton.atrapp.ui.auth.forgetPassword.checkOtp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.viewModels
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.data.statuesValue.auth.forgetPassword.chackOtp.CheckOtpIntent
import com.akhnaton.atrapp.data.statuesValue.auth.forgetPassword.chackOtp.CheckOtpStatus
import com.akhnaton.atrapp.ui.auth.forgetPassword.changePassword.NewPasswordActivity
import com.akhnaton.atrapp.databinding.ActivityOtpactivityBinding
import com.akhnaton.atrapp.shared.BaseActivity
import com.akhnaton.atrapp.shared.Common
import kotlinx.coroutines.launch

class OTPActivity : BaseActivity() {
    lateinit var binding: ActivityOtpactivityBinding
    private val viewModel: CheckOTPViewModel by viewModels()
    var email = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        onClick()
    }

    private fun init() {
        email = intent!!.getStringExtra("email")?:""
        observeCheckOtp()
        setupOtpInputs()
    }
    
    private fun setupOtpInputs() {
        val editTexts = listOf(
            binding.et1, binding.et2, binding.et3, binding.et4
        )
        
        binding.et1.requestFocus()
        binding.et1.postDelayed({
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.et1, InputMethodManager.SHOW_IMPLICIT)
        }, 200)
        
        for (i in editTexts.indices) {
            val editText = editTexts[i]
            
            editText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    val inputs = editTexts.map { it.text.toString() }
                    val isComplete = inputs.all { it.length == 1 }
                    
                    // Show/hide button based on completion
                    if (isComplete) {
                        if (binding.btnNext.isInvisible) {
                            binding.btnNext.isEnabled = true
                            binding.btnNext.visibility = View.VISIBLE
                            val slideUp = AnimationUtils.loadAnimation(this@OTPActivity, R.anim.slide_up)
                            binding.btnNext.startAnimation(slideUp)
                        }
                    } else {
                        if (binding.btnNext.isVisible) {
                            binding.btnNext.isEnabled = false
                            val slideDown = AnimationUtils.loadAnimation(this@OTPActivity, R.anim.slide_down)
                            binding.btnNext.startAnimation(slideDown)
                            binding.btnNext.visibility = View.INVISIBLE
                        }
                    }
                    
                    if (s?.length == 1 && i < editTexts.size - 1) {
                        editTexts[i + 1].requestFocus()
                    }
                }
                
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
            
            editText.setOnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_DEL &&
                    event.action == KeyEvent.ACTION_DOWN &&
                    editText.text.isEmpty() &&
                    i > 0
                ) {
                    editTexts[i - 1].requestFocus()
                    editTexts[i - 1].setSelection(editTexts[i - 1].text.length)
                }
                false
            }
        }
    }

    private fun onClick() {
        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.btnNext.setOnClickListener {
            fetchCheckOTP()
        }
    }

    private fun observeCheckOtp() {
        lifecycleScope.launch {
            viewModel.state.collect {
                when (it) {
                    is CheckOtpStatus.Idle -> Log.d(Common.KeroDebug, "observeLogin: it")
                    is CheckOtpStatus.Loading -> {
                        Log.d(Common.KeroDebug, "observeLogin: it")
                        showProgressDialog(binding.progressLoading)
                    }

                    is CheckOtpStatus.CheckOtp -> {
                        if (it.data.status == 200) {
                            hideProgressDialog(binding.progressLoading)
                            showToastSnack(it.data.message, false)

                            val intent = Intent(baseContext, NewPasswordActivity::class.java)
                            val editTexts = listOf(binding.et1, binding.et2, binding.et3, binding.et4)
                            val otp = editTexts.joinToString("") { it.text.toString() }
                            intent.putExtra("email", email)
                            intent.putExtra("otp", otp)
                            startActivity(intent)

                        } else {
                            hideProgressDialog(binding.progressLoading)
                            showToastSnack(it.data.message, true)
                        }
                    }

                    is CheckOtpStatus.Error -> {
                        Log.d(Common.KeroDebug, "observeLogin Error: ${it.error.toString()}")
                        hideProgressDialog(binding.progressLoading)
                        showToastSnack(it.error.toString(), true)
                    }
                }
            }
        }
    }

    private fun fetchCheckOTP() {
        val editTexts = listOf(binding.et1, binding.et2, binding.et3, binding.et4)
        val otp = editTexts.joinToString("") { it.text.toString() }
        
        lifecycleScope.launch {
            viewModel.sendOtpIntent.send(
                CheckOtpIntent.SendOtp(
                    email,
                    otp.lowercase().trim(),
                )
            )
        }
    }

}