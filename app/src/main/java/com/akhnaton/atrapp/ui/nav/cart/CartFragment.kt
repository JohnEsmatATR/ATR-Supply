package com.akhnaton.atrapp.ui.nav.cart

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.akhnaton.atrapp.data.model.CartProduct
import com.akhnaton.atrapp.data.model.CartResponse
import com.akhnaton.atrapp.data.statuesValue.nav.cart.addToCart.AddToCartIntent
import com.akhnaton.atrapp.data.statuesValue.nav.cart.addToCart.AddToCartStatus
import com.akhnaton.atrapp.data.statuesValue.nav.cart.getMyCart.CartIntent
import com.akhnaton.atrapp.data.statuesValue.nav.cart.getMyCart.CartStatus
import com.akhnaton.atrapp.data.statuesValue.nav.home.address.AddressIntent
import com.akhnaton.atrapp.data.statuesValue.nav.home.address.AddressStatus
import com.akhnaton.atrapp.databinding.FragmentCartBinding
import com.akhnaton.atrapp.shared.BaseFragment
import com.akhnaton.atrapp.shared.Common
import com.akhnaton.atrapp.shared.SharedPreferenceHelper
import com.akhnaton.atrapp.shared.ShimmerAdapterCart
import com.akhnaton.atrapp.ui.nav.cart.addresses.AddressesActivity
import com.akhnaton.atrapp.ui.nav.cart.addresses.AddressesViewModel
import com.akhnaton.atrapp.ui.nav.cart.checkout.CartParentAdapter
import com.akhnaton.atrapp.ui.nav.cart.checkout.CheckoutActivity
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.NumberFormat

class CartFragment : BaseFragment() {

    private lateinit var binding: FragmentCartBinding
    private val cartViewModel: CartViewModel by viewModels()
    private val addToCartViewModel: AddToCartViewModel by viewModels()

    private lateinit var parentAdapter: CartParentAdapter
    private lateinit var shimmerAdapter: ShimmerAdapterCart
    private val viewModel: AddressesViewModel by viewModels()

