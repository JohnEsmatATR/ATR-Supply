package com.akhnaton.atrapp.ui.nav.home.search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.data.model.ProductModel
import com.akhnaton.atrapp.data.statuesValue.nav.home.favorite.FavoriteIntent
import com.akhnaton.atrapp.data.statuesValue.nav.home.favorite.FavoriteStatus
import com.akhnaton.atrapp.data.statuesValue.nav.home.products.ProductsIntent
import com.akhnaton.atrapp.data.statuesValue.nav.home.search.SearchIntent
import com.akhnaton.atrapp.data.statuesValue.nav.home.search.SearchStatus
import com.akhnaton.atrapp.databinding.ActivitySearchBinding
import com.akhnaton.atrapp.shared.BaseActivity
import com.akhnaton.atrapp.shared.Common
import com.akhnaton.atrapp.shared.SharedPreferenceHelper
import com.akhnaton.atrapp.ui.nav.favorite.FavoriteViewModel
import com.akhnaton.atrapp.ui.nav.home.ProductAdapter
import com.akhnaton.atrapp.ui.nav.home.product.productDetails.ProductDetailsActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchActivity : BaseActivity() {
    lateinit var binding: ActivitySearchBinding
    private val searchViewModel: SearchViewModel by viewModels()
    private val favoriteViewModel: FavoriteViewModel by viewModels()
    lateinit var adapter: ProductAdapter
    private var searchWord = ""
    private var flag = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        onClick()
    }

    private fun init() {
        searchObserve()
        //favoriteObserve()
        addToCartObserve()
    }

    private fun onClick() {
        binding.txtSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(qString: String): Boolean {
                return true
            }
            override fun onQueryTextSubmit(qString: String): Boolean {
                if (qString.isNotEmpty()) {
                    searchWord = qString
                    searchProduct(qString)
                }
                return true
            }
        })
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun searchObserve() {
        lifecycleScope.launch {
            searchViewModel.state.collect {
                when (it) {
                    is SearchStatus.Idle -> Log.d(Common.KeroDebug, "observeHome: Idle")
                    is SearchStatus.Loading -> {
                        Log.d(Common.KeroDebug, "observeHome: Loading")
                        showProgressDialog(binding.progressLoading)
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

    private fun searchProduct(word: String) {
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

        } else {
            adapter.setData(list, false, flag)
        }
    }


//    private fun favoriteObserve() {
//        lifecycleScope.launch {
//            favoriteViewModel.state.collect {
//                when (it) {
//                    is FavoriteStatus.Idle -> Log.d(Common.KeroDebug, "observeHome: Idle")
//                    is FavoriteStatus.Loading -> {
//                        Log.d(Common.KeroDebug, "observeHome: Loading")
//                        showProgressDialog(binding.progressLoading)
//                    }
//
//                    is FavoriteStatus.GetFavorite -> {
//                        if (it.data.status == 200) {
//                            hideProgressDialog(binding.progressLoading)
//                            Log.d(Common.KeroDebug, "observeHome: GetProducts")
//                            //setupProductsRecycler(it.data.data!!)
//
//                            searchObserve()
//
//                        } else if (it.data.status == 401) {
//                            hideProgressDialog(binding.progressLoading)
////                            onTokenExpired(it.data.errors!![0])
//
//                        } else {
//                            hideProgressDialog(binding.progressLoading)
//                            showToastSnack(it.data.message, true)
//                        }
//
//                    }
//
//
//                    is FavoriteStatus.Error -> {
//                        Log.d(Common.KeroDebug, "observeHome Error: ${it.error.toString()}")
//                        hideProgressDialog(binding.progressLoading)
//                        showToastSnack(it.error.toString(), true)
//                    }
//
//                    is FavoriteStatus.AddProductToFavourites -> {
//                        hideProgressDialog(binding.progressLoading)
//                        Log.d(Common.KeroDebug, "observeHome: GetProducts")
//                        showToastSnack(it.data.message, false)
//                    }
//
//                    is FavoriteStatus.DeleteProductToFavourites -> TODO()
//                }
//            }
//        }
//    }

    private fun addToCartObserve() {
//        lifecycleScope.launch {
//            addToCartViewModel.state.collect {
//                when (it) {
//                    is AddToCartStatus.Idle -> Log.d(Common.KeroDebug, "observeHome: Idle")
//                    is AddToCartStatus.Loading -> {
//                        Log.d(Common.KeroDebug, "observeHome: Loading")
//                        showProgressDialog(binding.progressLoading)
//                    }
//
//                    is AddToCartStatus.AddProductToCart -> {
//                        if (it.data.status == 1) {
//                            hideProgressDialog(binding.progressLoading)
//                            Log.d(Common.KeroDebug, "observeHome: GetProducts")
//
//                            showToastSnack(it.data.message, false)
//
//                        } else if (it.data.status == 401) {
//                            hideProgressDialog(binding.progressLoading)
//                            onTokenExpired(it.data.errors!![0])
//
//                        } else {
//                            hideProgressDialog(binding.progressLoading)
//                            showToastSnack(it.data.message, true)
//                        }
//
//                    }
//
//
//                    is AddToCartStatus.Error -> {
//                        Log.d(Common.KeroDebug, "observeHome Error: ${it.error.toString()}")
//                        hideProgressDialog(binding.progressLoading)
//                        showToastSnack(it.error.toString(), true)
//                    }
//
//                }
//            }
//        }
    }


    private fun getMyFavorite() {
        lifecycleScope.launch {
            favoriteViewModel.favoriteIntent.send(
                FavoriteIntent.GetFavorite
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

//    private fun addProductToCart(product: ProductModel) {
//        lifecycleScope.launch {
//            addToCartViewModel.addToCartIntent.send(
//                AddToCartIntent.AddProductToCart(
//                    "Bearer ${SharedPreferenceHelper.userToken}",
//                    product.product_id,
//                    1,
//                    product.price,
//                    product.flag,
//                    product.price_after_discount
//                )
//            )
//        }
//    }

//    private fun setupFavoriteRecycler(list: List<ProductModel>) {
//        val layoutManager =
//            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
//        favoriteAdapter = FavoriteAdapter(
//            onClick = { product, position ->
//                val intent = Intent(requireContext(), ProductDetailsActivity::class.java)
//                product.in_favourite = true
//                product.id = product.product_id
//                intent.putExtra("product", product)
//                startActivity(intent)
//            },
//            onAddToProductClick = { product, position ->
//                val intent = Intent(requireContext(), ProductDetailsActivity::class.java)
//                product.in_favourite = true
//                product.id = product.product_id
//                intent.putExtra("product", product)
//                startActivity(intent)
//            },
//            onFavoriteClick = { product, position ->
//                addProductToFavorite(product.product_id, false)
//                productsUnFavorite = products
//                (productsUnFavorite as ArrayList).removeAt(position)
//            },
//        )
//
//        favoriteAdapter.setData(list)
//        binding.recycler.layoutManager = layoutManager
//        binding.recycler.adapter = favoriteAdapter
//    }


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