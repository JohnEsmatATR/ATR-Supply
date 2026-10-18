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
import com.akhnaton.atrapp.ui.auth.customer_code.code.CustomerInvoiceCodeActivity
import com.akhnaton.atrapp.ui.auth.customer_code.login.LoginWithCodeActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class OTPCustomerCodeActivity : BaseActivity() {

    private lateinit var binding: ActivityOtpCustomerCodeBinding
    private val otpViewModel: OtpViewModel by viewModels()
    private val validateOtpViewModel: ValidateOtpViewModel by viewModels()

    private lateinit var invoiceCode: String
    private lateinit var phoneNumber: String
    private lateinit var email: String

    companion object {
        private const val TAG_OTP = "OTP"
        private const val LANGUAGE_AR = "ar"
        private const val HTTP_STATUS_SUCCESS = 200
        private const val DELAY_NAVIGATE_MS = 500L
        private const val KEYBOARD_DELAY_MS = 200L
        const val EXTRA_INVOICE_CODE = "invoice_code"
        const val EXTRA_PHONE_NUMBER = "phone_number"
        const val EXTRA_EMAIL = "email"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpCustomerCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val isArabic = SharedPreferenceHelper.language == LANGUAGE_AR
        if (isArabic) {
            binding.btnBack.setImageResource(R.drawable.ic_back_ar)
        } else {
            binding.btnBack.setImageResource(R.drawable.ic_back)
        }

        invoiceCode = intent.getStringExtra(CustomerInvoiceCodeActivity.KEY_INVOICE_CODE).orEmpty()
        phoneNumber = intent.getStringExtra(CustomerInvoiceCodeActivity.KEY_PHONE_NUMBER).orEmpty()
        email = intent.getStringExtra(CustomerInvoiceCodeActivity.KEY_EMAIL).orEmpty()

        observer()
        Log.d(TAG_OTP, "Invoice Code: $invoiceCode, Phone: $phoneNumber")

        binding.email.text = email
        val editTexts = listOf(
            binding.et1, binding.et2, binding.et3,
            binding.et4, binding.et5, binding.et6
        )

        binding.et1.requestFocus()
        binding.et1.postDelayed({
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.et1, InputMethodManager.SHOW_IMPLICIT)
        }, KEYBOARD_DELAY_MS)

        setupOtpInputs(editTexts)
        binding.verificationButton.visibility = View.INVISIBLE

        otpViewModel.isOtpComplete.observe(this) { complete ->
            if (complete) {
                if (binding.verificationButton.isInvisible) {
                    binding.verificationButton.isEnabled = true
                    binding.verificationButton.visibility = View.VISIBLE
                    val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up)
                    binding.verificationButton.startAnimation(slideUp)
                }
            } else {
                if (binding.verificationButton.isVisible) {
                    binding.verificationButton.isEnabled = false
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
                        if (state.state == HTTP_STATUS_SUCCESS) {
                            showToastSnack(state.message, false)
                            delay(DELAY_NAVIGATE_MS)
                            Log.d(TAG_OTP, "Success: ${state.message}")
                            val intent = Intent(
                                this@OTPCustomerCodeActivity,
                                LoginWithCodeActivity::class.java
                            ).apply {
                                putExtra(EXTRA_INVOICE_CODE, invoiceCode)
                                putExtra(EXTRA_PHONE_NUMBER, phoneNumber)
                                putExtra(EXTRA_EMAIL, email)
                            }
                            startActivity(intent)
                        } else {
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