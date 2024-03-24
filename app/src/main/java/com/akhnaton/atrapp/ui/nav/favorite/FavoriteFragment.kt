package com.akhnaton.atrapp.ui.nav.favorite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.akhnaton.atrapp.data.model.ProductModel
import com.akhnaton.atrapp.databinding.FragmentFavoriteBinding
import com.akhnaton.atrapp.shared.BaseFragment
import com.akhnaton.atrapp.shared.Common
import com.akhnaton.atrapp.ui.nav.home.ProductAdapter
import com.akhnaton.atrapp.ui.nav.home.product.productDetails.ProductDetailsActivity

class FavoriteFragment : BaseFragment() {
    lateinit var binding: FragmentFavoriteBinding
    private var products: List<ProductModel> = ArrayList()
    lateinit var adapter: ProductAdapter
    private var flag = ""
//    private val favoriteViewModel: FavoriteViewModel by viewModels()
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
        fillList()

        return binding.root
    }



    private fun fillList(){
        val productName = "Eva Hair clinic - Gold Argan - Triple defense"
        val list = ArrayList<ProductModel>()
        list.add(ProductModel(0, 0,0,0,productName,productName, productName,productName + productName, "", "","",0.2,0.2,0.2,0.2,0.2,0.2,0,false,"", "", 0, 0,"","","","",0.2,0.2,"","",false))
        list.add(ProductModel(0, 0,0,0,productName,productName, productName,productName + productName, "", "","",0.2,0.2,0.2,0.2,0.2,0.2,0,false,"", "", 0, 0,"","","","",0.2,0.2,"","",false))
        list.add(ProductModel(0, 0,0,0,productName,productName, productName,productName + productName, "", "","",0.2,0.2,0.2,0.2,0.2,0.2,0,false,"", "", 0, 0,"","","","",0.2,0.2,"","",false))
        list.add(ProductModel(0, 0,0,0,productName,productName, productName,productName + productName, "", "","",0.2,0.2,0.2,0.2,0.2,0.2,0,false,"", "", 0, 0,"","","","",0.2,0.2,"","",false))
        list.add(ProductModel(0, 0,0,0,productName,productName, productName,productName + productName, "", "","",0.2,0.2,0.2,0.2,0.2,0.2,0,false,"", "", 0, 0,"","","","",0.2,0.2,"","",false))
        list.add(ProductModel(0, 0,0,0,productName,productName, productName,productName + productName, "", "","",0.2,0.2,0.2,0.2,0.2,0.2,0,false,"", "", 0, 0,"","","","",0.2,0.2,"","",false))

        setupProductsRecycler(list)
    }


    private fun setupProductsRecycler(list: List<ProductModel>) {
        val layoutManager = GridLayoutManager(requireContext(), 2)
        adapter = ProductAdapter(
            onClick = { product, position ->
                val intent = Intent(requireContext(), ProductDetailsActivity::class.java)
                intent.putExtra("flag", Common.category)
                intent.putExtra("id", product.id)
                startActivity(intent)
            },
            onFavoriteClick = { product, position, isFavorite ->
                addProductToFavorite(product.id, isFavorite)
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
//                            setupFavoriteRecycler(productsUnFavorite)
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
//                    is FavoriteStatus.GetMyFavourites -> {
//                        if (it.data.status == 1) {
//                            hideProgressDialog(binding.progressLoading)
//                            Log.d(Common.KeroDebug, "observeHome: GetProducts")
//                            products = it.data.data!!
//                            setupFavoriteRecycler(it.data.data!!)
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
//
//                    is FavoriteStatus.Error -> {
//                        Log.d(Common.KeroDebug, "observeHome Error: ${it.error.toString()}")
//                        hideProgressDialog(binding.progressLoading)
//                        showToastSnack(it.error.toString(), true)
//                    }
//
//                }
//            }
//        }
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
//        lifecycleScope.launch {
//            favoriteViewModel.favoriteIntent.send(
//                FavoriteIntent.GetMyFavourites(
//                    "Bearer ${SharedPreferenceHelper.userToken}",
//                )
//            )
//        }
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