package com.akhnaton.atrapp.ui.nav.home.product

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.data.model.CategoriesModel
import com.akhnaton.atrapp.data.model.OrderTypeModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton

class FilterProductsBottomSheet :
    BottomSheetDialogFragment() {

    private lateinit var orderTypes: ArrayList<OrderTypeModel>

    private val selectedCategories = mutableListOf<CategoriesModel>()

    private lateinit var rvCategories: RecyclerView
    private lateinit var rvProducts: RecyclerView

    private lateinit var categoryAdapter: FilterCategoryAdapter
    private lateinit var productAdapter: FilterProductAdapter

    companion object {
        private const val ARG_ORDER_TYPES = "arg_order_types"
        fun newInstance(
            orderTypes: ArrayList<OrderTypeModel>
        ): FilterProductsBottomSheet {

            return FilterProductsBottomSheet().apply {

                arguments = Bundle().apply {

                    putParcelableArrayList(
                        ARG_ORDER_TYPES,
                        orderTypes
                    )
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        orderTypes =
            requireArguments().getParcelableArrayList<OrderTypeModel>(
                ARG_ORDER_TYPES
            ) ?: throw IllegalArgumentException(
                "orderTypes is required"
            )
    }

    override fun onCreateDialog(
        savedInstanceState: Bundle?
    ): Dialog {

        val dialog = super.onCreateDialog(savedInstanceState)

        dialog.setOnShowListener {

            val bottomSheet =
                dialog.findViewById<View>(
                    com.google.android.material.R.id.design_bottom_sheet
                )

            bottomSheet?.let { sheet ->

                sheet.background =
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.bg_filter_bottom_sheet
                    )
            }
        }

        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(
            R.layout.bottom_sheet_filter_products,
            container,
            false
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {

        super.onViewCreated(view, savedInstanceState)
        rvCategories =
            view.findViewById(R.id.rvCategories)
        rvProducts =
            view.findViewById(R.id.rvProducts)
        val btnClose =
            view.findViewById<ImageButton>(R.id.btnClose)
        val btnReset =
            view.findViewById<MaterialButton>(R.id.btnReset)
        val btnApply =
            view.findViewById<MaterialButton>(R.id.btnApply)
        setupCategories()
        setupProducts()

        if (orderTypes.isNotEmpty()) {
            categoryAdapter.setSelectedPosition(0)
            onOrderTypeSelected(0)
        }

        btnClose.setOnClickListener {
            dismiss()
        }
        btnReset.setOnClickListener {
            selectedCategories.forEach {
                it.selected = false
            }
            productAdapter.notifyDataSetChanged()
        }
        btnApply.setOnClickListener {
            val selectedProducts =
                selectedCategories.filter {
                    it.selected
                }
            // Return selected filters to your Activity/ViewModel
            onFiltersApplied(selectedProducts)
            dismiss()
        }
    }


    private fun setupCategories() {

        categoryAdapter =
            FilterCategoryAdapter(
                orderTypes
            ) { position ->

                categoryAdapter.setSelectedPosition(position)

                onOrderTypeSelected(position)
            }

        rvCategories.apply {

            layoutManager =
                LinearLayoutManager(requireContext())

            adapter = categoryAdapter

            itemAnimator = null
        }
    }

    private fun onOrderTypeSelected(position: Int) {

        val selectedOrderType =
            orderTypes[position]

        selectedCategories.clear()

        selectedCategories.addAll(
            selectedOrderType.categories
        )

        productAdapter.notifyDataSetChanged()
    }

    private fun setupProducts() {

        productAdapter =
            FilterProductAdapter(
                selectedCategories
            ) { position, selected ->

                selectedCategories[position].selected = selected
            }

        rvProducts.apply {

            layoutManager =
                LinearLayoutManager(requireContext())

            adapter = productAdapter

            itemAnimator = null
        }
    }


    private fun loadProductsForCategory(
        position: Int
    ) {

        // Example:
        //
        // when (position) {
        //     0 -> loadSuppliers()
        //     1 -> loadTherapeuticClass1()
        //     4 -> loadGenericNames()
        // }

        productAdapter.notifyDataSetChanged()
    }


    private fun onFiltersApplied(
        selectedProducts: List<CategoriesModel>
    ) {

        val selectedNames =
            selectedProducts.map {
                it.CATEGORY_NAME
            }

        Log.d(
            "FILTER",
            "Selected: $selectedNames"
        )
    }


    override fun onStart() {

        super.onStart()

        val bottomSheet =
            dialog?.findViewById<View>(
                com.google.android.material.R.id.design_bottom_sheet
            )

        bottomSheet?.let { sheet ->

            val behavior = BottomSheetBehavior.from(sheet)

            val screenHeight = resources.displayMetrics.heightPixels
            val bottomSheetHeight = (screenHeight * 0.75).toInt()

            sheet.layoutParams.height = bottomSheetHeight
            sheet.requestLayout()

            behavior.peekHeight = bottomSheetHeight
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.skipCollapsed = true
            behavior.isDraggable = true
        }
    }
}