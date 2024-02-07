package com.akhnaton.atrapp.ui.nav.cart

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.akhnaton.atrapp.databinding.FragmentCartBinding
import com.akhnaton.atrapp.shared.BaseFragment
import com.akhnaton.atrapp.ui.nav.cart.CartViewModel
import kotlinx.coroutines.launch
import java.io.Serializable

class CartFragment : BaseFragment() {
    lateinit var binding: FragmentCartBinding
    private val cartViewModel: CartViewModel by viewModels()
//    private val addToCartViewModel: AddToCartViewModel by viewModels()
//    lateinit var cartAdapter: CartAdapter
//    private var products: List<ProductModel> = ArrayList()
//    private lateinit var cart: CartDataModel
//    private var productsRemoved: List<ProductModel> = emptyList()
//    private var wallet_states = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater)

//        cartObserve()
//        addToCartObserve()
        onClick()

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        init()
    }

    private fun init() {

//        if (SharedPreferenceHelper.showWallet!!) {
//            binding.layoutWallet.visibility = View.VISIBLE
//        } else {
//            binding.layoutWallet.visibility = View.GONE
//        }

//        getMyCart()
    }

    private fun onClick() {
//        binding.btnCheckout.setOnClickListener {
//            if (cart.total_products_price <= 50) {
//                showToastSnack("\nYou cannot make order less than 50 LE", true)
//            } else if (filterProducts().isEmpty()) {
//                showToastSnack("\nYou cannot make this order because all products in the cart are out of stock!", true)
//            } else {
//                if (filterProducts().size == products.size) {
//                    val intent = Intent(context, CheckoutActivity::class.java)
//                    intent.putExtra("products", filterProducts() as Serializable)
//                    intent.putExtra("wallet_states", wallet_states)
//                    startActivity(intent)
//                } else {
//                    showOkDialog("Warning..!", getString(R.string.warning_out_of_stock))
//                }
//            }
//        }
//        binding.chWallet.setOnCheckedChangeListener { buttonView, isChecked ->
//            wallet_states = isChecked
//        }
    }

    private fun checkNoProducts() {
//        if (products.isEmpty()) {
//            binding.txtNoProducts.visibility = View.VISIBLE
//            binding.layoutCart.visibility = View.GONE
//        } else {
//            binding.txtNoProducts.visibility = View.GONE
//            binding.layoutCart.visibility = View.VISIBLE
//        }
    }


