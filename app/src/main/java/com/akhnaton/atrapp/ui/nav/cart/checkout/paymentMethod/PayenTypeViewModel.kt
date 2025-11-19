package com.akhnaton.atrapp.ui.nav.cart.checkout.paymentMethod

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akhnaton.atrapp.data.statuesValue.nav.cart.paymentType.PaymentIndent
import com.akhnaton.atrapp.data.statuesValue.nav.cart.paymentType.PaymentTypeStatus
import com.akhnaton.atrapp.domain.CartRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class PayenTypeViewModel : ViewModel() {

    val paymentIntent = Channel<PaymentIndent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow<PaymentTypeStatus>(PaymentTypeStatus.Idle)
    val state: StateFlow<PaymentTypeStatus> get() = _state

    init {
        observeIntent()
    }

    private fun observeIntent() {
        viewModelScope.launch {
            paymentIntent.consumeAsFlow().collect { intent ->
                when (intent) {
                    is PaymentIndent.Checkout -> getPaymentTypes()
                }
            }
        }
    }

    private fun getPaymentTypes() {
        viewModelScope.launch {
            _state.value = PaymentTypeStatus.Loading
            try {
                val response = CartRepository().getPaymentType()
                if (response.isSuccessful && response.body() != null) {
                    val result = response.body()!!
                    _state.value = PaymentTypeStatus.Checkout(result)
                } else {
                    _state.value = PaymentTypeStatus.Error(response.message())
                }
            } catch (e: Exception) {
                _state.value = PaymentTypeStatus.Error(e.message)
            }
        }
    }

}
