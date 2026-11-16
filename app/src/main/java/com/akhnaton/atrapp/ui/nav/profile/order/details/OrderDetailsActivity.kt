package com.akhnaton.atrapp.ui.nav.profile.order.details

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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
import com.akhnaton.atrapp.ui.nav.profile.order.history.OrderStatusTimelineHelper
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.collections.orEmpty

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

        binding.orderNumber.text = "#$ORDER_ID"

        handleTopBottomKeyboard()
        observe()
        getOrderDetails(ORDER_ID)
        getOrderStates(ORDER_ID)
    }

    override fun onClick(v: View) {
        if (v.id == binding.btnBack.id) {
            finish()
        }
    }

    private fun bindStatusBadge(
        steps: List<com.akhnaton.atrapp.data.model.orderHistory.StatusGroup>
    ) {

        val currentStep =
            steps
                .filter {
                    it.isCompleted == true
                }
                .lastOrNull()
                ?: steps.firstOrNull()

        if (currentStep == null) {

            binding.tvBadgeStatus.visibility =
                View.GONE

            return
        }

        binding.tvBadgeStatus.visibility =
            View.VISIBLE

        val color = parseColor(
            currentStep.color,
            Color.parseColor("#FF9500")
        )

        binding.tvBadgeStatus.text =
            "• ${currentStep.name.orEmpty()}"

        binding.tvBadgeStatus.setTextColor(
            color
        )

        binding.tvBadgeStatus.background =
            createRoundedBackground(
                color = color,
                cornerRadius = 30f,
                alpha = 35
            )
    }

    private fun parseColor(
        color: String?,
        defaultColor: Int
    ): Int {

        return try {

            Color.parseColor(
                color ?: ""
            )

        } catch (e: Exception) {

            defaultColor
        }
    }

    private fun createRoundedBackground(
        color: Int,
        cornerRadius: Float,
        alpha: Int
    ): GradientDrawable {

        val backgroundColor = Color.argb(
            alpha,
            Color.red(color),
            Color.green(color),
            Color.blue(color)
        )

        return GradientDrawable().apply {

            shape =
                GradientDrawable.RECTANGLE

            setColor(backgroundColor)

            this.cornerRadius =
                cornerRadius
        }
    }

    private fun observe() {
        lifecycleScope.launch {
            orderDetailsViewModel.state.collect { status ->
                when (status) {
                    is MyOrderDetailsStatus.Idle -> Log.d(Common.KeroDebug, "observeHome: Idle")
                    is MyOrderDetailsStatus.Loading -> {
                        Log.d(Common.KeroDebug, "observeHome: Loading")
                        binding.loadingOverlay.root.visibility = View.VISIBLE
                    }

                    is MyOrderDetailsStatus.GetMyOrderDetails -> {
                        binding.loadingOverlay.root.visibility = View.GONE
                        if (status.myOrderDetailsRes.status == 200) {
                            Log.d(Common.KeroDebug, "observeHome: GetProducts")

                            mList.clear()
                            mList.addAll(status.myOrderDetailsRes.item)
                            mAdapter.setData(mList)

                            val itemsCount = status.myOrderDetailsRes.item?.size ?: 0
                            val totalPieces = status.myOrderDetailsRes.item?.sumOf { it.QUANTITY ?: 1 } ?: 0

                            val subtotalVal =
                                status.myOrderDetailsRes.totalOrderPriceWithoutTax?.toDouble() ?: 0.0
                            val taxVal = status.myOrderDetailsRes.totalOrderTax?.toDouble() ?: 0.0
                            val grandTotalVal =
                                status.myOrderDetailsRes.totalOrderPriceWithTax?.toDouble() ?: 0.0

                            val currency = getString(R.string.currency)
                            binding.total.text = String.format(Locale.US, "%,.3f %s", grandTotalVal, currency)

                            binding.btnMoreDetails.setOnClickListener {
                                val bottomSheet = OrderBottomSheet().apply {
                                    arguments = Bundle().apply {
                                        putDouble("SUBTOTAL", subtotalVal)
                                        putDouble("TAX", taxVal)
                                        putDouble("GRAND_TOTAL", grandTotalVal)
                                        putInt("ITEMS_COUNT", itemsCount)
                                        putInt("TOTAL_PIECES", totalPieces)
                                    }
                                }
                                bottomSheet.show(supportFragmentManager, "OrderBottomSheet")
                            }

                            val statusTimelineHelper =
                                OrderStatusTimelineHelper(
                                    binding.statusContainer
                                )

                            val steps = status.myOrderDetailsRes.statusGroup

                            statusTimelineHelper.bind(steps)
                            bindStatusBadge(steps)

                        } else {
                            binding.loadingOverlay.root.visibility = View.GONE
                            showToastSnack(status.myOrderDetailsRes.message, true)
                        }
                    }

                    is MyOrderDetailsStatus.Error -> {
                        Log.d(Common.KeroDebug, "observeHome Error: ${status.error}")
                        binding.loadingOverlay.root.visibility = View.GONE
                        showToastSnack(status.error.toString(), true)
                    }
                }
            }
        }
    }

    private fun handleTopBottomKeyboard() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            val imeInsets = insets.getInsets(
                WindowInsetsCompat.Type.ime()
            )
            binding.root.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                maxOf(imeInsets.bottom, systemBars.bottom)
            )

            insets
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