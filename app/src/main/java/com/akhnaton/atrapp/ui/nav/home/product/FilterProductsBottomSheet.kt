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

    private var selectedOrderTypeKey: String? = null
    private var selectedChildId: Int? = null
    private var selectedPositionInTap: Int = -1
    private var viewedOrderTypeKey: String? = null
    var onSelectionChanged: ((orderType: String?, childId: Int?) -> Unit)? = null


    private lateinit var orderTypes: ArrayList<OrderTypeModel>
//    private var selectedCategoryIdParam: Int? = null //new malak
//    private val selectedCategoryData = mutableMapOf<String, Pair<Int?, Int?>>() //new malak
    private val selectedCategories = mutableListOf<CategoriesModel>()
//    private var selectedOrderTypeParam: String? = null //malak
//    private var selectedChildIdParam: Int? = null
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

//        selectedOrderTypeParam = args.getString(ARG_SELECTED_ORDER_TYPE)
        if (savedInstanceState != null) {
            selectedOrderTypeKey = savedInstanceState.getString("sel_order_type")
            selectedChildId = if (savedInstanceState.containsKey("sel_child_id"))
                savedInstanceState.getInt("sel_child_id") else null

            selectedPositionInTap = savedInstanceState.getInt("sel_position", -1)
            viewedOrderTypeKey = savedInstanceState.getString("viewed_order_type")
        } else {
            selectedOrderTypeKey = args.getString(ARG_SELECTED_ORDER_TYPE)
            selectedChildId = if (args.containsKey(ARG_SELECTED_CHILD_ID))
                args.getInt(ARG_SELECTED_CHILD_ID) else null

            viewedOrderTypeKey = selectedOrderTypeKey

            val tab = orderTypes.find { it.order_type_index == selectedOrderTypeKey }
            val myIndex = tab?.categories?.indexOfFirst { it.selected && it.ID == selectedChildId } ?: -1

            selectedPositionInTap = if (myIndex >= 0) {
                myIndex
            } else {
                tab?.categories?.indexOfFirst { it.ID == selectedChildId } ?: -1
            }
        }

//        if (args.containsKey(ARG_SELECTED_CHILD_ID)) {
//            selectedChildIdParam = args.getInt(ARG_SELECTED_CHILD_ID)
//
//            // new malak
//            val currentOrderTypeObj = orderTypes.find {
//                it.order_type_index == selectedOrderTypeParam
//            }
//
//            val initialCategory = currentOrderTypeObj?.categories?.find { it.ID == selectedChildIdParam && selectedChildIdParam != 0 }
//
//            selectedCategoryIdParam = initialCategory?.ID //new malak
//
//            selectedOrderTypeParam?.let { key ->
//                selectedChildIdParam?.let { childId ->
//                    selectedCategoryData[key] = Pair(selectedCategoryIdParam, childId) //new malak
//                }
//            }
//        }
    }


    //malak task filter selection
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("sel_order_type", selectedOrderTypeKey)
        selectedChildId?.let { outState.putInt("sel_child_id", it) }
        outState.putInt("sel_position", selectedPositionInTap)
        outState.putString("viewed_order_type", viewedOrderTypeKey)
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
//            selectedCategoryData.clear() //new malak
//            selectedChildIdParam = null
//            selectedCategoryIdParam = null //new malak
//            selectedOrderTypeParam = null //new malak

            //task malak filter selection
            selectedOrderTypeKey = null
            selectedChildId = null
            selectedPositionInTap = -1

//            selectedCategories.forEach { it.selected = false }
            orderTypes.forEach { type -> type.categories.forEach { it.selected = false } }
            productAdapter.setSelectedPosition(-1)
            productAdapter.notifyDataSetChanged()

            onSelectionChanged?.invoke(null, null)


