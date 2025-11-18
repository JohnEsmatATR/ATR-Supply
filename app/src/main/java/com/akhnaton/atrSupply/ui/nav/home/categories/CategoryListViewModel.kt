package com.akhnaton.atrSupply.ui.nav.home.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akhnaton.atrSupply.data.statuesValue.nav.home.CategoriesIntent
import com.akhnaton.atrSupply.data.statuesValue.nav.home.CategoryState
import com.akhnaton.atrSupply.domain.HomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CategoryListViewModel: ViewModel() {

    private val _state = MutableStateFlow<CategoryState>(CategoryState.Idle)
    val state: StateFlow<CategoryState> = _state

    fun handleIntent(intent: CategoriesIntent) {
        when (intent) {
            is CategoriesIntent.GetCategories -> {
                getCategories(intent.parentCategory)
            }
        }
    }

    private fun getCategories(parentCategory: String) {
        viewModelScope.launch {
            _state.value = CategoryState.Loading
            try {
                val response = HomeRepository().getCategory(parentCategory)
                if (response.isSuccessful && response.body() != null) {
                    _state.value = CategoryState.Success(response.body()!!)
                }else {
                    _state.value = CategoryState.Error(response.message())
                }
            } catch (e: Exception) {
                _state.value = CategoryState.Error(e.localizedMessage ?: "Unknown Error")
            }
        }
    }
}