package com.akhnaton.atrapp.ui.nav.home.product.productDetails

import android.annotation.SuppressLint
import android.app.ComponentCaller
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.data.model.ProductModel
import com.akhnaton.atrapp.data.model.review.ReviewModel
import com.akhnaton.atrapp.data.statuesValue.nav.cart.addToCart.AddToCartIntent
import com.akhnaton.atrapp.data.statuesValue.nav.cart.addToCart.AddToCartStatus
import com.akhnaton.atrapp.data.statuesValue.nav.home.bestSeller.BestSellerIntent
import com.akhnaton.atrapp.data.statuesValue.nav.home.category.CategoryIntent
import com.akhnaton.atrapp.data.statuesValue.nav.home.favorite.FavoriteIntent
import com.akhnaton.atrapp.data.statuesValue.nav.home.productDetails.ProductDetailsIntent
import com.akhnaton.atrapp.data.statuesValue.nav.home.productDetails.ProductDetailsStatus
import com.akhnaton.atrapp.databinding.ActivityProductDetailsBinding
import com.akhnaton.atrapp.shared.BaseActivity
import com.akhnaton.atrapp.shared.Common
import com.akhnaton.atrapp.shared.SharedPreferenceHelper
import com.akhnaton.atrapp.ui.nav.HomeActivity
import com.akhnaton.atrapp.ui.nav.cart.AddToCartViewModel
import com.akhnaton.atrapp.ui.nav.favorite.FavoriteViewModel
import com.akhnaton.atrapp.ui.nav.home.BestSellerViewModel
import com.akhnaton.atrapp.ui.nav.home.reviews.ReviewActivity
import kotlinx.coroutines.launch

class ProductDetailsActivity : BaseActivity() {
    lateinit var binding: ActivityProductDetailsBinding
    private val addCartViewModel: AddToCartViewModel by viewModels()
    private val favoriteViewModel :FavoriteViewModel by  viewModels()
    private val viewModel : ProductDetailsViewModel by viewModels()
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

        binding.btnGoToCart.setOnClickListener {
            val intent = Intent(this@ProductDetailsActivity, HomeActivity::class.java)
            intent.putExtra("open_cart", true)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

    }

    @SuppressLint("SuspiciousIndentation")
    private fun init() {

        binding.txtOldPrice.paintFlags =
            binding.txtOldPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        binding.layoutReviews.visibility = View.VISIBLE
        binding.txtYouMightAlsoLike.visibility = View.GONE

        product = intent.getSerializableExtra("product") as ProductModel
        val transitionName = intent.getStringExtra("transitionName")
   //     Log.d("TAG", "received product from intent: ${product}")


            ViewCompat.setTransitionName(binding.txtItemName, transitionName)
        binding.imProduce.load(product.IMAGE_URL) {
            crossfade(true)
            placeholder(R.drawable.ic_logo)
            error(R.drawable.ic_logo)
        }
        val productId = product.ID.toInt()
        //getBestSeller(productId)


//        binding.txtItemName.text = product.TITLE
//        binding.txtPrice.text = "${product.PRICE_AFTER_DISCOUNT} LE"
//        binding.txtOldPrice.text = "${product.PRICE_WITH_TAX} LE"
//        binding.txtSize.text = "${product.WEIGHT}"
//        binding.txtDescription.text = "${product.DESCRIPTION}"
//        binding.isStock.apply {
//            text = if (product.IN_STOCK) "In Stock" else "Out of Stock"
//            setTextColor(
//                ContextCompat.getColor(
//                    context,
//                    if (product.IN_STOCK) R.color.snack_green else R.color.snack_red
//                )
//            )
//        }




        observeProduct()
        getProductDetails(productId)
        addToCartObserve()
       // favoriteObserve()

    }


