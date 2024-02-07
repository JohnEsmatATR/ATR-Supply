package com.akhnaton.atrapp.ui.nav.home.product

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.akhnaton.atrapp.data.model.ProductModel
import com.akhnaton.atrapp.databinding.ActivityProductsBinding
import com.akhnaton.atrapp.shared.BaseActivity
import com.akhnaton.atrapp.shared.Common
import com.akhnaton.atrapp.ui.nav.favorite.FavoriteViewModel
import com.akhnaton.atrapp.ui.nav.home.ProductAdapter
import java.util.Locale

class ProductsActivity : BaseActivity() {
    lateinit var binding: ActivityProductsBinding
//    private val viewModel: ProductsViewModel by viewModels()
    private val favoriteViewModel: FavoriteViewModel by viewModels()
    private var products: List<ProductModel> = ArrayList()
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

        flag = intent.getStringExtra("flag") ?: ""
        id = intent.getIntExtra("id", -1)

        when (flag) {
            Common.category -> {
                getProductsBasedOnCategory(id)
            }

            Common.subBrand -> {
                getProductsBasedOnSubBrand(id)
            }

            Common.bestSeller -> {
                getProductsBestSeller()
            }
        }

        observeLogin()
        favoriteObserve()
        search()


        val list = ArrayList<ProductModel>()
        list.add(ProductModel(0, 0,0,0,"mmmm","mmmm", "mmmm","mmmm mmmm mmmm", "", "","",0.2,0.2,0.2,0.2,0.2,0.2,0,false,"", "", 0, 0,"","","","",0.2,0.2,"","",false))
        list.add(ProductModel(0, 0,0,0,"mmmm","mmmm", "mmmm","mmmm mmmm mmmm", "", "","",0.2,0.2,0.2,0.2,0.2,0.2,0,false,"", "", 0, 0,"","","","",0.2,0.2,"","",false))
        list.add(ProductModel(0, 0,0,0,"mmmm","mmmm", "mmmm","mmmm mmmm mmmm", "", "","",0.2,0.2,0.2,0.2,0.2,0.2,0,false,"", "", 0, 0,"","","","",0.2,0.2,"","",false))
        list.add(ProductModel(0, 0,0,0,"mmmm","mmmm", "mmmm","mmmm mmmm mmmm", "", "","",0.2,0.2,0.2,0.2,0.2,0.2,0,false,"", "", 0, 0,"","","","",0.2,0.2,"","",false))
        list.add(ProductModel(0, 0,0,0,"mmmm","mmmm", "mmmm","mmmm mmmm mmmm", "", "","",0.2,0.2,0.2,0.2,0.2,0.2,0,false,"", "", 0, 0,"","","","",0.2,0.2,"","",false))
        list.add(ProductModel(0, 0,0,0,"mmmm","mmmm", "mmmm","mmmm mmmm mmmm", "", "","",0.2,0.2,0.2,0.2,0.2,0.2,0,false,"", "", 0, 0,"","","","",0.2,0.2,"","",false))


