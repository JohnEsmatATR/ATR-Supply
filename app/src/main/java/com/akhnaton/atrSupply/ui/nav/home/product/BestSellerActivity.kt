package com.akhnaton.atrSupply.ui.nav.home.product

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.akhnaton.atrSupply.data.model.ProductModel
import com.akhnaton.atrSupply.data.statuesValue.nav.home.bestSeller.BestSellerIntent
import com.akhnaton.atrSupply.data.statuesValue.nav.home.bestSeller.BestSellerStatus
import com.akhnaton.atrSupply.databinding.ActivityBestSallerBinding
import com.akhnaton.atrSupply.shared.BaseActivity
import com.akhnaton.atrSupply.shared.Common
import com.akhnaton.atrSupply.ui.nav.favorite.FavoriteViewModel
import com.akhnaton.atrSupply.ui.nav.home.BestSellerViewModel
import com.akhnaton.atrSupply.ui.nav.home.ProductAdapter
import kotlinx.coroutines.launch

class BestSellerActivity : BaseActivity() {
    private val viewModel: ProductsViewModel by viewModels()
    private val bestSellerViewModel: BestSellerViewModel by viewModels()
    private val favoriteViewModel: FavoriteViewModel by viewModels()
    private var products: MutableList<ProductModel> = ArrayList()
    lateinit var adapter: ProductAdapter
    private lateinit var binding: ActivityBestSallerBinding
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
        finish()
        }
    }
}