package com.akhnaton.atrapp.ui.nav.home.product.productDetails

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.data.model.AddToCartResponse
import com.akhnaton.atrapp.data.model.ProductModel
import com.akhnaton.atrapp.data.model.common.BaseModel
import com.akhnaton.atrapp.data.statuesValue.nav.cart.addToCart.AddToCartIntent
import com.akhnaton.atrapp.data.statuesValue.nav.cart.addToCart.AddToCartStatus
import com.akhnaton.atrapp.data.statuesValue.nav.home.productDetails.ProductDetailsIntent
import com.akhnaton.atrapp.data.statuesValue.nav.home.productDetails.ProductDetailsStatus
import com.akhnaton.atrapp.databinding.ActivityProductDetailsBinding
import com.akhnaton.atrapp.shared.BaseActivity
import com.akhnaton.atrapp.shared.Common
import com.akhnaton.atrapp.shared.SharedPreferenceHelper
import com.akhnaton.atrapp.ui.nav.HomeActivity
import com.akhnaton.atrapp.ui.nav.cart.AddToCartViewModel
import com.akhnaton.atrapp.ui.nav.home.reviews.ReviewActivity
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

class ProductDetailsActivity : BaseActivity() {
    lateinit var binding: ActivityProductDetailsBinding
    private val addCartViewModel: AddToCartViewModel by viewModels()
    private val viewModel : ProductDetailsViewModel by viewModels()
    lateinit var product: ProductModel
    var quantity: Int = 1
    private var productQuantity : Int=0
    private lateinit var flag: String
    private var inStoke : Boolean = false
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
        val lan = SharedPreferenceHelper.language ?: "en"
        if (lan == "ar"){
            binding.ar.visibility = View.VISIBLE
            binding.en.visibility = View.GONE
        }else{
            binding.ar.visibility = View.GONE
            binding.en.visibility = View.VISIBLE
        }
        setupQuantityEditText()

        flag = intent.getStringExtra("flag") ?: ""

        binding.layoutReviews.visibility = View.GONE
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
        val lan = SharedPreferenceHelper.language ?: "en"
        bonusAdapter = BonusAdapter(lan)
        binding.bonusRecycler.apply {
            adapter = bonusAdapter
            layoutManager = LinearLayoutManager(this@ProductDetailsActivity)

        }
        binding.bonusRecyclerAr.apply {
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
                                // en
                                binding.txtItemName.text = productData.TITLE
                                binding.txtPrice.text = "${productData.PRICE_AFTER_DISCOUNT} LE"
                                binding.txtSize.text = productData.WEIGHT
                                binding.txtDescription.text = productData.DESCRIPTION
                                productQuantity = productData.QUANTITY
                                Log.d("TAG", "observeProduct productQuantity : $productQuantity")
                                if (productData.MY_QUANTITY != 0){
                                    binding.txtQuantity.setText(productData.MY_QUANTITY.toString())
                                    binding.txtQuantityAr.setText(productData.MY_QUANTITY.toString())
                                }else{
                                    binding.txtQuantity.setText("1")
                                    binding.txtQuantityAr.setText("1")
                                }
                                inStoke = productData.IN_STOCK


                                binding.isStock.text = if (productData.IN_STOCK) {
                                    getString(R.string.in_stock)
                                } else {
                                    getString(R.string.out_of_stock)
                                }

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

                                // ar
                                binding.txtItemNameAr.text = productData.TITLE
                                binding.txtPriceAr.text = "${productData.PRICE_AFTER_DISCOUNT} LE"
                                binding.txtSizeAr.text = productData.WEIGHT
                                binding.txtDescriptionAr.text = productData.DESCRIPTION
                                productQuantity = productData.QUANTITY
                                Log.d("TAG", "observeProduct productQuantity : $productQuantity")


                                binding.isStockAr.text = if (productData.IN_STOCK) "In Stock" else "Out of Stock"
                                binding.isStockAr.setTextColor(
                                    ContextCompat.getColor(
                                        binding.root.context,
                                        if (productData.IN_STOCK) R.color.snack_green else R.color.snack_red
                                    )
                                )
                                binding.txtQuantityAr.isEnabled=productData.IN_STOCK


                                val bonusListAr = productData.BONUS_DATA
                                if (!bonusListAr.isNullOrEmpty()) {
                                    binding.bonusRecyclerAr.visibility = View.VISIBLE
                                    bonusAdapter.setData(bonusListAr)
                                } else {
                                    binding.bonusRecyclerAr.visibility = View.GONE
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

        binding.btnPlusAr.setOnClickListener {
            if (validateIncreaseQuantity(quantity, productQuantity)) {
                quantity++
                Log.d("TAG", "onClick QUANTITY :${productQuantity} ")
                binding.txtQuantityAr.setText(quantity.toString())

            }
        }
        binding.btnMinusAr.setOnClickListener {
            if (validateDecreaseQuantity(quantity)) {
                quantity--
                binding.txtQuantityAr.setText(quantity.toString())
            }
        }
        binding.btnAddToCartAr.setOnClickListener {
            addProductToCartAr()
        }

    }

    private fun addToCartObserve() {
        lifecycleScope.launch {
            addCartViewModel.state.collect { it ->
                when (it) {
                    is AddToCartStatus.Idle -> Log.d(Common.KeroDebug, "observeHome: Idle")

                    is AddToCartStatus.Loading -> {
                        Log.d(Common.KeroDebug, "observeHome: Loading")
                        showProgressDialog(binding.progressLoading)
                    }

                    is AddToCartStatus.AddToCart -> {
                        hideProgressDialog(binding.progressLoading)
                        if (it.data.status == 200) {
                            Log.d(Common.KeroDebug, "observeHome: AddToCart Success")

                            showAddToCartDialog(it.data)

                            // ✅ إظهار زر الذهاب للكارت
                           // binding.btnGoToCart.visibility = View.VISIBLE
                            val animation = AnimationUtils.loadAnimation(this@ProductDetailsActivity, R.anim.slide_up)
                          //  binding.btnGoToCart.startAnimation(animation)

                        } else {
                            showToastSnack(it.data.message, true)
                        }
                    }

                    is AddToCartStatus.Error -> {
                        Log.d(Common.KeroDebug, "observeHome Error: ${it.error}")
                        hideProgressDialog(binding.progressLoading)
                        showToastSnack(it.error.toString(), true)
                    }
                }
            }
        }
    }

    @SuppressLint("StringFormatMatches")
    private fun showAddToCartDialog(response: BaseModel<AddToCartResponse>) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_go_to_cart, null)