//    private fun cartObserve() {
//        lifecycleScope.launch {
//            cartViewModel.state.collect {
//                when (it) {
//                    is CartStatus.Idle -> Log.d(Common.KeroDebug, "observeHome: Idle")
//                    is CartStatus.Loading -> {
//                        Log.d(Common.KeroDebug, "observeHome: Loading")
//                        showProgressDialog(binding.progressLoading)
//
//                    }
//
//                    is CartStatus.GetMyCart -> {
//                        if (it.data.status == 1) {
//                            hideProgressDialog(binding.progressLoading)
//
//                            Log.d(Common.KeroDebug, "observeHome: GetProducts")
//
//                            binding.cart = it.data.data!!.cart
//                            cart = it.data.data!!.cart
//                            products = it.data.data!!.products
//
//                            try {
//                                binding.txtWallet.text = "Use wallet (" + it.data.data!!.wallet.current_wallet +")"
//                            } catch (e: Exception) {
//                                binding.txtWallet.text = "Use wallet (" + 0 +")"
//                            }
//
//                            if (it.data.data!!.cart.shipping_amount == 0.0) {
//                                binding.txtShipping.text = "50.0 LE"
//                                binding.txtShipping.paintFlags =
//                                    binding.txtShipping.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
//                            } else {
//                                binding.txtShipping.text = "${it.data.data!!.cart.shipping_amount} LE"
//                                binding.txtShipping.paintFlags = binding.txtShipping.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
//
//                            }
//
//                            checkNoProducts()
//
//                            setupMyCartRecycler(products)
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
//                    is CartStatus.DeleteProductFromCart -> {
//                        if (it.data.status == 1) {
//                            hideProgressDialog(binding.progressLoading)
//                            Log.d(Common.KeroDebug, "observeHome: GetProducts")
//                            setupMyCartRecycler(productsRemoved)
//                            checkNoProducts()
//                            getMyCart()
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
//                    is CartStatus.Error -> {
//                        Log.d(Common.KeroDebug, "observeHome Error: ${it.error.toString()}")
//                        hideProgressDialog(binding.progressLoading)
//                        showToastSnack(it.error.toString(), true)
//                    }
//
//                    else -> {}
//                }
//            }
//        }
//    }
//
//    private fun filterProducts(): List<ProductModel> {
//        return products.filter { product ->
//            product.stock_code != 0
//        }
//    }
//
//    private fun addToCartObserve() {
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
//                            getMyCart()
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
//                    is AddToCartStatus.Error -> {
//                        Log.d(Common.KeroDebug, "observeHome Error: ${it.error.toString()}")
//                        hideProgressDialog(binding.progressLoading)
//                        showToastSnack(it.error.toString(), true)
//                    }
//
//                }
//            }
//        }
//    }
//
//
//    private fun getMyCart() {
//        lifecycleScope.launch {
//            cartViewModel.cartIntent.send(
//                CartIntent.GetMyCart(
//                    "Bearer ${SharedPreferenceHelper.userToken}",
//                )
//            )
//        }
//    }
//
//    private fun addProductToCart(
//        productId: Int,
//        quantity: Int,
//        price: Double,
//        flag: Int,
//        price_after_discount: Double
//    ) {
//        lifecycleScope.launch {
//            addToCartViewModel.addToCartIntent.send(
//                AddToCartIntent.AddProductToCart(
//                    "Bearer ${SharedPreferenceHelper.userToken}",
//                    productId,
//                    quantity,
//                    price,
//                    flag,
//                    price_after_discount
//                )
//            )
//        }
//    }
//
//    private fun deleteProductFromCart(productId: Int) {
//        lifecycleScope.launch {
//            cartViewModel.cartIntent.send(
//                CartIntent.DeleteProductFromCart(
//                    "Bearer ${SharedPreferenceHelper.userToken}",
//                    productId,
//                )
//            )
//        }
//    }
//
//    private fun setupMyCartRecycler(list: List<ProductModel>) {
//        val layoutManager =
//            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
//        cartAdapter = CartAdapter(
//            onClick = { product, position ->
//
//            },
//            onPlusClick = { product, position, quantity ->
//                lifecycleScope.launch {
//                    (products as ArrayList)[position] = product
//                    addProductToCart(
//                        product.product_id,
//                        quantity,
//                        product.price,
//                        product.flag,
//                        product.price_after_discount
//                    )
//                }
//            },
//            onMinusClick = { product, position, quantity ->
//                (products as ArrayList)[position] = product
//                lifecycleScope.launch {
//                    addProductToCart(
//                        product.product_id,
//                        quantity,
//                        product.price,
//                        product.flag,
//                        product.price_after_discount
//                    )
//                }
//            }, onDeleteClick = { product, position ->
//                deleteProductFromCart(product.product_id)
//                productsRemoved = products
//                (productsRemoved as ArrayList).removeAt(position)
//            })
//        cartAdapter.setData(list, true, "cart")
//        binding.recycler.layoutManager = layoutManager
//        binding.recycler.adapter = cartAdapter
//    }
//
//
//    private fun showOkDialog(title: String, message: String) {
//        val builder = AlertDialog.Builder(requireContext())
//        builder.setTitle(title)
//        builder.setMessage(message)
//        builder.setPositiveButton("OK") { dialog, which ->
//            dialog.dismiss()
//        }
//
//        val dialog = builder.create()
//        dialog.show()
//    }
}