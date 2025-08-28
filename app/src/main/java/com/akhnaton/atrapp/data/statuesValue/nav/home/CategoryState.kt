package com.akhnaton.atrapp.data.statuesValue.nav.home

import com.akhnaton.atrapp.data.model.CategoriesModel
import com.akhnaton.atrapp.data.model.common.BaseModel

sealed class CategoryState {
    object Idle : CategoryState()
    object Loading : CategoryState()
    data class Success(val response: BaseModel<List<CategoriesModel>>) : CategoryState()
    data class Error(val message: String) : CategoryState()
}