        setupProductsRecycler(list)
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
                    if (product.name_en.lowercase(Locale.getDefault()).trim()
                            .contains(txt ?: "".lowercase(Locale.getDefault()).trim())
                        || product.name_ar.lowercase(Locale.getDefault()).trim()
                            .contains(txt ?: "".lowercase(Locale.getDefault()).trim())
                        || product.oracle_short_code.lowercase(Locale.getDefault()).trim()
                            .contains(txt ?: "".lowercase(Locale.getDefault()).trim())) {
                        (list as ArrayList).add(product)
                    }
                }
                setupProductsRecycler(list)
                return true
            }
        })
    }

    private fun observeLogin() {
//        lifecycleScope.launch {
//            viewModel.state.collect {
//                when (it) {
//                    is ProductsStatus.Idle ->  {
//                        Log.d(Common.KeroDebug, "observeProducts: Idle")
//                        binding.txtNoProducts.visibility = View.VISIBLE
//
//                    }
//                    is ProductsStatus.Loading -> {
//                        Log.d(Common.KeroDebug, "observeProducts: Loading")
//                        showProgressDialog(binding.progressLoading)
//                    }
//
//                    is ProductsStatus.GetProductsBasedOnCategory -> {
//                        if (it.data.status != -1) {
//                            hideProgressDialog(binding.progressLoading)
//                            if (it.data.data!!.products.isNotEmpty()){
//                                Log.d(Common.KeroDebug, "observeLogin: yes")
//                                binding.txtNoProducts.visibility = View.GONE
//                            }
//                            else {
//                                Log.d(Common.KeroDebug, "observeLogin: no")
//                                binding.txtNoProducts.visibility = View.VISIBLE
//                            }
//                            hideProgressDialog(binding.progressLoading)
//                            Log.d(Common.KeroDebug, "observeProducts: GetProductsBasedOnCategory")
//
//                            products = it.data.data!!.products
//                            setupProductsRecycler(it.data.data!!.products)
//                        } else if (it.data.status == 401) {
//                            hideProgressDialog(binding.progressLoading)
//                            hideProgressDialog(binding.progressLoading)
//                            onTokenExpired(it.data.errors!![0])
//
//                        } else {
//                            hideProgressDialog(binding.progressLoading)
//                            binding.txtNoProducts.visibility = View.VISIBLE
//
//                            showToastSnack(it.data.message, true)
//                        }
//                    }
//
//                    is ProductsStatus.GetProductsBasedOnSubBrand -> {
//                        if (it.data.status != -1) {
//                            if (it.data.data!!.products.isNotEmpty()){
//                                Log.d(Common.KeroDebug, "observeLogin: yes")
//                                binding.txtNoProducts.visibility = View.GONE
//                            }
//                            else {
//                                Log.d(Common.KeroDebug, "observeLogin: no")
//                                binding.txtNoProducts.visibility = View.VISIBLE
//                            }
//
//                            hideProgressDialog(binding.progressLoading)
//                            Log.d(Common.KeroDebug, "observeProducts: GetProductsBasedOnSubBrand")
//
//                            products = it.data.data!!.products
//                            setupProductsRecycler(it.data.data!!.products)
//                        } else if (it.data.status == 401) {
//                            binding.txtNoProducts.visibility = View.VISIBLE
//                            hideProgressDialog(binding.progressLoading)
//                            onTokenExpired(it.data.errors!![0])
//
//                        } else {
//                            hideProgressDialog(binding.progressLoading)
//                            binding.txtNoProducts.visibility = View.VISIBLE
//                            showToastSnack(it.data.message, true)
//                        }
//                    }
//
//                    is ProductsStatus.GetProductsBestSeller -> {
//                        if (it.data.status == 1) {
//                            hideProgressDialog(binding.progressLoading)
//                            if (it.data.data!!.products.isNotEmpty()){
//                                Log.d(Common.KeroDebug, "observeLogin: yes")
//                                binding.txtNoProducts.visibility = View.GONE
//                            }
//                            else {
//                                Log.d(Common.KeroDebug, "observeLogin: no")
//                                binding.txtNoProducts.visibility = View.VISIBLE
//                            }
//
//                            Log.d(Common.KeroDebug, "observeProducts: GetProductsBestSeller")
//
//                            products = it.data.data!!.products
//                            setupProductsRecycler(it.data.data!!.products)
//                        } else if (it.data.status == 401) {
//                            hideProgressDialog(binding.progressLoading)
//                            binding.txtNoProducts.visibility = View.VISIBLE
//                            onTokenExpired(it.data.errors!![0])
//
//                        } else {
//                            hideProgressDialog(binding.progressLoading)
//                            binding.txtNoProducts.visibility = View.VISIBLE
//                            showToastSnack(it.data.message, true)
//                        }
//                    }
//
//                    is ProductsStatus.Error -> {
//                        Log.d(Common.KeroDebug, "observeProducts Error: ${it.error.toString()}")
//                        hideProgressDialog(binding.progressLoading)
//                        binding.txtNoProducts.visibility = View.VISIBLE
//                        showToastSnack(it.error.toString(), true)
//                    }
//
//                }
//            }
//        }
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
//        lifecycleScope.launch {
//            viewModel.homeIntent.send(
//                ProductsIntent.GetProductsBasedOnCategory(
//                    "Bearer ${SharedPreferenceHelper.userToken}",
//                    categoryId,
//                )
//            )
//        }
    }

    private fun getProductsBasedOnSubBrand(subBrandId: Int) {
//        lifecycleScope.launch {
//            viewModel.homeIntent.send(
//                ProductsIntent.GetProductsBasedOnSubBrand(
//                    "Bearer ${SharedPreferenceHelper.userToken}",
//                    subBrandId,
//                )
//            )
//        }
    }

    private fun getProductsBestSeller() {
//        lifecycleScope.launch {
//            viewModel.homeIntent.send(
//                ProductsIntent.GetProductsBestSeller(
//                    "Bearer ${SharedPreferenceHelper.userToken}",
//                    0
//                )
//            )
//        }
    }


    private fun setupProductsRecycler(list: List<ProductModel>) {
        val layoutManager = GridLayoutManager(baseContext, 2)
        adapter = ProductAdapter(
            onClick = { product, position ->
//                val intent = Intent(baseContext, ProductDetailsActivity::class.java)
//                intent.putExtra("product", product)
//                startActivity(intent)
            },
            onFavoriteClick = { product, position, isFavorite ->
                addProductToFavorite(product.id, isFavorite)
            }
        )
        adapter.setData(list, false, flag)
        binding.recycler.layoutManager = layoutManager
        binding.recycler.adapter = adapter
    }


}