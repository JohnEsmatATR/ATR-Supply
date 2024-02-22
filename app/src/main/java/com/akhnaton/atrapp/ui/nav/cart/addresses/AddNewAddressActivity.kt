package com.akhnaton.atrapp.ui.nav.cart.addresses

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.data.model.MapModel
import com.akhnaton.atrapp.databinding.ActivityAddNewAddressBinding
import com.akhnaton.atrapp.shared.BaseActivity

class AddNewAddressActivity : BaseActivity() {
    lateinit var binding: ActivityAddNewAddressBinding
    private val addressesViewModel: AddressesViewModel by viewModels()
    private var citiesList: ArrayList<MapModel> = ArrayList()
    private val areasList: ArrayList<MapModel> = ArrayList()
    private var citiesPosition: Int = 0
    private var areasPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        onClick()
    }

    private fun init() {
        addressesObserve()
        observeRegister()
        getCities()
        spCitiesSelected()
        spAreasSelected()
    }

    private fun onClick() {
        binding.btnAdd.setOnClickListener {
            addNewAddress()
        }
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun observeRegister() {
//        lifecycleScope.launch {
//            registerViewModel.state.collect { it ->
//                when (it) {
//                    is RegisterStatus.Idle -> Log.d(Common.KeroDebug, "observeRegister: Idle")
//                    is RegisterStatus.Loading -> {
//                        Log.d(Common.KeroDebug, "observeRegister: Loading")
//                        showProgressDialog(binding.progressLoading)
//                    }
//
//                    is RegisterStatus.GetCountries -> {
//                        if (it.data.status != -1) {
//                            hideProgressDialog(binding.progressLoading)
//                            Log.d(Common.KeroDebug, "observeRegister: " + it.data.message)
//
//                            countriesList.addAll(it.data.data!!.countries)
//
//                            val items = ArrayList<String>()
//                            for (i in it.data.data!!.countries) {
//                                items.add(i.name_en)
//                            }
//                            val adapter: ArrayAdapter<String> =
//                                ArrayAdapter<String>(
//                                    this@AddNewAddressActivity,
//                                    android.R.layout.simple_spinner_dropdown_item,
//                                    items
//                                )
//
//                            binding.spCountry.setAdapter(adapter)
//
//                        } else {
//                            hideProgressDialog(binding.progressLoading)
//                            showToastSnack(it.data.message, true)
//                        }
//                    }
//
//                    is RegisterStatus.GetCities -> {
//                        if (it.data.status != -1) {
//                            hideProgressDialog(binding.progressLoading)
//                            Log.d(Common.KeroDebug, "observeRegister: " + it.data.message)
//
//                            citiesList.addAll(it.data.data!!.cities)
//
//                            val items = ArrayList<String>()
//                            for (i in it.data.data!!.cities) {
//                                items.add(i.name_en)
//                            }
//                            val adapter: ArrayAdapter<String> =
//                                ArrayAdapter<String>(
//                                    this@AddNewAddressActivity,
//                                    android.R.layout.simple_spinner_dropdown_item,
//                                    items
//                                )
//
//                            binding.spCity.setAdapter(adapter)
//
//                        } else {
//                            hideProgressDialog(binding.progressLoading)
//                            showToastSnack(it.data.message, true)
//                        }
//                    }
//
//                    is RegisterStatus.GetAreas -> {
//                        if (it.data.status != -1) {
//                            hideProgressDialog(binding.progressLoading)
//                            Log.d(Common.KeroDebug, "observeRegister: " + it.data.message)
//
//                            areasList.addAll(it.data.data!!.areas)
//
//                            val items = ArrayList<String>()
//                            for (i in it.data.data!!.areas) {
//                                items.add(i.name_en)
//                            }
//                            val adapter: ArrayAdapter<String> =
//                                ArrayAdapter<String>(
//                                    this@AddNewAddressActivity,
//                                    android.R.layout.simple_spinner_dropdown_item,
//                                    items
//                                )
//
//                            binding.spArea.setAdapter(adapter)
//                        } else {
//                            hideProgressDialog(binding.progressLoading)
//                            showToastSnack(it.data.message, false)
//                        }
//                    }
//
//                    is RegisterStatus.Error -> {
//                        Log.d("cjdjndvjnkndv", "observeRegister Error: ${it.error.toString()}")
//
//                        hideProgressDialog(binding.progressLoading)
//                        showToastSnack(it.error.toString(), true)
//                    }
//
//                    else -> {}
//                }
//            }
//        }
    }

    private fun addressesObserve() {
//        lifecycleScope.launch {
//            addressesViewModel.state.collect {
//                when (it) {
//                    is AddressStatus.Idle -> Log.d(Common.KeroDebug, "observeHome: Idle")
//                    is AddressStatus.Loading -> {
//                        Log.d(Common.KeroDebug, "observeHome: Loading")
//                        showProgressDialog(binding.progressLoading)
//                    }
//
//                    is AddressStatus.AddUserAddress -> {
//                        if (it.data.status == 1) {
//                            hideProgressDialog(binding.progressLoading)
//                            Log.d(Common.KeroDebug, "observeHome: GetProducts")
//                            showToastSnack(it.data.message, false)
//                            finish()
//
//                        } else if (it.data.status == 401) {
//                            hideProgressDialog(binding.progressLoading)
//
//                            showDialog("Warning!", it.data.message, true)
//                                .setPositiveButton("Ok") { dialog: DialogInterface, i: Int ->
//                                    SharedPreferenceHelper.let { sharedPref ->
//                                        sharedPref.isLogged = false
//                                        sharedPref.isWelcomeShowed = false
//                                        sharedPref.userObj = null
//                                        sharedPref.membership = 0
//                                        sharedPref.userToken = ""
//                                    }
//                                    startActivity(Intent(baseContext, AuthActivity::class.java))
//                                    finish()
//                                    dialog.dismiss()
//                                }.create().show()
//
//                        } else {
//                            hideProgressDialog(binding.progressLoading)
//                            errorHandling(it.data.errors!!)
//                        }
//                    }
//
//                    is AddressStatus.Error -> {
//                        Log.d(Common.KeroDebug, "observeHome Error: ${it.error.toString()}")
//                        hideProgressDialog(binding.progressLoading)
//                        showToastSnack(it.error.toString(), true)
//                    }
//
//                    else -> {}
//                }
//            }
//        }
    }


    private fun getCountries() {
//        lifecycleScope.launch {
//            registerViewModel.registerIntent.send(
//                RegisterIntent.GetCountries
//            )
//        }
    }

    private fun getCities() {
        val myArray = resources.getStringArray(R.array.cities)

        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(
                this@AddNewAddressActivity,
                android.R.layout.simple_spinner_dropdown_item,
                myArray
            )
        binding.spCity.setAdapter(adapter)

//        lifecycleScope.launch {
//            registerViewModel.registerIntent.send(
//                RegisterIntent.GetCities(id)
//            )
//        }
    }

    private fun getAreas(id: String) {
//        lifecycleScope.launch {
//            registerViewModel.registerIntent.send(
//                RegisterIntent.GetAreas(id)
//            )
//        }
    }

    private fun spCitiesSelected() {
        binding.spCity.setOnItemClickListener { parent, _, position, _ ->
            citiesPosition = citiesList[position].id
            areasList.clear()
            areasPosition = 0
            binding.spArea.setText("")

            val myArray = resources.getStringArray(R.array.cairo_areas)

            val itemsArea = ArrayList<String>()
            for (i in areasList) {
                itemsArea.add(i.name_en)
            }
            val adapterAreas: ArrayAdapter<String> =
                ArrayAdapter<String>(
                    this@AddNewAddressActivity,
                    android.R.layout.simple_spinner_dropdown_item,
                    myArray
                )
            binding.spArea.setAdapter(adapterAreas)


            getAreas(citiesPosition.toString())
        }
    }


    private fun spAreasSelected() {
        binding.spArea.setOnItemClickListener { parent, _, position, _ ->
            areasPosition = areasList[position].id
        }
    }

    private fun addNewAddress() {
        var isError = true

        val city_id = citiesPosition
        val area_id = areasPosition
        val address = binding.layoutAddress.editText!!.text.toString()


        if (city_id == 0) {
            showToastSnack("City id field is empty", true)
            isError = false
        }
        if (area_id == 0) {
            showToastSnack("Area id field is empty", true)
            isError = false
        }
        if (address.isEmpty()) {
            showToastSnack("address field is empty", true)
            isError = false
        }
        if (isError) {
//            lifecycleScope.launch {
//                addressesViewModel.addressIntent.send(
//                    AddressIntent.AddUserAddress(
//                        "Bearer ${SharedPreferenceHelper.userToken}",
//                        address,
//                        landMark,
//                        receiverName,
//                        receiverPhone,
//                        floorNumber.toInt(),
//                        apartmentNumber.toInt(),
//                        city_id,
//                        country_id,
//                        area_id,
//                        0
//                    )
//                )
//            }
        }
    }

}