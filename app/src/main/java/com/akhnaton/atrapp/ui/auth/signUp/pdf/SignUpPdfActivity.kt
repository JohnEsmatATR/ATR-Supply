package com.akhnaton.atrapp.ui.auth.signUp.pdf

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.akhnaton.atrapp.data.statuesValue.auth.register.RegisterIntent
import com.akhnaton.atrapp.data.statuesValue.auth.register.RegisterStatus
import com.akhnaton.atrapp.data.statuesValue.nav.home.favorite.FavoriteIntent
import com.akhnaton.atrapp.data.statuesValue.nav.home.favorite.FavoriteStatus
import com.akhnaton.atrapp.databinding.ActivitySignUpPdfBinding
import com.akhnaton.atrapp.shared.BaseActivity
import com.akhnaton.atrapp.shared.Common
import com.akhnaton.atrapp.ui.auth.signUp.RegisterViewModel
import com.akhnaton.atrapp.ui.auth.signUp.map.SignUpMapsActivity
import com.akhnaton.atrapp.ui.auth.waiting.WaitingActivity
import com.akhnaton.atrapp.ui.nav.favorite.FavoriteViewModel
import kotlinx.coroutines.launch

class SignUpPdfActivity : BaseActivity() {
    lateinit var binding: ActivitySignUpPdfBinding
    private val registerViewModel: RegisterViewModel by viewModels()
    var firstName = ""
    var lastName = ""
    var email = ""
    var password = ""
    var phone = ""
    var latitude = ""
    var longitude = ""
    var title = ""
    var address = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpPdfBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        onClick()
    }

    private fun init() {
        firstName = intent.getStringExtra("firstName")?:""
        lastName = intent.getStringExtra("lastName")?:""
        email = intent.getStringExtra("email")?:""
        password = intent.getStringExtra("password")?:""
        phone = intent.getStringExtra("phone")?:""
        latitude = intent.getStringExtra("latitude")?:""
        longitude = intent.getStringExtra("longitude")?:""
        title = intent.getStringExtra("title")?:""
        address = intent.getStringExtra("address")?:""

        registerObserve()
    }

    private fun onClick() {
        binding.btnNext.setOnClickListener {
            sendRegister()
        }
        binding.btnBack.setOnClickListener {
            finish()
        }

    }


    private fun registerObserve() {
        lifecycleScope.launch {
            registerViewModel.state.collect {
                when (it) {
                    is RegisterStatus.Idle -> Log.d(Common.KeroDebug, "observeHome: Idle")
                    is RegisterStatus.Loading -> {
                        Log.d(Common.KeroDebug, "observeHome: Loading")
                        showProgressDialog(binding.progressLoading)
                    }

                    is RegisterStatus.Register -> {
                        if (it.data.status == 200) {
                            hideProgressDialog(binding.progressLoading)
                            Log.d(Common.KeroDebug, "observeHome: GetProducts")
                            showToastSnack("Registered Successfully!", false)
                            val intent = Intent(this@SignUpPdfActivity, WaitingActivity::class.java)
                            startActivity(intent)

                        } else if (it.data.status == 401) {
                            hideProgressDialog(binding.progressLoading)
//                            onTokenExpired(it.data.errors!![0])

                        } else {
                            hideProgressDialog(binding.progressLoading)
                            showToastSnack(it.data.message, true)
                        }

                    }


                    is RegisterStatus.Error -> {
                        Log.d(Common.KeroDebug, "observeHome Error: ${it.error.toString()}")
                        hideProgressDialog(binding.progressLoading)
                        showToastSnack(it.error.toString(), true)
                    }

                }
            }
        }
    }


    private fun sendRegister() {

        lifecycleScope.launch {
            registerViewModel.registerIntent.send(
                RegisterIntent.Register(

                )
            )
        }
    }

}