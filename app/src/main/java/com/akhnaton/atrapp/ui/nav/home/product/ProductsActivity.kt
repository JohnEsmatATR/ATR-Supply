package com.akhnaton.atrapp.ui.nav.home.product

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akhnaton.atrapp.data.model.CategoryModel
import com.akhnaton.atrapp.data.model.ProductModel
import com.akhnaton.atrapp.data.statuesValue.nav.home.bestSeller.BestSellerIntent
import com.akhnaton.atrapp.data.statuesValue.nav.home.bestSeller.BestSellerStatus
import com.akhnaton.atrapp.data.statuesValue.nav.home.favorite.FavoriteIntent
import com.akhnaton.atrapp.data.statuesValue.nav.home.products.ProductsIntent
import com.akhnaton.atrapp.data.statuesValue.nav.home.products.ProductsStatus
import com.akhnaton.atrapp.databinding.ActivityProductsBinding
import com.akhnaton.atrapp.shared.BaseActivity
import com.akhnaton.atrapp.shared.Common
import com.akhnaton.atrapp.shared.SharedPreferenceHelper
import com.akhnaton.atrapp.ui.nav.favorite.FavoriteViewModel
import com.akhnaton.atrapp.ui.nav.home.BestSellerViewModel
import com.akhnaton.atrapp.ui.nav.home.ProductAdapter
import com.akhnaton.atrapp.ui.nav.home.product.productDetails.ProductDetailsActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

