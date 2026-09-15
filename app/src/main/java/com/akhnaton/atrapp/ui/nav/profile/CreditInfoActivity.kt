package com.akhnaton.atrapp.ui.nav.profile

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.akhnaton.atrapp.data.interfaces.IProfile
import com.akhnaton.atrapp.databinding.ActivityCreditInfoBinding
import com.akhnaton.atrapp.shared.RetrofitClient
import kotlinx.coroutines.launch

class CreditInfoActivity : AppCompatActivity() {

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

                Log.d("API_RAW", "HTTP Code: ${response.code()}")
                Log.d("API_RAW", "Raw Body: ${response.body()}")

                if (response.isSuccessful && response.body() != null) {
                    val baseResponse = response.body()!!

                    val creditData = baseResponse.creditData

                    if (creditData != null) {
                        binding.tvCreditLimit.text = "${creditData.creditLimit ?: 0.0} EGP"
                        binding.tvUsedCredit.text = "${creditData.usedCredit ?: 0.0} EGP"
                        binding.tvAvailableCredit.text = "${creditData.availableCredit ?: 0.0} EGP"
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