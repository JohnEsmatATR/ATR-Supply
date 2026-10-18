package com.akhnaton.atrapp.ui.nav.cart

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.databinding.ActivityCartBottomSheetBinding
import com.akhnaton.atrapp.ui.nav.cart.checkout.CheckoutActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.Locale

class CartBottomSheetActivity : BottomSheetDialogFragment() {

    private var _binding: ActivityCartBottomSheetBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ActivityCartBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val itemTotal = arguments?.getDouble(ARG_ITEM_TOTAL, 0.0) ?: 0.0
        val totalDiscount = arguments?.getDouble(ARG_DISCOUNT, 0.0) ?: 0.0
        //malak task
        val shippingFees = arguments?.getDouble(ARG_SHIPPING_FEES, 0.0) ?: 0.0
        val grandTotal = arguments?.getDouble(ARG_GRAND_TOTAL, 0.0) ?: 0.0

        val currency = getString(R.string.currency)
        val currentLocale = Locale.getDefault()

//        binding.txtGrandTotal.text = "%,d".format(grandTotal, currency)

        binding.txtItemTotal.text = String.format(currentLocale, "%,.3f %s", itemTotal, currency)
        binding.txtDiscount.text = String.format(currentLocale, "%,.3f %s", totalDiscount, currency)
        binding.txtDeliveryFee.text = String.format(currentLocale, "%,.3f %s", shippingFees, currency)
        binding.txtGrandTotal.text = String.format(currentLocale, "%,.3f %s", grandTotal, currency)
        binding.cardCheckout.setOnClickListener {
            dismiss()
            val intent = Intent(requireContext(), CheckoutActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ARG_ITEM_TOTAL = "ITEM_TOTAL"
        const val ARG_DISCOUNT = "DISCOUNT"
        const val ARG_SHIPPING_FEES = "SHIPPING_FEES"
        const val ARG_GRAND_TOTAL = "GRAND_TOTAL"

        fun newInstance(itemTotal: Double, discount: Double, grandTotal: Double, shippingFees: Double,): CartBottomSheetActivity {
            val fragment = CartBottomSheetActivity()
            val args = Bundle().apply {
                putDouble(ARG_ITEM_TOTAL, itemTotal)
                putDouble(ARG_DISCOUNT, discount)
                putDouble(ARG_SHIPPING_FEES, shippingFees)
                putDouble(ARG_GRAND_TOTAL, grandTotal)
            }
            fragment.arguments = args
            return fragment
        }
    }
}