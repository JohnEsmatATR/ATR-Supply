package com.akhnaton.atrapp.data.statuesValue.nav.home.search

import com.akhnaton.atrapp.data.statuesValue.nav.home.category.CategoryStatus

sealed class SearchIntent {

    data class SearchProduct(val word: String?= "", val categoryId :Int?= null) : SearchIntent()
 
}