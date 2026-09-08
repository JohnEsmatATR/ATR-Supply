//package com.akhnaton.atrapp.ui.nav.home.product
//
//import android.app.Dialog
//import android.os.Bundle
//import android.view.View
//import androidx.core.content.ContextCompat
//import com.akhnaton.atrapp.R
//import com.google.android.material.bottomsheet.BottomSheetDialogFragment
//
//class SortProductsBottomSheet : BottomSheetDialogFragment {
//    companion object {
//        fun newInstance(): SortProductsBottomSheet {
//            return SortProductsBottomSheet()
//        }
//    }
//
//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        val dialog = super.onCreateDialog(savedInstanceState)
//        dialog.setOnShowListener {
//            val bottomSheet = dialog.findViewById<View>(
//                com.google.android.material.R.id.design_bottom_sheet
//            )
//            bottomSheet?.let {
//                sheet -> sheet.background = ContextCompat.getDrawable(
//                    requireContext(),
//                    R.drawable.bg_filter_bottom_sheet
//                )
//            }
//        }
//        return dialog
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//
//    }
//}