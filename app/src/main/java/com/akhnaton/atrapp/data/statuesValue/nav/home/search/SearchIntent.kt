package com.akhnaton.atrapp.data.statuesValue.nav.home.search

sealed class SearchIntent {

    data class SearchProduct(val version: String, val word: String) : SearchIntent()
}