package com.akhnaton.atrapp.data.statuesValue.nav.home.category

import com.akhnaton.atrapp.data.model.OrderTypeModel
import com.akhnaton.atrapp.data.model.common.BaseModel

sealed class CategoryStatus {

    data object Idle : CategoryStatus()
    data object Loading : CategoryStatus()
    data class GetCategory(val data: BaseModel<List<OrderTypeModel>>) : CategoryStatus()
    data class Error(val error: String?) : CategoryStatus()

}