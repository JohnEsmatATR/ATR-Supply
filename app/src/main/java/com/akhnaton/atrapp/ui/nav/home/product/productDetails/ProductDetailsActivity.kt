package com.akhnaton.atrapp.ui.nav.home.product.productDetails

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.View
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
    private val viewModel: ProductDetailsViewModel by viewModels()
    lateinit var product: ProductModel
    var quantity: Int = 1
    private var productQuantity: Int = 0
    private lateinit var flag: String
    private var inStoke: Boolean = false
    private lateinit var bonusAdapter: BonusAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        onClick()
    }

    @SuppressLint("SuspiciousIndentation")
    private fun init() {
        setupQuantityEditText()

        flag = intent.getStringExtra("flag") ?: ""

        binding.layoutReviews.visibility = View.GONE
        binding.txtYouMightAlsoLike.visibility = View.GONE

        product = intent.getSerializableExtra("product") as ProductModel
        val transitionName = intent.getStringExtra("transitionName")

        ViewCompat.setTransitionName(binding.txtItemName, transitionName)
        binding.imProduce.load(product.IMAGE_URL) {
            crossfade(true)
            placeholder(R.drawable.ic_logo)
            error(R.drawable.ic_logo)
        }
        val productId = product.ID.toInt()

        val isArabic = SharedPreferenceHelper.language == "ar"
        if (isArabic) binding.btnBack.setImageResource(R.drawable.ic_back_ar)
        else binding.btnBack.setImageResource(R.drawable.ic_back)

        observeProduct()
        getProductDetails(productId, flag)
        addToCartObserve()
        initBonusRecycler()
    }

    private fun initBonusRecycler() {
        val lan = SharedPreferenceHelper.language ?: "ar"
        bonusAdapter = BonusAdapter(lan)

        binding.bonusRecycler.apply {
            adapter = bonusAdapter
            layoutManager = LinearLayoutManager(this@ProductDetailsActivity)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun observeProduct() {
        lifecycleScope.launch {
            viewModel.state.collect { status ->
                when (status) {
                    is ProductDetailsStatus.Error -> {
                        hideProgressDialog(binding.progressLoading)
                        Toast.makeText(this@ProductDetailsActivity, "Error: ${status.error}", Toast.LENGTH_SHORT).show()
                        Log.d("TAG", "observeProduct: ${status.error}")
                    }

                    is ProductDetailsStatus.GetProductDetails -> {
                        if (status.data.status == 200) {
                            hideProgressDialog(binding.progressLoading)
                            val productData = status.data.data?.firstOrNull()
                            Log.d("DEBUG_PRODUCT", "Returned Weight = '${productData?.WEIGHT}' | Returned Stock = ${productData?.IN_STOCK}")

                            if (productData != null) {
                                binding.txtItemName.text = productData.TITLE ?: ""
                                binding.txtCategory.text = productData.category?.TITLE ?: ""
                                binding.txtPriceValue.text = "${productData.PRICE_AFTER_DISCOUNT ?: 0.0}"
                                // 1. Dosage / Size Handling from API
                                val dosageText = productData.WEIGHT
                                if (!dosageText.isNullOrEmpty()) {
                                    binding.txtSize.text = dosageText
                                } else {
                                    binding.txtSize.text = "N/A"
                                }

                                binding.txtDescription.text = productData.DESCRIPTION ?: ""
                                productQuantity = productData.QUANTITY

                                // Quantity Setup
                                if (productData.MY_QUANTITY != 0) {
                                    binding.txtQuantity.setText(productData.MY_QUANTITY.toString())
                                    quantity = productData.MY_QUANTITY
                                } else {
                                    binding.txtQuantity.setText("1")
                                    quantity = 1
                                }
                                // 2. Stock Handling from API (True / False)
                                inStoke = productData.IN_STOCK
                                binding.isStock.text = if (inStoke) {
                                    getString(R.string.in_stock)
                                } else {
                                    getString(R.string.out_of_stock)
                                }

                                binding.isStock.setTextColor(
                                    ContextCompat.getColor(
                                        binding.root.context,
                                        if (inStoke) R.color.snack_green else R.color.snack_red
                                    )
                                )

                                // Disable/Enable buttons according to stock
                                binding.btnPlus.isEnabled = inStoke
                                binding.btnMinus.isEnabled = inStoke
                                binding.btnAddToCart.isEnabled = inStoke

                                // Bonus Recycler
                                val bonusList = productData.BONUS_DATA
                                if (!bonusList.isNullOrEmpty()) {
                                    binding.bonusRecycler.visibility = View.VISIBLE
                                    bonusAdapter.setData(bonusList)
                                } else {
                                    binding.bonusRecycler.visibility = View.GONE
                                }

                                binding.nestedScrollView.visibility = View.VISIBLE
                                binding.productNotFound.visibility = View.GONE

                                // Update Total Price Bottom
                                updateTotalPrice(productData.PRICE_AFTER_DISCOUNT)

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

    private fun updateTotalPrice(pricePerUnit: Double? = null) {
        val unitPrice = pricePerUnit ?: product.PRICE_AFTER_DISCOUNT
        val total = unitPrice * quantity
        binding.txtBottomTotalPrice.text = "$total L.E"
    }

    private fun getProductDetails(productId: Int, category: String) {
        lifecycleScope.launch {
            viewModel.homeIntent.send(
                ProductDetailsIntent.GetProductDetails(productId, category)
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
        binding.btnBottomAddToCart?.setOnClickListener {
            addProductToCart()
        }
        binding.btnGoToCart.setOnClickListener {
            val intent = Intent(this@ProductDetailsActivity, HomeActivity::class.java)
            intent.putExtra("open_cart", true)
            intent.putExtra("from_product_details", true)
            intent.putExtra("product", product)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }
        binding.btnPlus.setOnClickListener {
            if (validateIncreaseQuantity(quantity, productQuantity)) {
                quantity++
                binding.txtQuantity.setText(quantity.toString()) // استخدام setText
                updateTotalPrice()
            }
        }

        binding.btnMinus.setOnClickListener {
            if (validateDecreaseQuantity(quantity)) {
                quantity--
                binding.txtQuantity.setText(quantity.toString()) // استخدام setText
                updateTotalPrice()
            }
        }
    }

    private fun addToCartObserve() {
        lifecycleScope.launch {
            addCartViewModel.state.collect { status ->
                when (status) {
                    is AddToCartStatus.Idle -> Log.d(Common.KeroDebug, "observeHome: Idle")

                    is AddToCartStatus.Loading -> {
                        showProgressDialog(binding.progressLoading)
                    }

                    is AddToCartStatus.AddToCart -> {
                        hideProgressDialog(binding.progressLoading)
                        if (status.data.status == 200) {
                            showAddToCartDialog(status.data)
                        } else {
                            showToastSnack(status.data.message, true)
                        }
                    }

                    is AddToCartStatus.Error -> {
                        hideProgressDialog(binding.progressLoading)
                        showToastSnack(status.error.toString(), true)
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
        if (inStoke) {
            lifecycleScope.launch {
                addCartViewModel.addToCartIntent.send(AddToCartIntent.AddProductToCart(product.ID, quantity, flag))
            }
        } else {
            showToastSnack("Product Out Of Stock", true)
        }
    }

    private fun setupQuantityEditText() {
        if (binding.txtQuantity is TextView) return

        var isUpdating = false
        binding.txtQuantity.doOnTextChanged { text, _, _, _ ->
            if (isUpdating) return@doOnTextChanged

            val value = text.toString().toIntOrNull()
            if (value != null) {
                when {
                    value < 1 -> {
                        isUpdating = true
                        quantity = 1
                        binding.txtQuantity.setText("1") // استخدام setText
                        isUpdating = false
                    }
                    value > productQuantity -> {
                        isUpdating = true
                        quantity = productQuantity
                        binding.txtQuantity.setText(productQuantity.toString()) // استخدام setText
                        isUpdating = false
                    }
                    else -> {
                        quantity = value
                    }
                }
            } else {
                quantity = 0
            }
            updateTotalPrice()
        }
    }
}