    private var cartData: List<CartResponse> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)
        observeCart()
        getAddress()
        observeAddress()
        observeCartQuantity()
        setupClickListeners()
        binding.cardAddress.setOnClickListener {
            val intent = Intent(requireContext(), AddressesActivity::class.java)
            startActivity(intent)
        }
        val currentLang = SharedPreferenceHelper.language
        if (currentLang == "ar") {
            binding.layoutCart.layoutDirection = View.LAYOUT_DIRECTION_RTL
        } else {
            binding.layoutCart.layoutDirection = View.LAYOUT_DIRECTION_LTR
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        initCart()
    }
    private fun observeAddress() {
        lifecycleScope.launch {
            viewModel.state.collect {
                Log.d("DEBUGGGGG", "Received state: $it")
                when (it) {
                    is AddressStatus.Idle -> Log.d(Common.KeroDebug, "observeHome: Idleeeee")
                    is AddressStatus.Loading -> {
                        Log.d(Common.KeroDebug, "observeHome: Loading")
                        showProgressDialog(binding.progressLoading)
                    }

                    is AddressStatus.GetMyAddresses -> {
                        if (it.result.status == 200) {
                            hideProgressDialog(binding.progressLoading)
                            Log.d(Common.KeroDebug, "Received: GetProducts")

                            val addresses = it.result.data ?: emptyList()


                            addresses.forEach { address ->
                                if (address.prime == 1) {
                                    Log.d("DEBUG_ADDRESS", "Prime address found: $address")
                                    binding.defaultAddress.text=address.TITLE
                                }
                            }

                            val hasDefault = addresses.any { address -> address.prime == 1 }
                            if (!hasDefault) {
                                val intent = Intent(requireContext(), AddressesActivity::class.java)
                                startActivity(intent)
                            }

                        } else {
                            hideProgressDialog(binding.progressLoading)
                        }
                    }



                    is AddressStatus.MakeAddressPrime -> {}

                    is AddressStatus.Error -> {

                    }

                }
            }
        }
    }
    private fun getAddress() {
        lifecycleScope.launch {
            viewModel.addressIntent.send(
                AddressIntent.GetMyAddresses
            )
        }
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
                    is CartStatus.Idle -> {}
                    is CartStatus.Loading -> {
                        showProgressDialog(binding.progressLoading)
                    }
                    is CartStatus.GetMyCart -> {
                        hideProgressDialog(binding.progressLoading)
                        cartData = state.data.data.firstOrNull()?.carts ?: emptyList()
                        if (cartData.isNotEmpty()) {
                            setupParentRecycler(cartData)
                            updateCartSummaryUI()
                        } else {
                            showEmptyState()
                        }
                    }
                    is CartStatus.Error -> {
                        Log.d("TAG", "observeCart: ${state.error.toString()}")
                        showToastSnack(state.error.toString(), true)
                        showEmptyState()
                    }
                }
            }
        }
    }

    private fun observeCartQuantity() {
        lifecycleScope.launch {
            addToCartViewModel.state.collect { state ->
                when (state) {
                    is AddToCartStatus.AddToCart -> {
                        getMyCart()
                        hideProgressDialog(binding.progressLoading)
                    }
                    is AddToCartStatus.Error -> {
                        Log.e(Common.KeroDebug, "Cart Error: ${state.error}")
                        showToastSnack(state.error.toString(), true)
                        hideProgressDialog(binding.progressLoading)
                    }
                    AddToCartStatus.Idle -> hideProgressDialog(binding.progressLoading)
                    AddToCartStatus.Loading -> showProgressDialog(binding.progressLoading)
                }
            }
        }
    }

    private fun getMyCart() {
        lifecycleScope.launch {
            val lang = SharedPreferenceHelper.language ?: "en"
            cartViewModel.cartIntent.send(CartIntent.GetMyCart(lang))
        }
    }

    private fun setupParentRecycler(list: List<CartResponse>) {
        parentAdapter = CartParentAdapter(
            onClick = { _, _ -> },
            onPlusClick = { product, _, quantity ->
                lifecycleScope.launch {
                    addProductToCart(product.id, quantity, product)
                //    updateCartSummaryUI()
                }
            },
            onMinusClick = { product, _, quantity ->
                lifecycleScope.launch {
                    addProductToCart(product.id, quantity, product)
                  //  updateCartSummaryUI()
                }
            },
            onDeleteClick = { product, _ ->
                lifecycleScope.launch {
                    deleteProductFromCart(product)
                }
            }
        )
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = parentAdapter
        parentAdapter.setData(list)
        binding.layoutCart.visibility = View.VISIBLE
    }

    private fun updateCartSummaryUI() {
        if (cartData.isEmpty()) {
            binding.txtItemTotal.text = "0.0"
            binding.txtDiscount.text = "0.0"
            binding.txtDeliveryFree.text = "0.0"
            binding.txtGrandTotal.text = "0.0"
            return
        }


        val allProducts = cartData.flatMap { it.items.products }
        val totals = cartViewModel.calculateCartTotals(allProducts)
        val decimalFormat = DecimalFormat("#0.0")
        binding.txtItemTotal.text = decimalFormat.format(totals.totalBeforeDiscount)
        binding.txtDiscount.text = decimalFormat.format(totals.discount)
        binding.txtDeliveryFree.text = "Free Delivery"
        binding.txtGrandTotal.text = decimalFormat.format(totals.grandTotal)
    }

    private fun deleteProductFromCart(product: CartProduct) {
        lifecycleScope.launch {

            cartData = cartData.map { cartResponse ->
                cartResponse.copy(
                    items = cartResponse.items.copy(
                        products = cartResponse.items.products.filter { it.id != product.id }
                    )
                )
            }.filter { it.items.products.isNotEmpty() }

            parentAdapter.setData(cartData)
            updateCartSummaryUI()
            checkEmptyAfterDelete()

            addToCartViewModel.addToCartIntent.send(
                AddToCartIntent.AddProductToCart(product.id, 0, product.title)

            )
        }
    }

    private fun addProductToCart(productId: Int, quantity: Int, product: CartProduct) {
        lifecycleScope.launch {
            addToCartViewModel.addToCartIntent.send(
                AddToCartIntent.AddProductToCart(productId, quantity, product.title)
            )
        }
    }

    private fun checkEmptyAfterDelete() {
        if (cartData.isEmpty()) {
            showEmptyState()
        }
    }

    private fun showEmptyState() {
        binding.recycler.visibility = View.GONE
        binding.layoutCart.visibility = View.GONE
        binding.txtNoProducts.visibility = View.VISIBLE
        updateCartSummaryUI()
    }

    fun formatNumber(value: Double, context: Context): String {
        val currentLocale = context.resources.configuration.locales[0] // أو locale من إعداداتك
        val formatter = NumberFormat.getInstance(currentLocale)
        return formatter.format(value)
    }

}


