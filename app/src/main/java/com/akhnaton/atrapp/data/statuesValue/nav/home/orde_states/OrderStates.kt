package com.akhnaton.atrapp.data.statuesValue.nav.home.orde_states

import com.akhnaton.atrapp.data.model.common.BaseModel
import com.akhnaton.atrapp.data.model.order_states.OrderStatesModel
import retrofit2.Response

sealed class OrderStates {
    data object Loading : OrderStates()
    data class OnCusses(val data: Response<BaseModel<List<OrderStatesModel>>>) : OrderStates()
    data class OnFailer(val massage : String?): OrderStates()
}