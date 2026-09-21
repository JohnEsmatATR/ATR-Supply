package com.akhnaton.atrapp.ui.nav.profile.order.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.databinding.BottomSheetOrderBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.Locale

class OrderBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetOrderBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemsCount = arguments?.getInt("ITEMS_COUNT") ?: 0
        val totalPieces = arguments?.getInt("TOTAL_PIECES") ?: 0

        val subtotal = arguments?.getDouble("SUBTOTAL") ?: 0.0
        val tax = arguments?.getDouble("TAX") ?: 0.0
        val grandTotal = arguments?.getDouble("GRAND_TOTAL") ?: 0.0

        val currency = getString(R.string.currency)

        binding.txtItemsCount.text = getString(R.string.items_unit_format, itemsCount)
        binding.txtTotalPieces.text = getString(R.string.pieces_unit_format, totalPieces)

        val currentLocale = Locale.getDefault()
        binding.txtSubtotal.text = String.format(currentLocale, "%.2f %s", subtotal, currency)
        binding.txtTax.text = String.format(currentLocale, "%.2f %s", tax, currency)
        binding.txtGrandTotal.text = String.format(currentLocale, "%.2f %s", grandTotal, currency)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


















