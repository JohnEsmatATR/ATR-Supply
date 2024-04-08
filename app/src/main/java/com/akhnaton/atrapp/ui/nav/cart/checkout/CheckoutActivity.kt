package com.akhnaton.atrapp.ui.nav.cart.checkout

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.databinding.ActivityChackoutBinding
import com.akhnaton.atrapp.shared.BaseActivity
import com.akhnaton.atrapp.shared.CustomDialog
import com.akhnaton.atrapp.ui.nav.cart.addresses.AddressesActivity
import com.akhnaton.atrapp.ui.nav.cart.checkout.paymentMethod.AddCardActivity
import com.akhnaton.atrapp.ui.nav.cart.checkout.paymentMethod.FawryActivity


class CheckoutActivity : BaseActivity(), OnClickListener {
    lateinit var binding: ActivityChackoutBinding
    lateinit var mDialog: CustomDialog
    private var paymentCheck = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
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
            if (paymentCheck == 0) {
                mDialog.showDialog()
            } else if (paymentCheck == 1) {
                val intent = Intent(this@CheckoutActivity, FawryActivity::class.java)
                startActivity(intent)
            } else {
                showToastSnack("please select payment method first", true)
            }
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


}