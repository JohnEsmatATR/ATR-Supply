package com.akhnaton.atrapp.data.statuesValue.nav.home.category

sealed class CategoryIntent {

    data object GetCategories : CategoryIntent()

}