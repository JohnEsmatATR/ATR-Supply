package com.akhnaton.atrapp.ui.nav.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akhnaton.atrapp.data.model.ProductModel
import com.akhnaton.atrapp.data.model.common.BaseModel
import com.akhnaton.atrapp.data.statuesValue.nav.cart.getMyCart.CartIntent
import com.akhnaton.atrapp.data.statuesValue.nav.cart.getMyCart.CartStatus
import com.akhnaton.atrapp.domain.CartRepository
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException

class CartViewModel : ViewModel() {

    val cartIntent = Channel<CartIntent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow<CartStatus>(CartStatus.Idle)

    val state: StateFlow<CartStatus> get() = _state

    init {
        makeHomeObserve()
    }

    private fun makeHomeObserve() {
        viewModelScope.launch {
            cartIntent.consumeAsFlow().collect {
                when (it) {
                    is CartIntent.GetMyCart -> getMyCart()
                }
            }
        }
    }

    private fun getMyCart() {
        viewModelScope.launch {
            _state.value = CartStatus.Loading
            _state.value = try {
                val response = CartRepository().getMyCart()
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

}

