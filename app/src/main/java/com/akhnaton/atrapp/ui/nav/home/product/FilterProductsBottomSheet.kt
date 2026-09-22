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

class FilterProductsBottomSheet : BottomSheetDialogFragment() {

    private lateinit var orderTypes: ArrayList<OrderTypeModel>

    private val selectedCategories = mutableListOf<CategoriesModel>()

    private var selectedOrderTypeParam: String? = null //malak
    private var selectedChildIdParam: Int? = null
    private val selectedChildIds = mutableMapOf<String, Int>() //// newwww malak

    var onFilterAppliedListeners: ((orderType: String?, childId: Int?) -> Unit)? = null //malak

    private lateinit var rvCategories: RecyclerView
    private lateinit var rvProducts: RecyclerView

    private lateinit var categoryAdapter: FilterCategoryAdapter
    private lateinit var productAdapter: FilterProductAdapter

    companion object {

        private const val ARG_ORDER_TYPES = "arg_order_types"
        private const val ARG_SELECTED_ORDER_TYPE = "arg_selected_order_type"
        private const val ARG_SELECTED_CHILD_ID = "arg_selected_child_id"

        fun newInstance(
            orderTypes: ArrayList<OrderTypeModel>,
            selectedOrderType: String?,
            selectedChildId: Int?
        ): FilterProductsBottomSheet {

            return FilterProductsBottomSheet().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ARG_ORDER_TYPES, orderTypes)
                    putString(ARG_SELECTED_ORDER_TYPE, selectedOrderType)
                    if (selectedChildId != null) {
                        putInt(ARG_SELECTED_CHILD_ID, selectedChildId)
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args = requireArguments()

        orderTypes = args.getParcelableArrayList<OrderTypeModel>(ARG_ORDER_TYPES)
            ?: throw IllegalArgumentException("orderTypes is required")

        selectedOrderTypeParam = args.getString(ARG_SELECTED_ORDER_TYPE)

        if (args.containsKey(ARG_SELECTED_CHILD_ID)) {
            selectedChildIdParam = args.getInt(ARG_SELECTED_CHILD_ID)
            selectedOrderTypeParam?.let { key ->
                selectedChildIdParam?.let { id ->
                    selectedChildIds[key] = id
                }
            }
        }

        Log.d(
            "filter_restore",
            "BottomSheet opened -> orderType=$selectedOrderTypeParam, childId=$selectedChildIdParam"
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener {
            val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let { sheet ->
                sheet.background = ContextCompat.getDrawable(
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
        return inflater.inflate(R.layout.bottom_sheet_filter_products, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvCategories = view.findViewById(R.id.rvCategories)
        rvProducts = view.findViewById(R.id.rvProducts)
        val btnClose = view.findViewById<ImageButton>(R.id.btnClose)
        val btnReset = view.findViewById<MaterialButton>(R.id.btnReset)
        val btnApply = view.findViewById<MaterialButton>(R.id.btnApply)

        setupCategories()
        setupProducts()

        restorePreviousSelection()

        btnClose.setOnClickListener {
            dismiss()
        }

        btnReset.setOnClickListener {
            selectedChildIds.clear() ////newww malakkk
            selectedChildIdParam = null

            selectedCategories.forEach { it.selected = false }

            if (::productAdapter.isInitialized) {
                productAdapter.setSelectedPosition(-1)
                productAdapter.notifyDataSetChanged()
            }

            Log.d("filter_reset", "Filters reset")
        }

        btnApply.setOnClickListener {
            val currentChildId = selectedOrderTypeParam?.let { selectedChildIds[it] }

            Log.d("filter_test", "Apply clicked -> orderType: $selectedOrderTypeParam, childId: $currentChildId")
            onFilterAppliedListeners?.invoke(selectedOrderTypeParam, currentChildId) //malak
            dismiss()
        }
    }

    private fun restorePreviousSelection() {
        if (orderTypes.isEmpty()) return

        val selectedOrderTypePosition = orderTypes.indexOfFirst {
            it.order_type_index == selectedOrderTypeParam
        }

        if (selectedOrderTypePosition >= 0) {
            categoryAdapter.setSelectedPosition(selectedOrderTypePosition)
            onOrderTypeSelected(selectedOrderTypePosition)
        } else {
            categoryAdapter.setSelectedPosition(0)
            onOrderTypeSelected(0)
        }
    }

    private fun setupCategories() {
        categoryAdapter = FilterCategoryAdapter(orderTypes) { position ->
            categoryAdapter.setSelectedPosition(position)
            onOrderTypeSelected(position)
        }

        rvCategories.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = categoryAdapter
            itemAnimator = null
        }
    }

    private fun onOrderTypeSelected(position: Int) {
        val selectedOrderType = orderTypes[position]
        val currentOrderTypeKey = selectedOrderType.order_type_index ?: ""

        selectedOrderTypeParam = currentOrderTypeKey

        selectedCategories.clear()
        selectedCategories.addAll(selectedOrderType.categories)

        selectedCategories.forEach { it.selected = false }

        val savedChildId = selectedChildIds[currentOrderTypeKey]
        var selectedPosition = -1

        if (savedChildId != null) {
            selectedPosition = selectedCategories.indexOfFirst {
                it.CHILD_ID?.toIntOrNull() == savedChildId
            }

            if (selectedPosition >= 0) {
                selectedCategories[selectedPosition].selected = true
            }
        }

        productAdapter.setSelectedPosition(selectedPosition)
        productAdapter.notifyDataSetChanged()
    }

    private fun setupProducts() {
        productAdapter = FilterProductAdapter(selectedCategories) { position, selected ->
            if (position < 0 || position >= selectedCategories.size) return@FilterProductAdapter

            val currentOrderTypeKey = selectedOrderTypeParam ?: return@FilterProductAdapter

            if (selected) {
                selectedChildIds.clear()

                val childIdRaw = selectedCategories[position].CHILD_ID
                val childId = childIdRaw?.toString()?.toIntOrNull()

                if (childId != null) {
                    selectedChildIds[currentOrderTypeKey] = childId
                    selectedChildIdParam = childId
                }

                selectedCategories.forEachIndexed { index, item ->
                    item.selected = (index == position)
                }
                productAdapter.setSelectedPosition(position)

            } else {
                selectedChildIds.remove(currentOrderTypeKey)
                selectedChildIdParam = null
                selectedCategories[position].selected = false
                productAdapter.setSelectedPosition(-1)
            }

            productAdapter.notifyDataSetChanged()
        }

        rvProducts.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = productAdapter
            itemAnimator = null
        }
    }

    override fun onStart() {
        super.onStart()
        val bottomSheet = dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
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