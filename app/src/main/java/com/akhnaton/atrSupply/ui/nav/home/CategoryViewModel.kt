package com.akhnaton.atrSupply.ui.nav.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akhnaton.atrSupply.data.statuesValue.nav.home.category.CategoryIntent
import com.akhnaton.atrSupply.data.statuesValue.nav.home.category.CategoryStatus
import com.akhnaton.atrSupply.domain.CategoriesRepository
import com.akhnaton.atrSupply.shared.Common
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class CategoryViewModel : ViewModel() {

    val categoryIntent = Channel<CategoryIntent>(Channel.UNLIMITED)

    private val _state = MutableStateFlow<CategoryStatus>(CategoryStatus.Idle)
    val state: StateFlow<CategoryStatus> get() = _state

    init {
        observeIntents()
    }

    private fun observeIntents() {
        viewModelScope.launch {
            categoryIntent.consumeAsFlow().collect {
                when (it) {
                    is CategoryIntent.GetCategories -> getCategoriesRepo()
                }
            }
        }
    }

    private fun getCategoriesRepo() {
        viewModelScope.launch {
            _state.value = CategoryStatus.Loading
            _state.value = try {
                val response = CategoriesRepository().getCategories()
                Log.d(Common.KeroDebug, "getCategoriesRepo ${response}")
                CategoryStatus.GetCategory(response)
            } catch (e: Exception) {
                CategoryStatus.Error(e.message)
            }
        }
    }
}