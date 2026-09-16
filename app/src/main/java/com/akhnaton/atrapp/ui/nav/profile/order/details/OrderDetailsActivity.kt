package com.akhnaton.atrapp.ui.nav.profile.order.details

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.data.model.orderHistory.Item
import com.akhnaton.atrapp.data.model.orderHistory.OrderDetailsModel
import com.akhnaton.atrapp.data.statuesValue.nav.home.orde_states.OrderStatesIntent
import com.akhnaton.atrapp.data.statuesValue.nav.profile.orderHistory.orderDetails.MyOrderDetailsIntent
import com.akhnaton.atrapp.data.statuesValue.nav.profile.orderHistory.orderDetails.MyOrderDetailsStatus
import com.akhnaton.atrapp.databinding.ActivityOrderDetailsBinding
import com.akhnaton.atrapp.domain.OrderStateRepository
import com.akhnaton.atrapp.shared.BaseActivity
import com.akhnaton.atrapp.shared.Common
import kotlinx.coroutines.launch

class OrderDetailsActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityOrderDetailsBinding
    private val orderDetailsViewModel: MyOrderDetailsViewModel by viewModels()
    private var mAdapter = OrderDetailsAdapter()
    var mList = mutableListOf<Item>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private val viewModel: OrderStatesViewModel by viewModels {
        OrderStatesViewModelFactory(OrderStateRepository())
    }

    private fun init() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_details)

        val ORDER_ID = intent.getStringExtra("ORDER_ID") ?: ""

        Log.d(Common.KeroDebug, "OrderDetailsActivity: Entered screen with ORDER_ID = $ORDER_ID")

        binding.btnBack.setOnClickListener(this)
        binding.productRecycler.apply {
            layoutManager = LinearLayoutManager(
                this@OrderDetailsActivity,
                LinearLayoutManager.VERTICAL, false
            )
        }
        binding.productRecycler.adapter = mAdapter

        binding.orderNumber.text = "${resources.getString(R.string.order_number)}: ${ORDER_ID}"

        observe()
        getOrderDetails(ORDER_ID)
        getOrderStates(ORDER_ID)
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

                            mList.addAll(it.data.item)
                            mAdapter.setData(mList)

                            val grandTotalVal = it.data.totalOrderPriceWithTax?.toInt() ?: 0
                            val subtotalVal = grandTotalVal
                            val taxVal = 0.0

                            binding.total.text = "$grandTotalVal EGP"

                            binding.total.setOnClickListener {
                                val intent = Intent(this@OrderDetailsActivity, OrderBottomSheet::class.java).apply {
                                    putExtra("SUBTOTAL", subtotalVal)
                                    putExtra("TAX", taxVal)
                                    putExtra("GRAND_TOTAL", grandTotalVal)
                                }
                                startActivity(intent)
                            }

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

    private fun getOrderDetails(ORDER_ID: String) {
        lifecycleScope.launch {
            orderDetailsViewModel.orderDetailsIntent.send(
                MyOrderDetailsIntent.GetMyOrderDetails(ORDER_ID)
            )
        }
    }

    private fun getOrderStates(ORDER_ID: String) {
        viewModel.handleIntent(OrderStatesIntent.GetOrderState, ORDER_ID)
    }
}