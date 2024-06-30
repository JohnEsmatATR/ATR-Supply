package com.akhnaton.atrapp.ui.auth.signUp.pdf

import android.app.Activity
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
import com.github.dhaval2404.imagepicker.ImagePicker.Companion.with
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class SignUpPdfActivity : BaseActivity() {
    private lateinit var binding: ActivitySignUpPdfBinding
    private val registerViewModel: RegisterViewModel by viewModels()
    private var firstName = ""
    private var lastName = ""
    private var email = ""
    private var password = ""
    private var phone = ""
    private var latitude = ""
    private var longitude = ""
    private var title = ""
    private var address = ""

    private var imNationalId: String = ""
    private var imLicense: String = ""
    private var imCommercialRegister: String = ""
    private var imLeaseOrOwnershipContract: String = ""
    private var imTaxCard: String = ""

    private var requestCode = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpPdfBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        onClick()
    }

    private fun init() {
        firstName = intent.getStringExtra("firstName") ?: ""
        lastName = intent.getStringExtra("lastName") ?: ""
        email = intent.getStringExtra("email") ?: ""
        password = intent.getStringExtra("password") ?: ""
        phone = intent.getStringExtra("phone") ?: ""
        latitude = intent.getStringExtra("latitude") ?: ""
        longitude = intent.getStringExtra("longitude") ?: ""
        title = intent.getStringExtra("title") ?: ""
        address = intent.getStringExtra("address") ?: ""

        registerObserve()
    }

    private fun onClick() {
        binding.btnNext.setOnClickListener {
            sendRegister()
        }
        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.layoutUploadNationalId.setOnClickListener {
            chooseIdPhoto(100)
        }
        binding.layoutUploadLicense.setOnClickListener {
            chooseIdPhoto(200)
        }
        binding.layoutUploadCommercialRegister.setOnClickListener {
            chooseIdPhoto(300)
        }
        binding.layoutUploadLeaseOrOwnershipContract.setOnClickListener {
            chooseIdPhoto(400)
        }
        binding.layoutUploadTaxCard.setOnClickListener {
            chooseIdPhoto(500)
        }

    }

    private fun chooseIdPhoto(code: Int) {
        requestCode = code
        with(this)
            .crop()
            .compress(300)
            .maxResultSize(
                1080,
                1080
            )
            .start()
    }

    private fun uploadImagesId(img: String, name: String): MultipartBody.Part {
        val mSaveBit = File(img)

        val requestBody = mSaveBit.asRequestBody("image/*".toMediaTypeOrNull())

        return MultipartBody.Part.createFormData(name, mSaveBit.name, requestBody)
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
                            finish()

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

        val _firstName = firstName.toRequestBody("text/plain".toMediaTypeOrNull())
        val _lastName = lastName.toRequestBody("text/plain".toMediaTypeOrNull())
        val _email = email.toRequestBody("text/plain".toMediaTypeOrNull())
        val _password = password.toRequestBody("text/plain".toMediaTypeOrNull())
        val _phone = phone.toRequestBody("text/plain".toMediaTypeOrNull())
        val _latitude = latitude.toRequestBody("text/plain".toMediaTypeOrNull())
        val _longitude = longitude.toRequestBody("text/plain".toMediaTypeOrNull())
        val _title = title.toRequestBody("text/plain".toMediaTypeOrNull())
        val _address = address.toRequestBody("text/plain".toMediaTypeOrNull())
        val _firebaseToken = "_firebaseToken".toRequestBody("text/plain".toMediaTypeOrNull())
        val _imNationalId = uploadImagesId(imNationalId, "attach_identity")
        val _imLicense = uploadImagesId(imLicense, "attach_license")
        val _imCommercialRegister =
            uploadImagesId(imCommercialRegister, "attach_coomercial_register")
        val _imLeaseOrOwnershipContract =
            uploadImagesId(imLeaseOrOwnershipContract, "attach_ownership")
        val _imTaxCard = uploadImagesId(imTaxCard, "attach_tax")

        if (imNationalId.isEmpty() || imNationalId == "" ||
            imLicense.isEmpty() || imLicense == "" ||
            imCommercialRegister.isEmpty() || imCommercialRegister == "" ||
            imLeaseOrOwnershipContract.isEmpty() || imLeaseOrOwnershipContract == "" ||
            imTaxCard.isEmpty() || imTaxCard == ""
        ) {
            showToastSnack("please check for upload all attaches.", true)
        } else {
            lifecycleScope.launch {
                registerViewModel.registerIntent.send(
                    RegisterIntent.Register(
                        _firstName,
                        _lastName,
                        _email,
                        _phone,
                        _password,
                        _title,
                        _address,
                        _latitude,
                        _longitude,
                        _firebaseToken,
                        _imNationalId,
                        _imLicense,
                        _imCommercialRegister,
                        _imLeaseOrOwnershipContract,
                        _imTaxCard,
                    )
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_CANCELED) {
            return
        }

        val contentURI = data?.let { it.data }

        if (this.requestCode == 100 && resultCode == RESULT_OK) {
            if (data != null && contentURI != null) {
                imNationalId = contentURI.path!!
                binding.txtUploadNationalId.setText(contentURI.path.toString())
            }
        } else if (this.requestCode == 200 && resultCode == RESULT_OK) {
            if (data != null && contentURI != null) {
                imLicense = contentURI.path!!
                binding.txtUploadLicense.setText(contentURI.path.toString())
            }
        } else if (this.requestCode == 300 && resultCode == RESULT_OK) {
            if (data != null && contentURI != null) {
                imCommercialRegister = contentURI.path!!
                binding.txtUploadCommercialRegister.setText(contentURI.path.toString())
            }
        } else if (this.requestCode == 400 && resultCode == RESULT_OK) {
            if (data != null && contentURI != null) {
                imLeaseOrOwnershipContract = contentURI.path!!
                binding.txtUploadLeaseOrOwnershipContract.setText(contentURI.path.toString())
            }
        } else if (this.requestCode == 500 && resultCode == RESULT_OK) {
            if (data != null && contentURI != null) {
                imTaxCard = contentURI.path!!
                binding.txtUploadTaxCard.setText(contentURI.path.toString())
            }
        } else {
            showToastSnack("something went wrong, Add Image Again", true)
        }
    }
}



