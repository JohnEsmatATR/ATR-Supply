package com.akhnaton.atrapp.ui.nav.home.product.productDetails

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.data.model.ProductModel
import com.akhnaton.atrapp.data.model.review.ReviewModel
import com.akhnaton.atrapp.data.statuesValue.nav.cart.addToCart.AddToCartIntent
import com.akhnaton.atrapp.data.statuesValue.nav.cart.addToCart.AddToCartStatus
import com.akhnaton.atrapp.data.statuesValue.nav.home.favorite.FavoriteIntent
import com.akhnaton.atrapp.data.statuesValue.nav.home.favorite.FavoriteStatus
import com.akhnaton.atrapp.databinding.ActivityProductDetailsBinding
import com.akhnaton.atrapp.shared.BaseActivity
import com.akhnaton.atrapp.shared.Common
import com.akhnaton.atrapp.shared.SharedPreferenceHelper
import com.akhnaton.atrapp.ui.nav.cart.AddToCartViewModel
import com.akhnaton.atrapp.ui.nav.favorite.FavoriteViewModel
import com.akhnaton.atrapp.ui.nav.home.ProductAdapter
import com.akhnaton.atrapp.ui.nav.home.reviews.ReviewActivity
import kotlinx.coroutines.launch

class ProductDetailsActivity : BaseActivity() {
    lateinit var binding: ActivityProductDetailsBinding
    private val addCartViewModel: AddToCartViewModel by viewModels()
    private val favoriteViewModel :FavoriteViewModel by  viewModels()
    lateinit var productSuggestAdapter: ProductAdapter
    lateinit var reviewAdapter: ReviewAdapter
    lateinit var product: ProductModel
    var quantity: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        onClick()
        binding.btnBack.setOnClickListener {
            finish()
        }

        if (product.IS_LIKED) {
            binding.btnFavorite.load(R.drawable.ic_favorite_fill)
        } else {
            binding.btnFavorite.load(R.drawable.ic_favorite)
        }

