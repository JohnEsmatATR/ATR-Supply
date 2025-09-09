package com.akhnaton.atrapp.ui.auth.customer_code.code

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.akhnaton.atrapp.data.statuesValue.appSetting.AppSettingIntent
import com.akhnaton.atrapp.data.statuesValue.appSetting.AppSettingState
import com.akhnaton.atrapp.data.statuesValue.auth.loginWithCustomerCode.SentOtpIntent
import com.akhnaton.atrapp.data.statuesValue.auth.loginWithCustomerCode.SentOtpState
import com.akhnaton.atrapp.databinding.ActivityCustomerInvoiceCodeBinding
import com.akhnaton.atrapp.shared.BaseActivity
import com.akhnaton.atrapp.ui.auth.customer_code.otp.OTPCustomerCodeActivity
import com.bumptech.glide.Glide
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.core.view.isVisible

class CustomerInvoiceCodeActivity : BaseActivity() {
    private lateinit var binding: ActivityCustomerInvoiceCodeBinding
    private val viewModel: SentOtpViewModel by viewModels()
    private val appSettingViewModel: AppSettingViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerInvoiceCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observer()
        observerAppSetting()
        lifecycleScope.launch {
            appSettingViewModel.intent.send(AppSettingIntent.GetAppSetting)
        }
        binding.findImage.setOnClickListener {
            if (binding.invoiceImage.isVisible) {
                binding.invoiceImage.visibility = View.GONE
            } else {
                binding.invoiceImage.visibility = View.VISIBLE
            }
        }

        binding.btnEnterInvoice.setOnClickListener {
            val invoiceCode = binding.txtCustomerInvoiceCode.text.toString().trim()
            val phoneNumber = binding.txtCustomerPhoneNumber.text.toString().trim()

            if (!validateInputs(invoiceCode, phoneNumber)) return@setOnClickListener

            lifecycleScope.launch {
                viewModel.otpIntent.send(
                    SentOtpIntent.SentCode(
                        code = invoiceCode,
                        phone = phoneNumber
                    )
                )
            }
        }



    }

    private fun validateInputs(invoiceCode: String, phone: String): Boolean {
        return when {
            invoiceCode.isEmpty() -> {
                binding.txtCustomerInvoiceCode.error = "ادخل رقم الفاتورة"
                false
            }

            phone.isEmpty() -> {
                binding.txtCustomerPhoneNumber.error = "ادخل رقم الهاتف"
                false
            }

            phone.length != 11 -> {
                binding.txtCustomerPhoneNumber.error = "رقم الهاتف يجب أن يكون 11 رقم"
                false
            }

            else -> true
        }
    }
    private fun observer(){
        lifecycleScope.launch {
            viewModel.state.collect { state ->
                when (state) {
                    is SentOtpState.Idle -> Unit
                    is SentOtpState.Loading -> {
                            showProgressDialog(binding.progressLoading)

                    }
                    is SentOtpState.Success -> {
                        hideProgressDialog(binding.progressLoading)
                        if (state.state == 200){
                            showToastSnack(state.message, false)
                            delay(500)
                            val invoiceCode = binding.txtCustomerInvoiceCode.text.toString().trim()
                            val phoneNumber = binding.txtCustomerPhoneNumber.text.toString().trim()

                            val intent = Intent(
                                this@CustomerInvoiceCodeActivity,
                                OTPCustomerCodeActivity::class.java
                            ).apply {
                                putExtra("invoice_code", invoiceCode)
                                putExtra("phone_number", phoneNumber)
                            }

                            startActivity(intent)
                        }else{
                            showToastSnack(state.message, false)
                        }

                    }


                    is SentOtpState.Error -> {
                            hideProgressDialog(binding.progressLoading)
                        showToastSnack(state.error, true)
                    }
                }
            }
        }
    }
    private fun observerAppSetting() {
        lifecycleScope.launch {
            appSettingViewModel.state.collect { state ->
                when (state) {
                    is AppSettingState.Idle -> Unit
                    is AppSettingState.Loading -> {
                        showProgressDialog(binding.progressLoading)
                    }

                    is AppSettingState.Success -> {
                        hideProgressDialog(binding.progressLoading)
                        if (state.data.status ==200 ){
                            val imageUrl = state.data.data?.customer_code_help_image
                            if (!imageUrl.isNullOrEmpty()) {

                                Glide.with(this@CustomerInvoiceCodeActivity)
                                    .load(imageUrl)
                                    .into(binding.invoiceImage)
                            }
                        }else{
                            showToastSnack(state.data.message, true)
                        }

                    }

                    is AppSettingState.Error -> {
                        hideProgressDialog(binding.progressLoading)
                        showToastSnack(state.error, true)
                    }
                }
            }
        }
    }
}