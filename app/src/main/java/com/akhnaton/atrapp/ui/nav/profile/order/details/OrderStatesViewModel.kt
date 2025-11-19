package com.akhnaton.atrapp.ui.nav.profile.order.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akhnaton.atrapp.data.statuesValue.nav.home.orde_states.OrderStates
import com.akhnaton.atrapp.data.statuesValue.nav.home.orde_states.OrderStatesIntent
import com.akhnaton.atrapp.domain.OrderStateRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrderStatesViewModel(
    private val repository: OrderStateRepository
) : ViewModel() {

    private val _state = MutableStateFlow<OrderStates>(OrderStates.Loading)
    val state: StateFlow<OrderStates> get() = _state

    fun handleIntent(intent: OrderStatesIntent, id: String) {
        when (intent) {
            is OrderStatesIntent.GetOrderState -> {
                getOrderStates(id)
            }
        }
    }

    private fun getOrderStates(id: String) {
        viewModelScope.launch {
            try {
                _state.value = OrderStates.Loading
                val response= repository.getOrderStates(id)
                _state.value = OrderStates.OnCusses(response)
            } catch (e: Exception) {
                _state.value = OrderStates.OnFailer(e.message)
            }
        }
    }
}