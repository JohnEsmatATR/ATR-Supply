package com.akhnaton.atrapp.ui.nav.cart.addresses

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.data.model.AddressModel
import com.akhnaton.atrapp.databinding.ActivityAddressesBinding
import com.akhnaton.atrapp.shared.BaseActivity

class   AddressesActivity : BaseActivity() {
    private lateinit var binding: ActivityAddressesBinding
    private val viewModel: AddressesViewModel by viewModels()
    lateinit var addressesAdapter: AddressesAdapter
    var isChecked: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@AddressesActivity, R.layout.activity_addresses)

        observe()
        onClick()
    }

    private fun setupBinding() {

    }
    override fun onResume() {
        super.onResume()
        init()
    }

    private fun init() {
        getAddresses()
    }

    private fun onClick() {
        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.btnAddAddress.setOnClickListener {
            startActivity(Intent(this@AddressesActivity, AddNewAddressActivity::class.java))
            finish()
        }

    }

    private fun observe() {
//        lifecycleScope.launch {
//            viewModel.state.collect {
//                when (it) {
//                    is AddressStatus.Idle -> Log.d(Common.KeroDebug, "observeHome: Idle")
//                    is AddressStatus.Loading -> {
//                        Log.d(Common.KeroDebug, "observeHome: Loading")
//                        showProgressDialog(binding.progressLoading)
//                    }
//
//                    is AddressStatus.GetMyAddresses -> {
//                        if (it.data.status == 1) {
//                            hideProgressDialog(binding.progressLoading)
//                            Log.d(Common.KeroDebug, "observeHome: GetProducts")
//                            if (isChecked) {
//                                setResult(RESULT_OK)
//                                finish()
//                            }
//                            setupMyCartRecycler(it.data.data!!.addresses)
//
//                        } else if (it.data.status == 401) {
//                            hideProgressDialog(binding.progressLoading)
//                            onTokenExpired(it.data.errors!![0])
//
//                        } else {
//                            hideProgressDialog(binding.progressLoading)
//                            showToastSnack(it.data.message, true)
//                        }
//
//                    }
//
//                    is AddressStatus.AddUserAddress -> {
//
//                    }
//
//                    is AddressStatus.MakeAddressPrime -> {
//                        if (it.data.status == 1) {
//                            hideProgressDialog(binding.progressLoading)
//                            Log.d(Common.KeroDebug, "observeHome: GetProducts")
//                            showToastSnack(it.data.message, false)
//                            getAddresses()
//
//                        } else if (it.data.status == 401) {
//                            hideProgressDialog(binding.progressLoading)
//                            onTokenExpired(it.data.errors!![0])
//
//                        } else {
//                            hideProgressDialog(binding.progressLoading)
//                            showToastSnack(it.data.message, true)
//                        }
//
//                    }
//
//                    is AddressStatus.Error -> {
//                        Log.d(Common.KeroDebug, "observeHome Error: ${it.error.toString()}")
//                        hideProgressDialog(binding.progressLoading)
//                        showToastSnack(it.error.toString(), true)
//                    }
//
//                }
//            }
//        }
    }

    private fun getAddresses() {
        val list = ArrayList<AddressModel>()
        val item = AddressModel(ID= 0, city_id = 0, area_id = 0, receiver_name = "asdasd", receiver_phone = "phone",
            user_id = 0, country_name = "Egypt", city_name = "Cairo", area_name = "Area", floor_number = 0, apartment_number = 0, ADDRESS = "Pharmacy", landmark = "Landmark", prime = 0 )
        list.add(item)
        list.add(item)
        list.add(item)
        list.add(item)
        setupMyCartRecycler(list)
//        lifecycleScope.launch {
//            viewModel.addressIntent.send(
//                AddressIntent.GetMyAddresses(
//                    "Bearer ${SharedPreferenceHelper.userToken}",
//                )
//            )
//        }
    }


    private fun makeAddressPrime(addressId: Int) {
//        lifecycleScope.launch {
//            viewModel.addressIntent.send(
//                AddressIntent.MakeAddressPrime(
//                    "Bearer ${SharedPreferenceHelper.userToken}",
//                    addressId,
//                )
//            )
//        }
    }


    private fun setupMyCartRecycler(list: List<AddressModel>) {
        val layoutManager = LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false)
        addressesAdapter =  AddressesAdapter(onClick = { address, position ->
            makeAddressPrime(address.ID)
            isChecked = true
        })
        addressesAdapter.setData(list)
        binding.recycler.layoutManager = layoutManager
        binding.recycler.adapter = addressesAdapter
    }

}