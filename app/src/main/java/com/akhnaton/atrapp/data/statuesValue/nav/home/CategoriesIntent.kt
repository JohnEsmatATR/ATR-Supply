package com.akhnaton.atrapp.data.statuesValue.nav.home

sealed class CategoriesIntent {
    data class GetCategories(val parentCategory: String) : CategoriesIntent()
}