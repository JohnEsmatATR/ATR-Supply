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
import com.akhnaton.atrapp.data.model.OrderTypeModel
import com.akhnaton.atrapp.data.model.ProductModel
import com.akhnaton.atrapp.data.statuesValue.nav.cart.addToCart.AddToCartIntent
import com.akhnaton.atrapp.data.statuesValue.nav.cart.addToCart.AddToCartStatus
import com.akhnaton.atrapp.data.statuesValue.nav.home.category.CategoryIntent
import com.akhnaton.atrapp.data.statuesValue.nav.home.category.CategoryStatus
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
import com.akhnaton.atrapp.ui.nav.cart.AddToCartViewModel
import com.akhnaton.atrapp.ui.nav.favorite.FavoriteViewModel
import com.akhnaton.atrapp.ui.nav.home.CategoryViewModel
import com.akhnaton.atrapp.ui.nav.home.ProductAdapter
import com.akhnaton.atrapp.ui.nav.home.product.productDetails.ProductDetailsActivity
import com.akhnaton.atrapp.ui.nav.home.search.SearchViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.collections.emptyList

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

    private var currentPage = 1
    private var pageSize = 10
    private var isFiltered = false
    private var isSearchMode = false
    private var searchJob: Job? = null
    private var categories: ArrayList<OrderTypeModel> = ArrayList()
    private lateinit var flag: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        onClick()
        observeAddToCart()

        val showCard = intent.getBooleanExtra("show_card", false)
        binding.cardSearchingPharma.visibility = if (showCard) {
            View.VISIBLE
        } else {
            View.GONE
        }

        binding.btnChangeInFilters.paintFlags =
            binding.btnChangeInFilters.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        currentPage = intent.getIntExtra("saved_page", 1)
    }

    private fun handleLoadingState(isLoading: Boolean, isPagination: Boolean) {
        if (isLoading) {
            if (isPagination) {
                binding.paginationLoadingLayout.visibility = View.VISIBLE
                binding.progressLoading.visibility = View.GONE
            } else {
                binding.progressLoading.visibility = View.VISIBLE
                binding.paginationLoadingLayout.visibility = View.GONE
            }
        } else {
            binding.progressLoading.visibility = View.GONE
            binding.paginationLoadingLayout.visibility = View.GONE
        }
    }

    private fun init() {
        flag = intent.getStringExtra("flag") ?: ""
        categoryId = intent.getIntExtra("categoryId", 0)

        val isArabic = SharedPreferenceHelper.language == "ar"
        if (isArabic) binding.btnBack.setImageResource(R.drawable.ic_back_ar)
        else binding.btnBack.setImageResource(R.drawable.ic_back)

        binding.txtSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(qString: String): Boolean {
                searchWord = qString
                searchJob?.cancel()

                searchJob = lifecycleScope.launch {
                    delay(500)

                    if (qString.isNotEmpty()) {
                        isSearchMode = true
                        currentPage = 1
                        isLastPage = false

                        if (::adapter.isInitialized) {
                            adapter.clear()
                        }

                        searchProduct(qString, flag, categoryId)
                    } else {
                        resetToNormalProducts()
                    }
                }

                return true
            }

            override fun onQueryTextSubmit(qString: String): Boolean {
                if (qString.isNotEmpty()) {
                    startNewSearch(qString)
                } else {
                    getProductsBasedOnCategory(categoryId, category = flag)
                }
                return true
            }
        })

        searchObserve()
        productsObserve()

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
                        handleLoadingState(isLoading = true, isPagination = false)
                    }

                    is CategoryStatus.GetCategory -> {
                        handleLoadingState(isLoading = false, isPagination = false)
                        categories = state.data.data!! as ArrayList<OrderTypeModel>
                    }

                    is CategoryStatus.Error -> {
                        handleLoadingState(isLoading = false, isPagination = false)
                    }
                }
            }
        }
    }

    fun updateFilterUiState(selectedCount: Int) {
        isFiltered = selectedCount > 0

        binding.cardSearchingPharma.visibility =
            if (isFiltered) View.GONE else View.VISIBLE

        if (isFiltered) {
            binding.tvFilterBadge.text = selectedCount.toString()
            binding.tvFilterBadge.visibility = View.VISIBLE
        } else {
            binding.tvFilterBadge.text = ""
            binding.tvFilterBadge.visibility = View.GONE
        }
    }

    private fun searchObserve() {
        lifecycleScope.launch {
            searchViewModel.state.collect { state ->
                when (state) {
                    is SearchStatus.Idle -> Log.d(Common.KeroDebug, "observeHome: Idle")
                    is SearchStatus.Loading -> {
                        isLoading = true
                        val isPagination = currentPage > 1
                        handleLoadingState(isLoading = true, isPagination = isPagination)
                        binding.txtNoProducts.visibility = View.GONE
                    }

                    is SearchStatus.SearchProduct -> {
                        isLoading = false
                        handleLoadingState(isLoading = false, isPagination = false)
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
                        } else {
                            showToastSnack(state.data.message ?: "", true)
                        }
                    }

                    is SearchStatus.Error -> {
                        isLoading = false
                        handleLoadingState(isLoading = false, isPagination = false)
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

        binding.btnChangeInFilters.setOnClickListener {
            binding.layoutFilter.performClick()
        }

        binding.layoutFilter.setOnClickListener {
            val selectedChildId = categoryId.takeIf { it != 0 }
            val selectedOrderType = flag.takeIf { it.isNotEmpty() }

            val bottomSheet =
                FilterProductsBottomSheet.newInstance(
                    orderTypes = categories,
                    selectedOrderType = selectedOrderType,
                    selectedChildId = selectedChildId
                )

            bottomSheet.onFilterAppliedListeners = { orderType, childId ->
                currentPage = 1
                products.clear()

                if (::adapter.isInitialized) {
                    adapter.clear()
                }

                flag = orderType.orEmpty()
                categoryId = childId ?: 0

                val selectedCount = if (!orderType.isNullOrEmpty() || (childId != null && childId != 0)) 1 else 0

                updateFilterUiState(selectedCount)

                getProductsBasedOnCategory(
                    categoryId = categoryId,
                    page = 1,
                    category = flag
                )
            }

            bottomSheet.show(
                supportFragmentManager,
                "FilterProductsBottomSheet"
            )
        }

        binding.layoutSort.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.bottom_sheet_sort, null)

            val rvSortingOptions = view.findViewById<RecyclerView>(R.id.rvSortingOptions)
            val btnClose = view.findViewById<ImageButton>(R.id.btnClose)
            val btnApplySort =
                view.findViewById<com.google.android.material.button.MaterialButton>(R.id.btnApplySort)

            val sortingOptions = listOf(
                getString(R.string.alphabetical_a_z),
                getString(R.string.alphabetical_z_a),
                getString(R.string.price_l_h),
                getString(R.string.price_h_l),
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
                        is AddToCartStatus.Loading -> handleLoadingState(isLoading = true, isPagination = false)
                        is AddToCartStatus.AddToCart -> {
                            handleLoadingState(isLoading = false, isPagination = false)
                            if (state.data.status == 200) {
                                showToastSnack(state.data.message ?: "", false)
                            } else {
                                showToastSnack(state.data.message ?: "", true)
                            }
                            addCartViewModel.resetState()
                        }

                        is AddToCartStatus.Error -> {
                            handleLoadingState(isLoading = false, isPagination = false)
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
                        binding.txtNoProducts.visibility = View.GONE
                    }

                    is ProductsStatus.Loading -> {
                        isLoading = true
                        val isPagination = currentPage > 1
                        handleLoadingState(isLoading = true, isPagination = isPagination)
                        binding.txtNoProducts.visibility = View.GONE
                        binding.recycler.visibility = View.VISIBLE
                    }

                    is ProductsStatus.GetProducts -> {
                        isLoading = false
                        handleLoadingState(isLoading = false, isPagination = false)

                        if (state.data.status != -1) {
                            val dataList = state.data.data ?: emptyList()

                            pageSize = state.data.pagination?.page_size ?: pageSize
                            isSearchMode = false

                            if (currentPage == 1) {
                                products.clear()
                                if (::adapter.isInitialized) {
                                    adapter.clear()
                                }
                            }

                            if (dataList.isNotEmpty()) {
                                products.addAll(dataList)
                                binding.txtNoProducts.visibility = View.GONE
                                binding.recycler.visibility = View.VISIBLE

                                setupProductsRecycler(dataList)
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
                        isLoading = false
                        handleLoadingState(isLoading = false, isPagination = false)
                        if (currentPage == 1) {
                            binding.txtNoProducts.visibility = View.VISIBLE
                        }
                        showToastSnack(state.error.toString(), true)
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
                    category
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
                    val intent =
                        Intent(this@ProductsActivity, ProductDetailsActivity::class.java).apply {
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
        if (::adapter.isInitialized) {
            adapter.clear()
        }

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