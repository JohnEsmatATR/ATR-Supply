package com.akhnaton.atrapp.data.statuesValue.nav.home.bestSeller

import com.akhnaton.atrapp.data.model.ProductModel
import com.akhnaton.atrapp.data.model.common.BaseModel

sealed class BestSellerStatus {

    data object Idle : BestSellerStatus()
    data object Loading : BestSellerStatus()
    data class GetBestSeller(val data: BaseModel<List<ProductModel>>) : BestSellerStatus()
    data class Error(val error: String?) : BestSellerStatus()

}