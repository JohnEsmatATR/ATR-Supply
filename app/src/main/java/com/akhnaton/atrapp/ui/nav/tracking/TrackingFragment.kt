package com.akhnaton.atrapp.ui.nav.tracking

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.data.model.orderHistory.OrderHistoryModel
import com.akhnaton.atrapp.data.statuesValue.nav.home.address.AddressIntent
import com.akhnaton.atrapp.data.statuesValue.nav.home.address.AddressStatus
import com.akhnaton.atrapp.data.statuesValue.nav.profile.orderHistory.myOrders.MyOrdersIntent
import com.akhnaton.atrapp.data.statuesValue.nav.profile.orderHistory.myOrders.MyOrdersStatus
import com.akhnaton.atrapp.databinding.FragmentTrackingBinding
import com.akhnaton.atrapp.shared.BaseFragment
import com.akhnaton.atrapp.shared.Common
import com.akhnaton.atrapp.shared.SharedPreferenceHelper
import com.akhnaton.atrapp.ui.auth.login.LoginActivity
import com.akhnaton.atrapp.ui.nav.cart.addresses.AddressesActivity
import com.akhnaton.atrapp.ui.nav.cart.addresses.AddressesViewModel
import com.akhnaton.atrapp.ui.nav.profile.order.details.OrderDetailsActivity
import com.akhnaton.atrapp.ui.nav.profile.order.history.MyOrdersViewModel
import com.akhnaton.atrapp.ui.nav.profile.order.history.OrderHistoryAdapter
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TrackingFragment : BaseFragment(), OrderHistoryAdapter.OnProductClickListener,
    View.OnClickListener {
    lateinit var binding: FragmentTrackingBinding
    private val ordersViewModel: MyOrdersViewModel by viewModels()
    private var mAdapter = OrderHistoryAdapter()
    private val viewModel: AddressesViewModel by viewModels()
    private var mList = mutableListOf<OrderHistoryModel>()
    private val calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTrackingBinding.inflate(inflater)

        guestHandling()
        if (SharedPreferenceHelper.isLogged!!) {
            observe()
            setRecycler()
            getMyOrders()
            getAddress()
            observeAddress()
            binding.cardAddress.setOnClickListener {
                val intent = Intent(requireContext(), AddressesActivity::class.java)
                startActivity(intent)
            }
        }
        return binding.root
    }

    private fun guestHandling() {
        if (!SharedPreferenceHelper.isLogged!!) {
            binding.clGuest.visibility = View.VISIBLE
            binding.orderRecycler.visibility = View.GONE
            binding.layoutAddress.visibility = View.GONE
            binding.appBar.visibility = View.GONE
        }

        binding.btnLogin.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
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
                            // hideProgressDialog(binding.progressLoading)
                            Log.d(Common.KeroDebug, "Received: GetProducts")

                            val addresses = it.result.data ?: emptyList()


                            addresses.forEach { address ->
                                if (address.prime == 1) {
                                    Log.d("DEBUG_ADDRESS", "Prime address found: $address")
                                    binding.defaultAddress.text = address.TITLE
                                }
                            }

                            val hasDefault = addresses.any { address -> address.prime == 1 }
                            if (!hasDefault) {
                                val intent = Intent(requireContext(), AddressesActivity::class.java)
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

    private fun setRecycler() {
        binding.orderRecycler.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        binding.orderRecycler.adapter = mAdapter
    }

    override fun onClick(v: View) {
        showDatePicker(v.id)
    }

    private fun showDatePicker(id: Int) {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
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
                        hideProgressDialog(binding.progressLoading)
                        if (it.data.status == 200) {
                            Log.d(Common.KeroDebug, "observeHome: GetProducts")

                            mList.addAll(it.data.data!!)
                            mAdapter.setData(mList, this@TrackingFragment)
                            if (mList.size == 0) {
                                binding.txtNoTrackingHistory.visibility=View.VISIBLE
                            }

                        } else {
                            hideProgressDialog(binding.progressLoading)
                            showToastSnack(it.data.message, true)
                        }

                    }

                    is MyOrdersStatus.Error -> {
                        Log.d(Common.KeroDebug, "observeHome Error: ${it.error.toString()}")
                        // hideProgressDialog(binding.progressLoading)
                        // showToastSnack(it.error.toString(), true)
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


    override fun onProductClick(data: OrderHistoryModel) {
        Log.d("ORDER_PASS", "111")

        val intent = Intent(requireContext(), OrderDetailsActivity::class.java)
        intent.putExtra("ORDER_ID", data.ORDER_ID)
        startActivity(intent)
    }
}