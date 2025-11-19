package com.akhnaton.atrapp.data.statuesValue.nav.home.products

import com.akhnaton.atrapp.data.model.ProductModel
import com.akhnaton.atrapp.data.model.common.BaseModel

sealed class ProductsStatus {

    data object Idle : ProductsStatus()
    data object Loading : ProductsStatus()
    data class GetProducts(val data: BaseModel<List<ProductModel>>) : ProductsStatus()
    data class Error(val error: String?) : ProductsStatus()

}