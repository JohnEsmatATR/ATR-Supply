package com.akhnaton.atrapp.data.statuesValue.nav.home.productDetails

import com.akhnaton.atrapp.data.model.ProductModel
import com.akhnaton.atrapp.data.model.common.BaseModel

sealed class ProductDetailsStatus {

    data object Idle : ProductDetailsStatus()
    data object Loading : ProductDetailsStatus()
    data class GetProductDetails(val data: BaseModel<List<ProductModel>>) : ProductDetailsStatus()
    data class Error(val error: String?) : ProductDetailsStatus()

}