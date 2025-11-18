package com.akhnaton.atrSupply.data.statuesValue.nav.home.category

import com.akhnaton.atrSupply.data.model.OrderTypeModel
import com.akhnaton.atrSupply.data.model.common.BaseModel

sealed class CategoryStatus {

    data object Idle : CategoryStatus()
    data object Loading : CategoryStatus()
    data class GetCategory(val data: BaseModel<List<OrderTypeModel>>) : CategoryStatus()
    data class Error(val error: String?) : CategoryStatus()

}