    private fun getProductDetails(productId: Int) {
        lifecycleScope.launch {
            viewModel.homeIntent.send(
                ProductDetailsIntent.GetProductDetails(productId)
            )
        }
    }

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
                            binding.btnGoToCart.visibility= View.VISIBLE
                            val animation = AnimationUtils.loadAnimation(this@ProductDetailsActivity, R.anim.slide_up)
                            binding.btnGoToCart.startAnimation(animation)
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
        val valiablity = binding.isStock.text.toString()
        if (valiablity == "In Stock"){
            lifecycleScope.launch {
                addCartViewModel.addToCartIntent.send(AddToCartIntent.AddProductToCart(product.ID, quantity))
            }
        }else {
            showToastSnack("Product Out Of Stock", true)
        }

    }

//   // private fun setupProductSuggestRecycler(list: List<ProductModel>) {
//        val layoutManager =
//            LinearLayoutManager(baseContext, LinearLayoutManager.HORIZONTAL, false)
//        productSuggestAdapter = ProductAdapter(
//            onClick = { product, position ->
//                val intent = Intent(baseContext, ProductDetailsActivity::class.java)
//                intent.putExtra("flag", Common.category)
//                intent.putExtra("product", product)
//                startActivity(intent)
//            },
//            onFavoriteClick = { product, position, isFavorite ->
//                if (isFavorite) {
//                    addProductToFavorite(product.ID, isFavorite)
//                } else {
//                    deleteProductToFavorite(product.ID, isFavorite)
//                }
//            }
//        )
//        productSuggestAdapter.setData(list, true, Common.bestSeller)
//        binding.recyclerSuggest.layoutManager = layoutManager
//        binding.recyclerSuggest.adapter = productSuggestAdapter
//    }
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

//    if (product.IS_LIKED) {
//        binding.btnFavorite.load(R.drawable.ic_favorite_fill)
//    } else {
//        binding.btnFavorite.load(R.drawable.ic_favorite)
//    }
//
//    binding.btnFavorite.setOnClickListener {
//        if (product.IS_LIKED) {
//            binding.btnFavorite.load(R.drawable.ic_favorite)
//            deleteProductToFavorite(product.ID, false)
//        } else {
//            binding.btnFavorite.load(R.drawable.ic_favorite_fill)
//            addProductToFavorite(product.ID, true)
//        }
//        product.IS_LIKED = !product.IS_LIKED
//    }
private fun observeProduct() {
    lifecycleScope.launch {
        viewModel.state.collect {
            when (it) {
                is ProductDetailsStatus.Error -> {
                    Toast.makeText(this@ProductDetailsActivity, "Error: ${it.error}", Toast.LENGTH_SHORT).show()
                    Log.d("TAG", "observeProduct: ${it.error}")
                }

                is ProductDetailsStatus.GetProductDetails -> {
                    if (it.data.status == 200) {
                        val productData = it.data.data?.firstOrNull()
                        if (productData != null) {
                            // عرض بيانات المنتج
                            binding.txtItemName.text = productData.TITLE
                            binding.txtPrice.text = "${productData.PRICE_AFTER_DISCOUNT} LE"
                            binding.txtOldPrice.text = "${productData.PRICE_WITH_TAX} LE"
                            binding.txtSize.text = productData.WEIGHT
                            binding.txtDescription.text = productData.DESCRIPTION

                            binding.isStock.text = if (productData.IN_STOCK) "In Stock" else "Out of Stock"
                            binding.isStock.setTextColor(
                                ContextCompat.getColor(
                                    binding.root.context,
                                    if (productData.IN_STOCK) R.color.snack_green else R.color.snack_red
                                )
                            )


                            binding.nestedScrollView.visibility = View.VISIBLE
                            binding.productNotFound.visibility = View.GONE
                        } else {

                            binding.nestedScrollView.visibility = View.GONE
                            binding.productNotFound.visibility = View.VISIBLE
                        }
                    }
                }


                ProductDetailsStatus.Idle -> {
                    Log.d(Common.KeroDebug, "observeProduct: Idle")
                }

                ProductDetailsStatus.Loading -> {
                    // يمكن تضيف شريط تحميل هنا
                }
            }
        }
    }
}




}