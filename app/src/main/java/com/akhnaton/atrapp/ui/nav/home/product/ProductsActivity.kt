package com.akhnaton.atrapp.ui.nav.home.product

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import com.akhnaton.atrapp.shared.SharedPreferenceHelper
import com.akhnaton.atrapp.ui.nav.favorite.FavoriteViewModel
import com.akhnaton.atrapp.ui.nav.home.ProductAdapter
import com.akhnaton.atrapp.ui.nav.home.product.productDetails.ProductDetailsActivity
import com.akhnaton.atrapp.ui.nav.home.search.SearchViewModel
import com.akhnaton.atrapp.ui.nav.cart.AddToCartViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProductsActivity : BaseActivity() {
    lateinit var binding: ActivityProductsBinding
    private val viewModel: ProductsViewModel by viewModels()
    private val favoriteViewModel: FavoriteViewModel by viewModels()
    private val searchViewModel: SearchViewModel by viewModels()
    private val addCartViewModel: AddToCartViewModel by viewModels()
    private var products: MutableList<ProductModel> = ArrayList()
    lateinit var adapter: ProductAdapter
    private var searchWord = ""
    private var isLoading = false
    private var isLastPage = false
    private var categoryId: Int = 0

    // this just flag not pagination values
    private var currentPage = 1
    private var pageSize = 10
    private var isSearchMode = false
    private var searchJob: Job? = null
    private lateinit var flag: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Window insets are now handled automatically by BaseActivity
        init()
        onClick()
        observeAddToCart()

        currentPage = intent.getIntExtra("saved_page", 1)
    }


    private fun init() {
        flag = intent.getStringExtra("flag") ?: ""
        categoryId = intent.getIntExtra("categoryId", 0)


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
    }

    private fun searchObserve() {
        lifecycleScope.launch {
            searchViewModel.state.collect {
                when (it) {
                    is SearchStatus.Idle -> Log.d(Common.KeroDebug, "observeHome: Idle")
                    is SearchStatus.Loading -> {
                        Log.d(Common.KeroDebug, "observeHome: Loading")
                        showProgressDialog(binding.progressLoading)
                        binding.txtNoProducts.visibility = View.GONE
                    }

                    is SearchStatus.SearchProduct -> {
                        if (it.data.status == 200) {
                            hideProgressDialog(binding.progressLoading)

                            isSearchMode = true
                            if (it.data.data!!.isNotEmpty()) {
                                setupProductsRecycler(it.data.data!!)
                                binding.txtNoProducts.visibility = View.GONE
                                binding.recycler.visibility = View.VISIBLE
                            } else {
                                binding.txtNoProducts.visibility = View.VISIBLE
                                binding.recycler.visibility = View.GONE
                            }


                        } else if (it.data.status == 401) {
                            hideProgressDialog(binding.progressLoading)
//                            onTokenExpired(it.data.errors!![0])

                        } else {
                            hideProgressDialog(binding.progressLoading)
                            showToastSnack(it.data.message, true)
                        }

                    }


                    is SearchStatus.Error -> {
                        Log.d(Common.KeroDebug, "observeHome Error: ${it.error.toString()}")
                        hideProgressDialog(binding.progressLoading)
                        showToastSnack(it.error.toString(), true)
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
                    }

                    is ProductsStatus.GetProducts -> {
                        isLoading = false
                        hideProgressDialog(binding.progressLoading)

                        if (state.data.status != -1) {
                            val dataList = state.data.data ?: emptyList()
                            pageSize = state.data.pagination?.page_size ?: pageSize

                            isSearchMode = false
                            if (dataList.isNotEmpty()) {
                                products.addAll(dataList)
                                setupProductsRecycler(dataList)
                                binding.txtNoProducts.visibility = View.GONE
                            } else {
                                binding.txtNoProducts.visibility = View.VISIBLE
                            }


                        } else {
                            binding.txtNoProducts.visibility = View.VISIBLE
                            showToastSnack(state.data.message, true)
                        }
                    }

                    is ProductsStatus.Error -> {
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
                onClick = { product, position, sharedView, transitionName ->
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
                onFavoriteClick = { product, position, isFavorite ->
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

//            adapter.setData(list, false, flag)

//            adapter.addData(list)
            if (currentPage == 1) {
                adapter.setData(list, false, flag)
            } else {
                adapter.addData(list)
            }
            if (list.size < pageSize) {
                isLastPage = true
            }

            binding.recycler.layoutManager = layoutManager
            binding.recycler.adapter = adapter

            if (list.size < pageSize) {
                isLastPage = true
            }

            binding.recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy > 0) {
                        val visibleItemCount = layoutManager.childCount
                        val totalItemCount = layoutManager.itemCount
                        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                        if (!isLoading && !isLastPage) {
                            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount - 3) {
                                Log.d(
                                    Common.KeroDebug,
                                    "Pagination Log: Triggering load for page $currentPage"
                                )
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


        } else {
            if (isSearchMode) {

//                adapter.setData(list, false, flag)
//                adapter.addData(list)
                adapter.addData(list)
                if (currentPage == 1) {
                    adapter.setData(list, false, flag)
                } else {
                    adapter.addData(list)
                }
                if (list.size < pageSize) {
                    isLastPage = true
                }
            } else {

//                adapter.addData(list)
                adapter.addData(list)
                if (currentPage == 1) {
                    adapter.setData(list, false, flag)
                } else {
                    adapter.addData(list)
                }
                if (list.size < pageSize) {
                    isLastPage = true
                }
                if (list.size < pageSize) {
                    isLastPage = true
                }
            }
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