package com.akhnaton.atrapp.ui.auth.customer_code.otp

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
import com.akhnaton.atrapp.data.statuesValue.auth.loginWithCustomerCode.ValidateOtpIntent
import com.akhnaton.atrapp.data.statuesValue.auth.loginWithCustomerCode.ValidateOtpState
import com.akhnaton.atrapp.databinding.ActivityOtpCustomerCodeBinding
import com.akhnaton.atrapp.shared.BaseActivity
import com.akhnaton.atrapp.shared.SharedPreferenceHelper
import com.akhnaton.atrapp.ui.auth.customer_code.login.LoginWithCodeActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class OTPCustomerCodeActivity : BaseActivity() {
    private lateinit var binding: ActivityOtpCustomerCodeBinding
    private val otpViewModel: OtpViewModel by viewModels()
    private val validateOtpViewModel: ValidateOtpViewModel by viewModels()
    private lateinit var invoiceCode : String
    private lateinit var phoneNumber : String
    private lateinit var email : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpCustomerCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var isArabic = SharedPreferenceHelper.language == "ar"
        if (isArabic) binding.btnBack.setImageResource(R.drawable.ic_back_ar)
        else binding.btnBack.setImageResource(R.drawable.ic_back)

         invoiceCode = intent.getStringExtra("invoice_code").toString()
         phoneNumber = intent.getStringExtra("phone_number").toString()
        email = intent.getStringExtra("email").toString()

        observer()
        Log.d("OTP", "Invoice Code: $invoiceCode, Phone: $phoneNumber")
//        binding.phoneNumber.text = phoneNumber
        binding.email.text = email
        val editTexts = listOf(
            binding.et1, binding.et2, binding.et3,
            binding.et4, binding.et5, binding.et6
        )
        binding.et1.requestFocus()
        binding.et1.postDelayed({
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.et1, InputMethodManager.SHOW_IMPLICIT)
        }, 200)


        setupOtpInputs(editTexts)
        binding.verificationButton.visibility = View.INVISIBLE
        otpViewModel.isOtpComplete.observe(this) { complete ->
            if (complete) {
                if (binding.verificationButton.isInvisible) {
                    binding.verificationButton.isEnabled=true
                    binding.verificationButton.visibility = View.VISIBLE
                    val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up)
                    binding.verificationButton.startAnimation(slideUp)
                }
            } else {
                if (binding.verificationButton.isVisible) {
                    binding.verificationButton.isEnabled=false
                    val slideDown = AnimationUtils.loadAnimation(this, R.anim.slide_down)
                    binding.verificationButton.startAnimation(slideDown)
                    binding.verificationButton.visibility = View.INVISIBLE
                }
            }
        }
        binding.verificationButton.setOnClickListener {
            val otp = editTexts.joinToString("") { it.text.toString() }
            lifecycleScope.launch {
                validateOtpViewModel.otpIntent.send(
                    ValidateOtpIntent.ValidateOtp(invoiceCode, phoneNumber, otp)
                )
            }
        }

    }

    private fun setupOtpInputs(editTexts: List<EditText>) {
        for (i in editTexts.indices) {
            val editText = editTexts[i]

            editText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {

                    val inputs = editTexts.map { it.text.toString() }
                    otpViewModel.checkOtp(inputs)

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


                    val inputs = editTexts.map { it.text.toString() }
                    otpViewModel.checkOtp(inputs)
                }
                false
            }
        }
    }
    private fun observer() {
        lifecycleScope.launch {
            validateOtpViewModel.state.collect { state ->
                when (state) {
                    is ValidateOtpState.Idle -> Unit
                    is ValidateOtpState.Loading -> {
                        showProgressDialog(binding.progressLoading)
                    }
                    is ValidateOtpState.Success -> {
                        hideProgressDialog(binding.progressLoading)
                        if (state.state==200){
                            showToastSnack(state.message, false)
                            delay(500)
                            Log.d("OTP", "Success: ${state.message}")
                            val intent = Intent(
                                this@OTPCustomerCodeActivity,
                                LoginWithCodeActivity::class.java
                            ).apply {
                                putExtra("invoice_code", invoiceCode)
                                putExtra("phone_number", phoneNumber)
                                putExtra("email", email)
                            }
                            startActivity(intent)
                        }
                        else{
                            showToastSnack(state.message, true)
                        }

                    }
                    is ValidateOtpState.Error -> {
                        hideProgressDialog(binding.progressLoading)
                        showToastSnack(state.error, true)
                    }
                }
            }
        }
    }

}