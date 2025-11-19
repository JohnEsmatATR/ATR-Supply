package com.akhnaton.atrapp.data.statuesValue.nav.home.search

import com.akhnaton.atrapp.data.model.ProductModel
import com.akhnaton.atrapp.data.model.common.BaseModel

sealed class SearchStatus {

    data object Idle : SearchStatus()
    data object Loading : SearchStatus()
    data class SearchProduct(val data: BaseModel<List<ProductModel>>) : SearchStatus()
    data class Error(val error: String?) : SearchStatus()
}