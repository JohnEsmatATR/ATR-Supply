package com.akhnaton.atrapp.ui.nav.cart.checkout

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.data.statuesValue.nav.cart.checkout.CheckoutIntent
import com.akhnaton.atrapp.data.statuesValue.nav.cart.checkout.CheckoutStatus
import com.akhnaton.atrapp.data.statuesValue.nav.cart.paymentType.PaymentIndent
import com.akhnaton.atrapp.data.statuesValue.nav.cart.paymentType.PaymentTypeStatus
import com.akhnaton.atrapp.databinding.ActivityChackoutBinding
import com.akhnaton.atrapp.shared.BaseActivity
import com.akhnaton.atrapp.shared.Common
import com.akhnaton.atrapp.shared.CustomDialog
import com.akhnaton.atrapp.ui.nav.cart.checkout.paymentMethod.OrderNumberActivity
import com.akhnaton.atrapp.ui.nav.cart.checkout.paymentMethod.PayenTypeViewModel
import com.akhnaton.atrapp.ui.nav.cart.checkout.paymentMethod.PaymentAdapter
import kotlinx.coroutines.launch


class CheckoutActivity : BaseActivity() {
    lateinit var binding: ActivityChackoutBinding
    lateinit var mDialog: CustomDialog
    val checkoutViewModel: CheckoutViewModel by viewModels()
    private val paymentTypeViewModel: PayenTypeViewModel by viewModels()
    private lateinit var paymentAdapter: PaymentAdapter
    private var selectedPaymentId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        cartObserve()
        binding.imBack.setOnClickListener {
            finish()
        }
        binding.btnConfirmOrder.setOnClickListener {
            selectedPaymentId?.let { paymentId ->
                checkOut(paymentId)
            }
        }
    }

    private fun setupBinding() {
        binding = DataBindingUtil.setContentView(this@CheckoutActivity, R.layout.activity_chackout)
        mDialog = CustomDialog(this)

        setupPaymentRecycler()
        observePaymentTypes()
        getPaymentTypes()
    }



    private fun cartObserve() {
        lifecycleScope.launch {
            checkoutViewModel.state.collect {
                when (it) {
                    is CheckoutStatus.Idle -> Log.d(Common.KeroDebug, "observeHome: Idle")
                    is CheckoutStatus.Loading -> {
                        Log.d(Common.KeroDebug, "observeHome: Loading")
                        showProgressDialog(binding.progressLoading)
                    }

                    is CheckoutStatus.Checkout -> {
                        hideProgressDialog(binding.progressLoading)
                        if (it.data.status == 200) {

                            Log.d(Common.KeroDebug, "observeHome: GetProducts")

                            val orderNumbers = it.data.data?.orderNumbers?.toString()
                            val msg = it.data.data?.msg?.toString()
                            Log.d("TAG", "cartObserve orderNumbers :${orderNumbers} ")

                            val intent = Intent(this@CheckoutActivity, OrderNumberActivity::class.java).apply {
                                putExtra("order_numbers", orderNumbers)
                                putExtra("msg", msg)

                            }
                        startActivity(intent)

                        finish()

                    } else {

                    showToastSnack(it.data.message, true)
                    Log.d("TAG", "cartObserve: ${it.data.message}")
                }
                    }


                    is CheckoutStatus.Error -> {
                        Log.d(Common.KeroDebug, "observeHome Error: ${it.error.toString()}")
                        hideProgressDialog(binding.progressLoading)
                        showToastSnack(it.error.toString(), true)
                    }

                }
            }
        }
    }

    private fun checkOut(paymentId : Int) {
        lifecycleScope.launch {
            checkoutViewModel.checkoutIntent.send(
                CheckoutIntent.Checkout(paymentId, "Cosmetics")
            )
        }
    }

    private fun setupPaymentRecycler() {
        paymentAdapter = PaymentAdapter { selectedPayment, position ->
            // Store selected payment ID
            selectedPaymentId = selectedPayment.paymentId
            // Enable the confirm order button
            binding.btnConfirmOrder.isEnabled = true
           // Toast.makeText(this, "Selected: ${selectedPayment.paymentName}", Toast.LENGTH_SHORT).show()
        }

        binding.paymentTypesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@CheckoutActivity, LinearLayoutManager.VERTICAL, false)
            adapter = paymentAdapter
        }
    }
    private fun observePaymentTypes() {
        lifecycleScope.launch {
            paymentTypeViewModel.state.collect { state ->
                when (state) {
                    is PaymentTypeStatus.Idle -> {}
                    is PaymentTypeStatus.Loading -> {
                        showProgressDialog(binding.progressLoading)
                    }
                    is PaymentTypeStatus.Checkout -> {
                        hideProgressDialog(binding.progressLoading)
                        val list = state.data.data ?: emptyList()
                        paymentAdapter.setData(list)
                        Log.d("TAG", "observePaymentTypes: ${list}")
                        Log.d("TAG", "observePaymentTypes: ${list.size}")

                    }
                    is PaymentTypeStatus.Error -> {
                        hideProgressDialog(binding.progressLoading)
                        showToastSnack("Error: ${state.error}", true)
                    }
                }
            }
        }
    }

    private fun getPaymentTypes() {
        lifecycleScope.launch {
            paymentTypeViewModel.paymentIntent.send(PaymentIndent.Checkout)
        }
    }

}