package com.akhnaton.atrapp.ui.nav.favorite

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.akhnaton.atrapp.data.model.ProductModel
import com.akhnaton.atrapp.data.statuesValue.nav.home.favorite.FavoriteIntent
import com.akhnaton.atrapp.data.statuesValue.nav.home.favorite.FavoriteStatus
import com.akhnaton.atrapp.databinding.FragmentFavoriteBinding
import com.akhnaton.atrapp.shared.BaseFragment
import com.akhnaton.atrapp.shared.Common
import com.akhnaton.atrapp.shared.SharedPreferenceHelper
import com.akhnaton.atrapp.shared.ShimmerAdapter
import com.akhnaton.atrapp.ui.nav.home.ProductAdapter
import com.akhnaton.atrapp.ui.nav.home.product.productDetails.ProductDetailsActivity
import kotlinx.coroutines.launch
import java.util.Locale

class FavoriteFragment : BaseFragment() {
    lateinit var binding: FragmentFavoriteBinding
    private var products: List<ProductModel> = ArrayList()
    lateinit var adapter: ProductAdapter
    private var flag = ""
    private val favoriteViewModel: FavoriteViewModel by viewModels()
    private var productList = mutableListOf<ProductModel>()
    private lateinit var shimmerAdapter: ShimmerAdapter

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
        search()
        init()

        return binding.root
    }

    private fun setupProductsRecycler(list: List<ProductModel>) {
        productList.clear()
        productList.addAll(list)

        val layoutManager = GridLayoutManager(requireContext(), 2)
        adapter = ProductAdapter(
            onClick = { product, position, sharedView, transitionName ->
                val intent = Intent(requireContext(), ProductDetailsActivity::class.java).apply {
                    putExtra("flag", Common.category)
                    putExtra("product", product)
                    putExtra("transitionName", transitionName)
                }

                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    requireActivity(),
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
        adapter.setData(productList, false, flag)
        binding.recycler.layoutManager = layoutManager
        binding.recycler.adapter = adapter
    }


    override fun onResume() {
        super.onResume()

        //init()

    }

    private fun init() {
        shimmerAdapter = ShimmerAdapter(10)

        binding.recycler.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = shimmerAdapter
        }
        getMyFavorite()
    }


    private fun favoriteObserve() {
        lifecycleScope.launch {
            favoriteViewModel.state.collect {
                when (it) {
                    is FavoriteStatus.Idle -> Log.d(Common.KeroDebug, "observeHome: Idle")
                    is FavoriteStatus.Loading -> {
                        Log.d(Common.KeroDebug, "observeHome: Loading")
                        //showProgressDialog(binding.progressLoading)
                        binding.recycler.adapter = shimmerAdapter
                    }

                    is FavoriteStatus.GetFavorite -> {
                        if (it.data.status == 200) {
                            hideProgressDialog(binding.progressLoading)
                            Log.d(Common.KeroDebug, "observeHome: GetProducts")
                            setupProductsRecycler(it.data.data!!)
                            Log.d(Common.KeroDebug, "favoriteObserve: ${it.data.data!!.size}")

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

                    is FavoriteStatus.AddProductToFavourites -> TODO()
                    is FavoriteStatus.DeleteProductToFavourites -> {
                        hideProgressDialog(binding.progressLoading)

                        if (it.data.status == 200) {
                            Log.d(Common.KeroDebug, "observeHome: Product deleted from favorites")
                            //showToastSnack(it.data.message, false)

                            productList.removeIf { product -> product.ID == it.productId }

                            if (binding.recycler.adapter != adapter) {
                                binding.recycler.adapter = adapter
                            }

                            adapter.setData(productList, false, flag)

                        } else if (it.data.status == 401) {
                            // onTokenExpired(it.data.errors!![0])
                        } else {
                           // showToastSnack(it.data.message, true)
                        }
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


    private fun search() {
        binding.txtSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(txt: String?): Boolean {
                val query = txt?.lowercase(Locale.getDefault())?.trim() ?: ""
                val filteredList = if (query.isEmpty()) {
                    productList
                } else {
                    productList.filter { product ->
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