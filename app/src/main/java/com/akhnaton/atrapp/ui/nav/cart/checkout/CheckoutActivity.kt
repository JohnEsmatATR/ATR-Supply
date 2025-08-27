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
import com.akhnaton.atrapp.ui.nav.cart.checkout.paymentMethod.FawryActivity
import com.akhnaton.atrapp.ui.nav.cart.checkout.paymentMethod.PayenTypeViewModel
import com.akhnaton.atrapp.ui.nav.cart.checkout.paymentMethod.PaymentAdapter
import kotlinx.coroutines.launch


class CheckoutActivity : BaseActivity() {
    lateinit var binding: ActivityChackoutBinding
    lateinit var mDialog: CustomDialog
    val checkoutViewModel: CheckoutViewModel by viewModels()
    private val paymentTypeViewModel: PayenTypeViewModel by viewModels()
    private lateinit var paymentAdapter: PaymentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        cartObserve()
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

                            val orderNumbers = it.data.data?.orderNumbers

                            val intent = Intent(this@CheckoutActivity, FawryActivity::class.java).apply {
                                putExtra("order_numbers", orderNumbers)
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
                CheckoutIntent.Checkout(paymentId)
            )
        }
    }

    private fun setupPaymentRecycler() {
        paymentAdapter = PaymentAdapter { selectedPayment, position ->
            // Example: Store selected type in ViewModel or local variable
            val paymentId= selectedPayment.paymentId
            checkOut(paymentId)
           // Toast.makeText(this, "Selected: ${selectedPayment.paymentName}", Toast.LENGTH_SHORT).show()
        }

        binding.paymentTypesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@CheckoutActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = paymentAdapter
        }
    }
    private fun observePaymentTypes() {
        lifecycleScope.launch {
            paymentTypeViewModel.state.collect { state ->
                when (state) {
                    is PaymentTypeStatus.Idle -> {}
                    is PaymentTypeStatus.Loading -> {

                    }
                    is PaymentTypeStatus.Checkout -> {
                        val list = state.data.data ?: emptyList()
                        paymentAdapter.setData(list)

                    }
                    is PaymentTypeStatus.Error -> {
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