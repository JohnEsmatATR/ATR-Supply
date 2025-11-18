package com.akhnaton.atrSupply.ui.nav.profile.order.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akhnaton.atrSupply.data.statuesValue.nav.profile.orderHistory.orderDetails.MyOrderDetailsIntent
import com.akhnaton.atrSupply.data.statuesValue.nav.profile.orderHistory.orderDetails.MyOrderDetailsStatus
import com.akhnaton.atrSupply.domain.OrdersRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class MyOrderDetailsViewModel : ViewModel() {

    val orderDetailsIntent = Channel<MyOrderDetailsIntent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow<MyOrderDetailsStatus>(MyOrderDetailsStatus.Idle)

    val state: StateFlow<MyOrderDetailsStatus> get() = _state

    init {
        makeHomeObserve()
    }

    private fun makeHomeObserve() {
        viewModelScope.launch {
            orderDetailsIntent.consumeAsFlow().collect {
                when (it) {
                    is MyOrderDetailsIntent.GetMyOrderDetails -> getOrderDetails(it.orgSysId)
                }
            }
        }
    }

    private fun getOrderDetails(orgSysId: String) {
        viewModelScope.launch {
            _state.value = MyOrderDetailsStatus.Loading
            _state.value = try {
                val response = OrdersRepository().getOrderDetails(orgSysId)
                if (response.code() == 200) {
                    MyOrderDetailsStatus.GetMyOrderDetails(response.body()!!)
                } else if (response.code() == 401) {
                    MyOrderDetailsStatus.Error(response.body()!!.message)
                } else {
                    MyOrderDetailsStatus.Error(response.message())
                }
            } catch (e: Exception) {
                MyOrderDetailsStatus.Error(e.message)
            }
        }
    }
}

