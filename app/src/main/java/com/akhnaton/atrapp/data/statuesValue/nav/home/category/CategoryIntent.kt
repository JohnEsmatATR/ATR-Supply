package com.akhnaton.atrapp.data.statuesValue.nav.home.category

sealed class CategoryIntent {

    data class GetCategories(val version: String) : CategoryIntent()

}