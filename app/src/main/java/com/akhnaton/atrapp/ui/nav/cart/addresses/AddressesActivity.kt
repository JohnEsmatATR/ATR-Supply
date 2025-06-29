package com.akhnaton.atrapp.ui.nav.cart.addresses

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.data.model.AddressModel
import com.akhnaton.atrapp.data.statuesValue.nav.home.address.AddressIntent
import com.akhnaton.atrapp.data.statuesValue.nav.home.address.AddressStatus
import com.akhnaton.atrapp.data.statuesValue.nav.home.category.CategoryIntent
import com.akhnaton.atrapp.databinding.ActivityAddressesBinding
import com.akhnaton.atrapp.shared.BaseActivity
import com.akhnaton.atrapp.shared.Common
import kotlinx.coroutines.launch

class   AddressesActivity : BaseActivity() {
    private lateinit var binding: ActivityAddressesBinding
    private val viewModel: AddressesViewModel by viewModels()
    lateinit var addressesAdapter: AddressesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@AddressesActivity, R.layout.activity_addresses)

        observe()
        onClick()
        getAddress()
    }
    private fun onClick() {
        binding.btnBack.setOnClickListener {
            finish()
        }


    }
    private fun observe() {
        lifecycleScope.launch {
            viewModel.state.collect {
                Log.d("DEBUG", "Received state: $it")
                when (it) {
                    is AddressStatus.Idle -> Log.d(Common.KeroDebug, "observeHome: Idleeeee")
                    is AddressStatus.Loading -> {
                        Log.d(Common.KeroDebug, "observeHome: Loading")
                        showProgressDialog(binding.progressLoading)
                    }

                    is AddressStatus.GetMyAddresses -> {
                        if (it.result.status == 200) {
                            hideProgressDialog(binding.progressLoading)
                            Log.d(Common.KeroDebug, "observeHome: GetProducts")

                            setupMyCartRecycler(it.result.data!!)

                        } else if (it.result.status == 401) {
                            hideProgressDialog(binding.progressLoading)


                        } else {
                            hideProgressDialog(binding.progressLoading)

                        }

                    }



                    is AddressStatus.MakeAddressPrime -> {
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

                    }

                    is AddressStatus.Error -> {
//                        Log.d(Common.KeroDebug, "observeHome Error: ${it.error.toString()}")
//                        hideProgressDialog(binding.progressLoading)
//                        showToastSnack(it.error.toString(), true)
                    }

                }
            }
        }
    }




    private fun makeAddressPrime(addressId: String) {
//        lifecycleScope.launch {
//            viewModel.addressIntent.send(
//                AddressIntent.MakeAddressPrime(
//                    "Bearer ${SharedPreferenceHelper.userToken}",
//                    addressId,
//                )
//            )
//        }
    }

    private fun getAddress() {

        lifecycleScope.launch {
            viewModel.addressIntent.send(
                AddressIntent.GetMyAddresses(12447)
            )
        }
    }

    private fun setupMyCartRecycler(list: List<AddressModel>) {
        val layoutManager = LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false)
        addressesAdapter =  AddressesAdapter(onClick = { address, position ->
            Log.d("DEBUG", "First address: ${list.firstOrNull()?.receiver_name}")

        })
        addressesAdapter.setData(list)
        binding.recycler.layoutManager = layoutManager
        binding.recycler.adapter = addressesAdapter
    }

}