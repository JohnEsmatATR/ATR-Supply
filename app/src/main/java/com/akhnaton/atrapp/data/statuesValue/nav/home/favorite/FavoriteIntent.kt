package com.akhnaton.atrapp.data.statuesValue.nav.home.favorite

sealed class FavoriteIntent {

    data object GetFavorite : FavoriteIntent()
    data class AddProductToFavourites(val token: String, val productId: Int, val add: Boolean,val categories: String) : FavoriteIntent()
    data class DeleteFromFavourites(val token: String, val productId: Int, val add: Boolean, val categories: String):FavoriteIntent()

}