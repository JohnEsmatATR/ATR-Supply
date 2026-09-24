package com.akhnaton.atrapp.ui.nav.cart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akhnaton.atrapp.data.statuesValue.nav.cart.addToCart.AddToCartIntent
import com.akhnaton.atrapp.data.statuesValue.nav.cart.addToCart.AddToCartStatus
import com.akhnaton.atrapp.domain.CartRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class AddToCartViewModel : ViewModel() {

    val addToCartIntent = Channel<AddToCartIntent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow<AddToCartStatus>(AddToCartStatus.Idle)
    val state: StateFlow<AddToCartStatus> get() = _state

    init {
        makeHomeObserve()
    }

    private fun makeHomeObserve() {
        viewModelScope.launch {
            addToCartIntent.consumeAsFlow().collect { intent ->
                Log.d("TEST_CART", "1. ViewModel Received Intent: $intent")
                when (intent) {
                    is AddToCartIntent.AddProductToCart -> addProductToCart(
                        intent.productId,
                        intent.quantity,
                        intent.category
                    )

                    is AddToCartIntent.deleteProductToCart -> deleteProductFromCart(
                        intent.productId,
                        intent.quantity
                    )
                }
            }
        }
    }

    fun resetState() {
        _state.value = AddToCartStatus.Idle
    }

    private fun addProductToCart(
        productId: Int?,
        quantity: Int?,
        category: String
    ) {
        viewModelScope.launch {
            Log.d("TEST_CART", "2. ViewModel emitting Loading state")
            _state.value = AddToCartStatus.Loading

            try {
                Log.d("TEST_CART", "3. Calling CartRepository.addProductToCart...")
                val response = CartRepository().addProductToCart(
                    productId,
                    quantity,
                    category
                )

                Log.d("TEST_CART", "4. API Response Code: ${response.code()}")
                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    Log.d("TEST_CART", "5. Success Body Status: ${body.status}")
                    _state.value = AddToCartStatus.AddToCart(body)
                } else {
                    val errorMsg = response.errorBody()?.string() ?: response.message()
                    Log.e("TEST_CART", "5. API Error: $errorMsg")
                    _state.value = AddToCartStatus.Error(errorMsg)
                }
            } catch (e: Exception) {
                Log.e("TEST_CART", "Exception in addProductToCart: ${e.localizedMessage}", e)
                _state.value = AddToCartStatus.Error(e.localizedMessage ?: "Unknown Error")
            }
        }
    }

    private fun deleteProductFromCart(
        productId: Int?,
        quantity: Int?,
    ) {
        viewModelScope.launch {
            _state.value = AddToCartStatus.Loading
            try {
                val response = CartRepository().deleteProductFromCart(
                    productId,
                    quantity,
                )
                if (response.isSuccessful && response.body() != null) {
                    _state.value = AddToCartStatus.AddToCart(response.body()!!)
                } else {
                    _state.value = AddToCartStatus.Error(response.message())
                }
            } catch (e: Exception) {
                _state.value = AddToCartStatus.Error(e.localizedMessage)
            }
        }
    }
}