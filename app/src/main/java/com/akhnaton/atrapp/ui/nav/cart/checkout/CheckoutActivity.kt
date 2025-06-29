package com.akhnaton.atrapp.ui.nav.cart.checkout

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.data.statuesValue.nav.cart.addToCart.AddToCartStatus
import com.akhnaton.atrapp.data.statuesValue.nav.cart.checkout.CheckoutIntent
import com.akhnaton.atrapp.data.statuesValue.nav.cart.checkout.CheckoutStatus
import com.akhnaton.atrapp.data.statuesValue.nav.cart.getMyCart.CartIntent
import com.akhnaton.atrapp.data.statuesValue.nav.cart.getMyCart.CartStatus
import com.akhnaton.atrapp.databinding.ActivityChackoutBinding
import com.akhnaton.atrapp.shared.BaseActivity
import com.akhnaton.atrapp.shared.Common
import com.akhnaton.atrapp.shared.CustomDialog
import com.akhnaton.atrapp.ui.nav.cart.addresses.AddressesActivity
import com.akhnaton.atrapp.ui.nav.cart.checkout.paymentMethod.AddCardActivity
import com.akhnaton.atrapp.ui.nav.cart.checkout.paymentMethod.FawryActivity
import kotlinx.coroutines.launch


class CheckoutActivity : BaseActivity(), OnClickListener {
    lateinit var binding: ActivityChackoutBinding
    lateinit var mDialog: CustomDialog
    val checkoutViewModel: CheckoutViewModel by viewModels()
    private var paymentCheck = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        cartObserve()
    }

    private fun setupBinding() {
        binding = DataBindingUtil.setContentView(this@CheckoutActivity, R.layout.activity_chackout)
        mDialog = CustomDialog(this)

        binding.btnCheckout.setOnClickListener(this)
        binding.imAddressChange.setOnClickListener(this)
        binding.imBack.setOnClickListener(this)
        binding.layoutCash.setOnClickListener(this)
        binding.layoutFawry.setOnClickListener(this)
        binding.layoutVisa.setOnClickListener(this)
        selectPaymentMethod(binding.layoutCash, binding.imCashChecked)
    }


    override fun onClick(v: View) {
        if (v.id == binding.layoutCash.id) {
            paymentCheck = 0
            selectPaymentMethod(binding.layoutCash, binding.imCashChecked)
        }

        if (v.id == binding.layoutFawry.id) {
            paymentCheck = 1
            selectPaymentMethod(binding.layoutFawry, binding.imFawryChecked)
        }

        if (v.id == binding.layoutVisa.id) {
            val intent = Intent(this@CheckoutActivity, AddCardActivity::class.java)
            startActivity(intent)
        }

        if (v.id == binding.imAddressChange.id) {
            val intent = Intent(this@CheckoutActivity, AddressesActivity::class.java)
            startActivity(intent)
        }

        if (v.id == binding.btnCheckout.id) {
            getMyCart()
        }


        if (v.id == binding.imBack.id) {
            finish()
        }
    }

    private fun selectPaymentMethod(layout: ConstraintLayout, checkedIcon: ImageView) {
        resetPaymentMethodsAppearance()
        layout.setBackgroundResource(R.drawable.style_background_payment_method_selected)
        checkedIcon.visibility = View.VISIBLE
    }

    private fun resetPaymentMethodsAppearance() {
        binding.layoutCash.setBackgroundResource(R.drawable.style_background_payment_method)
        binding.imCashChecked.visibility = View.GONE

        binding.layoutFawry.setBackgroundResource(R.drawable.style_background_payment_method)
        binding.imFawryChecked.visibility = View.GONE

        binding.layoutVisa.setBackgroundResource(R.drawable.style_background_payment_method)
        binding.imVisaChecked.visibility = View.GONE
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
                        if (it.data.status == 200) {
                            hideProgressDialog(binding.progressLoading)
                            Log.d(Common.KeroDebug, "observeHome: GetProducts")

                            val intent = Intent(this@CheckoutActivity, FawryActivity::class.java)
                            startActivity(intent)
                        } else {
                            hideProgressDialog(binding.progressLoading)
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

    private fun getMyCart() {
        lifecycleScope.launch {
            checkoutViewModel.checkoutIntent.send(
                CheckoutIntent.Checkout
            )
        }
    }
}