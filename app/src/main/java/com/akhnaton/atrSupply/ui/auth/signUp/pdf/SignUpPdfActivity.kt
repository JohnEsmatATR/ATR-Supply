package com.akhnaton.atrSupply.ui.auth.signUp.pdf

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.akhnaton.atrSupply.data.statuesValue.auth.register.RegisterIntent
import com.akhnaton.atrSupply.data.statuesValue.auth.register.RegisterStatus
import com.akhnaton.atrSupply.databinding.ActivitySignUpPdfBinding
import com.akhnaton.atrSupply.shared.BaseActivity
import com.akhnaton.atrSupply.shared.Common
import com.akhnaton.atrSupply.ui.auth.signUp.RegisterViewModel
import com.akhnaton.atrSupply.ui.auth.waiting.WaitingActivity
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream

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
    private var mapAddress=""
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
        mapAddress=intent.getStringExtra("mapLocation")?:""
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
            choosePdfFile(100)
        }
        binding.layoutUploadLicense.setOnClickListener {
            choosePdfFile(200)
        }
        binding.layoutUploadCommercialRegister.setOnClickListener {
            choosePdfFile(300)
        }
        binding.layoutUploadLeaseOrOwnershipContract.setOnClickListener {
            choosePdfFile(400)
        }
        binding.layoutUploadTaxCard.setOnClickListener {
            choosePdfFile(500)
        }


    }

    private fun choosePdfFile(code: Int) {
        requestCode = code
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/pdf"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), code)
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
                            finishAffinity()

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
        val _mapLocation = mapAddress.toRequestBody("text/plain".toMediaTypeOrNull())
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
                        _mapLocation
                    )
                )
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK || data == null || data.data == null) {
            showToastSnack("Something went wrong. Please select the PDF again.", true)
            return
        }

        val uri = data.data!!
        val filePath = getRealPathFromURI(uri)
        val fileName = getFileName(uri)

        when (this.requestCode) {
            100 -> {
                imNationalId = filePath
                binding.txtUploadNationalId.text = fileName
            }
            200 -> {
                imLicense = filePath
                binding.txtUploadLicense.text = fileName
            }
            300 -> {
                imCommercialRegister = filePath
                binding.txtUploadCommercialRegister.text = fileName
            }
            400 -> {
                imLeaseOrOwnershipContract = filePath
                binding.txtUploadLeaseOrOwnershipContract.text = fileName
            }
            500 -> {
                imTaxCard = filePath
                binding.txtUploadTaxCard.text = fileName
            }
        }
    }

    private fun getFileName(uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
                }
            } finally {
                cursor?.close()
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result?.lastIndexOf('/')
            if (cut != -1 && cut != null) {
                result = result?.substring(cut + 1)
            }
        }
        return result ?: "unknown.pdf"
    }

    private fun getRealPathFromURI(uri: Uri): String {
        val inputStream = contentResolver.openInputStream(uri)
        val file = File(cacheDir, getFileName(uri))
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        return file.absolutePath
    }


}