//            if (::productAdapter.isInitialized) {
//                productAdapter.setSelectedPosition(-1)
//                productAdapter.notifyDataSetChanged()
//            }

            Log.d("filter_reset", "Filters reset")
        }

        btnApply.setOnClickListener {
//            val selectedPair = selectedOrderTypeParam?.let { selectedCategoryData[it] } //new malak
//            val currentChildId = selectedPair?.second //new malak
//            val currentCategoryId = selectedPair?.first //new malak
//
//            val finalOrderType = if (currentChildId != null) selectedOrderTypeParam else null //new malak
//
//            Log.d("filter_test", "Apply clicked -> orderTypeIndex: $selectedOrderTypeParam, categoryId: $currentCategoryId, childId: $currentChildId")
//
//            onFilterAppliedListeners?.invoke(finalOrderType, currentChildId) //malak
//            dismiss()
//        }

            val childId = selectedChildId
            val orderType = if (childId != null) selectedOrderTypeKey else null
            onFilterAppliedListeners?.invoke(orderType, childId)
            dismiss()
        }
    }

    private fun restorePreviousSelection() {
        if (orderTypes.isEmpty()) return

//        val selectedOrderTypePosition = orderTypes.indexOfFirst {
//            it.order_type_index == selectedOrderTypeParam
//        }
//
//        if (selectedOrderTypePosition >= 0) {
//            categoryAdapter.setSelectedPosition(selectedOrderTypePosition)
//            onOrderTypeSelected(selectedOrderTypePosition)
//        } else {
//            categoryAdapter.setSelectedPosition(0)
//            onOrderTypeSelected(0)
//        }
        val myPosition = orderTypes.indexOfFirst { it.order_type_index == viewedOrderTypeKey }
            .takeIf { it >= 0 } ?: 0

        categoryAdapter.setSelectedPosition(myPosition)
        onOrderTypeSelected(myPosition)
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
//        val selectedOrderType = orderTypes[position]
//        val currentOrderTypeKey = selectedOrderType.order_type_index ?: ""

//        selectedOrderTypeParam = currentOrderTypeKey
//
//        selectedCategories.clear()
//        selectedCategories.addAll(selectedOrderType.categories)
//
//        selectedCategories.forEach { it.selected = false }
//
//        val dataPair = selectedCategoryData[currentOrderTypeKey] //new malak
//        var selectedPosition = -1
//
//        if (dataPair != null) {
//            val (savedCategoryId, savedChildId) = dataPair //new malak
//
//            // new malak
//            selectedPosition = selectedCategories.indexOfFirst { select ->
//
//                //malak task filter selection
//                val catId = select.ID
//                val targetId = savedChildId ?: savedCategoryId
//
//                targetId != null && targetId != 0 && catId == targetId
//            }
//
//            if (selectedPosition >= 0) {
//                selectedCategories[selectedPosition].selected = true
//            }
//        }
//
//        productAdapter.setSelectedPosition(selectedPosition)
//        productAdapter.notifyDataSetChanged()

        val orderType = orderTypes[position]
        viewedOrderTypeKey = orderType.order_type_index
        selectedCategories.clear()
        selectedCategories.addAll(orderType.categories)

        val isSelectionInThisTab = viewedOrderTypeKey == selectedOrderTypeKey
        val mySelectedPosition = if (
            isSelectionInThisTab && selectedChildId != null && selectedPositionInTap in selectedCategories.indices
            && selectedCategories[selectedPositionInTap].ID == selectedChildId
        ) selectedPositionInTap else -1

        selectedCategories.forEachIndexed { index, item ->
            item.selected = (index == mySelectedPosition)

            productAdapter.setSelectedPosition(mySelectedPosition)
            productAdapter.notifyDataSetChanged()
        }
    }

    private fun setupProducts() {
        productAdapter = FilterProductAdapter(selectedCategories) { position, selected ->
            if (position < 0 || position >= selectedCategories.size) return@FilterProductAdapter

            //malak task filter selection
            val currentTypeKey = viewedOrderTypeKey ?: return@FilterProductAdapter

//            val currentOrderTypeKey = selectedOrderTypeParam ?: return@FilterProductAdapter

            if (selected) {
//                selectedCategoryData.clear() //new malak
                selectedOrderTypeKey = currentTypeKey
                selectedChildId = selectedCategories[position].ID
                selectedPositionInTap = position

                selectedCategories.forEachIndexed { index, item -> item.selected = (index == position) }
                productAdapter.setSelectedPosition(position)


                //malak task filter selection
//                val selectedCategory = selectedCategories[position]
//                val childId = selectedCategory.ID
//                val categoryId = selectedCategory.ID
//
//                selectedCategoryData[currentOrderTypeKey] = Pair(categoryId, childId)
//                selectedChildIdParam = childId
//                selectedCategoryIdParam = categoryId
//
//                selectedCategories.forEachIndexed { index, item ->
//                    item.selected = (index == position)
//                }
//                productAdapter.setSelectedPosition(position)

            } else {
//                selectedCategoryData.remove(currentOrderTypeKey) //new malak
//                selectedChildIdParam = null
//                selectedCategoryIdParam = null //new malak
//                selectedCategories[position].selected = false
//                productAdapter.setSelectedPosition(-1)
                selectedOrderTypeKey = null
                selectedChildId = null
                selectedPositionInTap = -1
                selectedCategories[position].selected = false
                productAdapter.setSelectedPosition(-1)
            }

            onSelectionChanged?.invoke(selectedOrderTypeKey, selectedChildId)
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
            val bottomSheetHeight = (screenHeight * 0.76).toInt()

            sheet.layoutParams.height = bottomSheetHeight
            sheet.requestLayout()

            behavior.peekHeight = bottomSheetHeight
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.skipCollapsed = true
            behavior.isDraggable = true
        }
    }
}