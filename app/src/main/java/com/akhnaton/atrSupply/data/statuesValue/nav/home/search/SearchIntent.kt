package com.akhnaton.atrSupply.data.statuesValue.nav.home.search

sealed class SearchIntent {

    data class SearchProduct(val word: String?= "", val categoryId : String, val categories : Int) : SearchIntent()
 
}