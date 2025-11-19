package com.akhnaton.atrapp.ui.nav.profile.order.history

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.data.model.orderHistory.OrderHistoryModel
import com.akhnaton.atrapp.data.statuesValue.nav.home.address.AddressIntent
import com.akhnaton.atrapp.data.statuesValue.nav.home.address.AddressStatus
import com.akhnaton.atrapp.data.statuesValue.nav.profile.orderHistory.myOrders.MyOrdersIntent
import com.akhnaton.atrapp.data.statuesValue.nav.profile.orderHistory.myOrders.MyOrdersStatus
import com.akhnaton.atrapp.databinding.ActivityOrderHistoryBinding
import com.akhnaton.atrapp.shared.BaseActivity
import com.akhnaton.atrapp.shared.Common
import com.akhnaton.atrapp.ui.nav.cart.addresses.AddressesActivity
import com.akhnaton.atrapp.ui.nav.cart.addresses.AddressesViewModel
import com.akhnaton.atrapp.ui.nav.profile.order.details.OrderDetailsActivity
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class OrderHistoryActivity : BaseActivity(), OrderHistoryAdapter.OnProductClickListener,
    View.OnClickListener {
    private lateinit var binding: ActivityOrderHistoryBinding
    private val ordersViewModel: MyOrdersViewModel by viewModels()
    private var mAdapter = OrderHistoryAdapter()
    private var mList = mutableListOf<OrderHistoryModel>()
    private val viewModel: AddressesViewModel by viewModels()
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        observe()
        getMyOrders()
        getAddress()
        observeAddress()
        binding.cardAddress.setOnClickListener {
            val intent = Intent(applicationContext, AddressesActivity::class.java)
            startActivity(intent)
        }
    }
    private fun observeAddress() {
        lifecycleScope.launch {
            viewModel.state.collect {
                Log.d("DEBUGGGGG", "Received state: $it")
                when (it) {
                    is AddressStatus.Idle -> Log.d(Common.KeroDebug, "observeHome: Idleeeee")
                    is AddressStatus.Loading -> {
                        Log.d(Common.KeroDebug, "observeHome: Loading")
                        showProgressDialog(binding.progressLoading)
                    }

                    is AddressStatus.GetMyAddresses -> {
                        if (it.result.status == 200) {
                            hideProgressDialog(binding.progressLoading)
                            Log.d(Common.KeroDebug, "Received: GetProducts")

                            val addresses = it.result.data ?: emptyList()


                            addresses.forEach { address ->
                                if (address.prime == 1) {
                                    Log.d("DEBUG_ADDRESS", "Prime address found: $address")
                                    binding.defaultAddress.text=address.TITLE
                                }
                            }

                            val hasDefault = addresses.any { address -> address.prime == 1 }
                            if (!hasDefault) {
                                val intent = Intent(applicationContext, AddressesActivity::class.java)
                                startActivity(intent)
                            }

                        } else {
                            hideProgressDialog(binding.progressLoading)
                        }
                    }



                    is AddressStatus.MakeAddressPrime -> {}

                    is AddressStatus.Error -> {

                    }

                }
            }
        }
    }
    private fun getAddress() {
        lifecycleScope.launch {
            viewModel.addressIntent.send(
                AddressIntent.GetMyAddresses
            )
        }
    }

    private fun setupBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_history)

        binding.orderRecycler.apply {
            layoutManager =
                LinearLayoutManager(this@OrderHistoryActivity, LinearLayoutManager.VERTICAL, false)
        }

        binding.orderRecycler.adapter = mAdapter
        binding.fromLayout.setOnClickListener(this)
        binding.fromED.setOnClickListener(this)
        binding.toLayout.setOnClickListener(this)
        binding.toED.setOnClickListener(this)
        binding.btnBack.setOnClickListener(this)
    }


    override fun onProductClick(data: OrderHistoryModel) {
        val intent = Intent(this, OrderDetailsActivity::class.java)
        intent.putExtra("orgSysId", data.ORIG_SYS_DOCUMENT_REF)
        startActivity(intent)
    }

    override fun onClick(v: View) {
        if (v.id == binding.btnBack.id) {
            finish()
        } else {
            showDatePicker(v.id)
        }

    }


    private fun showDatePicker(id: Int) {
        val datePickerDialog = DatePickerDialog(
            this,
            R.style.CustomDatePickerDialog,
            { _, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)
                if (binding.fromED.id == id) {
                    binding.fromED.setText(formattedDate.toString())
                } else {
                    binding.toED.setText(formattedDate.toString())
                }


            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun observe() {
        lifecycleScope.launch {
            ordersViewModel.state.collect {
                when (it) {
                    is MyOrdersStatus.Idle -> Log.d(Common.KeroDebug, "observeHome: Idle")
                    is MyOrdersStatus.Loading -> {
                        Log.d(Common.KeroDebug, "observeHome: Loading")
                        showProgressDialog(binding.progressLoading)
                    }

                    is MyOrdersStatus.GetMyOrders -> {
                        if (it.data.status == 200) {
                            hideProgressDialog(binding.progressLoading)
                            Log.d(Common.KeroDebug, "observeHome: GetProducts")

                            mList.addAll(it.data.data!!)
                            mAdapter.setData(mList, this@OrderHistoryActivity)

                        } else {
                            hideProgressDialog(binding.progressLoading)
                            showToastSnack(it.data.message, true)
                        }

                    }

                    is MyOrdersStatus.Error -> {
                        Log.d(Common.KeroDebug, "observeHome Error: ${it.error.toString()}")
                        hideProgressDialog(binding.progressLoading)
                        showToastSnack(it.error.toString(), true)
                    }
                }
            }
        }
    }

    private fun getMyOrders() {
        lifecycleScope.launch {
            ordersViewModel.ordersIntent.send(
                MyOrdersIntent.GetMyOrders
            )
        }
    }

}