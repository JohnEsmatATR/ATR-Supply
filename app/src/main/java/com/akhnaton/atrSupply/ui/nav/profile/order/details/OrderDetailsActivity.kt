package com.akhnaton.atrSupply.ui.nav.profile.order.details

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.akhnaton.atrSupply.R
import com.akhnaton.atrSupply.data.model.orderHistory.OrderDetailsModel
import com.akhnaton.atrSupply.data.statuesValue.nav.home.orde_states.OrderStatesIntent
import com.akhnaton.atrSupply.data.statuesValue.nav.profile.orderHistory.orderDetails.MyOrderDetailsIntent
import com.akhnaton.atrSupply.data.statuesValue.nav.profile.orderHistory.orderDetails.MyOrderDetailsStatus
import com.akhnaton.atrSupply.databinding.ActivityOrderDetailsBinding
import com.akhnaton.atrSupply.domain.OrderStateRepository
import com.akhnaton.atrSupply.shared.BaseActivity
import com.akhnaton.atrSupply.shared.Common
import kotlinx.coroutines.launch

class OrderDetailsActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityOrderDetailsBinding
    private val orderDetailsViewModel: MyOrderDetailsViewModel by viewModels()
    private var mAdapter = OrderDetailsAdapter()
 var mList = mutableListOf<OrderDetailsModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()
    }
    private val viewModel: OrderStatesViewModel by viewModels {
        OrderStatesViewModelFactory(OrderStateRepository())
    }
    private fun init() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_details)

        binding.btnBack.setOnClickListener(this)
        binding.productRecycler.apply {
            layoutManager =
                LinearLayoutManager(
                    this@OrderDetailsActivity,
                    LinearLayoutManager.VERTICAL, false
                )
        }
        binding.productRecycler.adapter = mAdapter

        val orgSysId = intent.getStringExtra("orgSysId")?:""

        binding.orderNumber.text = "Order Number ${orgSysId}"

        observe()
        getOrderDetails(orgSysId)
       getOrderStates(orgSysId)
    }


    override fun onClick(v: View) {

        if (v.id == binding.btnBack.id) {
            finish()
        }
    }

    private fun observe() {
        lifecycleScope.launch {
            orderDetailsViewModel.state.collect {
                when (it) {
                    is MyOrderDetailsStatus.Idle -> Log.d(Common.KeroDebug, "observeHome: Idle")
                    is MyOrderDetailsStatus.Loading -> {
                        Log.d(Common.KeroDebug, "observeHome: Loading")
                        showProgressDialog(binding.progressLoading)
                    }

                    is MyOrderDetailsStatus.GetMyOrderDetails -> {
                        if (it.data.status == 200) {
                            hideProgressDialog(binding.progressLoading)
                            Log.d(Common.KeroDebug, "observeHome: GetProducts")

                            mList.addAll(it.data.data!!)
                            mAdapter.setData(mList)

                            binding.total.text = "${it.data.total} EGP"
                        } else {
                            hideProgressDialog(binding.progressLoading)
                            showToastSnack(it.data.message, true)
                        }

                    }

                    is MyOrderDetailsStatus.Error -> {
                        Log.d(Common.KeroDebug, "observeHome Error: ${it.error.toString()}")
                        hideProgressDialog(binding.progressLoading)
                        showToastSnack(it.error.toString(), true)
                    }
                }
            }
        }
    }
    private fun getOrderDetails(orgSysId: String) {
        lifecycleScope.launch {
            orderDetailsViewModel.orderDetailsIntent.send(
                MyOrderDetailsIntent.GetMyOrderDetails(orgSysId)
            )
        }
    }
    private fun getOrderStates(orgSysId: String) {
        viewModel.handleIntent(OrderStatesIntent.GetOrderState, orgSysId)
    }
}