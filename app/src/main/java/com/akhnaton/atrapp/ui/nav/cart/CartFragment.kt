package com.akhnaton.atrapp.ui.nav.cart

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.akhnaton.atrapp.data.model.ProductModel
import com.akhnaton.atrapp.data.statuesValue.nav.cart.addToCart.AddToCartIntent
import com.akhnaton.atrapp.data.statuesValue.nav.cart.addToCart.AddToCartStatus
import com.akhnaton.atrapp.data.statuesValue.nav.cart.getMyCart.CartIntent
import com.akhnaton.atrapp.data.statuesValue.nav.cart.getMyCart.CartStatus
import com.akhnaton.atrapp.databinding.FragmentCartBinding
import com.akhnaton.atrapp.shared.BaseFragment
import com.akhnaton.atrapp.shared.Common
import com.akhnaton.atrapp.ui.nav.cart.checkout.CheckoutActivity
import kotlinx.coroutines.launch

class CartFragment : BaseFragment() {
    lateinit var binding: FragmentCartBinding
    private val cartViewModel: CartViewModel by viewModels()
    private val addToCartViewModel: AddToCartViewModel by viewModels()
    private lateinit var cartAdapter: CartAdapter
    private var products: List<ProductModel> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater)

        cartObserve()
        addToCartObserve()
        onClick()

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        init()
    }

    private fun init() {


        getMyCart()
    }

    private fun onClick() {
        binding.btnCheckout.setOnClickListener {
            val intent = Intent(requireContext(), CheckoutActivity::class.java)
            startActivity(intent)
        }
    }


    private fun cartObserve() {
        lifecycleScope.launch {
            cartViewModel.state.collect {
                when (it) {
                    is CartStatus.Idle -> Log.d(Common.KeroDebug, "observeHome: Idle")
                    is CartStatus.Loading -> {
                        Log.d(Common.KeroDebug, "observeHome: Loading")
                        showProgressDialog(binding.progressLoading)

                    }

                    is CartStatus.GetMyCart -> {
                        if (it.data.status == 200) {
                            hideProgressDialog(binding.progressLoading)

                            Log.d(Common.KeroDebug, "observeHome: GetProducts")

                            products = it.data.data!!

                            checkNoProducts()

                            setupMyCartRecycler(products)
                        } else {
                            hideProgressDialog(binding.progressLoading)
                            showToastSnack(it.data.message, true)
                        }
                    }


                    is CartStatus.Error -> {
                        Log.d(Common.KeroDebug, "observeHome Error: ${it.error.toString()}")
                        hideProgressDialog(binding.progressLoading)
                        showToastSnack(it.error.toString(), true)
                    }

                }
            }
        }
    }

    private fun addToCartObserve() {
        lifecycleScope.launch {
            addToCartViewModel.state.collect {
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
                            getMyCart()

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


    private fun getMyCart() {
        lifecycleScope.launch {
            cartViewModel.cartIntent.send(
                CartIntent.GetMyCart
            )
        }
    }

    private fun addProductToCart(
        productId: Int,
        quantity: Int,
    ) {
        lifecycleScope.launch {
            addToCartViewModel.addToCartIntent.send(
                AddToCartIntent.AddProductToCart(
                    productId,
                    quantity,
                )
            )
        }
    }

    private fun deleteProductFromCart(productId: Int) {
        lifecycleScope.launch {
            addToCartViewModel.addToCartIntent.send(
                AddToCartIntent.AddProductToCart(
                    productId,
                    0
                )
            )
        }
    }

    private fun setupMyCartRecycler(list: List<ProductModel>) {
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        cartAdapter = CartAdapter(
            onClick = { product, position ->

            },
            onPlusClick = { product, position, quantity ->
                lifecycleScope.launch {
                    (products as ArrayList)[position] = product
                    addProductToCart(
                        product.ID,
                        quantity,
                    )
                }
            },
            onMinusClick = { product, position, quantity ->
                lifecycleScope.launch {
                    (products as ArrayList)[position] = product
                    addProductToCart(
                        product.ID,
                        quantity,
                    )
                }
            })
        cartAdapter.setData(list, true, "cart")
        binding.recycler.layoutManager = layoutManager
        binding.recycler.adapter = cartAdapter
    }


    private fun checkNoProducts() {
        if (products.isEmpty()) {
            binding.txtNoProducts.visibility = View.VISIBLE
            binding.layoutCart.visibility = View.GONE
        } else {
            binding.txtNoProducts.visibility = View.GONE
            binding.layoutCart.visibility = View.VISIBLE
        }
    }

}