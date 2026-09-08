package com.akhnaton.atrapp.ui.nav.profile.order.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akhnaton.atrapp.data.statuesValue.nav.profile.orderHistory.myOrders.MyOrdersIntent
import com.akhnaton.atrapp.data.statuesValue.nav.profile.orderHistory.myOrders.MyOrdersStatus
import com.akhnaton.atrapp.domain.OrdersRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class MyOrdersViewModel : ViewModel() {
    val ordersIntent = Channel<MyOrdersIntent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow<MyOrdersStatus>(MyOrdersStatus.Idle)
    val state: StateFlow<MyOrdersStatus> get() = _state

    init {
        makeHomeObserve()
    }

    private fun makeHomeObserve() {
        viewModelScope.launch {
            ordersIntent.consumeAsFlow().collect {
                when (it) {
                    is MyOrdersIntent.GetMyOrders -> getMyOrders()
                }
            }
        }
    }

    private fun getMyOrders() {
        viewModelScope.launch {
            _state.value = MyOrdersStatus.Loading
            _state.value = try {
                val response = OrdersRepository().getMyOrders()
                if (response.code() == 200) {
                    MyOrdersStatus.GetMyOrders(response.body()!!)
                } else if (response.code() == 401) {
                    MyOrdersStatus.Error(response.body()!!.message)
                } else {
                    MyOrdersStatus.Error(response.message())
                }
            } catch (e: Exception) {
                MyOrdersStatus.Error(e.message)
            }
        }
    }
}