        binding.btnFavorite.setOnClickListener {
            if (product.IS_LIKED) {
                binding.btnFavorite.load(R.drawable.ic_favorite)
                deleteProductToFavorite(product.ID, false)
            } else {
                binding.btnFavorite.load(R.drawable.ic_favorite_fill)
                addProductToFavorite(product.ID, true)
            }
            product.IS_LIKED = !product.IS_LIKED
        }


    }

    private fun init() {
        binding.txtOldPrice.paintFlags =
            binding.txtOldPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        binding.layoutReviews.visibility = View.VISIBLE
        binding.txtYouMightAlsoLike.visibility = View.GONE

        product = intent.getSerializableExtra("product") as ProductModel

        binding.imProduce.load(product.IMAGE_URL) {
            crossfade(true)
            placeholder(R.drawable.ic_logo)
            error(R.drawable.ic_logo)
        }
        binding.txtItemName.text = product.TITLE
        binding.txtPrice.text = "${product.PRICE_AFTER_DISCOUNT} LE"
        binding.txtOldPrice.text = "${product.PRICE_WITH_TAX} LE"
        binding.txtSize.text = "${product.WEIGHT}"
        binding.txtDescription.text = "${product.DESCRIPTION}"
        binding.isStock.apply {
            text = if (product.IN_STOCK) "In Stock" else "Out of Stock"
            setTextColor(
                ContextCompat.getColor(
                    context,
                    if (product.IN_STOCK) R.color.snack_green else R.color.snack_red
                )
            )
        }

        binding.btnBack.isEnabled = product.IN_STOCK




        addToCartObserve()
       // favoriteObserve()

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
//                    is FavoriteStatus.AddProductToFavourites -> {
//                        hideProgressDialog(binding.progressLoading)
//                        if (it.data.status == 200) {
//                            showToastSnack(it.data.message, false)
//
//                        } else if (it.data.status == 401) {
//
//                        } else {
//                            showToastSnack(it.data.message, true)
//                        }
//                    }
//
//                    is FavoriteStatus.GetFavorite -> {
//                        hideProgressDialog(binding.progressLoading)
//                        Log.d(Common.KeroDebug, "observeHome: GetProducts")
//
//                    }
//
//                    is FavoriteStatus.Error -> {
//                        Log.d(Common.KeroDebug, "observeHome Error: ${it.error.toString()}")
//                        hideProgressDialog(binding.progressLoading)
//                        showToastSnack(it.error.toString(), true)
//                    }
//
//                    is FavoriteStatus.DeleteProductToFavourites -> {
//                        hideProgressDialog(binding.progressLoading)
//                        if (it.data.status == 200) {
//                            showToastSnack(it.data.message, false)
//
//                        } else if (it.data.status == 401) {
//
//                        } else {
//                            showToastSnack(it.data.message, true)
//                        }
//                    }
//                }
//            }
//        }
//    }
    private fun onClick() {
        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.layoutViewAllReviews.setOnClickListener {
            val intent = Intent(this@ProductDetailsActivity, ReviewActivity::class.java)
            intent.putExtra("product", product)
            startActivity(intent)
        }
        binding.btnAddToCart.setOnClickListener {
            addProductToCart()
        }
        binding.btnPlus.setOnClickListener {
//            if (validateIncreaseQuantity(quantity, product.QUANTITY)) {
                quantity++
                binding.txtQuantity.setText(quantity.toString())

//            }
        }
        binding.btnMinus.setOnClickListener {
            if (validateDecreaseQuantity(quantity)) {
                quantity--
                binding.txtQuantity.setText(quantity.toString())
            }
        }

    }

    private fun addToCartObserve() {
        lifecycleScope.launch {
            addCartViewModel.state.collect {
                when (it) {
                    is AddToCartStatus.Idle -> Log.d(Common.KeroDebug, "observeHome: Idle")
                    is AddToCartStatus.Loading -> {
                        Log.d(Common.KeroDebug, "observeHome: Loading")
                        showProgressDialog(binding.progressLoading)
                    }

                    is AddToCartStatus.AddToCart -> {
                        if (it.data.status == 200) {
                            hideProgressDialog(binding.progressLoading)
                            Log.d(Common.KeroDebug, "observeHome: GetProducts")
                            showToastSnack(it.data.message, false)
                        } else {
                            hideProgressDialog(binding.progressLoading)
                            showToastSnack(it.data.message, true)
                        }

                    }

                    is AddToCartStatus.Error -> {
                        Log.d(Common.KeroDebug, "observeHome Error: ${it.error.toString()}")
                        hideProgressDialog(binding.progressLoading)
                        showToastSnack(it.error.toString(), true)
                    }
                }
            }
        }
    }

    private fun addProductToCart() {
        if (product.IN_STOCK){
            lifecycleScope.launch {
                addCartViewModel.addToCartIntent.send(AddToCartIntent.AddProductToCart(product.ID, quantity))
            }
        }else {
            showToastSnack("Product Out Of Stock", true)
        }

    }

    private fun setupProductSuggestRecycler(list: List<ProductModel>) {
        val layoutManager =
            LinearLayoutManager(baseContext, LinearLayoutManager.HORIZONTAL, false)
        productSuggestAdapter = ProductAdapter(
            onClick = { product, position ->
                val intent = Intent(baseContext, ProductDetailsActivity::class.java)
                intent.putExtra("flag", Common.category)
                intent.putExtra("product", product)
                startActivity(intent)
            },
            onFavoriteClick = { product, position, isFavorite ->
//                addProductToFavorite(product.id, isFavorite)
            }
        )
        productSuggestAdapter.setData(list, true, Common.bestSeller)
        binding.recyclerSuggest.layoutManager = layoutManager
        binding.recyclerSuggest.adapter = productSuggestAdapter
    }
    private fun setupReviewRecycler(list: List<ReviewModel>) {
        val layoutManager =
            LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false)
        reviewAdapter = ReviewAdapter(
            onClick = { product, position -> },
        )
        reviewAdapter.setData(list)
        binding.recyclerReviews.layoutManager = layoutManager
        binding.recyclerReviews.adapter = reviewAdapter
    }



    private fun addProductToFavorite(productId: Int, add: Boolean, ) {
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