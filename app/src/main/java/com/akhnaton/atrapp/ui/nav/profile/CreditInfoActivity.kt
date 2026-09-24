package com.akhnaton.atrapp.ui.nav.profile

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.data.interfaces.IProfile
import com.akhnaton.atrapp.databinding.ActivityCreditInfoBinding
import com.akhnaton.atrapp.shared.BaseActivity
import com.akhnaton.atrapp.shared.RetrofitClient
import com.akhnaton.atrapp.shared.SharedPreferenceHelper
import kotlinx.coroutines.launch
import java.util.Locale

class CreditInfoActivity : BaseActivity() {

    private lateinit var binding: ActivityCreditInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityCreditInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnBack.setOnClickListener {
            finish()
        }

        fetchAndDisplayCreditData()
    }

    private fun fetchAndDisplayCreditData() {
        lifecycleScope.launch {
            try {
                val apiService = RetrofitClient.getInstance(IProfile::class.java)
                val response = apiService.getCreditInfo()

                if (response.isSuccessful && response.body() != null) {
                    val baseResponse = response.body()!!
                    val creditData = baseResponse.creditData

                    binding.progressLoading.visibility = View.GONE

                    if (creditData != null) {
                        val currency = getString(R.string.currency)

                        binding.tvCreditLimit.text = String.format(Locale.getDefault(), "%.2f %s", creditData.creditLimit ?: 0.0, currency)
                        binding.tvUsedCredit.text = String.format(Locale.getDefault(), "%.2f %s", creditData.usedCredit ?: 0.0, currency)
                        binding.tvAvailableCredit.text = String.format(Locale.getDefault(), "%.2f %s", creditData.availableCredit ?: 0.0, currency)
                    } else {
                        Log.e("API_ERROR", "Data object inside response is NULL. Message: ${baseResponse.message}")
                    }
                } else {
                    Log.e("API_ERROR", "HTTP Error: ${response.code()} - ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("API_ERROR", "Exception: ${e.localizedMessage}")
            }
        }
    }
}