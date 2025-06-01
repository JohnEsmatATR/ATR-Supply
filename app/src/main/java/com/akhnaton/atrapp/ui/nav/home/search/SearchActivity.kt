package com.akhnaton.atrapp.ui.nav.home.search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.data.model.ProductModel
import com.akhnaton.atrapp.data.statuesValue.nav.home.category.CategoryIntent
import com.akhnaton.atrapp.data.statuesValue.nav.home.category.CategoryStatus
import com.akhnaton.atrapp.data.statuesValue.nav.home.favorite.FavoriteIntent
import com.akhnaton.atrapp.data.statuesValue.nav.home.search.SearchIntent
import com.akhnaton.atrapp.data.statuesValue.nav.home.search.SearchStatus
import com.akhnaton.atrapp.databinding.ActivitySearchBinding
import com.akhnaton.atrapp.shared.BaseActivity
import com.akhnaton.atrapp.shared.Common
import com.akhnaton.atrapp.shared.SharedPreferenceHelper
import com.akhnaton.atrapp.ui.nav.favorite.FavoriteViewModel
import com.akhnaton.atrapp.ui.nav.home.CategoryViewModel
import com.akhnaton.atrapp.ui.nav.home.ProductAdapter
import com.akhnaton.atrapp.ui.nav.home.product.productDetails.ProductDetailsActivity
import kotlinx.coroutines.launch

class SearchActivity : BaseActivity() {
    lateinit var binding: ActivitySearchBinding
    private val searchViewModel: SearchViewModel by viewModels()
    private val favoriteViewModel: FavoriteViewModel by viewModels()
    private val cateViewModel : CategoryViewModel by  viewModels()
    lateinit var adapter: ProductAdapter
    private var searchWord = ""
    private var flag = ""
    private lateinit var filterAdapter: FilterProductsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)


        onClick()
        setupCategoryRecyclerView()
        observeCategoryState()
        getCategories()
        searchObserve()
    }



    private fun onClick() {
        binding.txtSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(qString: String): Boolean {
                searchWord = qString
                return true
            }
            override fun onQueryTextSubmit(qString: String): Boolean {
                if (qString.isNotEmpty()) {

                    searchProduct(qString)
                }
                return true
            }

        })
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun searchObserve()
    {
        lifecycleScope.launch {
            searchViewModel.state.collect {
                when (it) {
                    is SearchStatus.Idle -> Log.d(Common.KeroDebug, "observeHome: Idle")
                    is SearchStatus.Loading -> {
                        Log.d(Common.KeroDebug, "observeHome: Loading")
                        showProgressDialog(binding.progressLoading)
                        binding.txtNoProducts.visibility=View.GONE
                    }

                    is SearchStatus.SearchProduct -> {
                        if (it.data.status == 200) {
                            hideProgressDialog(binding.progressLoading)

                            if (it.data.data!!.isNotEmpty()) {
                                Log.d(Common.KeroDebug, "observeHome: ${it.data.data!!}")
                                setupProductsRecycler(it.data.data!!)
                                binding.txtNoProducts.visibility = View.GONE
                                binding.txtItemsCount.text = "${it.data.data!!.size} Items"

                            } else {
                                binding.txtNoProducts.visibility = View.VISIBLE
                                binding.txtItemsCount.text = "0 Item"

                            }
                            binding.txtSearchWord.text = searchWord

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

    private fun searchProduct(word: String, categoryId: Int? = null) {
        lifecycleScope.launch {
            searchViewModel.searchIntent.send(

                    SearchIntent.SearchProduct(word)


            )
        }
    }


    private fun setupProductsRecycler(list: List<ProductModel>) {
        if (!::adapter.isInitialized) {
            val layoutManager = GridLayoutManager(this, 2)
            adapter = ProductAdapter(
                onClick = { product, position, sharedView, transitionName ->
                    val intent = Intent(this@SearchActivity, ProductDetailsActivity::class.java).apply {
                        putExtra("flag", Common.category)
                        putExtra("product", product)
                        putExtra("transitionName", transitionName)
                    }

                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        this@SearchActivity,  // لو داخل Fragment
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
                }
            )

            adapter.setData(list, false, flag)
            binding.recycler.layoutManager = layoutManager
            binding.recycler.adapter = adapter

        } else {
            adapter.setData(list, false, flag)
        }
    }

    private fun setupCategoryRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.filter_recy)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        filterAdapter = FilterProductsAdapter(
            onClick = { categoryId, title ->
                var searchWord = binding.txtSearch.query.toString().trim()
                if (searchWord.isEmpty()) {
                    searchWord = ""
                }
                binding.txtSearchWord.text = searchWord
                if (::adapter.isInitialized) {
                    adapter.setData(emptyList(), false, flag)
                }
                lifecycleScope.launch {
                    searchViewModel.searchIntent.send(
                        SearchIntent.SearchProduct(searchWord, categoryId)
                    )
                }
            },
            categories = emptyList()
        )

        recyclerView.adapter = filterAdapter
    }






    private fun observeCategoryState() {
        lifecycleScope.launch {
            cateViewModel.state.collect { state ->
                when (state) {
                    is CategoryStatus.GetCategory -> {
                        filterAdapter.updateCategories(state.data.data ?: emptyList())
                    }
                    is CategoryStatus.Error -> {
                        Toast.makeText(this@SearchActivity, "Error: ${state.error}", Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }
    }



    private fun getCategories() {
        lifecycleScope.launch {
            cateViewModel.homeIntent.send(
                CategoryIntent.GetCategories
            )
        }
    }
    private fun addProductToFavorite(
        productId: Int,
        add: Boolean,
    ) {
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
}