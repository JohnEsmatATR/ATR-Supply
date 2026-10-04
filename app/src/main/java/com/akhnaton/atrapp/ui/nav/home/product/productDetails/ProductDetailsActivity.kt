package com.akhnaton.atrapp.ui.nav.home.product.productDetails

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.akhnaton.atrapp.R
import com.akhnaton.atrapp.data.model.AddToCartResponse
import com.akhnaton.atrapp.data.model.ProductModel
import com.akhnaton.atrapp.data.model.common.BaseModel
import com.akhnaton.atrapp.data.statuesValue.nav.cart.addToCart.AddToCartIntent
import com.akhnaton.atrapp.data.statuesValue.nav.cart.addToCart.AddToCartStatus
import com.akhnaton.atrapp.data.statuesValue.nav.home.favorite.FavoriteIntent
import com.akhnaton.atrapp.data.statuesValue.nav.home.favorite.FavoriteStatus
import com.akhnaton.atrapp.data.statuesValue.nav.home.productDetails.ProductDetailsIntent
import com.akhnaton.atrapp.data.statuesValue.nav.home.productDetails.ProductDetailsStatus
import com.akhnaton.atrapp.databinding.ActivityProductDetailsBinding
import com.akhnaton.atrapp.shared.BaseActivity
import com.akhnaton.atrapp.shared.Common
import com.akhnaton.atrapp.shared.DialogUtils
import com.akhnaton.atrapp.shared.SharedPreferenceHelper
import com.akhnaton.atrapp.ui.auth.login.LoginActivity
import com.akhnaton.atrapp.ui.nav.HomeActivity
import com.akhnaton.atrapp.ui.nav.cart.AddToCartViewModel
import com.akhnaton.atrapp.ui.nav.favorite.FavoriteViewModel
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
    var productId: Int = 0
    private var inStoke: Boolean = false
    private lateinit var bonusAdapter: BonusAdapter
    private lateinit var productData: ProductModel
    private val favoriteViewModel: FavoriteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleTopBottomKeyboard()
        init()
        onClick()
    }

    private fun handleTopBottomKeyboard() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            val imeInsets = insets.getInsets(
                WindowInsetsCompat.Type.ime()
            )
            view.setPadding(
                view.paddingLeft, systemBars.top, view.paddingRight, maxOf(
                    imeInsets.bottom, systemBars.bottom
                )
            )
            insets
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun init() {
        setupQuantityEditText()

        flag = intent.getStringExtra("flag") ?: ""

        product = intent.getSerializableExtra("product") as ProductModel
        val transitionName = intent.getStringExtra("transitionName")

        ViewCompat.setTransitionName(binding.txtItemName, transitionName)
        binding.imProduce.load(product.IMAGE_URL) {
            crossfade(true)
            placeholder(R.drawable.ic_logo)
            error(R.drawable.ic_logo)
        }
        productId = product.ID.toInt()

        val isArabic = SharedPreferenceHelper.language == "ar"
        if (isArabic) binding.btnBack.setImageResource(R.drawable.ic_back_ar)
        else binding.btnBack.setImageResource(R.drawable.ic_back)

        observeProduct()
        getProductDetails(productId, flag)
        addToCartObserve()
//        initBonusRecycler()
        favoriteObserve()
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
                        Toast.makeText(
                            this@ProductDetailsActivity,
                            "Error: ${status.error}",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d("TAG", "observeProduct: ${status.error}")
                    }

                    is ProductDetailsStatus.GetProductDetails -> {
                        if (status.data.status == 200) {
                            hideProgressDialog(binding.progressLoading)
                            productData = status.data.data?.firstOrNull()!!
                            Log.d(
                                "DEBUG_PRODUCT",
                                "Returned Weight = '${productData?.WEIGHT}' | Returned Stock = ${productData?.IN_STOCK}"
                            )

                            if (productData != null) {
                                if (productData.IS_LIKED) {
                                    binding.btnFavIcon.setImageResource(R.drawable.ic_favorite_fill2)
                                    val orangeColor = Color.parseColor("#EE7907")
                                    binding.btnFavIcon.imageTintList = ColorStateList.valueOf(orangeColor)
                                } else {
                                    binding.btnFavIcon.setImageResource(R.drawable.ic_favorite2)
                                    val grayColor = Color.parseColor("#C1C1C1")
                                    binding.btnFavIcon.imageTintList =
                                        ColorStateList.valueOf(grayColor)
                                }
                                binding.txtItemName.text = productData.TITLE ?: ""
                                binding.txtCategory.text = productData.category?.TITLE ?: ""
                                binding.txtPriceValue.text =
                                    "${productData.PRICE_AFTER_DISCOUNT ?: 0.0}"
                                binding.tvPriceWithoutTax.text =
                                    "${getString(R.string.price_without_tax)}: ${productData.PRICE_WITHOUT_TAX ?: 0.0} ${
                                        getString(
                                            R.string.currency
                                        )
                                    }"
                                binding.tvTax.text =
                                    "${getString(R.string.tax)}: ${productData.TAX ?: 0.0} ${
                                        getString(
                                            R.string.currency
                                        )
                                    }"
                                // 1. Dosage / Size Handling from API
                                val dosageText = productData.WEIGHT
                                if (!dosageText.isNullOrEmpty()) {
                                    binding.txtSize.text = dosageText
                                } else {
                                    binding.txtSize.text = "N/A"
                                }

                                productQuantity = productData.QUANTITY

                                // Quantity Setup
                                if (productData.MY_QUANTITY != 0) {
                                    binding.txtQuantity.setText(productData.MY_QUANTITY.toString())
                                    quantity = productData.MY_QUANTITY
                                } else {
                                    binding.txtQuantity.setText("1")
                                    quantity = 1
                                }

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

                                binding.btnPlus.isEnabled = inStoke
                                binding.btnMinus.isEnabled = inStoke

                                // Bonus Recycler
                                val bonusList = productData.BONUS_DATA
                                if (!bonusList.isNullOrEmpty()) {
                                    binding.cardBonus.visibility = View.VISIBLE
                                    val bonusString =
                                        productData.BONUS_DATA.orEmpty().joinToString("\n") {
                                            "Buy ${it.BUY}, GET ${it.GET}"
                                        }
                                    binding.tvBonus.text = bonusString
//                                    binding.bonusRecycler.visibility = View.VISIBLE
//                                    bonusAdapter.setData(bonusList)
                                } else {
//                                    binding.bonusRecycler.visibility = View.GONE
                                }

//                                binding.nestedScrollView.visibility = View.VISIBLE
                                binding.productNotFound.visibility = View.GONE

                                // Update Total Price Bottom
                                updateTotalPrice(productData.PRICE_AFTER_DISCOUNT)

                            } else {
//                                binding.nestedScrollView.visibility = View.GONE
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
        val currentQty = binding.txtQuantity.text.toString().toIntOrNull() ?: quantity //malaaaaaak
        val total = unitPrice * currentQty // malakkkkkkkk
        binding.txtBottomTotalPrice.text = "$total ${getString(R.string.currency)}"
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
        binding.btnShare.setOnClickListener {
            if (SharedPreferenceHelper.isLogged == false) DialogUtils.showResultDialog(
                context = this@ProductDetailsActivity,
                isDismissable = true,
                icon = R.drawable.ic_error,
                title = getString(R.string.you_are_not_logged_in),
                description = getString(R.string.please_log_in_to_access_this_feature),
                isOkMessage = true,
                yesText = getString(R.string.login),
                onConfirm = {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }) else shareProduct()
        }
        binding.btnFavIcon.setOnClickListener {
            if (SharedPreferenceHelper.isLogged == false) DialogUtils.showResultDialog(
                context = this@ProductDetailsActivity,
                isDismissable = true,
                icon = R.drawable.ic_error,
                title = getString(R.string.you_are_not_logged_in),
                description = getString(R.string.please_log_in_to_access_this_feature),
                isOkMessage = true,
                yesText = getString(R.string.login),
                onConfirm = {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }) else if (productData.IS_LIKED) deleteProductToFavorite(
                productId = product.ID, add = true
            ) else if (!productData.IS_LIKED) addProductToFavorite(
                productId = product.ID, add = true
            )
        }
        binding.btnBottomAddToCart?.setOnClickListener {
            if (SharedPreferenceHelper.isLogged == false) DialogUtils.showResultDialog(
                context = this@ProductDetailsActivity,
                isDismissable = true,
                icon = R.drawable.ic_error,
                title = getString(R.string.you_are_not_logged_in),
                description = getString(R.string.please_log_in_to_access_this_feature),
                isOkMessage = true,
                yesText = getString(R.string.login),
                onConfirm = {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                })
            else addProductToCart()
        }
        binding.btnPlus.setOnClickListener {
            if (validateIncreaseQuantity(quantity, productQuantity)) {
                quantity++
                Log.d("WHATquantity", quantity.toString())
                binding.txtQuantity.setText(quantity.toString())
                updateTotalPrice()
            }
        }

        binding.btnMinus.setOnClickListener {
            if (validateDecreaseQuantity(quantity)) {
                quantity--
                Log.d("WHATquantity", quantity.toString())
                binding.txtQuantity.setText(quantity.toString())
                updateTotalPrice()
            }
        }
    }

    private fun shareProduct() {

        val imageUri: Uri = product.IMAGE_URL.toUri() // your image Uri

        val shareText = """
        Check out this product!
        
        Product: ${product.TITLE}
        Price: ${product.PRICE_WITH_TAX} ${getString(R.string.currency)}
        Price Without Tax: ${product.PRICE_WITHOUT_TAX} ${getString(R.string.currency)}
        Tax: ${product.TAX} ${getString(R.string.currency)}
    """.trimIndent()

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/*"

            putExtra(Intent.EXTRA_TEXT, shareText)
            putExtra(Intent.EXTRA_STREAM, imageUri)

            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        startActivity(
            Intent.createChooser(
                shareIntent, "Share product"
            )
        )
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

        val dialog = AlertDialog.Builder(this).setView(dialogView).setCancelable(false).create()

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
                addCartViewModel.addToCartIntent.send(
                    AddToCartIntent.AddProductToCart(
                        product.ID, quantity, flag
                    )
                )
            }
        } else {
            showToastSnack("Product Out Of Stock", true)
        }
    }

    private fun setupQuantityEditText() {

        binding.txtQuantity.doAfterTextChanged { editable ->

            val value = editable?.toString()?.toIntOrNull()

            Log.d("WHATquantity", "typed value = $value")

            quantity = when {
                value == null -> 0
                value < 1 -> 1
                value > productQuantity -> productQuantity
                else -> value
            }

            Log.d("WHATquantity", "quantity = $quantity")

            updateTotalPrice()
        }
    }

    private fun addProductToFavorite(
        productId: Int,
        add: Boolean,
    ) {
        lifecycleScope.launch {
            favoriteViewModel.favoriteIntent.send(
                FavoriteIntent.AddProductToFavourites(
                    "Bearer ${SharedPreferenceHelper.userToken}", productId, add, flag
                )
            )
        }
    }

    private fun deleteProductToFavorite(productId: Int, add: Boolean) {
        lifecycleScope.launch {
            favoriteViewModel.favoriteIntent.send(
                FavoriteIntent.DeleteFromFavourites(
                    "Bearer ${SharedPreferenceHelper.userToken}",
                    productId,
                    add,
                    flag
                )
            )
        }
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

                    is FavoriteStatus.Error -> {
                        Log.d(Common.KeroDebug, "observeHome Error: ${it.error.toString()}")
                        hideProgressDialog(binding.progressLoading)
                        showToastSnack(it.error.toString(), true)
                    }

                    is FavoriteStatus.AddProductToFavourites -> {
                        binding.btnFavIcon.setImageResource(R.drawable.ic_favorite_fill2)
                        val orangeColor = Color.parseColor("#EE7907")
                        binding.btnFavIcon.imageTintList = ColorStateList.valueOf(orangeColor)
                        hideProgressDialog(binding.progressLoading)
                        showToastSnack(it.data.message.toString(), false)
                        getProductDetails(productId, flag)
                    }

                    is FavoriteStatus.DeleteProductToFavourites -> {
                        hideProgressDialog(binding.progressLoading)

                        if (it.data.status == 200) {
                            binding.btnFavIcon.setImageResource(R.drawable.ic_favorite2)
                            val grayColor = Color.parseColor("#C1C1C1")
                            binding.btnFavIcon.imageTintList = ColorStateList.valueOf(grayColor)
                            getProductDetails(productId, flag)

                            Log.d(Common.KeroDebug, "observeHome: Product deleted from favorites")
                            showToastSnack(it.data.message, false)

                        } else if (it.data.status == 401) {
                            // onTokenExpired(it.data.errors!![0])
                        } else {
                            // showToastSnack(it.data.message, true)
                        }
                    }

                    else -> {}

                }
            }
        }
    }
}