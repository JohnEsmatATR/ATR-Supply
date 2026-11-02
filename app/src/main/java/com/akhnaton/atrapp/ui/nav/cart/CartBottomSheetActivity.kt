
package com.akhnaton.atrapp.ui.nav.cart

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.data.model._ExtraData
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

        val cartTotalTax = arguments?.getInt(ARG_TOTAL_TAX, 0) ?: 0
        val cartTotalWithoutTax = arguments?.getDouble(ARG_TOTAL_WITHOUT_TAX, 0.0) ?: 0.0
        val cartTotalDiscount = arguments?.getInt(ARG_TOTAL_DISCOUNT, 0) ?: 0
        val cartTotalShippingFees = arguments?.getInt(ARG_TOTAL_SHIPPING_FEES, 0) ?: 0
        val cartTotalWithTax = arguments?.getDouble(ARG_TOTAL_WITH_TAX, 0.0) ?: 0.0

        val currency = getString(R.string.currency)
        val currentLocale = Locale.getDefault()

        binding.totalTax.text = String.format(currentLocale, "%,.3f %s", cartTotalTax.toDouble(), currency)

        binding.totalWithoutTax.text = String.format(currentLocale, "%,.3f %s", cartTotalWithoutTax.toDouble(), currency)
        binding.totalDiscount.text = String.format(currentLocale, "%,.3f %s", cartTotalDiscount.toDouble(), currency)

        binding.totalShippingFees.text = String.format(currentLocale, "%,.3f %s", cartTotalShippingFees.toDouble(), currency)

        binding.totalWithTax.text = String.format(currentLocale, "%,.3f %s", cartTotalWithTax, currency)

        binding.cardCheckout.setOnClickListener {
            dismiss()
            startActivity(
                Intent(requireContext(), CheckoutActivity::class.java)
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_TOTAL_TAX = "TAX"
        private const val ARG_TOTAL_WITHOUT_TAX = "WITHOUT_TAX"
        private const val ARG_TOTAL_DISCOUNT = "DISCOUNT"
        private const val ARG_TOTAL_SHIPPING_FEES = "SHIPPING_FEES"
        private const val ARG_TOTAL_WITH_TAX = "WITH_TAX"

        fun newInstance(extraData: _ExtraData): CartBottomSheetActivity {
            return CartBottomSheetActivity().apply {
                arguments = Bundle().apply {
                    putInt(ARG_TOTAL_TAX, extraData.cartTotalTax)
                    putDouble(ARG_TOTAL_WITHOUT_TAX, extraData.cartTotalWithoutTax)
                    putInt(ARG_TOTAL_DISCOUNT, extraData.cartTotalDiscount)
                    putInt(ARG_TOTAL_SHIPPING_FEES, extraData.cartTotalShippingFess)
                    putDouble(ARG_TOTAL_WITH_TAX, extraData.cartTotalWithTax)
                }
            }
        }
    }
}
