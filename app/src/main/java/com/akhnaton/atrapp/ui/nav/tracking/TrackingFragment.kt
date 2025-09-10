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
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.data.model.orderHistory.OrderHistoryModel
import com.akhnaton.atrapp.data.statuesValue.nav.profile.orderHistory.myOrders.MyOrdersIntent
import com.akhnaton.atrapp.data.statuesValue.nav.profile.orderHistory.myOrders.MyOrdersStatus
import com.akhnaton.atrapp.databinding.FragmentTrackingBinding
import com.akhnaton.atrapp.shared.BaseFragment
import com.akhnaton.atrapp.shared.Common
import com.akhnaton.atrapp.ui.nav.profile.order.details.OrderDetailsActivity
import com.akhnaton.atrapp.ui.nav.profile.order.history.MyOrdersViewModel
import com.akhnaton.atrapp.ui.nav.profile.order.history.OrderHistoryAdapter
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TrackingFragment : BaseFragment()  , OrderHistoryAdapter.OnProductClickListener,
    View.OnClickListener {
    lateinit var binding: FragmentTrackingBinding
    private val ordersViewModel: MyOrdersViewModel by viewModels()
    private var mAdapter = OrderHistoryAdapter()
    private var mList = mutableListOf<OrderHistoryModel>()
    private val calendar = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTrackingBinding.inflate(inflater)
        getMyOrders()
        observe()
        getMyOrders()
        return binding.root
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
                        if (it.data.status == 200) {
                            hideProgressDialog(binding.progressLoading)
                            Log.d(Common.KeroDebug, "observeHome: GetProducts")

                            mList.addAll(it.data.data!!)
                            mAdapter.setData(mList, this@TrackingFragment)

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


    override fun onProductClick(data: OrderHistoryModel) {
        val intent = Intent(requireContext(), OrderDetailsActivity::class.java)
        intent.putExtra("orgSysId", data.ORIG_SYS_DOCUMENT_REF)
        startActivity(intent)
    }
}