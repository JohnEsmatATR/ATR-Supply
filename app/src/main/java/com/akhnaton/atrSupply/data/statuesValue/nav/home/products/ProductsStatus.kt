package com.akhnaton.atrSupply.data.statuesValue.nav.home.products

import com.akhnaton.atrSupply.data.model.ProductModel
import com.akhnaton.atrSupply.data.model.common.BaseModel

sealed class ProductsStatus {

    data object Idle : ProductsStatus()
    data object Loading : ProductsStatus()
    data class GetProducts(val data: BaseModel<List<ProductModel>>) : ProductsStatus()
    data class Error(val error: String?) : ProductsStatus()

}