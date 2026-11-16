package com.akhnaton.atrapp.data.statuesValue.nav.home.favorite

import com.akhnaton.atrapp.data.model.ProductModel
import com.akhnaton.atrapp.data.model.common.BaseModel

sealed class FavoriteStatus {
    data object Idle : FavoriteStatus()
    data object Loading : FavoriteStatus()
    data class GetFavorite(val data: BaseModel<List<ProductModel>>) : FavoriteStatus()
    data class AddProductToFavourites(val data: BaseModel<Any>) : FavoriteStatus()
    data class DeleteProductToFavourites(val data: BaseModel<Any>) : FavoriteStatus()
    data class Error(val error: String?) : FavoriteStatus()
}
