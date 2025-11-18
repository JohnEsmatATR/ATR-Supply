package com.akhnaton.atrSupply.ui.nav.favorite

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.akhnaton.atrSupply.data.model.ProductModel
import com.akhnaton.atrSupply.data.statuesValue.nav.cart.addToCart.AddToCartIntent
import com.akhnaton.atrSupply.data.statuesValue.nav.cart.addToCart.AddToCartStatus
import com.akhnaton.atrSupply.data.statuesValue.nav.home.favorite.FavoriteIntent
import com.akhnaton.atrSupply.data.statuesValue.nav.home.favorite.FavoriteStatus
import com.akhnaton.atrSupply.databinding.FragmentFavoriteBinding
import com.akhnaton.atrSupply.shared.BaseFragment
import com.akhnaton.atrSupply.shared.Common
import com.akhnaton.atrSupply.shared.SharedPreferenceHelper
import com.akhnaton.atrSupply.shared.ShimmerAdapter
import com.akhnaton.atrSupply.ui.nav.home.ProductAdapter
import com.akhnaton.atrSupply.ui.nav.home.product.productDetails.ProductDetailsActivity
import com.akhnaton.atrSupply.ui.nav.cart.AddToCartViewModel
import kotlinx.coroutines.launch
import java.util.Locale

class FavoriteFragment : BaseFragment() {
    lateinit var binding: FragmentFavoriteBinding
    lateinit var adapter: ProductAdapter
    private var flag = ""
    private val favoriteViewModel: FavoriteViewModel by viewModels()
    private val addCartViewModel: AddToCartViewModel by viewModels()
    private var productList = mutableListOf<ProductModel>()
    private lateinit var shimmerAdapter: ShimmerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(inflater)

        favoriteObserve()
        search()
        init()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeAddToCart()
    }

    private fun setupProductsRecycler(list: List<ProductModel>) {
        productList.clear()
        productList.addAll(list)

        val layoutManager = GridLayoutManager(requireContext(), 2)
        adapter = ProductAdapter(
            onClick = { product, position, sharedView, transitionName ->
                val intent = Intent(requireContext(), ProductDetailsActivity::class.java).apply {
                    putExtra("flag", product.ITEM_TYPE)
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
            },
            onAddToCartClick = { product ->
                handleAddToCart(product)
            }
        )
        adapter.setData(productList, false, flag)
        binding.recycler.layoutManager = layoutManager
        binding.recycler.adapter = adapter
    }

    private fun observeAddToCart() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
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

    private fun handleAddToCart(product: ProductModel) {
        if (!product.IN_STOCK) {
            showToastSnack("Product Out Of Stock", true)
            return
        }

        val categoryForRequest = when {
            product.ITEM_TYPE.isNotEmpty() -> product.ITEM_TYPE
            flag.isNotEmpty() -> flag
            else -> null
        }

        if (categoryForRequest == null) {
            showToastSnack("Unable to add product to cart", true)
            return
        }

        viewLifecycleOwner.lifecycleScope.launch {
            addCartViewModel.addToCartIntent.send(
                AddToCartIntent.AddProductToCart(
                    productId = product.ID,
                    quantity = 1,
                    category = categoryForRequest
                )
            )
        }
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
                    "Pharma"
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
                    "Pharma"
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