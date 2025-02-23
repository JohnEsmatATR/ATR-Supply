package com.akhnaton.atrapp.ui.nav.cart.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akhnaton.atrapp.data.model.ProductModel
import com.akhnaton.atrapp.data.model.common.BaseModel
import com.akhnaton.atrapp.data.statuesValue.nav.cart.checkout.CheckoutIntent
import com.akhnaton.atrapp.data.statuesValue.nav.cart.checkout.CheckoutStatus
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

class CheckoutViewModel : ViewModel() {

    val checkoutIntent = Channel<CheckoutIntent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow<CheckoutStatus>(CheckoutStatus.Idle)

    val state: StateFlow<CheckoutStatus> get() = _state

    init {
        makeHomeObserve()
    }

    private fun makeHomeObserve() {
        viewModelScope.launch {
            checkoutIntent.consumeAsFlow().collect {
                when (it) {
                    is CheckoutIntent.Checkout -> checkout()
                }
            }
        }
    }

    private fun checkout() {
        viewModelScope.launch {
            _state.value = CheckoutStatus.Loading
            _state.value = try {
                val response = CartRepository().checkout()
                if (response.code() == 200) {
                    CheckoutStatus.Checkout(response.body()!!)
                } else if (response.code() == 401) {
                    CheckoutStatus.Error(response.body()!!.message)
                } else {
                    CheckoutStatus.Error(response.message())
                }
            } catch (e: Exception) {
                CheckoutStatus.Error(e.message)
            }
        }
    }

}

