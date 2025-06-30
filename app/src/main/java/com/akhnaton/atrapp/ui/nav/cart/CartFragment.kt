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
import com.akhnaton.atrapp.shared.ShimmerAdapterCart
import com.akhnaton.atrapp.ui.nav.cart.checkout.CheckoutActivity
import kotlinx.coroutines.launch
import java.text.DecimalFormat

class CartFragment : BaseFragment() {

    private lateinit var binding: FragmentCartBinding
    private val cartViewModel: CartViewModel by viewModels()
    private val addToCartViewModel: AddToCartViewModel by viewModels()

    private lateinit var cartAdapter: CartAdapter
    private lateinit var shimmerAdapter: ShimmerAdapterCart

    private var products: List<ProductModel> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        observeCart()
        observeAddToCart()
        setupClickListeners()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        initCart()
    }

    private fun initCart() {
        shimmerAdapter = ShimmerAdapterCart(10)
        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = shimmerAdapter
        }
        getMyCart()
    }

    private fun setupClickListeners() {
        binding.btnCheckout.setOnClickListener {
            val intent = Intent(requireContext(), CheckoutActivity::class.java)
            startActivity(intent)
        }
    }

    private fun observeCart() {
        lifecycleScope.launch {
            cartViewModel.state.collect { state ->
                when (state) {
                    is CartStatus.Idle -> Log.d(Common.KeroDebug, "CartStatus: Idle")
                    is CartStatus.Loading -> {
                        binding.recycler.adapter = shimmerAdapter
                        binding.recycler.visibility = View.VISIBLE
                    }
                    is CartStatus.GetMyCart -> {
                        hideProgressDialog(binding.progressLoading)
                        products = state.data.data ?: emptyList()
                        if (products.isNotEmpty()) {
                            setupMyCartRecycler(products)
                            updateCartSummaryUI()
                            binding.txtNoProducts.visibility = View.GONE
                            binding.recycler.visibility = View.VISIBLE
                        } else {
                            showEmptyState()
                        }
                    }
                    is CartStatus.Error -> {
                        hideProgressDialog(binding.progressLoading)
                        showToastSnack(state.error.toString(), true)
                        showEmptyState()
                    }
                }
            }
        }
    }

    private fun observeAddToCart() {
        lifecycleScope.launch {
            addToCartViewModel.state.collect { state ->
                when (state) {
                    is AddToCartStatus.Idle -> Log.d(Common.KeroDebug, "AddToCart: Idle")
                    is AddToCartStatus.Loading -> {}
                    is AddToCartStatus.AddToCart -> {
                        if (state.data.status != 200) {
                            showToastSnack(state.data.message, true)
                        }
                    }
                    is AddToCartStatus.Error -> {
                        showToastSnack(state.error.toString(), true)
                    }
                }
            }
        }
    }

    private fun getMyCart() {
        lifecycleScope.launch {
            cartViewModel.cartIntent.send(CartIntent.GetMyCart)
        }
    }

    private fun setupMyCartRecycler(list: List<ProductModel>) {
        cartAdapter = CartAdapter(
            onClick = { _, _ -> },
            onPlusClick = { product, position, quantity ->
                lifecycleScope.launch {
                    (products as ArrayList)[position] = product.copy(MY_QUANTITY = quantity)
                    cartAdapter.setData(products, true, "cart")
                    updateCartSummaryUI()
                    addProductToCart(product.ID, quantity)
                }
            },
            onMinusClick = { product, position, quantity ->
                lifecycleScope.launch {
                    (products as ArrayList)[position] = product.copy(MY_QUANTITY = quantity)
                    cartAdapter.setData(products, true, "cart")
                    updateCartSummaryUI()
                    addProductToCart(product.ID, quantity)
                }
            },
            onDeleteClick = { product, position ->
                lifecycleScope.launch {
                    deleteProductFromCart(product.ID, position)
                }
            }
        )
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = cartAdapter
        cartAdapter.setData(list, true, "cart")
        binding.layoutCart.visibility = View.VISIBLE
    }

    private fun updateCartSummaryUI() {
        if (products.isEmpty()) {
            binding.txtItemTotal.text = "0.0"
            binding.txtDiscount.text = "0.0"
            binding.txtDeliveryFree.text = "0.0"
            binding.txtGrandTotal.text = "0.0"
            return
        }

        val totals = cartViewModel.calculateCartTotals(products)
        val decimalFormat = DecimalFormat("#0.0")
        binding.txtItemTotal.text = decimalFormat.format(totals.totalBeforeDiscount)
        binding.txtDiscount.text = decimalFormat.format(totals.discount)
        binding.txtDeliveryFree.text =
            if (totals.deliveryFee == 0.0) "Free Delivery" else decimalFormat.format(totals.deliveryFee)
        binding.txtGrandTotal.text = decimalFormat.format(totals.grandTotal)
    }

    private fun deleteProductFromCart(productId: Int, position: Int) {
        lifecycleScope.launch {
            (products as ArrayList).removeAt(position)
            cartAdapter.setData(products, true, "cart")
            updateCartSummaryUI()
            checkEmptyAfterDelete()
            addToCartViewModel.addToCartIntent.send(
                AddToCartIntent.AddProductToCart(productId, 0)
            )
        }
    }

    private fun addProductToCart(productId: Int, quantity: Int) {
        lifecycleScope.launch {
            addToCartViewModel.addToCartIntent.send(
                AddToCartIntent.AddProductToCart(productId, quantity)
            )
        }
    }

    private fun checkEmptyAfterDelete() {
        if (products.isEmpty()) {
            showEmptyState()
        }
    }

    private fun showEmptyState() {
        binding.recycler.visibility = View.GONE
        binding.layoutCart.visibility = View.GONE
        binding.txtNoProducts.visibility = View.VISIBLE
        updateCartSummaryUI()
    }

}
