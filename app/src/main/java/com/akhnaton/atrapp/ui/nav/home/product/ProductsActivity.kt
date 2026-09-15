package com.akhnaton.atrapp.ui.nav.home.product

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.data.model.CategoriesModel
import com.akhnaton.atrapp.data.model.OrderTypeModel
import com.akhnaton.atrapp.data.model.ProductModel
import com.akhnaton.atrapp.data.statuesValue.nav.cart.addToCart.AddToCartIntent
import com.akhnaton.atrapp.data.statuesValue.nav.cart.addToCart.AddToCartStatus
import com.akhnaton.atrapp.data.statuesValue.nav.home.favorite.FavoriteIntent
import com.akhnaton.atrapp.data.statuesValue.nav.home.products.ProductsIntent
import com.akhnaton.atrapp.data.statuesValue.nav.home.products.ProductsStatus
import com.akhnaton.atrapp.data.statuesValue.nav.home.search.SearchIntent
import com.akhnaton.atrapp.data.statuesValue.nav.home.search.SearchStatus
import com.akhnaton.atrapp.databinding.ActivityProductsBinding
import com.akhnaton.atrapp.shared.BaseActivity
import com.akhnaton.atrapp.shared.Common
import com.akhnaton.atrapp.shared.GridSpacingItemDecoration
import com.akhnaton.atrapp.shared.SharedPreferenceHelper
import com.akhnaton.atrapp.ui.nav.favorite.FavoriteViewModel
import com.akhnaton.atrapp.ui.nav.home.ProductAdapter
import com.akhnaton.atrapp.ui.nav.home.product.productDetails.ProductDetailsActivity
import com.akhnaton.atrapp.ui.nav.home.search.SearchViewModel
import com.akhnaton.atrapp.ui.nav.cart.AddToCartViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.collections.emptyList
import androidx.fragment.app.FragmentManager
import com.akhnaton.atrapp.data.statuesValue.nav.home.category.CategoryIntent
import com.akhnaton.atrapp.data.statuesValue.nav.home.category.CategoryStatus
import com.akhnaton.atrapp.ui.nav.home.CategoryViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class ProductsActivity : BaseActivity() {
    lateinit var binding: ActivityProductsBinding
    private val viewModel: ProductsViewModel by viewModels()
    private val favoriteViewModel: FavoriteViewModel by viewModels()
    private val searchViewModel: SearchViewModel by viewModels()
    private val addCartViewModel: AddToCartViewModel by viewModels()
    private val categoryViewModel: CategoryViewModel by viewModels()
    private var products: MutableList<ProductModel> = ArrayList()
    lateinit var adapter: ProductAdapter
    private var searchWord = ""
    private var isLoading = false
    private var isLastPage = false
    private var categoryId: Int = 0

    // this just flag not pagination values
    private var currentPage = 1
    private var pageSize = 10
    private var isFiltered = false //malak
    private var isSearchMode = false
    private var searchJob: Job? = null
    private var categories: ArrayList<OrderTypeModel> = ArrayList()
    private lateinit var flag: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Window insets are now handled automatically by BaseActivity
        init()
        onClick()
        observeAddToCart()

        binding.btnChangeInFilters.paintFlags = binding.btnChangeInFilters.paintFlags or Paint.UNDERLINE_TEXT_FLAG //malak
        currentPage = intent.getIntExtra("saved_page", 1)
    }


    private fun init() {
        flag = intent.getStringExtra("flag") ?: ""
        categoryId = intent.getIntExtra("categoryId", 0)

        var isArabic = SharedPreferenceHelper.language == "ar"
        if (isArabic) binding.btnBack.setImageResource(R.drawable.ic_back_ar)
        else binding.btnBack.setImageResource(R.drawable.ic_back)

        binding.txtSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(qString: String): Boolean {
                searchWord = qString
                searchJob?.cancel()

                searchJob = lifecycleScope.launch {
                    delay(500)

                    if (qString.isNotEmpty()) {
                        // 🔍 New search
                        isSearchMode = true
                        currentPage = 1
                        isLastPage = false

                        if (::adapter.isInitialized) {
                            adapter.clear()
                        }

                        searchProduct(qString, flag, categoryId)
                    } else {
                        // ❌ Search cleared
                        resetToNormalProducts()
                    }
                }

                return true
            }

            override fun onQueryTextSubmit(qString: String): Boolean {
                if (qString.isNotEmpty()) {
//                    searchProduct(qString, flag, categoryId)
                    startNewSearch(qString)
                } else {
                    getProductsBasedOnCategory(categoryId, category = flag)
                }
                return true
            }
        })

        searchObserve()
        productsObserve()
        Log.d("TAG", "ProductsActivity: ProductsActivity ")


        Log.d("TAG", "init flag: ${flag}")
        Log.d("TAG", "init categoryId: ${categoryId}")
        getProductsBasedOnCategory(categoryId, category = flag)

        lifecycleScope.launch {
            categoryViewModel.categoryIntent.send(CategoryIntent.GetCategories)
        }
        observeCategories()
    }

    private fun observeCategories() {
        lifecycleScope.launch {
            categoryViewModel.state.collect { state ->
                when (state) {
                    is CategoryStatus.Idle -> Unit
                    is CategoryStatus.Loading -> {
                        showProgressDialog(binding.progressLoading)
                    }

                    is CategoryStatus.GetCategory -> {
                        hideProgressDialog(binding.progressLoading)
                        categories = state.data.data!! as ArrayList<OrderTypeModel>
                    }

                    is CategoryStatus.Error -> {
                        hideProgressDialog(binding.progressLoading)
//                        binding.recyclerPharma.visibility = View.VISIBLE
                        //   Toast.makeText(requireContext(), state.error ?: "Error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    fun updateFilterUiState(selectedCount: Int) {
        Log.d("filter_ui", "updateFilterUiState called with count: $selectedCount, isFiltered: $isFiltered")

        // malak
        if (selectedCount > 0 || isFiltered) {
            isFiltered = true //malak
            binding.cardSearchingPharma.visibility = View.GONE //malak
        }

        // malak
        if (selectedCount > 0) {
            binding.tvFilterBadge.text = selectedCount.toString() //malak
            binding.tvFilterBadge.visibility = View.VISIBLE //malak
        } else {
            binding.tvFilterBadge.visibility = View.GONE //malak
        }
    }

    private fun searchObserve() {
        lifecycleScope.launch {
            searchViewModel.state.collect { state ->
                when (state) {
                    is SearchStatus.Idle -> Log.d(Common.KeroDebug, "observeHome: Idle")
                    is SearchStatus.Loading -> {
                        isLoading = true
                        Log.d(Common.KeroDebug, "observeHome: Loading")
                        showProgressDialog(binding.progressLoading)
                        binding.txtNoProducts.visibility = View.GONE
                    }

                    is SearchStatus.SearchProduct -> {
                        isLoading = false
                        hideProgressDialog(binding.progressLoading)
                        if (state.data.status == 200) {
                            isSearchMode = true
                            val searchResults = state.data.data ?: emptyList()

                            if (searchResults.isNotEmpty()) {
                                binding.txtNoProducts.visibility = View.GONE
                                binding.recycler.visibility = View.VISIBLE
                                setupProductsRecycler(searchResults)
                            } else {
                                binding.txtNoProducts.visibility = View.VISIBLE
                                binding.recycler.visibility = View.GONE
                            }
                        } else if (state.data.status == 401) {
                            hideProgressDialog(binding.progressLoading)
                        } else {
                            hideProgressDialog(binding.progressLoading)
                            showToastSnack(state.data.message ?: "", true)
                        }
                    }

                    is SearchStatus.Error -> {
                        isLoading = false
                        Log.d(Common.KeroDebug, "observeHome Error: ${state.error}")
                        hideProgressDialog(binding.progressLoading)
                        binding.txtNoProducts.visibility = View.VISIBLE
                        binding.recycler.visibility = View.GONE
                        showToastSnack(state.error.toString(), true)
                    }
                }
            }
        }
    }


    private fun searchProduct(word: String, orderType: String, category: Int) {
        lifecycleScope.launch {
            searchViewModel.searchIntent.send(
                SearchIntent.SearchProduct(
                    word,
                    orderType,
                    category,
                    currentPage
                )
            )
        }
    }


    private fun onClick() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        // malak
        binding.btnChangeInFilters.setOnClickListener {
            binding.layoutFilter.performClick() //malak
        }

        binding.layoutFilter.setOnClickListener {
            Log.d("WHATcategories.size", "${categories.size}")

            val bottomSheet = FilterProductsBottomSheet.newInstance(orderTypes = categories) //malak
            bottomSheet.onFilterAppliedListeners = { orderType, childId -> //malaakk
                Log.d("filter_test", "Received in Activity -> OrderType: $orderType, ChildId: $childId")
                currentPage = 1 //malak
                products.clear() // malak
                if(::adapter.isInitialized){
                    adapter.clear()
                }
                if(!orderType.isNullOrEmpty()) flag = orderType //malak

                categoryId = childId ?: 0

                // malak
                val selectedCount = if ((childId != null && childId != 0) || !orderType.isNullOrEmpty()) 1 else 0 //malak
                updateFilterUiState(selectedCount) //malak

                getProductsBasedOnCategory(
                    categoryId = categoryId,
                    page = 1,
                    category = flag
                )

            }
            bottomSheet
                .show(
                    supportFragmentManager,
                    "FilterProductsBottomSheet"
                )
        }

        binding.layoutSort.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.bottom_sheet_sort, null)

            val rvSortingOptions = view.findViewById<RecyclerView>(R.id.rvSortingOptions)
            val btnClose = view.findViewById<ImageButton>(R.id.btnClose)
            val btnApplySort = view.findViewById<com.google.android.material.button.MaterialButton>(R.id.btnApplySort)

            val sortingOptions = listOf(
                "Alphabetical (A - Z)",
                "Alphabetical (Z - A)",
                "Price: Low to High",
                "Price: High to Low"
            )

            var selectedSortByCode: String? = null

            rvSortingOptions.adapter = SortProductAdapter(sortingOptions) { position ->
                selectedSortByCode = when (position) {
                    0 -> "A-Z"
                    1 -> "Z-A"
                    2 -> "0-1"
                    3 -> "1-0"
                    else -> null
                }
            }

            btnClose.setOnClickListener {
                bottomSheetDialog.dismiss()
            }

            btnApplySort?.setOnClickListener {
                viewModel.selectedSortBy = selectedSortByCode

                currentPage = 1
                products.clear()
                if (::adapter.isInitialized) {
                    adapter.clear()
                }

                getProductsBasedOnCategory(categoryId, page = 1, category = flag)
                bottomSheetDialog.dismiss()
            }

            bottomSheetDialog.setContentView(view)
            bottomSheetDialog.show()
        }
    }

    private fun observeAddToCart() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                addCartViewModel.state.collect { state ->
                    when (state) {
                        is AddToCartStatus.Idle -> Unit
                        is AddToCartStatus.Loading -> showProgressDialog(binding.progressLoading)
                        is AddToCartStatus.AddToCart -> {
                            hideProgressDialog(binding.progressLoading)
                            if (state.data.status == 200) {
                                showToastSnack(state.data.message ?: "", false)
                            } else {
                                showToastSnack(state.data.message ?: "", true)
                            }
                            addCartViewModel.resetState()
                        }

                        is AddToCartStatus.Error -> {
                            hideProgressDialog(binding.progressLoading)
                            showToastSnack(state.error ?: "", true)
                            addCartViewModel.resetState()
                        }
                    }
                }
            }
        }
    }


    private fun productsObserve() {
        lifecycleScope.launch {
            viewModel.state.collect { state ->
                when (state) {
                    is ProductsStatus.Idle -> {
                        Log.d(Common.KeroDebug, "Pagination Log: State = Idle")
                        binding.txtNoProducts.visibility = View.GONE
                    }

                    is ProductsStatus.Loading -> {
                        isLoading = true
                        Log.d(Common.KeroDebug, "Pagination Log: Loading page $currentPage")
                        showProgressDialog(binding.progressLoading)
                        binding.txtNoProducts.visibility = View.GONE
                        binding.progressLoading.visibility = View.VISIBLE // malaakk
                        binding.recycler.visibility = View.INVISIBLE // malalak
                        binding.txtNoProducts.visibility = View.GONE // malak
                    }

                    is ProductsStatus.GetProducts -> {
                        isLoading = false
                        hideProgressDialog(binding.progressLoading)

                        if (state.data.status != -1) {
                            // malak
                            val dataList = state.data.data ?: emptyList()

                            pageSize = state.data.pagination?.page_size ?: pageSize
                            isSearchMode = false

                            // malak
                            if (currentPage == 1) {
                                products.clear()
                                if (::adapter.isInitialized) {
                                    adapter.clear() // malak
                                }
                            }

                            if (dataList.isNotEmpty()) {
                                Log.d("SORT_TEST", "Response First Product: ${dataList[0].TITLE} - Price: ${dataList[0].PRICE_AFTER_DISCOUNT}")

                                products.addAll(dataList) // malak
                                binding.txtNoProducts.visibility = View.GONE
                                binding.recycler.visibility = View.VISIBLE // malak

                                setupProductsRecycler(dataList)

                                binding.recycler.post {
                                    hideProgressDialog(binding.progressLoading)
                                }
                                // malak
                            } else {
                                if (currentPage == 1) {
                                    binding.txtNoProducts.visibility = View.VISIBLE
                                    binding.recycler.visibility = View.GONE
                                    if (::adapter.isInitialized) {
                                        adapter.clear()
                                    }
                                }
                            }
                        } else {
                            binding.txtNoProducts.visibility = View.VISIBLE
                            showToastSnack(state.data.message ?: "", true)
                        }
                    }

                    is ProductsStatus.Error -> {
                        // malak
                        isLoading = false
                        hideProgressDialog(binding.progressLoading)
                        binding.txtNoProducts.visibility = View.VISIBLE
                        showToastSnack(state.error.toString(), true)
                        Log.e(Common.KeroDebug, "Pagination Log: Error: ${state.error}")
                    }
                }
            }
        }
    }

    private fun addProductToFavorite(productId: Int, add: Boolean) {
        lifecycleScope.launch {
            favoriteViewModel.favoriteIntent.send(
                FavoriteIntent.AddProductToFavourites(
                    "Bearer ${SharedPreferenceHelper.userToken}",
                    productId,
                    add,
                    flag
                )
            )
        }
    }

    private fun deleteProductToFavorite(productId: Int, add: Boolean) {
        lifecycleScope.launch {
            favoriteViewModel.favoriteIntent.send(
                FavoriteIntent.DeleteFromFavourites(
                    "Bearer ${SharedPreferenceHelper.userToken}",
                    productId,
                    add,
                    flag
                )
            )
        }
    }


    private fun getProductsBasedOnCategory(categoryId: Int, page: Int = 1, category: String) {
        lifecycleScope.launch {
            viewModel.homeIntent.send(
                ProductsIntent.GetProducts(
                    categoryId = categoryId,
                    page = page,
                    category,

                    )
            )
        }
    }


    private fun handleAddToCart(product: ProductModel) {
        if (!product.IN_STOCK) {
            showToastSnack("Product Out Of Stock", true)
            return
        }

        val categoryForRequest = when {
            flag.isNotEmpty() -> flag
            product.ITEM_TYPE.isNotEmpty() -> product.ITEM_TYPE
            else -> null
        }

        if (categoryForRequest == null) {
            showToastSnack("Unable to add product to cart", true)
            return
        }

        lifecycleScope.launch {
            addCartViewModel.addToCartIntent.send(
                AddToCartIntent.AddProductToCart(
                    productId = product.ID,
                    quantity = 1,
                    category = categoryForRequest
                )
            )
        }
    }

    private fun setupProductsRecycler(list: List<ProductModel>) {
        if (!::adapter.isInitialized) {
            val layoutManager = GridLayoutManager(this, 2)

            adapter = ProductAdapter(
                onClick = { product, _, sharedView, transitionName ->
                    val intent = Intent(this@ProductsActivity, ProductDetailsActivity::class.java).apply {
                        putExtra("flag", flag)
                        putExtra("product", product)
                        putExtra("transitionName", transitionName)
                    }
                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        this@ProductsActivity,
                        sharedView,
                        transitionName
                    )
                    startActivity(intent, options.toBundle())
                },
                onFavoriteClick = { product, _, isFavorite ->
                    if (isFavorite) {
                        addProductToFavorite(product.ID, isFavorite)
                    } else {
                        deleteProductToFavorite(product.ID, isFavorite)
                    }
                },
                onAddToCartClick = { product ->
                    handleAddToCart(product)
                }
            )

            val spacing = resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._8sdp)
            binding.recycler.apply {
                this.layoutManager = layoutManager
                this.adapter = this@ProductsActivity.adapter

                if (itemDecorationCount == 0) {
                    addItemDecoration(
                        GridSpacingItemDecoration(
                            spanCount = 2,
                            spacing = spacing
                        )
                    )
                }
            }

            binding.recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy > 0) {
                        val visibleItemCount = layoutManager.childCount
                        val totalItemCount = layoutManager.itemCount
                        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                        if (!isLoading && !isLastPage) {
                            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount - 3) {
                                lifecycleScope.launch {
                                    currentPage++
                                    if (isSearchMode) {
                                        searchProduct(searchWord, flag, categoryId)
                                    } else {
                                        viewModel.homeIntent.send(
                                            ProductsIntent.GetProducts(
                                                categoryId,
                                                currentPage,
                                                flag
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            })
        }

        // malak
        if (currentPage == 1) {
            adapter.setData(list, false, flag)
        } else {
            adapter.addData(list)
        }
    }

    private fun startNewSearch(word: String) {
        isSearchMode = true
        currentPage = 1
        isLastPage = false
        products.clear()
        adapter.clear()

        searchProduct(word, flag, categoryId)
    }

    private fun resetToNormalProducts() {
        isSearchMode = false
        currentPage = 1
        isLastPage = false

        products.clear()

        if (::adapter.isInitialized) {
            adapter.clear()
        }

        getProductsBasedOnCategory(
            categoryId = categoryId,
            page = 1,
            category = flag
        )
    }

}