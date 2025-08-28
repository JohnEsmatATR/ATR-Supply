package com.akhnaton.atrapp.ui.nav.home.product

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.data.model.CategoryModel
import com.akhnaton.atrapp.data.model.ProductModel
import com.akhnaton.atrapp.data.statuesValue.nav.home.bestSeller.BestSellerIntent
import com.akhnaton.atrapp.data.statuesValue.nav.home.bestSeller.BestSellerStatus
import com.akhnaton.atrapp.data.statuesValue.nav.home.favorite.FavoriteIntent
import com.akhnaton.atrapp.data.statuesValue.nav.home.products.ProductsIntent
import com.akhnaton.atrapp.databinding.ActivityBestSallerBinding
import com.akhnaton.atrapp.shared.BaseActivity
import com.akhnaton.atrapp.shared.Common
import com.akhnaton.atrapp.shared.SharedPreferenceHelper
import com.akhnaton.atrapp.ui.nav.HomeActivity
import com.akhnaton.atrapp.ui.nav.favorite.FavoriteViewModel
import com.akhnaton.atrapp.ui.nav.home.BestSellerViewModel
import com.akhnaton.atrapp.ui.nav.home.ProductAdapter
import com.akhnaton.atrapp.ui.nav.home.product.productDetails.BestSellerDetailsActivity
import com.akhnaton.atrapp.ui.nav.home.product.productDetails.ProductDetailsActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.getValue

class BestSellerActivity : BaseActivity() {
    private val viewModel: ProductsViewModel by viewModels()
    private val bestSellerViewModel: BestSellerViewModel by viewModels()
    private val favoriteViewModel: FavoriteViewModel by viewModels()
    private var products: MutableList<ProductModel> = ArrayList()
    private var category = CategoryModel()
    lateinit var adapter: ProductAdapter
    private lateinit var binding: ActivityBestSallerBinding
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    private var pageSize = 10
    private var categoryId: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBestSallerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }
    private fun init(){
        getBestSeller()
        bestSellerObserve()
        handleBackPress()
    }
    private fun getBestSeller() {
        lifecycleScope.launch {
            bestSellerViewModel.homeIntent.send(
                BestSellerIntent.GetBestSeller(1)
            )
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
    private fun setupProductsRecycler(list: List<ProductModel>) {
//        if (!::adapter.isInitialized) {
//            val layoutManager = GridLayoutManager(this, 2)
//            adapter = ProductAdapter(
//                onClick = { product, position, sharedView, transitionName ->
//                    val intent = Intent(this@BestSellerActivity, BestSellerDetailsActivity::class.java).apply {
//                        putExtra("flag", Common.category)
//                        putExtra("product", product)
//                        putExtra("transitionName", transitionName)
//                    }
//
//                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
//                        this@BestSellerActivity,
//                        sharedView,
//                        transitionName
//                    )
//
//                    startActivity(intent, options.toBundle())
//                },
//                onFavoriteClick = { product, position, isFavorite ->
//                    if (isFavorite) {
//                        addProductToFavorite(product.ID, isFavorite)
//                    } else {
//                        deleteProductToFavorite(product.ID, isFavorite)
//                    }
//                }
//            )
//
//            adapter.setData(list, false,"dd")
//            binding.recycler.layoutManager = layoutManager
//            binding.recycler.adapter = adapter
//            binding.recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                    if (dy > 0) {
//                        val visibleItemCount = layoutManager.childCount
//                        val totalItemCount = layoutManager.itemCount
//                        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
//
////                        Log.d(Common.KeroDebug, "Pagination Log: Scroll - visible $visibleItemCount," +
////                                " total $totalItemCount, firstVisible $firstVisibleItemPosition")
//
//                        if (!isLoading && !isLastPage) {
//                            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount - 3) {
//                                Log.d(Common.KeroDebug, "Pagination Log: Triggering load for page $currentPage")
//                                lifecycleScope.launch(Dispatchers.Main) {
//                                    viewModel.homeIntent.send(ProductsIntent.GetProducts(
//                                        categoryId,
//                                        currentPage
//                                    ))
//                                }
//
//                            }
//                        }
//                    }
//                }
//            })
//
//
//        } else {
//            adapter.addData(list)
//        }
    }

//    private fun addProductToFavorite(productId: Int, add: Boolean, ) {
//        lifecycleScope.launch {
//            favoriteViewModel.favoriteIntent.send(
//                FavoriteIntent.AddProductToFavourites(
//                    "Bearer ${SharedPreferenceHelper.userToken}",
//                    productId,
//                    add,
//                )
//            )
//        }
//    }
//    private fun deleteProductToFavorite(productId: Int, add: Boolean, ) {
//        lifecycleScope.launch {
//            favoriteViewModel.favoriteIntent.send(
//                FavoriteIntent.DeleteFromFavourites(
//                    "Bearer ${SharedPreferenceHelper.userToken}",
//                    productId,
//                    add,
//                )
//            )
//        }
//    }

    private fun handleBackPress() {
        onBackPressedDispatcher.addCallback(this) {
            startActivity(Intent(applicationContext, HomeActivity::class.java))
        }
    }
}