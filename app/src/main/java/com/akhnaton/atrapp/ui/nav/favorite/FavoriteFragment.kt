package com.akhnaton.atrapp.ui.nav.favorite

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.akhnaton.atrapp.data.model.ProductModel
import com.akhnaton.atrapp.data.statuesValue.nav.home.favorite.FavoriteIntent
import com.akhnaton.atrapp.data.statuesValue.nav.home.favorite.FavoriteStatus
import com.akhnaton.atrapp.databinding.FragmentFavoriteBinding
import com.akhnaton.atrapp.shared.BaseFragment
import com.akhnaton.atrapp.shared.Common
import com.akhnaton.atrapp.ui.nav.home.ProductAdapter
import com.akhnaton.atrapp.ui.nav.home.product.productDetails.ProductDetailsActivity
import kotlinx.coroutines.launch

class FavoriteFragment : BaseFragment() {
    lateinit var binding: FragmentFavoriteBinding
    private var products: List<ProductModel> = ArrayList()
    lateinit var adapter: ProductAdapter
    private var flag = ""
    private val favoriteViewModel: FavoriteViewModel by viewModels()
//    private val addToCartViewModel: AddToCartViewModel by viewModels()
//    lateinit var favoriteAdapter: FavoriteAdapter
//    lateinit var products: List<ProductModel>
//    lateinit var productsUnFavorite: List<ProductModel>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater)

        favoriteObserve()
        addToCartObserve()

        return binding.root
    }

    private fun setupProductsRecycler(list: List<ProductModel>) {
        val layoutManager = GridLayoutManager(requireContext(), 2)
        adapter = ProductAdapter(
            onClick = { product, position ->
                val intent = Intent(requireContext(), ProductDetailsActivity::class.java)
                intent.putExtra("flag", Common.category)
                intent.putExtra("product", product)
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

    override fun onResume() {
        super.onResume()

        init()

    }

    private fun init() {
        getMyFavorite()
    }


    private fun favoriteObserve() {
        lifecycleScope.launch {
            favoriteViewModel.state.collect {
                when (it) {
                    is FavoriteStatus.Idle -> Log.d(Common.KeroDebug, "observeHome: Idle")
                    is FavoriteStatus.Loading -> {
                        Log.d(Common.KeroDebug, "observeHome: Loading")
                        showProgressDialog(binding.progressLoading)
                    }

                    is FavoriteStatus.GetFavorite -> {
                        if (it.data.status == 200) {
                            hideProgressDialog(binding.progressLoading)
                            Log.d(Common.KeroDebug, "observeHome: GetProducts")
                            setupProductsRecycler(it.data.data!!)

                        } else if (it.data.status == 401) {
                            hideProgressDialog(binding.progressLoading)
//                            onTokenExpired(it.data.errors!![0])

                        } else {
                            hideProgressDialog(binding.progressLoading)
                            showToastSnack(it.data.message, true)
                        }

                    }


                    is FavoriteStatus.Error -> {
                        Log.d(Common.KeroDebug, "observeHome Error: ${it.error.toString()}")
                        hideProgressDialog(binding.progressLoading)
                        showToastSnack(it.error.toString(), true)
                    }

                }
            }
        }
    }

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
                FavoriteIntent.GetFavorite(getVersion())
            )
        }
    }

    private fun addProductToFavorite(
        productId: Int,
        add: Boolean,
    ) {
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

}