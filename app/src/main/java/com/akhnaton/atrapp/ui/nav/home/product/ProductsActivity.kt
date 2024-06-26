package com.akhnaton.atrapp.ui.nav.home.product

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.akhnaton.atrapp.data.model.CategoryModel
import com.akhnaton.atrapp.data.model.ProductModel
import com.akhnaton.atrapp.data.statuesValue.nav.home.bestSeller.BestSellerIntent
import com.akhnaton.atrapp.data.statuesValue.nav.home.bestSeller.BestSellerStatus
import com.akhnaton.atrapp.data.statuesValue.nav.home.category.CategoryIntent
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
import kotlinx.coroutines.launch
import java.util.Locale

class ProductsActivity : BaseActivity() {
    lateinit var binding: ActivityProductsBinding
    private val viewModel: ProductsViewModel by viewModels()
    private val bestSellerViewModel: BestSellerViewModel by viewModels()
    private val favoriteViewModel: FavoriteViewModel by viewModels()
    private var products: MutableList<ProductModel> = ArrayList()
    private var category = CategoryModel()
    lateinit var adapter: ProductAdapter
    private var flag = ""
    private var id = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        onClick()
    }

    private fun init() {
        productsObserve()
        bestSellerObserve()
        favoriteObserve()
        search()

        flag = intent.getStringExtra("flag") ?: ""

        when (flag) {
            Common.category -> {
                category = intent.getSerializableExtra("category") as CategoryModel
                getProductsBasedOnCategory(category.ID)
            }
            Common.bestSeller -> {
                getBestSeller()
            }
        }

        
    }


    private fun onClick() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }


    private fun search() {
        binding.txtSearch.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(txt: String?): Boolean {
                val list: List<ProductModel> = ArrayList()
                for (product in products) {
                    if (product.TITLE.lowercase(Locale.getDefault()).trim()
                            .contains(txt ?: "".lowercase(Locale.getDefault()).trim())
                        || product.DESCRIPTION.lowercase(Locale.getDefault()).trim()
                            .contains(txt ?: "".lowercase(Locale.getDefault()).trim())) {
                        (list as ArrayList).add(product)
                    }
                }
                setupProductsRecycler(list)
                return true
            }
        })
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
                BestSellerIntent.GetBestSeller
            )
        }
    }


    private fun productsObserve() {
        lifecycleScope.launch {
            viewModel.state.collect {
                when (it) {
                    is ProductsStatus.Idle ->  {
                        Log.d(Common.KeroDebug, "observeProducts: Idle")
                        binding.txtNoProducts.visibility = View.VISIBLE

                    }
                    is ProductsStatus.Loading -> {
                        Log.d(Common.KeroDebug, "observeProducts: Loading")
                        showProgressDialog(binding.progressLoading)
                    }

                    is ProductsStatus.GetProducts -> {
                        if (it.data.status != -1) {
                            hideProgressDialog(binding.progressLoading)
                            if (it.data.data!!.isNotEmpty()){
                                Log.d(Common.KeroDebug, "observeLogin: yes")
                                binding.txtNoProducts.visibility = View.GONE
                            }
                            else {
                                Log.d(Common.KeroDebug, "observeLogin: no")
                                binding.txtNoProducts.visibility = View.VISIBLE
                            }
                            hideProgressDialog(binding.progressLoading)
                            Log.d(Common.KeroDebug, "observeProducts: GetProductsBasedOnCategory")

                            products.addAll(it.data.data!!)
                            setupProductsRecycler(it.data.data!!)
                        } else if (it.data.status == 401) {
                            hideProgressDialog(binding.progressLoading)
                            hideProgressDialog(binding.progressLoading)
//                            onTokenExpired(it.data.errors!![0])

                        } else {
                            hideProgressDialog(binding.progressLoading)
                            binding.txtNoProducts.visibility = View.VISIBLE

                            showToastSnack(it.data.message, true)
                        }
                    }


                    is ProductsStatus.Error -> {
                        Log.d(Common.KeroDebug, "observeProducts Error: ${it.error.toString()}")
                        hideProgressDialog(binding.progressLoading)
                        binding.txtNoProducts.visibility = View.VISIBLE
                        showToastSnack(it.error.toString(), true)
                    }

                }
            }
        }
    }

    private fun favoriteObserve() {
//        lifecycleScope.launch {
//            favoriteViewModel.state.collect {
//                when (it) {
//                    is FavoriteStatus.Idle -> Log.d(Common.KeroDebug, "observeHome: Idle")
//                    is FavoriteStatus.Loading -> {
//                        Log.d(Common.KeroDebug, "observeHome: Loading")
//                        showProgressDialog(binding.progressLoading)
//                    }
//
//                    is FavoriteStatus.AddProductToFavourites -> {
//                        if (it.data.status == 1) {
//                            hideProgressDialog(binding.progressLoading)
//                            Log.d(Common.KeroDebug, "observeHome: GetProducts")
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
//                    }
//
//                    is FavoriteStatus.GetMyFavourites -> {}
//
//                    is FavoriteStatus.Error -> {
//                        Log.d(Common.KeroDebug, "observeHome Error: ${it.error.toString()}")
//                        hideProgressDialog(binding.progressLoading)
//                        showToastSnack(it.error.toString(), true)
//                    }
//                }
//            }
//        }
    }


    private fun addProductToFavorite(productId: Int, add: Boolean, ) {
//        lifecycleScope.launch {
//            favoriteViewModel.favoriteIntent.send(
//                FavoriteIntent.AddProductToFavourites(
//                    "Bearer ${SharedPreferenceHelper.userToken}",
//                    productId,
//                    add,
//                )
//            )
//        }
    }


    private fun getProductsBasedOnCategory(categoryId: Int) {
        lifecycleScope.launch {
            viewModel.homeIntent.send(
                ProductsIntent.GetProducts(
                    categoryId,
                )
            )
        }
    }


    private fun setupProductsRecycler(list: List<ProductModel>) {
        val layoutManager = GridLayoutManager(baseContext, 2)
        adapter = ProductAdapter(
            onClick = { product, position ->
                val intent = Intent(this, ProductDetailsActivity::class.java)
                intent.putExtra("flag", Common.category)
                intent.putExtra("id", product.ID)
                startActivity(intent)
            },
            onFavoriteClick = { product, position, isFavorite ->
                addProductToFavorite(product.ID, isFavorite)
            }
        )
        adapter.setData(list, false, flag)
        binding.recycler.layoutManager = layoutManager
        binding.recycler.adapter = adapter
    }


}