package com.akhnaton.atrapp.ui.nav.home.product.productDetails

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.data.model.ProductModel
import com.akhnaton.atrapp.data.model.ReviewModel
import com.akhnaton.atrapp.data.statuesValue.nav.cart.addToCart.AddToCartIntent
import com.akhnaton.atrapp.data.statuesValue.nav.cart.addToCart.AddToCartStatus
import com.akhnaton.atrapp.databinding.ActivityProductDetailsBinding
import com.akhnaton.atrapp.shared.BaseActivity
import com.akhnaton.atrapp.shared.Common
import com.akhnaton.atrapp.ui.nav.cart.AddToCartViewModel
import com.akhnaton.atrapp.ui.nav.home.ProductAdapter
import com.akhnaton.atrapp.ui.nav.home.reviews.ReviewActivity
import kotlinx.coroutines.launch

class ProductDetailsActivity : BaseActivity() {
    lateinit var binding: ActivityProductDetailsBinding
    private val addCartViewModel: AddToCartViewModel by viewModels()
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

    }

    private fun init() {
        binding.txtOldPrice.paintFlags =
            binding.txtOldPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        binding.layoutViewAllReviews.visibility = View.GONE
        binding.layoutReviews.visibility = View.GONE
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
        binding.txtAvailability.text = "${product.WEIGHT}"
        binding.txtDescription.text = "${product.DESCRIPTION}"

        addToCartObserve()
    }

    private fun onClick() {
        binding.btnBack.setOnClickListener {
            finish()
        }
        binding.layoutViewAllReviews.setOnClickListener {
            val intent = Intent(this@ProductDetailsActivity, ReviewActivity::class.java)
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
        lifecycleScope.launch {
            addCartViewModel.addToCartIntent.send(
                AddToCartIntent.AddProductToCart(
                    product.ID,
                    quantity,
                )
            )
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

}