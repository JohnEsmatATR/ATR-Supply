package com.akhnaton.atrapp.ui.nav.cart.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akhnaton.atrapp.data.statuesValue.nav.cart.checkout.CheckoutIntent
import com.akhnaton.atrapp.data.statuesValue.nav.cart.checkout.CheckoutStatus
import com.akhnaton.atrapp.domain.CartRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

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
                    is CheckoutIntent.Checkout -> checkout(it.paymentId,it.category)
                }
            }
        }
    }

    private fun checkout(paymentId : Int, category: String) {
        viewModelScope.launch {
            _state.value = CheckoutStatus.Loading
            _state.value = try {
                val response = CartRepository().checkout(paymentId,category)
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

