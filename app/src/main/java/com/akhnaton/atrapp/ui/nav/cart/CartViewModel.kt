package com.akhnaton.atrapp.ui.nav.cart

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akhnaton.atrapp.data.model.CartProduct
import com.akhnaton.atrapp.data.model.CartResponse
import com.akhnaton.atrapp.data.statuesValue.nav.cart.getMyCart.CartIntent
import com.akhnaton.atrapp.data.statuesValue.nav.cart.getMyCart.CartStatus
import com.akhnaton.atrapp.domain.CartRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {

    val cartIntent = Channel<CartIntent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow<CartStatus>(CartStatus.Idle)
    val state: StateFlow<CartStatus> get() = _state

    private val _cartResponse = MutableLiveData<CartResponse?>()
    val cartResponse: LiveData<CartResponse?> = _cartResponse

    init {
        makeObserve()
    }

    private fun makeObserve() {
        viewModelScope.launch {
            cartIntent.consumeAsFlow().collect {
                when (it) {
                    is CartIntent.GetMyCart -> getMyCart(
                        it.language
                    )
                }
            }
        }
    }


    private fun getMyCart(language: String) {
        viewModelScope.launch {
            _state.value = CartStatus.Loading
            _state.value = try {
                val response = CartRepository().getMyCart(language)
                if (response.code() == 200) {
                    CartStatus.GetMyCart(response.body()!!)
                } else if (response.code() == 401) {
                    CartStatus.Error(response.body()!!.message)
                } else {
                    CartStatus.Error(response.message())
                }
            } catch (e: Exception) {
                CartStatus.Error(e.message)
            }
        }
    }

    // 🟢 الحساب بالموديل الجديد CartProduct
    fun calculateCartTotals(products: List<CartProduct>): CartTotals {
        var totalPriceBeforeDiscount = 0.0
        var totalDiscount = 0.0
        val deliveryFee = 0.0 // Free

        for (product in products) {
            val quantity = product.myQuantity
            val priceWithoutTax = product.priceWithoutTax
            val priceAfterDiscount = product.priceAfterDiscount

            totalPriceBeforeDiscount += priceWithoutTax * quantity
            totalDiscount += (priceWithoutTax - priceAfterDiscount) * quantity
        }

        val totalPriceAfterDiscount = totalPriceBeforeDiscount - totalDiscount
        val grandTotal = totalPriceAfterDiscount + deliveryFee

        return CartTotals(
            totalBeforeDiscount = totalPriceBeforeDiscount,
            discount = totalDiscount,
            deliveryFee = deliveryFee,
            grandTotal = grandTotal
        )
    }

    data class CartTotals(
        val totalBeforeDiscount: Double,
        val discount: Double,
        val deliveryFee: Double,
        val grandTotal: Double
    )
}


