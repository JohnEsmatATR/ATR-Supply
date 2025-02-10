package com.akhnaton.atrapp.data.statuesValue.nav.home.favorite

sealed class FavoriteIntent {

    data class GetFavorite(val version: String) : FavoriteIntent()
}