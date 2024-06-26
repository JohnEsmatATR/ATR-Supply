package com.akhnaton.atrapp.data.statuesValue.nav.home.category

import com.akhnaton.atrapp.data.model.CategoryModel
import com.akhnaton.atrapp.data.model.ListCategoryModel
import com.akhnaton.atrapp.data.model.common.BaseModel
import com.akhnaton.atrapp.data.model.auth.ErrorModel

sealed class CategoryStatus {

    data object Idle : CategoryStatus()
    data object Loading : CategoryStatus()
    data class GetCategory(val data: BaseModel<List<CategoryModel>>) : CategoryStatus()
    data class Error(val error: String?) : CategoryStatus()

}