class ProductsActivity : BaseActivity() {
    lateinit var binding: ActivityProductsBinding
    private val viewModel: ProductsViewModel by viewModels()
    private val bestSellerViewModel: BestSellerViewModel by viewModels()
    private val favoriteViewModel: FavoriteViewModel by viewModels()
   // private val searchViewModel: SearchViewModel by viewModels()
    private var products: MutableList<ProductModel> = ArrayList()
    private var category = CategoryModel()
    lateinit var adapter: ProductAdapter
    private var flag = ""
    private var id = -1
    private var isLoading = false
    private var isLastPage = false
    private var categoryId: Int = 0
    private var searchWord = ""
    // this just flag not pagination values
    private var currentPage = 1
    private var pageSize = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        onClick()
        search()
        currentPage = intent.getIntExtra("saved_page", 1)
    }


    private fun init() {

        flag = intent.getStringExtra("flag") ?: ""

        when (flag) {
            Common.category -> {
                productsObserve()
                category = intent.getSerializableExtra("category") as CategoryModel
                categoryId = category.ID  // أضف هذا السطر
                getProductsBasedOnCategory(categoryId)

            }
            Common.bestSeller -> {
                getBestSeller()
                bestSellerObserve()
            }
        }


    }

    private fun onClick() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun bestSellerObserve() {
        lifecycleScope.launch {
            bestSellerViewModel.state.collect {
                when (it) {
                    is BestSellerStatus.Idle -> Log.d(Common.KeroDebug, "observeHome: Idle")
                    is BestSellerStatus.Loading -> {
                        Log.d(Common.KeroDebug, "observeHome: Loading")
                        showProgressDialog(binding.progressLoading)
                    }

                    is BestSellerStatus.GetBestSeller -> {
                        if (it.data.status == 200) {
                            hideProgressDialog(binding.progressLoading)
                            Log.d(Common.KeroDebug, "observeHome: GetProducts")
                            if (it.data.data!!.isNotEmpty()) {
                                binding.txtNoProducts.visibility = View.GONE
                                products.addAll(it.data.data!!)
                                setupProductsRecycler(products)
                            } else {
                                binding.txtNoProducts.visibility = View.VISIBLE
                            }
                        } else {
                            hideProgressDialog(binding.progressLoading)
                            showToastSnack(it.data.message, true)
                        }
                    }

                    is BestSellerStatus.Error -> {
                        Log.d(Common.KeroDebug, "observeHome Error: ${it.error.toString()}")
                        hideProgressDialog(binding.progressLoading)
                        showToastSnack(it.error.toString(), true)
                    }

                }
            }
        }
    }

    private fun getBestSeller() {
        lifecycleScope.launch {
            bestSellerViewModel.homeIntent.send(
                BestSellerIntent.GetBestSeller(1)
            )
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

                            Log.d(Common.KeroDebug, "Pagination Log: Received ${dataList.size} items on page $currentPage")

                            if (dataList.size < pageSize) {
                                isLastPage = true
                                Log.d(Common.KeroDebug, "Pagination Log: Reached last page at page $currentPage")
                            } else {
                                isLastPage = false
                            }

                            if (dataList.isNotEmpty()) {
                                binding.txtNoProducts.visibility = View.GONE
                                products.addAll(dataList)
                                setupProductsRecycler(dataList)
                                currentPage++
                            } else {
                                binding.txtNoProducts.visibility = View.VISIBLE
                            }

                        } else {
                            binding.txtNoProducts.visibility = View.VISIBLE
                            showToastSnack(state.data.message, true)
                            Log.d(Common.KeroDebug, "Pagination Log: Error status from server: ${state.data.status}")
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

    private fun addProductToFavorite(productId: Int, add: Boolean, ) {
        lifecycleScope.launch {
            favoriteViewModel.favoriteIntent.send(
                FavoriteIntent.AddProductToFavourites(
                    "Bearer ${SharedPreferenceHelper.userToken}",
                    productId,
                    add,
                )
            )
        }
    }
    private fun deleteProductToFavorite(productId: Int, add: Boolean, ) {
        lifecycleScope.launch {
            favoriteViewModel.favoriteIntent.send(
                FavoriteIntent.DeleteFromFavourites(
                    "Bearer ${SharedPreferenceHelper.userToken}",
                    productId,
                    add,
                )
            )
        }
    }


    private fun getProductsBasedOnCategory(categoryId: Int, page: Int = 1) {
        lifecycleScope.launch {
            viewModel.homeIntent.send(
                ProductsIntent.GetProducts(
                    categoryId = categoryId,
                    page = page
                )
            )
        }
    }


    private fun setupProductsRecycler(list: List<ProductModel>) {
        if (!::adapter.isInitialized) {
            val layoutManager = GridLayoutManager(this, 2)
            adapter = ProductAdapter(
                onClick = { product, position ->
                    val intent = Intent(this, ProductDetailsActivity::class.java)
                    intent.putExtra("flag", Common.category)
                    intent.putExtra("product", product)
                    startActivity(intent)
                },
                onFavoriteClick = { product, position, isFavorite ->
                    if (isFavorite) {
                        addProductToFavorite(product.ID, isFavorite)
                    } else {
                        deleteProductToFavorite(product.ID, isFavorite)
                    }
                }
            )

            adapter.setData(list, false, flag)
            binding.recycler.layoutManager = layoutManager
            binding.recycler.adapter = adapter
            binding.recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy > 0) {
                        val visibleItemCount = layoutManager.childCount
                        val totalItemCount = layoutManager.itemCount
                        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

//                        Log.d(Common.KeroDebug, "Pagination Log: Scroll - visible $visibleItemCount," +
//                                " total $totalItemCount, firstVisible $firstVisibleItemPosition")

                        if (!isLoading && !isLastPage) {
                            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount - 3) {
                                Log.d(Common.KeroDebug, "Pagination Log: Triggering load for page $currentPage")
                                lifecycleScope.launch(Dispatchers.Main) {
                                    viewModel.homeIntent.send(ProductsIntent.GetProducts(
                                        categoryId,
                                        currentPage
                                    ))
                                }

                            }
                        }
                    }
                }
            })


        } else {
            adapter.addData(list)
        }
    }


    private fun search() {
        binding.txtSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(txt: String?): Boolean {
                val query = txt?.lowercase(Locale.getDefault())?.trim() ?: ""
                val filteredList = if (query.isEmpty()) {
                    products
                } else {
                    products.filter { product ->
                        product.TITLE.lowercase(Locale.getDefault()).contains(query) ||
                                product.DESCRIPTION.lowercase(Locale.getDefault()).contains(query)
                    }
                }

                if (::adapter.isInitialized) {
                    adapter.updateList(filteredList)
                }

                return true
            }
        })
    }





}