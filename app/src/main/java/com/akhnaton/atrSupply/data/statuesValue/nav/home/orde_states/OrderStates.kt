package com.akhnaton.atrSupply.data.statuesValue.nav.home.orde_states

import com.akhnaton.atrSupply.data.model.common.BaseModel
import com.akhnaton.atrSupply.data.model.order_states.OrderStatesModel
import retrofit2.Response

sealed class OrderStates {
    data object Loading : OrderStates()
    data class OnCusses(val data: Response<BaseModel<List<OrderStatesModel>>>) : OrderStates()
    data class OnFailer(val massage : String?): OrderStates()
}