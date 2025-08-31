package com.akhnaton.atrapp.ui.nav.home.product.productDetails

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.data.model.ProductModel
import com.akhnaton.atrapp.data.statuesValue.nav.cart.addToCart.AddToCartIntent
import com.akhnaton.atrapp.data.statuesValue.nav.cart.addToCart.AddToCartStatus
import com.akhnaton.atrapp.data.statuesValue.nav.home.productDetails.ProductDetailsIntent
import com.akhnaton.atrapp.data.statuesValue.nav.home.productDetails.ProductDetailsStatus
import com.akhnaton.atrapp.databinding.ActivityProductDetailsBinding
import com.akhnaton.atrapp.shared.BaseActivity
import com.akhnaton.atrapp.shared.Common
import com.akhnaton.atrapp.ui.nav.HomeActivity
import com.akhnaton.atrapp.ui.nav.cart.AddToCartViewModel
import com.akhnaton.atrapp.ui.nav.favorite.FavoriteViewModel
import com.akhnaton.atrapp.ui.nav.home.reviews.ReviewActivity
import kotlinx.coroutines.launch

class ProductDetailsActivity : BaseActivity() {
    lateinit var binding: ActivityProductDetailsBinding
    private val addCartViewModel: AddToCartViewModel by viewModels()
    private val favoriteViewModel :FavoriteViewModel by  viewModels()
    private val viewModel : ProductDetailsViewModel by viewModels()
    lateinit var product: ProductModel
    var quantity: Int = 1
    private var productQuantity : Int=0
    private lateinit var flag: String
    private lateinit var bonusAdapter: BonusAdapter
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
            intent.putExtra("from_product_details", true)
            intent.putExtra("product", product)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }



    }

    @SuppressLint("SuspiciousIndentation")
    private fun init() {
        setupQuantityEditText()

        flag = intent.getStringExtra("flag") ?: ""

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

        observeProduct()
        getProductDetails(productId,flag)
        addToCartObserve()
        initBonusRecycler()
    }

    private fun initBonusRecycler() {
        bonusAdapter = BonusAdapter()
        binding.bonusRecycler.apply {
            adapter = bonusAdapter
            layoutManager = LinearLayoutManager(this@ProductDetailsActivity)

        }
    }
    @SuppressLint("SetTextI18n")
    private fun observeProduct() {
        lifecycleScope.launch {
            viewModel.state.collect {
                when (it) {
                    is ProductDetailsStatus.Error -> {
                        hideProgressDialog(binding.progressLoading)
                        Toast.makeText(this@ProductDetailsActivity, "Error: ${it.error}", Toast.LENGTH_SHORT).show()
                        Log.d("TAG", "observeProduct: ${it.error}")
                    }

                    is ProductDetailsStatus.GetProductDetails -> {
                        if (it.data.status == 200) {
                            hideProgressDialog(binding.progressLoading)
                            val productData = it.data.data?.firstOrNull()

                            if (productData != null) {

                                binding.txtItemName.text = productData.TITLE
                                binding.txtPrice.text = "${productData.PRICE_AFTER_DISCOUNT} LE"
                                binding.txtOldPrice.text = "${productData.PRICE_WITH_TAX} LE"
                                binding.txtSize.text = productData.WEIGHT
                                binding.txtDescription.text = productData.DESCRIPTION
                                productQuantity = productData.QUANTITY
                                Log.d("TAG", "observeProduct productQuantity : $productQuantity")


                                binding.isStock.text = if (productData.IN_STOCK) "In Stock" else "Out of Stock"
                                binding.isStock.setTextColor(
                                    ContextCompat.getColor(
                                        binding.root.context,
                                        if (productData.IN_STOCK) R.color.snack_green else R.color.snack_red
                                    )
                                )
                                binding.txtQuantity.isEnabled=productData.IN_STOCK


                                val bonusList = productData.BONUS_DATA
                                if (!bonusList.isNullOrEmpty()) {
                                    binding.bonusRecycler.visibility = View.VISIBLE
                                    bonusAdapter.setData(bonusList)
                                } else {
                                    binding.bonusRecycler.visibility = View.GONE
                                }



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
                        showProgressDialog(binding.progressLoading)
                    }
                }
            }
        }
    }

    private fun getProductDetails(productId: Int,category: String) {
        lifecycleScope.launch {
            viewModel.homeIntent.send(
                ProductDetailsIntent.GetProductDetails(productId,category)
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
           if (validateIncreaseQuantity(quantity, productQuantity)) {
                quantity++
               Log.d("TAG", "onClick QUANTITY :${productQuantity} ")
                binding.txtQuantity.setText(quantity.toString())

           }
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
                addCartViewModel.addToCartIntent.send(AddToCartIntent.AddProductToCart(product.ID, quantity,flag))
            }
        }else {
            showToastSnack("Product Out Of Stock", true)
        }

    }

    @SuppressLint("SetTextI18n")
    private fun setupQuantityEditText() {
        binding.txtQuantity.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(4))

        // أثناء الكتابة
        binding.txtQuantity.doOnTextChanged { text, _, _, _ ->
            val value = text.toString().toIntOrNull()

            if (value != null) {
                when {
                    value < 1 -> {
                        quantity = 1
                        binding.txtQuantity.setText("1")
                        binding.txtQuantity.setSelection(binding.txtQuantity.text!!.length)
                    }
                    value > productQuantity -> {
                        quantity = productQuantity
                        binding.txtQuantity.setText(productQuantity.toString())
                        binding.txtQuantity.setSelection(binding.txtQuantity.text!!.length)
                    }
                    else -> {
                        quantity = value
                    }
                }
            } else {
                // هنا المستخدم مسح القيمة، هنسيبها فاضية مؤقتًا
                quantity = 0
            }
        }

        // أول ما يسيب EditText (يفقد التركيز)
        binding.txtQuantity.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                if (binding.txtQuantity.text.isNullOrEmpty()) {
                    quantity = 1
                    binding.txtQuantity.setText("1")
                }
            }
        }
    }


}