        val txtTitle = dialogView.findViewById<TextView>(R.id.txtTitle)
        val txtMessage = dialogView.findViewById<TextView>(R.id.txtMessage)
        val txtBonus = dialogView.findViewById<TextView>(R.id.txtBonus)
        val btnContinue = dialogView.findViewById<MaterialButton>(R.id.btnContinue)
        val btnGoToCart = dialogView.findViewById<MaterialButton>(R.id.btnGoToCart)

        txtTitle.text = getString(R.string.cart_added_title)
        txtMessage.text = getString(R.string.cart_added_message)

        // ✅ لو في bonus_quantity أكبر من 0، نظهر رسالة التهنئة
        val bonusQty = response.data?.bonus_quantity ?: 0
        if (bonusQty > 0) {
            txtBonus.visibility = View.VISIBLE
            txtBonus.text = getString(R.string.cart_bonus_message, bonusQty)

        } else {
            txtBonus.visibility = View.GONE
        }

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        btnContinue.setOnClickListener {
            dialog.dismiss()
            finish()
        }

        btnGoToCart.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("open_cart", true)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        dialog.show()
    }


    private fun addProductToCart() {
        if (inStoke){
            lifecycleScope.launch {
                addCartViewModel.addToCartIntent.send(AddToCartIntent.AddProductToCart(product.ID, quantity,flag))
            }
        }else {
            showToastSnack("Product Out Of Stock", true)
        }

    }
    private fun addProductToCartAr() {
        if (inStoke){
            lifecycleScope.launch {
                addCartViewModel.addToCartIntent.send(AddToCartIntent.AddProductToCart(product.ID, quantity.toInt(),flag))
            }
        }else {
            showToastSnack("Product Out Of Stock", true)
        }

    }


    @SuppressLint("SetTextI18n")
    private fun setupQuantityEditText() {
        binding.txtQuantity.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(4))
        binding.txtQuantityAr.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(4))

        var isUpdating = false

        // txtQuantity
        binding.txtQuantity.doOnTextChanged { text, _, _, _ ->
            if (isUpdating) return@doOnTextChanged

            val value = text.toString().toIntOrNull()
            if (value != null) {
                when {
                    value < 1 -> {
                        isUpdating = true
                        quantity = 1
                        binding.txtQuantity.setText("1")
                        binding.txtQuantity.setSelection(binding.txtQuantity.text!!.length)
                        isUpdating = false
                    }
                    value > productQuantity -> {
                        isUpdating = true
                        quantity = productQuantity
                        binding.txtQuantity.setText(productQuantity.toString())
                        binding.txtQuantity.setSelection(binding.txtQuantity.text!!.length)
                        isUpdating = false
                    }
                    else -> {
                        quantity = value
                    }
                }
            } else {
                quantity = 0
            }
        }

        binding.txtQuantity.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus && binding.txtQuantity.text.isNullOrEmpty()) {
                isUpdating = true
                quantity = 1
                binding.txtQuantity.setText("1")
                isUpdating = false
            }
        }

        // txtQuantityAr
        binding.txtQuantityAr.doOnTextChanged { text, _, _, _ ->
            if (isUpdating) return@doOnTextChanged

            val value = text.toString().toIntOrNull()
            if (value != null) {
                when {
                    value < 1 -> {
                        isUpdating = true
                        quantity = 1
                        binding.txtQuantityAr.setText("1")
                        binding.txtQuantityAr.setSelection(binding.txtQuantityAr.text!!.length)
                        isUpdating = false
                    }
                    value > productQuantity -> {
                        isUpdating = true
                        quantity = productQuantity
                        binding.txtQuantityAr.setText(productQuantity.toString())
                        binding.txtQuantityAr.setSelection(binding.txtQuantityAr.text!!.length)
                        isUpdating = false
                    }
                    else -> {
                        quantity = value
                    }
                }
            } else {
                quantity = 0
            }
        }

        binding.txtQuantityAr.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus && binding.txtQuantityAr.text.isNullOrEmpty()) {
                isUpdating = true
                quantity = 1
                binding.txtQuantityAr.setText("1")
                isUpdating = false
            }
        }
    }





}