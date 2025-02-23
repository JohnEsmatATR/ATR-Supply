package com.akhnaton.atrapp.data.statuesValue.nav.home.favorite

sealed class FavoriteIntent {

    data object GetFavorite : FavoriteIntent()
}